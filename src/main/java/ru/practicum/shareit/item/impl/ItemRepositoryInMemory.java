package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemRepositoryInMemory implements ItemRepository {

    private Integer id = 1;
    private final Map<Integer, Item> itemsMap = new HashMap<>();

    @Override
    public List<ItemDto> getAll(Integer ownerId) {
        List<ItemDto> itemList = new ArrayList<>();
        for (Item item : itemsMap.values()) {
            if (item.getOwnerId().equals(ownerId)) {
                itemList.add(ItemMapper.makeItemDto(item));
            }
        }
        return itemList;
    }

    @Override
    public ItemDto getById(Integer itemId, Integer ownerId) {
        if (!itemsMap.containsKey(itemId)) {
            throw new NotFoundException("Item");
        }
        return ItemMapper.makeItemDto(itemsMap.get(itemId));
    }

    @Override
    public ItemDto save(ItemDto itemDto, Integer ownerId) {
        Item item = ItemMapper.makeItem(new Item(), itemDto);
        item.setId(id++);
        item.setOwnerId(ownerId);
        itemsMap.put(item.getId(), item);
        return ItemMapper.makeItemDto(item);
    }

    @Override
    public ItemDto update(ItemDto itemDto, Integer itemId, Integer ownerId) {
        if (!itemsMap.containsKey(itemId)) {
            throw new NotFoundException("Item");
        }
        for (Item i : itemsMap.values()) {
            if (i.getId().equals(itemId) && !Objects.equals(i.getOwnerId(), ownerId)) {
                throw new NotFoundException("Item owner");
            }
        }
        Item item = ItemMapper.makeItem(itemsMap.get(itemId), itemDto);
        itemsMap.put(item.getId(), item);
        return ItemMapper.makeItemDto(item);
    }

    @Override
    public List<ItemDto> search(String text) {
        List<Item> items = itemsMap.values().stream().filter(i -> i.getDescription().toLowerCase()
                        .contains(text.toLowerCase()) && i.getAvailable().equals(true)).collect(Collectors.toList());
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : items) {
            itemsDto.add(ItemMapper.makeItemDto(item));
        }
        return itemsDto;
    }

    @Override
    public void delete(Integer itemId) {
        if (!itemsMap.containsKey(itemId)) {
            throw new NotFoundException("Item");
        }
        itemsMap.remove(itemId);
    }

}
