package net.expenses.recorder.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.expenses.recorder.dao.Category;
import net.expenses.recorder.dao.Entry;
import net.expenses.recorder.dao.Item;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.repository.ItemRepository;
import net.expenses.recorder.service.ItemService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Kazi Tanvir Azad
 */
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public void createItem(String itemName, double price, int count, String description, Entry entry, User user, Category category) {
        Item item = new Item();
        item.setItemName(itemName);
        if (price > 0.00)
            item.setPrice(price);
        if (count > 0)
            item.setCount(count);
        item.setDescription(description);
        item.setEntry(entry);
        if (user == null) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        item.setUser(user);
        if (category != null) {
            item.setCategory(category);
        }
        itemRepository.save(item);
    }

    @Transactional
    @Override
    public void modifyItem(Consumer<Item> itemConsumer, Item item) {
        if (item != null) {
            itemConsumer.accept(item);
            calculateItemAmount(item);
            itemRepository.save(item);
        }
    }

    @Transactional
    @Override
    public void modifyItem(Item item) {
        if (item != null) {
            calculateItemAmount(item);
            itemRepository.save(item);
        }
    }

    @Override
    public List<Item> getItemsByEntry(Entry entry) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<List<Item>> optionalItems = itemRepository.getItemsByEntry(entry.getEntryNo().toString(), user.getUserId());
        return optionalItems.orElseGet(ArrayList::new);
    }

    @Override
    public List<Item> getAllItems(User user) {
        if (user == null)
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<List<Item>> optionalItems = itemRepository.getAllItems(user.getUserId());
        return optionalItems.orElseGet(ArrayList::new);
    }

    @Override
    public List<Item> getItemsByCategory(Category category) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<List<Item>> optionalItems = itemRepository.getItemsByCategory(category.getCategoryId().toString(), user.getUserId());
        return optionalItems.orElseGet(ArrayList::new);
    }

    private void calculateItemAmount(Item item) {
        double totalAmount = item.getCount() * item.getPrice();
        item.setTotalAmount(totalAmount);
    }
}
