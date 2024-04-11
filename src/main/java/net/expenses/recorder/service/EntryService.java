package net.expenses.recorder.service;

import net.expenses.recorder.dao.Entry;
import net.expenses.recorder.dao.User;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author Kazi Tanvir Azad
 */
public interface EntryService {
    void createEntry(User user, String description, int year, String monthName);

    void modifyEntry(Entry entry);

    void modifyEntry(Consumer<Entry> entryConsumer, Entry entry);

    List<Entry> getAllEntries(User user);

    void calculateEntryAmount(Entry entry);
}
