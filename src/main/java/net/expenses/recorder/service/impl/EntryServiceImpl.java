package net.expenses.recorder.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.expenses.recorder.dao.Entry;
import net.expenses.recorder.dao.Item;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.dao.enums.Month;
import net.expenses.recorder.exception.InvalidInputException;
import net.expenses.recorder.repository.EntryRepository;
import net.expenses.recorder.service.EntryService;
import net.expenses.recorder.service.ItemService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Kazi Tanvir Azad
 */
@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {
    private final EntryRepository entryRepository;
    private final ItemService itemService;

    @Transactional
    @Override
    public void createEntry(User user, String description, int year, String monthName) {
        Entry entry = new Entry();
        if (user == null) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        entry.setUser(user);
        Month month = getMonthByName(monthName);
        entry.setMonthName(month);
        entry.setMonth(getEntryDate(month, year));
        entry.setDescription(description);
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
    public List<Entry> getAllEntries(User user) {
        if (user == null) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        Optional<List<Entry>> optionalEntries = entryRepository.findAllByUser(user.getUserId());
        return optionalEntries.orElseGet(ArrayList::new);
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

    private Month getMonthByName(String monthName) {
        if (!StringUtils.hasText(monthName) || !(monthName.trim().length() >= 3)) {
            throw new InvalidInputException("Invalid month name input. Month name should be at" +
                    " least first three letters of the actual month. Ex: January -> Jan");
        }
        return switch (monthName.trim().toLowerCase().substring(0, 3)) {
            case "jan" -> Month.JANUARY;
            case "feb" -> Month.FEBRUARY;
            case "mar" -> Month.MARCH;
            case "apr" -> Month.APRIL;
            case "may" -> Month.MAY;
            case "jun" -> Month.JUNE;
            case "jul" -> Month.JULY;
            case "aug" -> Month.AUGUST;
            case "sep" -> Month.SEPTEMBER;
            case "oct" -> Month.OCTOBER;
            case "nov" -> Month.NOVEMBER;
            case "dec" -> Month.DECEMBER;
            default -> throw new InvalidInputException("Invalid month name input. Month name should be at" +
                    " least first three letters of the actual month.");
        };
    }

    private int getMonthIndex(Month month) {
        return switch (month) {
            case Month.JANUARY -> 1;
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

    private Date getEntryDate(Month month, int year) {
        if (year > 1950 && year < 2099) {
            int monthIndex = getMonthIndex(month);
            if (monthIndex <= 0) {
                throw new InvalidInputException("Invalid month name input. Month name should be at" +
                        " least first three letters of the actual month.");
            }
            return Date.valueOf(year + "-" + getMonthIndex(month) + "-1");
        }
        throw new InvalidInputException("Year selection must be between 1950 to 2099");
    }
}
