package net.expenses.recorder.service;

import net.expenses.recorder.dao.Category;
import net.expenses.recorder.dao.Entry;
import net.expenses.recorder.dao.Item;
import net.expenses.recorder.dao.User;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author Kazi Tanvir Azad
 */
public interface ItemService {
    void createItem(String itemName, double price, int count, String description, Entry entry, User user, Category category);

    void modifyItem(Consumer<Item> itemConsumer, Item item);

    void modifyItem(Item item);

    List<Item> getItemsByEntry(Entry entry);

    List<Item> getAllItems(User user);

    List<Item> getItemsByCategory(Category category);

}
