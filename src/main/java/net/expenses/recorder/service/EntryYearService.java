package net.expenses.recorder.service;

import net.expenses.recorder.dao.Entry;
import net.expenses.recorder.dao.EntryYear;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.dto.EntryYearDto;

import java.util.List;
import java.util.UUID;

/**
 * @author Kazi Tanvir Azad
 */
public interface EntryYearService {
    void createEntryYear(Entry entry);

    void incrementYearEntryItemCount(EntryYear entryYear, int itemCount);

    void decrementEntryFromEntryYear(EntryYear entryYear, Integer itemCount);

    EntryYear getEntryYearByEntry(Entry entry);

    void deleteEntryYear(UUID entryYearId);

    List<EntryYearDto> getAllEntryYear(User user);
}
