package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    List<ItemDto> getAll(Integer ownerId);

    ItemDto getById(Integer itemId, Integer ownerId);

    ItemDto save(ItemDto itemDto, Integer ownerId);

    ItemDto update(ItemDto itemDto, Integer itemId, Integer ownerId);

    List<ItemDto> search(String text);

    void delete(Integer itemId);

    Item checkById(Integer id);

    Comment saveComment(CommentDto commentDto, Integer itemId, Integer userId);
}
