package net.expenses.recorder.service;

import net.expenses.recorder.dao.Entry;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.dto.EntryDto;
import net.expenses.recorder.dto.EntryFormDto;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author Kazi Tanvir Azad
 */
public interface EntryService {
    void createEntry(User user, EntryFormDto entryForm);

    void modifyEntry(Entry entry);

    void modifyEntry(Consumer<Entry> entryConsumer, Entry entry);

    List<EntryDto> getAllEntries(User user);

    void calculateEntryAmount(Entry entry);
}
