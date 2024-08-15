package net.expenses.recorder.service;

import net.expenses.recorder.dao.Entry;
import net.expenses.recorder.dao.EntryMonth;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.dto.EntryMonthDto;

import java.util.List;
import java.util.UUID;

/**
 * @author Kazi Tanvir Azad
 */
public interface EntryMonthService {
    void createEntryMonth(Entry entry);

    EntryMonth getEntryMonthByEntry(Entry entry);

    void decrementEntryFromEntryMonth(EntryMonth entryMonth, Integer itemCount);

    void incrementMonthEntryItemCount(EntryMonth entryMonth, int itemCount);

    void deleteEntryMonth(UUID entryMonthId);

    List<EntryMonthDto> getAllEntryMonth(User user, String year);
}