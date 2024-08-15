package net.expenses.recorder.repository;

import net.expenses.recorder.dao.Category;
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
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query(value = "SELECT c FROM EXPENSE_RECORDER.category c WHERE c.userid = :userId", nativeQuery = true)
    Optional<List<Category>> findAllByUser(@Param(value = "userId") Long userId);
}
