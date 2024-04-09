package net.expenses.recorder.repository;

import net.expenses.recorder.dao.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Kazi Tanvir Azad
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
