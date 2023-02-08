package ru.practicum.shareit.item.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryInMemory implements ItemRepository {

    private Integer id = 1;
    private final Map<Integer, Item> itemsMap = new HashMap<>();
    private final Map<Integer, Map<Integer, Item>> userItemIndex = new HashMap<>();

    @Override
    public List<Item> getAll(Integer ownerId) {
        return new ArrayList<>(userItemIndex.get(ownerId).values());
    }

    @Override
    public Item getById(Integer itemId, Integer ownerId) {
        if (!itemsMap.containsKey(itemId)) {
            throw new NotFoundException("Item");
        }
        return itemsMap.get(itemId);
    }

    @Override
    public Item save(Item item, Integer ownerId) {
        item.setId(id++);
        item.setOwnerId(ownerId);
        itemsMap.put(item.getId(), item);
        userItemIndex.put(ownerId, Map.of(item.getId(), itemsMap.get(item.getId())));
        return item;
    }

    @Override
    public Item update(Item item, Integer itemId, Integer ownerId) {
        if (!itemsMap.containsKey(itemId)) {
            throw new NotFoundException("Item");
        }
        if (!Objects.equals(itemsMap.get(itemId).getOwnerId(), ownerId)) {
            throw new NotFoundException("Item owner");
        }
        if (item.getName() != null && !item.getName().isBlank()) {
            itemsMap.get(itemId).setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            itemsMap.get(itemId).setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemsMap.get(itemId).setAvailable(item.getAvailable());
        }
        return itemsMap.get(itemId);
    }

    @Override
    public List<Item> search(String text) {
        return itemsMap.values().stream().filter(i -> i.getDescription().toLowerCase()
                .contains(text.toLowerCase()) && i.getAvailable().equals(true)).collect(Collectors.toList());
    }

    @Override
    public void delete(Integer itemId) {
        if (!itemsMap.containsKey(itemId)) {
            throw new NotFoundException("Item");
        }
        itemsMap.remove(itemId);
    }

}
