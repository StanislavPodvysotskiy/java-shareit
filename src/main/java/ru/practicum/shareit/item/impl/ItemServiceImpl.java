package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.LastBooking;
import ru.practicum.shareit.booking.model.NextBooking;
import ru.practicum.shareit.exception.BookingDateTimeException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Override
    public List<ItemResponseDto> getAll(Integer ownerId) {
        List<ItemResponseDto> items = ItemMapper.makeListItemDto(itemRepository.findAllByOwnerId(ownerId));
        Map<Integer, List<Comment>> comments = commentRepository.findAll().stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId()));
        for (ItemResponseDto item : items) {
            if (comments.containsKey(item.getId())) {
                item.setComments(comments.get(item.getId()));
            }
        }
        List<Booking> pastBooking = getLastBookings(ownerId);
        List<Booking> futureBooking = getNextBookings(ownerId);
        for (ItemResponseDto itemDto : items) {
            setLastBooking(itemDto, pastBooking);
            setNextBooking(itemDto, futureBooking);
        }
        return items;
    }

    @Override
    public ItemResponseDto getById(Integer itemId, Integer userId) {
        userRepository.getById(userId);
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new NotFoundException("Item");
        }
        ItemResponseDto itemDto = ItemMapper.makeItemDto(item.get());
        if (Objects.equals(item.get().getOwner().getId(), userId)) {
            List<Booking> pastBooking = getLastBookings(userId);
            List<Booking> futureBooking = getNextBookings(userId);
            setLastBooking(itemDto, pastBooking);
            setNextBooking(itemDto, futureBooking);
        }
        List<Comment> comments = commentRepository.findByItemId(itemDto.getId());
        itemDto.setComments(comments);
        return itemDto;
    }

    @Override
    @Transactional
    public ItemResponseDto save(ItemDto itemDto, Integer ownerId) {
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
    public ItemResponseDto update(ItemDto itemDto, Integer itemId, Integer ownerId) {
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
        return ItemMapper.makeItemDto(item);
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
        ItemResponseDto itemDto = ItemMapper.makeItemDto(item);
        List<Booking> pastBooking = getLastBookings(userId);
        List<Booking> futureBooking = getNextBookings(userId);
        setLastBooking(itemDto, pastBooking);
        setNextBooking(itemDto, futureBooking);
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
    public List<ItemResponseDto> search(String text) {
        return ItemMapper.makeListItemDto(itemRepository.search(text));
    }

    @Override
    public void delete(Integer itemId) {
        itemRepository.deleteById(itemId);
    }

    public Item checkById(Integer id) {
        return itemRepository.getById(id);
    }

    private List<Booking> getLastBookings(Integer userId) {
        return bookingRepository.findPastBooking(LocalDateTime.now(), userId,
                        Sort.by(DESC, "end")).stream()
                .filter(booking -> !booking.getStatus().equals(Status.REJECTED)).collect(toList());
    }

    private List<Booking> getNextBookings(Integer userId) {
        return bookingRepository.findFutureBooking(LocalDateTime.now(), userId,
                        Sort.by(DESC, "end")).stream()
                .filter(booking -> booking.getStatus().equals(Status.APPROVED)).collect(toList());
    }

    private void setLastBooking(ItemResponseDto itemDto, List<Booking> pastBooking) {
        List<LastBooking> lastBookings = bookingToLastBooking(pastBooking.stream()
                .filter(booking -> booking.getItem().getId().equals(itemDto.getId()))
                .sorted(Comparator.comparing(Booking::getEnd)).collect(toList()));
        if(!lastBookings.isEmpty()) {
            itemDto.setLastBooking(lastBookings.get(0));
        }
    }

    private void setNextBooking(ItemResponseDto itemDto, List<Booking> futureBooking) {
        List<NextBooking> nextBookings = bookingToNexBooking(futureBooking.stream()
                .filter(booking -> booking.getItem().getId().equals(itemDto.getId()))
                .sorted(Comparator.comparing(Booking::getStart)).collect(toList()));
        if(!nextBookings.isEmpty()) {
            itemDto.setNextBooking(nextBookings.get(0));
        }
    }

    private List<LastBooking> bookingToLastBooking(List<Booking> bookings) {
        return bookings.stream()
                .map(booking -> {
                    LastBooking lastBooking = new LastBooking();
                    lastBooking.setId(booking.getId());
                    lastBooking.setBookerId(booking.getBooker().getId());
                    return lastBooking;
                })
                .collect(toList());
    }

    private List<NextBooking> bookingToNexBooking(List<Booking> bookings) {
        return bookings.stream().map(booking -> {
                    NextBooking nextBooking = new NextBooking();
                    nextBooking.setId(booking.getId());
                    nextBooking.setBookerId(booking.getBooker().getId());
                    return nextBooking;
                })
                .collect(toList());
    }
}
