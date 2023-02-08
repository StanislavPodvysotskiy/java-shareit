package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    List<Item> getAll(Integer ownerId);

    Item getById(Integer itemId, Integer ownerId);

    Item save(Item item, Integer ownerId);

    Item update(Item item, Integer itemId, Integer ownerId);

    List<Item> search(String text);

    void delete(Integer itemId);

}
