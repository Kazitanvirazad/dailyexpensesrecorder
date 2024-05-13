package net.expenses.recorder.repository;

import net.expenses.recorder.dao.EntryMonth;
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
public interface EntryMonthRepository extends JpaRepository<EntryMonth, UUID> {

    @Query(value = "SELECT em.entrymonthid, em.userid, em.month, em.year, em.month_itemcount, em.month_entrycount " +
            "FROM EXPENSE_RECORDER.entry_month em " +
            "WHERE em.userid = :userId AND em.month = :month AND em.year = :year", nativeQuery = true)
    Optional<List<EntryMonth>> getReferenceByEntry(
            @Param(value = "userId") Long userId,
            @Param(value = "month") String month,
            @Param(value = "year") String year);

    @Modifying
    @Query(value = "UPDATE EXPENSE_RECORDER.entry_month set " +
            "month_entrycount = :entryCount, month_itemcount = :itemCount " +
            "WHERE entrymonthid = :entryMonthId", nativeQuery = true)
    void modifyMonthEntryItemCountById(
            @Param(value = "entryCount") int entryCount,
            @Param(value = "itemCount") int itemCount,
            @Param(value = "entryMonthId") UUID entryMonthId);
}
