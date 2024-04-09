package net.expenses.recorder.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.expenses.recorder.dao.Category;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.repository.CategoryRepository;
import net.expenses.recorder.service.CategoryService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Kazi Tanvir Azad
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public void createCategory(String categoryName, String description, User user) {
        if (user == null) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setDescription(description);
        category.setUser(user);
        categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategory(User user) {
        Optional<List<Category>> optionalCategories = categoryRepository.findAllByUser(user.getUserId());
        return optionalCategories.orElseGet(ArrayList::new);
    }

    @Transactional
    @Override
    public void modifyCategory(Category category) {
        if (category != null)
            categoryRepository.save(category);
    }
}
