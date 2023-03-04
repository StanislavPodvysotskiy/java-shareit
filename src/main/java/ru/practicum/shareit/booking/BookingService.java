package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    List<BookingResponseDto> findByBookerId(Integer userId);

    BookingResponseDto save(BookingDto bookingDto, Integer bookerId);

    BookingResponseDto update(Integer userId, Boolean isApproved, Integer bookingId);

    BookingResponseDto getById(Integer userId, Integer bookingId);

    List<BookingResponseDto> findByOwnerId(Integer ownerId);

    List<BookingResponseDto> findByStateUser(Integer userId, String state);

    List<BookingResponseDto> findByStateOwner(Integer userId, String state);

}
