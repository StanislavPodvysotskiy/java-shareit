package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.LastBooking;
import ru.practicum.shareit.booking.model.NextBooking;
import ru.practicum.shareit.exception.BookingDateTimeException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Override
    public List<ItemDto> getAll(Integer ownerId) {
        List<ItemDto> items = ItemMapper.makeListItemDto(itemRepository.findAllByOwnerId(ownerId));
        for (ItemDto itemDto : items) {
            setPastAndFutureBooking(itemDto, ownerId);
        }
        return items;
    }

    @Override
    public ItemDto getById(Integer itemId, Integer userId) {
        userRepository.getById(userId);
        Item item = itemRepository.getById(itemId);
        if (item == null) {
            throw new NotFoundException("Item");
        }
        ItemDto itemDto = ItemMapper.makeItemDto(item);
        if (Objects.equals(item.getOwner().getId(), userId)) {
            setPastAndFutureBooking(itemDto, userId);
        }
        List<Comment> comments = commentRepository.findByItemId(itemDto.getId());
        itemDto.setComments(comments);
        return itemDto;
    }

    @Override
    @Transactional
    public ItemDto save(ItemDto itemDto, Integer ownerId) {
        User user = userRepository.getById(ownerId);
        Item item = ItemMapper.makeItem(itemDto);
        if (user == null) {
            throw new NotFoundException("User");
        }
        item.setOwner(user);
        return ItemMapper.makeItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto itemDto, Integer itemId, Integer ownerId) {
        Item item = itemRepository.getById(itemId);
        if (!Objects.equals(item.getOwner().getId(), ownerId)) {
            throw new NotFoundException("Item owner");
        }
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.makeItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public Comment saveComment(CommentDto commentDto, Integer itemId, Integer userId) {
        Item item = itemRepository.getById(itemId);
        if (item == null) {
            throw new NotFoundException("Item");
        }
        User user = userRepository.getById(userId);
        if (user == null) {
            throw new NotFoundException("User");
        }
        ItemDto itemDto = ItemMapper.makeItemDto(item);
        setPastAndFutureBooking(itemDto, userId);
        if (itemDto.getLastBooking() == null) {
            throw new BookingDateTimeException("Item without previous booking");
        }
        Comment comment = new Comment();
        comment.setItem(item);
        comment.setText(commentDto.getText());
        comment.setAuthorName(user.getName());
        return commentRepository.save(comment);
    }

    @Override
    public List<ItemDto> search(String text) {
        return ItemMapper.makeListItemDto(itemRepository.search(text));
    }

    @Override
    public void delete(Integer itemId) {
        itemRepository.deleteById(itemId);
    }

    public Item checkById(Integer id) {
        return itemRepository.getById(id);
    }

    private void setPastAndFutureBooking(ItemDto itemDto, Integer userId) {
        List<LastBooking> pastBooking = bookingRepository.findPastBooking(LocalDateTime.now(), userId)
                .stream().filter(booking -> booking.getItem().getId().equals(itemDto.getId())).map(booking -> {
                    LastBooking lastBooking = new LastBooking();
                    lastBooking.setId(booking.getId());
                    lastBooking.setBookerId(booking.getBooker().getId());
                    return lastBooking;
                })
                .collect(Collectors.toList());
        List<NextBooking> futureBooking = bookingRepository.findFutureBooking(LocalDateTime.now(), userId)
                .stream().filter(booking -> booking.getItem().getId().equals(itemDto.getId())).map(booking -> {
                    NextBooking nextBooking = new NextBooking();
                    nextBooking.setId(booking.getId());
                    nextBooking.setBookerId(booking.getBooker().getId());
                    return nextBooking;
                }).collect(Collectors.toList());
        if (!pastBooking.isEmpty()) {
            itemDto.setLastBooking(pastBooking.get(0));
        }
        if (!futureBooking.isEmpty()) {
            itemDto.setNextBooking(futureBooking.get(0));
        }
    }
}
