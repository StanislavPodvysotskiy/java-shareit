package ru.practicum.shareit.booking.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<BookingResponseDto> findByBookerId(Integer userId) {
        checkUser(userId);
        return BookingMapper.makeListBookingDto(bookingRepository.findByBookerId(userId));
    }

    @Override
    public List<BookingResponseDto> findByOwnerId(Integer ownerId) {
        checkUser(ownerId);
        return BookingMapper.makeListBookingDto(bookingRepository.findByOwnerId(ownerId));
    }

    @Override
    public BookingResponseDto getById(Integer userId, Integer bookingId) {
        Booking booking = bookingRepository.getById(bookingId);
        if (booking == null) {
            throw  new NotFoundException("Booking");
        }
        checkUser(userId);
        if (!Objects.equals(userId, booking.getItem().getOwner().getId())
                && !Objects.equals(userId, booking.getBooker().getId())) {
            throw new NotFoundException("Item user");
        }
        return BookingMapper.makeBookingDto(booking);
    }

    @Override
    public List<BookingResponseDto> findByState(Integer userId, String state) {
        List<Booking> bookings;
        switch (state) {
            case "WAITING":
                bookings = bookingRepository.findByStatusAndBookerId("WAITING", userId);
                break;
            case "CURRENT":
                bookings = bookingRepository.findCurrentBooking(LocalDateTime.now(), userId);
                break;
            case "PAST":
                bookings = bookingRepository.findPastBooking(LocalDateTime.now(), userId);
                break;
            case "FUTURE":
                bookings = bookingRepository.findFutureBooking(LocalDateTime.now(), userId);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByStatusAndBookerId("REJECTED", userId);
                break;
            default:
                throw new BookingStatusException("Unknown state: " + state);
        }
        return BookingMapper.makeListBookingDto(bookings);
    }

    @Override
    @Transactional
    public BookingResponseDto save(BookingDto bookingDto, Integer bookerId) {
        Booking booking = new Booking();
        Item item = itemRepository.getById(bookingDto.getItemId());
        if (item == null) {
            throw new NotFoundException("Item");
        }
        User booker = userRepository.getById(bookerId);
        User owner = userRepository.getById(item.getOwner().getId());
        if (booker == null || owner ==null) {
            throw new NotFoundException("User");
        }
        if (Objects.equals(owner.getId(), bookerId)) {
            throw new BookingException("Owner can't booking item");
        }
        if (!item.getAvailable()) {
            throw new ItemNotAvailableException("Item not available now");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BookingDateTimeException("Wrong end date");
        }
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus("WAITING");
        return BookingMapper.makeBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponseDto update(Integer userId, Boolean isApproved, Integer bookingId) {
        Booking booking = bookingRepository.getById(bookingId);
        Item item = itemRepository.getById(booking.getItem().getId());
        if (booking.getStatus().equals("APPROVED") || booking.getStatus().equals("REJECTED")) {
            throw new BookingStatusException("Booking status can not be changed");
        }
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new NotFoundException("Item user");
        }
        if (isApproved) {
            booking.setStatus("APPROVED");
        } else {
            booking.setStatus("REJECTED");
        }
        bookingRepository.save(booking);
        return BookingMapper.makeBookingDto(booking);
    }

    private void checkUser(Integer userId) {
        User user = userRepository.getById(userId);
        if (user == null) {
            throw new NotFoundException("User");
        }
    }
}
