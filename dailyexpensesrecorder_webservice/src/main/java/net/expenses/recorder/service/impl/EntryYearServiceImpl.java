package net.expenses.recorder.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.expenses.recorder.dao.Entry;
import net.expenses.recorder.dao.EntryYear;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.dto.EntryYearDto;
import net.expenses.recorder.exception.EntryYearException;
import net.expenses.recorder.repository.EntryRepository;
import net.expenses.recorder.repository.EntryYearRepository;
import net.expenses.recorder.service.EntryYearService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.expenses.recorder.constants.CommonConstants.YEAR_FORMAT;
import static net.expenses.recorder.utils.CommonUtils.getFormattedDate;

/**
 * @author Kazi Tanvir Azad
 */
@Service
@RequiredArgsConstructor
public class EntryYearServiceImpl implements EntryYearService {
    private final EntryYearRepository entryYearRepository;
    private final EntryRepository entryRepository;

    @Transactional
    @Override
    public void createEntryYear(Entry entry) {
        if (entry == null) {
            throw new EntryYearException("Invalid Entry");
        }
        EntryYear entryYear = getEntryYearByEntry(entry);
        if (entryYear != null) {
            incrementYearEntryItemCount(entryYear, entry.getItemCount());
            return;
        }
        entryYear = new EntryYear();
        entryYear.setYear(getFormattedDate(entry.getEntryMonth(), YEAR_FORMAT));
        entryYear.setUser(entry.getUser());
        entryYear.setYearItemCount(0);
        entryYear.setYearEntryCount(1);
        entryYearRepository.save(entryYear);
    }

    @Transactional
    @Override
    public void incrementYearEntryItemCount(EntryYear entryYear, int itemCount) {
        if (entryYear == null) {
            throw new EntryYearException("Invalid EntryYear");
        }
        int entryCount = entryYear.getYearEntryCount() + 1;
        itemCount = entryYear.getYearItemCount() + itemCount;

        entryYearRepository.modifyYearEntryItemCountById(entryCount, itemCount, entryYear.getEntryYearId());
    }

    @Transactional
    @Override
    public void decrementEntryFromEntryYear(EntryYear entryYear, Integer itemCount) {
        if (entryYear == null) {
            throw new EntryYearException("EntryYear not found for the given Entry");
        }

        int existingEntryCount = entryYear.getYearEntryCount();
        int existingItemCount = entryYear.getYearItemCount();

        if (existingEntryCount <= 1) {
            deleteEntryYear(entryYear.getEntryYearId());
            return;
        }

        existingEntryCount = existingEntryCount - 1;

        if (existingItemCount < itemCount) {
            existingItemCount = 0;
        } else {
            existingItemCount = existingItemCount - itemCount;
        }

        entryYearRepository.modifyEntryYear(entryYear.getEntryYearId(), existingEntryCount, existingItemCount);
    }

    @Override
    public EntryYear getEntryYearByEntry(Entry entry) {
        if (entry == null) {
            throw new EntryYearException("Invalid Entry");
        }
        String year = getFormattedDate(entry.getEntryMonth(), YEAR_FORMAT);
        Optional<List<EntryYear>> entryYearOptional = entryYearRepository.getReferenceByEntry(entry.getUser().getUserId(), year);
        List<EntryYear> entryYearList = entryYearOptional.orElseGet(ArrayList::new);
        return !entryYearList.isEmpty() ? entryYearList.getFirst() : null;
    }

    @Transactional
    @Override
    public void deleteEntryYear(UUID entryYearId) {
        if (entryYearId == null) {
            throw new EntryYearException("Invalid EntryYear id");
        }
        entryYearRepository.deleteEntryYear(entryYearId);
    }

    @Override
    public List<EntryYearDto> getAllEntryYear(User user) {
        if (user == null) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        Optional<List<EntryYear>> optionalList = entryYearRepository.getAllEntryYear(user.getUserId());
        return getAllEntryYear(optionalList.orElseGet(ArrayList::new));
    }

    private List<EntryYearDto> getAllEntryYear(List<EntryYear> entryYearList) {
        List<EntryYearDto> entryYearDtoList = new ArrayList<>();
        entryYearList.forEach(entryYear -> entryYearDtoList.add(getEntryYearDto(entryYear)));
        return entryYearDtoList;
    }

    private EntryYearDto getEntryYearDto(EntryYear entryYear) {
        return entryYear != null ?
                new EntryYearDto(entryYear.getYear(), entryYear.getYearItemCount(), entryYear.getYearEntryCount())
                : null;
    }
}
