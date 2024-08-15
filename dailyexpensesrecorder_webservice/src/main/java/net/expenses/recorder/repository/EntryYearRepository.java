package net.expenses.recorder.repository;

import net.expenses.recorder.dao.EntryYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Kazi Tanvir Azad
 */
@Repository
public interface EntryYearRepository extends JpaRepository<EntryYear, UUID> {

    @Query(value = "SELECT ey.entryyearid, ey.userid, ey.year, ey.year_itemcount, ey.year_entrycount " +
            "FROM EXPENSE_RECORDER.entry_year ey WHERE ey.userid = :userId AND ey.year = :year", nativeQuery = true)
    Optional<List<EntryYear>> getReferenceByEntry(@Param(value = "userId") Long userId,
                                                  @Param(value = "year") String year);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE EXPENSE_RECORDER.entry_year SET year_entrycount = :entryCount, year_itemcount = :itemCount " +
            "WHERE entryyearid = :entryYearId", nativeQuery = true)
    void modifyEntryYear(@Param(value = "entryYearId") UUID entryYearId,
                         @Param(value = "entryCount") Integer entryCount,
                         @Param(value = "itemCount") Integer itemCount);

    @Modifying
    @Query(value = "UPDATE EXPENSE_RECORDER.entry_year " +
            "SET year_itemcount = :itemCount, year_entrycount = :entryCount " +
            "WHERE entryyearid = :id", nativeQuery = true)
    void modifyYearEntryItemCountById(@Param(value = "entryCount") int entryCount,
                                      @Param(value = "itemCount") int itemCount,
                                      @Param(value = "id") UUID id);

    @Modifying
    @Query(value = "DELETE FROM EXPENSE_RECORDER.entry_year WHERE entryyearid = :id", nativeQuery = true)
    void deleteEntryYear(@Param(value = "id") UUID id);

    @Query(value = "SELECT ey.entryyearid, ey.userid, ey.year, ey.year_itemcount, ey.year_entrycount " +
            "FROM EXPENSE_RECORDER.entry_year ey WHERE ey.userid = :userId ORDER BY ey.year DESC", nativeQuery = true)
    Optional<List<EntryYear>> getAllEntryYear(@Param(value = "userId") Long userId);
}
