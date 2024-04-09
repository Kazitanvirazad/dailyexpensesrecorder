package net.expenses.recorder.repository;

import net.expenses.recorder.dao.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Kazi Tanvir Azad
 */
@Repository
public interface EntryRepository extends JpaRepository<Entry, UUID> {
}
