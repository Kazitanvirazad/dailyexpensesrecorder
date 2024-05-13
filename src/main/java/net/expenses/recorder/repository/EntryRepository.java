package net.expenses.recorder.repository;

import net.expenses.recorder.dao.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Kazi Tanvir Azad
 */
@Repository
public interface EntryRepository extends JpaRepository<Entry, UUID> {
    @Query(value = "SELECT e.entryid, e.userid, e.creationtime, e.monthname, e.entrymonth, e.amount, e.description, " +
            "e.lastmodified, e.itemcount FROM EXPENSE_RECORDER.entry e WHERE e.userid = :userid", nativeQuery = true)
    Optional<List<Entry>> findAllByUser(@Param(value = "userid") Long userId);

    @Query(value = "SELECT e.entryid, e.userid, e.creationtime, e.monthname, e.entrymonth, e.amount, e.description, " +
            "e.lastmodified, e.itemcount FROM EXPENSE_RECORDER.entry e WHERE e.userid = :userid " +
            "AND e.entrymonth BETWEEN :start AND :end ORDER BY e.entrymonth DESC", nativeQuery = true)
    Optional<List<Entry>> findAllByUserEntryYear(@Param(value = "userid") Long userId,
                                                 @Param(value = "start") Date start,
                                                 @Param(value = "end") Date end);

    @Query(value = "SELECT e.entryid, e.userid, e.creationtime, e.monthname, e.entrymonth, e.amount, e.description, " +
            "e.lastmodified, e.itemcount FROM EXPENSE_RECORDER.entry e WHERE e.userid = :userid " +
            "AND e.entrymonth BETWEEN :start AND :end ORDER BY e.creationtime DESC", nativeQuery = true)
    Optional<List<Entry>> findAllByByUserEntryMonth(@Param(value = "userid") Long userId,
                                                    @Param(value = "start") Date start,
                                                    @Param(value = "end") Date end);

    @Modifying
    @Query(value = "UPDATE EXPENSE_RECORDER.entry SET itemcount = :count " +
            "WHERE userid = :userId AND entryid = :entryId", nativeQuery = true)
    void modifyItemCount(@Param(value = "count") int count, @Param(value = "userId") Long userId,
                         @Param(value = "entryId") UUID entryId);
}
