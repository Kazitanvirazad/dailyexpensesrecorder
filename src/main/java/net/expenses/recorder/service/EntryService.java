package net.expenses.recorder.service;

import net.expenses.recorder.dao.Entry;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.dto.EntryDto;
import net.expenses.recorder.dto.EntryFormDto;
import net.expenses.recorder.dto.EntryModifyFormDto;
import net.expenses.recorder.dto.EntryReferenceDto;

import java.util.List;

/**
 * @author Kazi Tanvir Azad
 */
public interface EntryService {
    EntryReferenceDto createEntry(User user, EntryFormDto entryForm);

    EntryReferenceDto modifyEntry(EntryModifyFormDto entryModifyFormDto);

    EntryDto getEntryById(String entryId);

    List<EntryDto> getAllEntriesByEntryYear(User user, String year);

    List<EntryDto> getAllEntriesByEntryMonth(User user, String year, String month);

    void calculateEntryAmount(Entry entry);

    void incrementItemCount(User user, Entry entry);

    void decrementItemCount(User user, Entry entry);
}
