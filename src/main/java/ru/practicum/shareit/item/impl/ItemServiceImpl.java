package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public List<Item> getAll(Integer ownerId) {
        return itemRepository.getAll(ownerId);
    }

    @Override
    public Item getById(Integer itemId, Integer ownerId) {
        return itemRepository.getById(itemId, ownerId);
    }

    @Override
    public Item save(Item item, Integer ownerId) {
        userService.getById(ownerId);
        return itemRepository.save(item, ownerId);
    }

    @Override
    public Item update(Item item, Integer itemId, Integer ownerId) {
        return itemRepository.update(item, itemId, ownerId);
    }

    @Override
    public List<Item> search(String text) {
        return itemRepository.search(text);
    }

    @Override
    public void delete(Integer itemId) {
        itemRepository.delete(itemId);
    }

}
