package net.expenses.recorder.service;

import net.expenses.recorder.dao.Entry;
import net.expenses.recorder.dao.EntryMonth;

/**
 * @author Kazi Tanvir Azad
 */
public interface EntryMonthService {
    void createEntryMonth(Entry entry);

    EntryMonth getEntryMonthByEntry(Entry entry);

    void incrementMonthEntryItemCount(EntryMonth entryMonth, int itemCount);
}
