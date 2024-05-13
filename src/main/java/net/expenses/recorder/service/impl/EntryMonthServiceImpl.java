package net.expenses.recorder.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.expenses.recorder.dao.Entry;
import net.expenses.recorder.dao.EntryMonth;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.exception.EntryMonthException;
import net.expenses.recorder.repository.EntryMonthRepository;
import net.expenses.recorder.service.EntryMonthService;
import net.expenses.recorder.utils.CommonConstants;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Kazi Tanvir Azad
 */
@Service
@RequiredArgsConstructor
public class EntryMonthServiceImpl implements EntryMonthService, CommonConstants {
    private final EntryMonthRepository entryMonthRepository;

    @Transactional
    @Override
    public void createEntryMonth(Entry entry) {
        if (entry == null) {
            throw new EntryMonthException("Invalid Entry");
        }
        EntryMonth entryMonth = getEntryMonthByEntry(entry);
        if (entryMonth != null) {
            incrementMonthEntryItemCount(entryMonth, entry.getItemCount());
            return;
        }
        entryMonth = new EntryMonth();
        entryMonth.setYear(getFormattedDate(entry.getEntryMonth(), YEAR_FORMAT));
        entryMonth.setMonth(getFormattedDate(entry.getEntryMonth(), MONTH_FORMAT).toUpperCase());
        entryMonth.setUser(entry.getUser());
        entryMonth.setMonthItemCount(0);
        entryMonth.setMonthEntryCount(1);
        entryMonthRepository.save(entryMonth);
    }

    @Override
    public EntryMonth getEntryMonthByEntry(Entry entry) {
        if (entry == null) {
            throw new EntryMonthException("Invalid Entry");
        }
        String year = getFormattedDate(entry.getEntryMonth(), YEAR_FORMAT);
        String month = getFormattedDate(entry.getEntryMonth(), MONTH_FORMAT).toUpperCase();
        User user = entry.getUser();
        if (user == null) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        Optional<List<EntryMonth>> optionalEntryMonths =
                entryMonthRepository.getReferenceByEntry(user.getUserId(), month, year);
        List<EntryMonth> entryMonthList = optionalEntryMonths.orElseGet(ArrayList::new);
        return !entryMonthList.isEmpty() ? entryMonthList.getFirst() : null;
    }

    @Transactional
    @Override
    public void incrementMonthEntryItemCount(EntryMonth entryMonth, int itemCount) {
        if (entryMonth == null) {
            throw new EntryMonthException("Invalid EntryMonth");
        }
        int entryCount = entryMonth.getMonthEntryCount() + 1;
        itemCount = entryMonth.getMonthItemCount() + itemCount;
        entryMonthRepository.modifyMonthEntryItemCountById(entryCount, itemCount, entryMonth.getEntryMonthId());
    }
}
