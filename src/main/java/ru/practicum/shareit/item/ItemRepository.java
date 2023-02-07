package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository {

    List<ItemDto> getAll(Integer ownerId);

    ItemDto getById(Integer itemId, Integer ownerId);

    ItemDto save(ItemDto itemDto, Integer ownerId);

    ItemDto update(ItemDto itemDto, Integer itemId, Integer ownerId);

    List<ItemDto> search(String text);

    void delete(Integer itemId);

}
