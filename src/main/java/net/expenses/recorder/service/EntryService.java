package net.expenses.recorder.service;

import net.expenses.recorder.dao.Entry;
import net.expenses.recorder.dao.User;

/**
 * @author Kazi Tanvir Azad
 */
public interface EntryService {
    void createEntry(User user, String description, int year, String monthName);

    void modifyEntry(Entry entry);
}
