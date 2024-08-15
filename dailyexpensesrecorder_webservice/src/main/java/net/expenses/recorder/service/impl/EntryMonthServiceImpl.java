package net.expenses.recorder.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.expenses.recorder.dao.Entry;
import net.expenses.recorder.dao.EntryMonth;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.dto.EntryMonthDto;
import net.expenses.recorder.exception.EntryMonthException;
import net.expenses.recorder.exception.ServerErrorException;
import net.expenses.recorder.repository.EntryMonthRepository;
import net.expenses.recorder.service.EntryMonthService;
import net.expenses.recorder.validation.EntryValidation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.expenses.recorder.constants.CommonConstants.MONTH_FORMAT;
import static net.expenses.recorder.constants.CommonConstants.YEAR_FORMAT;
import static net.expenses.recorder.constants.StaticTextResource.SERVER_ERROR_GENERIC_MESSAGE;
import static net.expenses.recorder.utils.CommonUtils.getFormattedDate;

/**
 * @author Kazi Tanvir Azad
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EntryMonthServiceImpl implements EntryMonthService {
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
    public void decrementEntryFromEntryMonth(EntryMonth entryMonth, Integer itemCount) {
        if (entryMonth == null) {
            throw new EntryMonthException("EntryMonth not found for the given Entry");
        }

        int existingEntryCount = entryMonth.getMonthEntryCount();
        int existingItemCount = entryMonth.getMonthItemCount();

        if (existingEntryCount <= 1) {
            deleteEntryMonth(entryMonth.getEntryMonthId());
            return;
        }

        existingEntryCount = existingEntryCount - 1;

        if (existingItemCount < itemCount) {
            existingItemCount = 0;
        } else {
            existingItemCount = existingItemCount - itemCount;
        }

        entryMonthRepository.modifyEntryMonth(entryMonth.getEntryMonthId(), existingEntryCount, existingItemCount);
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

    @Transactional
    @Override
    public void deleteEntryMonth(UUID entryMonthId) {
        if (entryMonthId == null) {
            throw new EntryMonthException("Invalid EntryMonth id");
        }
        entryMonthRepository.deleteEntryMonth(entryMonthId);
    }

    @Override
    public List<EntryMonthDto> getAllEntryMonth(User user, String year) {
        EntryValidation.validateEntryYear(year);
        if (user == null) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        Optional<List<EntryMonth>> optionalList =
                entryMonthRepository.getAllEntryMonth(user.getUserId(), year);
        return getAllEntryMonth(optionalList.orElseGet(ArrayList::new));
    }

    private List<EntryMonthDto> getAllEntryMonth(List<EntryMonth> entryMonthList) {
        List<EntryMonthDto> entryMonthDtoList = new ArrayList<>();
        entryMonthList.forEach(entryMonth -> entryMonthDtoList.add(getEntryMonthDto(entryMonth)));
        entryMonthDtoList.sort((month1, month2) -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MONTH_FORMAT);
            try {
                return simpleDateFormat.parse(month1.getMonth()).compareTo(simpleDateFormat.parse(month2.getMonth()));
            } catch (ParseException exception) {
                log.error("Parsing failed : {}", exception.getMessage());
                throw new ServerErrorException(SERVER_ERROR_GENERIC_MESSAGE, exception);
            }
        });
        return entryMonthDtoList;
    }

    private EntryMonthDto getEntryMonthDto(EntryMonth entryMonth) {
        return entryMonth != null ?
                new EntryMonthDto(entryMonth.getYear(),
                        StringUtils.hasText(entryMonth.getMonth()) ?
                                StringUtils.capitalize(entryMonth.getMonth().toLowerCase()) : entryMonth.getMonth(),
                        entryMonth.getMonthItemCount(), entryMonth.getMonthEntryCount()) : null;
    }
}
