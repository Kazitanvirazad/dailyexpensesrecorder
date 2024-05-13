package net.expenses.recorder.service;

import net.expenses.recorder.dao.Entry;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.dto.EntryDto;
import net.expenses.recorder.dto.EntryFormDto;
import net.expenses.recorder.dto.EntryModifyFormDto;

import java.util.List;

/**
 * @author Kazi Tanvir Azad
 */
public interface EntryService {
    void createEntry(User user, EntryFormDto entryForm);

    void modifyEntry(EntryModifyFormDto entryModifyFormDto);

    List<EntryDto> getAllEntriesByEntryYear(User user, String year);

    List<EntryDto> getAllEntriesByEntryMonth(User user, String year, String month);

    void calculateEntryAmount(Entry entry);

    void incrementItemCount(User user, Entry entry);

    void decrementItemCount(User user, Entry entry);
}
