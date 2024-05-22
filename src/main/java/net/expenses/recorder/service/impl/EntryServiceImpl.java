package net.expenses.recorder.service.impl;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.expenses.recorder.dao.Entry;
import net.expenses.recorder.dao.EntryMonth;
import net.expenses.recorder.dao.EntryYear;
import net.expenses.recorder.dao.Item;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.dao.enums.MonthName;
import net.expenses.recorder.dto.EntryDto;
import net.expenses.recorder.dto.EntryFormDto;
import net.expenses.recorder.dto.EntryModifyFormDto;
import net.expenses.recorder.dto.EntryReferenceDto;
import net.expenses.recorder.exception.EntryException;
import net.expenses.recorder.exception.InvalidInputException;
import net.expenses.recorder.repository.EntryRepository;
import net.expenses.recorder.service.EntryMonthService;
import net.expenses.recorder.service.EntryService;
import net.expenses.recorder.service.EntryYearService;
import net.expenses.recorder.service.ItemService;
import net.expenses.recorder.service.UserService;
import net.expenses.recorder.utils.CommonConstants;
import net.expenses.recorder.validation.EntryValidation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Kazi Tanvir Azad
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EntryServiceImpl implements EntryService, CommonConstants {
    private final EntryRepository entryRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final EntryYearService entryYearService;
    private final EntryMonthService entryMonthService;

    @Transactional
    @Override
    public EntryReferenceDto createEntry(User user, EntryFormDto entryForm) {
        EntryValidation.validateEntryForm(entryForm);

        if (user == null) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        MonthName monthName = getMonthByName(entryForm.getMonth().trim());

        Entry entry = new Entry();

        entry.setUser(user);

        Timestamp currentTimeStamp = getCurrentTimeStamp();

        entry.setMonthName(monthName);
        entry.setCreationTime(currentTimeStamp);
        entry.setLastModified(currentTimeStamp);
        entry.setEntryMonth(getEntryDate(monthName, entryForm.getYear()));

        String desc = entryForm.getDescription();
        entry.setDescription(desc != null ? desc.trim() : null);
        entry.setAmount(0.00d);
        entry.setItemCount(0);
        userService.incrementEntry(user);
        entry = entryRepository.save(entry);
        entryYearService.createEntryYear(entry);
        entryMonthService.createEntryMonth(entry);
        return new EntryReferenceDto(entry.getEntryId().toString(), getFormattedDate(entry.getEntryMonth(), MONTH_FORMAT),
                getFormattedDate(entry.getEntryMonth(), YEAR_FORMAT));
    }

    @Transactional
    @Override
    public EntryReferenceDto modifyEntry(EntryModifyFormDto entryModifyFormDto) {
        EntryValidation.validateEntryModifyForm(entryModifyFormDto);

        MonthName monthName = getMonthByName(entryModifyFormDto.getMonth().trim());

        try {
            Entry entry = entryRepository.
                    getReferenceById(UUID.fromString(entryModifyFormDto.getEntry_id()));

            EntryYear entryYear = entryYearService.getEntryYearByEntry(entry);
            entryYearService.decrementEntryFromEntryYear(entryYear, entry.getItemCount());

            EntryMonth entryMonth = entryMonthService.getEntryMonthByEntry(entry);
            entryMonthService.decrementEntryFromEntryMonth(entryMonth, entry.getItemCount());

            entry.setMonthName(monthName);
            if (StringUtils.hasText(entryModifyFormDto.getDescription()))
                entry.setDescription(entryModifyFormDto.getDescription());

            Timestamp currentTimeStamp = getCurrentTimeStamp();
            entry.setLastModified(currentTimeStamp);

            entry.setEntryMonth(getEntryDate(monthName, entryModifyFormDto.getYear()));
            entry = entryRepository.save(entry);
            entryYearService.createEntryYear(entry);
            entryMonthService.createEntryMonth(entry);
            return new EntryReferenceDto(entry.getEntryId().toString(), getFormattedDate(entry.getEntryMonth(), MONTH_FORMAT),
                    getFormattedDate(entry.getEntryMonth(), YEAR_FORMAT));
        } catch (PersistenceException | IllegalArgumentException exception) {
            throw new EntryException("Invalid Entry selection!");
        }
    }

    @Override
    public EntryDto getEntryById(String entryId) {
        if (!StringUtils.hasText(entryId))
            throw new EntryException("Invalid Entry Id!");
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Entry> optionalEntry = entryRepository.findReferenceByEntryId(user.getUserId(), UUID.fromString(entryId));
            Entry entry = optionalEntry.orElseThrow(() -> new EntryException("Invalid Entry Id!"));
            return getEntry(entry);
        } catch (IllegalArgumentException exception) {
            throw new EntryException("Malformed Entry Id!");
        }
    }

    @Override
    public List<EntryDto> getAllEntriesByEntryYear(User user, String year) {
        EntryValidation.validateEntryYear(year);
        if (user == null) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        String start = year + "-01-01";
        String end = year + "-12-31";
        Optional<List<Entry>> optionalEntries = entryRepository.findAllByUserEntryYear(user.getUserId(),
                Date.valueOf(start), Date.valueOf(end));
        return getAllEntries(optionalEntries.orElseGet(ArrayList::new));
    }

    @Override
    public List<EntryDto> getAllEntriesByEntryMonth(User user, String year, String month) {
        EntryValidation.validateEntryYear(year);
        int monthIndex = getMonthIndex(getMonthByName(month));
        String entryMonth = year + "-" + monthIndex + "-1";
        Optional<List<Entry>> optionalEntries = entryRepository.findAllByByUserEntryMonth(user.getUserId(),
                Date.valueOf(entryMonth));
        return getAllEntries(optionalEntries.orElseGet(ArrayList::new));
    }

    @Transactional
    @Override
    public void calculateEntryAmount(Entry entry) {
        double sum = 0.0;
        for (Item item : itemService.getItemsByEntry(entry)) {
            sum += item.getTotalAmount();
        }
        BigDecimal bigDecimal = new BigDecimal(sum).setScale(2, RoundingMode.HALF_UP);
        entry.setAmount(bigDecimal.doubleValue());
        entryRepository.save(entry);
    }

    @Transactional
    @Override
    public void incrementItemCount(User user, Entry entry) {
        if (user == null) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        if (entry != null && entry.getUser().equals(user)) {
            int incrementItemCount = entry.getItemCount() + 1;
            entryRepository.modifyItemCount(incrementItemCount, user.getUserId(), entry.getEntryId());
        }
    }

    @Transactional
    @Override
    public void decrementItemCount(User user, Entry entry) {
        if (user == null) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        if (entry != null && entry.getUser().equals(user)) {
            int itemCount = entry.getItemCount();
            if (itemCount > 0) {
                int decrementItemCount = itemCount - 1;
                entryRepository.modifyItemCount(decrementItemCount, user.getUserId(), entry.getEntryId());
            }
        }
    }

    private List<EntryDto> getAllEntries(List<Entry> entries) {
        List<EntryDto> entryDtos = new ArrayList<>();
        entries.forEach(entry -> entryDtos.add(getEntry(entry)));
        return entryDtos;
    }

    private EntryDto getEntry(Entry entry) {
        EntryDto entryDto = new EntryDto();
        if (entry != null) {
            entryDto.setEntryId(entry.getEntryId().toString());
            entryDto.setCreationTime(entry.getCreationTime());
            entryDto.setLastModified(entry.getLastModified());
            entryDto.setTotalAmount(entry.getAmount());
            entryDto.setDesc(entry.getDescription());
            entryDto.setItemCount(entry.getItemCount());

            entryDto.setMonth(getFormattedDate(entry.getEntryMonth(), MONTH_FORMAT));
            entryDto.setYear(getFormattedDate(entry.getEntryMonth(), YEAR_FORMAT));
        }
        return entryDto;
    }

    private MonthName getMonthByName(String monthName) {
        if (!StringUtils.hasText(monthName) || !(monthName.trim().length() >= 3)) {
            throw new InvalidInputException("Invalid month name input. Month name should be at" +
                    " least first three letters of the actual month. Ex: January -> Jan");
        }
        return switch (monthName.trim().toLowerCase().substring(0, 3)) {
            case "jan" -> MonthName.JANUARY;
            case "feb" -> MonthName.FEBRUARY;
            case "mar" -> MonthName.MARCH;
            case "apr" -> MonthName.APRIL;
            case "may" -> MonthName.MAY;
            case "jun" -> MonthName.JUNE;
            case "jul" -> MonthName.JULY;
            case "aug" -> MonthName.AUGUST;
            case "sep" -> MonthName.SEPTEMBER;
            case "oct" -> MonthName.OCTOBER;
            case "nov" -> MonthName.NOVEMBER;
            case "dec" -> MonthName.DECEMBER;
            default -> throw new InvalidInputException("Invalid month name input. Month name should be at" +
                    " least first three letters of the actual month.");
        };
    }

    private int getMonthIndex(MonthName monthName) {
        return switch (monthName) {
            case JANUARY -> 1;
            case FEBRUARY -> 2;
            case MARCH -> 3;
            case APRIL -> 4;
            case MAY -> 5;
            case JUNE -> 6;
            case JULY -> 7;
            case AUGUST -> 8;
            case SEPTEMBER -> 9;
            case OCTOBER -> 10;
            case NOVEMBER -> 11;
            case DECEMBER -> 12;
            case NOT_SPECIFIED -> 0;
        };
    }

    private Date getEntryDate(MonthName monthName, int year) {
        if (year >= ENTRY_MIN_YEAR && year <= ENTRY_MAX_YEAR) {
            int monthIndex = getMonthIndex(monthName);
            if (monthIndex <= 0) {
                throw new InvalidInputException("Invalid month name input. Month name should be at" +
                        " least first three letters of the actual month.");
            }
            return Date.valueOf(getDateString(monthName, year));
        }
        throw new InvalidInputException("Year selection must be from " + ENTRY_MIN_YEAR + " to " + ENTRY_MAX_YEAR);
    }

    private String getDateString(MonthName monthName, int year) {
        return year + "-" + getMonthIndex(monthName) + "-1";
    }
}
