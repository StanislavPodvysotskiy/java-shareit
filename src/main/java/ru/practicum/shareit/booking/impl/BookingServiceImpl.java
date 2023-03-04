package ru.practicum.shareit.booking.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.Status;
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
        return BookingMapper.makeListBookingDto(bookingRepository
                .findByBookerId(userId, Sort.by(Sort.Direction.DESC, "end")));
    }

    @Override
    public List<BookingResponseDto> findByOwnerId(Integer ownerId) {
        checkUser(ownerId);
        return BookingMapper.makeListBookingDto(bookingRepository
                .findByOwnerId(ownerId, Sort.by(Sort.Direction.DESC, "end")));
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
    public List<BookingResponseDto> findByStateUser(Integer userId, String state) {
        List<Booking> bookings;
        switch (state) {
            case "WAITING":
                bookings = bookingRepository.findByStatusAndBookerId(Status.WAITING, userId,
                        Sort.by(Sort.Direction.DESC, "end"));
                break;
            case "REJECTED":
                bookings = bookingRepository.findByStatusAndBookerId(Status.REJECTED, userId,
                        Sort.by(Sort.Direction.DESC, "end"));
                break;
            case "CURRENT":
                bookings = bookingRepository.findCurrentBooking(LocalDateTime.now(), userId);
                break;
            case "PAST":
                bookings = bookingRepository.findPastBooking(LocalDateTime.now(), userId,
                        Sort.by(Sort.Direction.DESC, "end"));
                break;
            case "FUTURE":
                bookings = bookingRepository.findFutureBooking(LocalDateTime.now(), userId,
                        Sort.by(Sort.Direction.DESC, "end"));
                break;
            default:
                throw new BookingStatusException("Unknown state: " + state);
        }
        return BookingMapper.makeListBookingDto(bookings);
    }

    @Override
    public List<BookingResponseDto> findByStateOwner(Integer ownerId, String state) {
        List<Booking> bookings;
        switch (state) {
            case "WAITING":
                bookings = bookingRepository.findByStatusAndOwnerId(Status.WAITING, ownerId,
                        Sort.by(Sort.Direction.DESC, "end"));
                break;
            case "REJECTED":
                bookings = bookingRepository.findByStatusAndOwnerId(Status.REJECTED, ownerId,
                        Sort.by(Sort.Direction.DESC, "end"));
                break;
            case "CURRENT":
                bookings = bookingRepository.findCurrentBooking(LocalDateTime.now(), ownerId);
                break;
            case "PAST":
                bookings = bookingRepository.findPastBooking(LocalDateTime.now(), ownerId,
                        Sort.by(Sort.Direction.DESC, "end"));
                break;
            case "FUTURE":
                bookings = bookingRepository.findFutureBooking(LocalDateTime.now(), ownerId,
                        Sort.by(Sort.Direction.DESC, "end"));
                break;
            default:
                throw new BookingStatusException("Unknown state: " + state);
        }
        return BookingMapper.makeListBookingDto(bookings);
    }

    @Override
    @Transactional
    public BookingResponseDto save(BookingDto bookingDto, Integer bookerId) {
        Item item = itemRepository.getById(bookingDto.getItemId());
        if (item == null) {
            throw new NotFoundException("Item");
        }
        User booker = userRepository.getById(bookerId);
        User owner = userRepository.getById(item.getOwner().getId());
        if (booker == null || owner == null) {
            throw new NotFoundException("User");
        }
        if (Objects.equals(owner.getId(), bookerId)) {
            throw new BookingException("Owner can't booking item");
        }
        if (!item.getAvailable()) {
            throw new ItemNotAvailableException("Item not available now");
        }
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        return BookingMapper.makeBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponseDto update(Integer userId, Boolean isApproved, Integer bookingId) {
        Booking booking = bookingRepository.getById(bookingId);
        Item item = itemRepository.getById(booking.getItem().getId());
        if (booking.getStatus().equals(Status.APPROVED) || booking.getStatus().equals(Status.REJECTED)) {
            throw new BookingStatusException("Booking status can not be changed");
        }
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new NotFoundException("Item user");
        }
        if (isApproved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
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
