package net.expenses.recorder.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.expenses.recorder.dao.Entry;
import net.expenses.recorder.dao.Item;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.dao.enums.MonthName;
import net.expenses.recorder.dto.EntryDto;
import net.expenses.recorder.dto.EntryFormDto;
import net.expenses.recorder.exception.EntryException;
import net.expenses.recorder.exception.InvalidInputException;
import net.expenses.recorder.repository.EntryRepository;
import net.expenses.recorder.service.EntryService;
import net.expenses.recorder.service.ItemService;
import net.expenses.recorder.service.UserService;
import net.expenses.recorder.utils.CommonConstants;
import net.expenses.recorder.validation.EntryValidation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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

    @Transactional
    @Override
    public void createEntry(User user, EntryFormDto entryForm) {
        EntryValidation.validateEntryForm(entryForm);

        if (user == null) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        MonthName monthName = getMonthByName(entryForm.getMonth().trim());

        Optional<List<Entry>> optionalEntries = entryRepository.findAllByMonth(Date.valueOf(getDateString(monthName, entryForm.getYear())),
                user.getUserId());
        List<Entry> entries = optionalEntries.orElseGet(ArrayList::new);
        if (!CollectionUtils.isEmpty(entries))
            throw new EntryException("Entry already exists for the selected month!");

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
        userService.incrementEntry(user);
        entryRepository.save(entry);
    }

    @Transactional
    @Override
    public void modifyEntry(Entry entry) {
        if (entry != null)
            entryRepository.save(entry);
    }

    @Transactional
    @Override
    public void modifyEntry(Consumer<Entry> entryConsumer, Entry entry) {
        if (entry != null) {
            entryConsumer.accept(entry);
            entryRepository.save(entry);
        }
    }

    @Override
    public List<EntryDto> getAllEntries(User user) {
        if (user == null) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        Optional<List<Entry>> optionalEntries = entryRepository.findAllByUser(user.getUserId());
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

            DateFormat monthFormat = new SimpleDateFormat("MMMM");
            DateFormat yearFormat = new SimpleDateFormat("yyyy");

            entryDto.setMonth(monthFormat.format(entry.getEntryMonth()));
            entryDto.setYear(yearFormat.format(entry.getEntryMonth()));
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
        if (year >= 1950 && year <= 2099) {
            int monthIndex = getMonthIndex(monthName);
            if (monthIndex <= 0) {
                throw new InvalidInputException("Invalid month name input. Month name should be at" +
                        " least first three letters of the actual month.");
            }
            return Date.valueOf(getDateString(monthName, year));
        }
        throw new InvalidInputException("Year selection must be from 1950 to 2099");
    }

    private String getDateString(MonthName monthName, int year) {
        return year + "-" + getMonthIndex(monthName) + "-1";
    }
}
