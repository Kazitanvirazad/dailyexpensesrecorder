package net.expenses.recorder.service;

import net.expenses.recorder.dao.Category;
import net.expenses.recorder.dao.User;

import java.util.List;

/**
 * @author Kazi Tanvir Azad
 */
public interface CategoryService {
    void createCategory(String categoryName, String description, User user);

    List<Category> getAllCategory(User user);

    void modifyCategory(Category category);
}
