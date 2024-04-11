package net.expenses.recorder.repository;

import net.expenses.recorder.dao.Item;
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
public interface ItemRepository extends JpaRepository<Item, UUID> {

    @Query(value = "SELECT i FROM EXPENSE_RECORDER.item i WHERE i.entryno = :entryNo AND i.userid = :userId", nativeQuery = true)
    Optional<List<Item>> getItemsByEntry(@Param(value = "entryNo") String entryNo, @Param(value = "userId") Long userId);

    @Query(value = "SELECT i FROM EXPENSE_RECORDER.item i WHERE i.userid = :userId", nativeQuery = true)
    Optional<List<Item>> getAllItems(@Param(value = "userId") Long userId);

    @Query(value = "SELECT i FROM EXPENSE_RECORDER.item i WHERE i.categoryid = :categoryId AND i.userid = :userId", nativeQuery = true)
    Optional<List<Item>> getItemsByCategory(@Param(value = "categoryId") String categoryId, @Param(value = "userId") Long userId);
}
