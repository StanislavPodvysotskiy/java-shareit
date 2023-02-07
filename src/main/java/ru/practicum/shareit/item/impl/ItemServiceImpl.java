package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public List<ItemDto> getAll(Integer ownerId) {
        return itemRepository.getAll(ownerId);
    }

    @Override
    public ItemDto getById(Integer itemId, Integer ownerId) {
        return itemRepository.getById(itemId, ownerId);
    }

    @Override
    public ItemDto save(ItemDto itemDto, Integer ownerId) {
        userService.getById(ownerId);
        return itemRepository.save(itemDto, ownerId);
    }

    @Override
    public ItemDto update(ItemDto itemDto, Integer itemId, Integer ownerId) {
        return itemRepository.update(itemDto, itemId, ownerId);
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemRepository.search(text);
    }

    @Override
    public void delete(Integer itemId) {
        itemRepository.delete(itemId);
    }

}
