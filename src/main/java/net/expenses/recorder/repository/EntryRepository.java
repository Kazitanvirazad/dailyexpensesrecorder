package net.expenses.recorder.repository;

import net.expenses.recorder.dao.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface EntryRepository extends JpaRepository<Entry, UUID> {
    @Query(value = "SELECT e FROM EXPENSE_RECORDER.entry e WHERE e.userid = :userid", nativeQuery = true)
    Optional<List<Entry>> findAllByUser(@Param(value = "userid") Long userId);
}
