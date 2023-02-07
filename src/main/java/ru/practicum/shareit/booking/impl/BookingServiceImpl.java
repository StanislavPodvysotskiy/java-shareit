package ru.practicum.shareit.booking.impl;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final List<Booking> bookingList = new ArrayList<>();

    public Collection<Booking> getAll() {
        return bookingList;
    }

    public Booking create(Booking booking) {
        if (bookingList.contains(booking)) {
            throw new ArithmeticException("Booking already exist");
        }
        bookingList.add(booking);
        return booking;
    }

    public Booking update(Booking booking) {
        if (!bookingList.contains(booking)) {
            throw   new NotFoundException("Booking not found");
        }
        bookingList.remove(booking);
        bookingList.add(booking);
        return booking;
    }

    public void delete(Booking booking) {
        if (!bookingList.contains(booking)) {
            throw   new NotFoundException("Booking not found");
        }
        bookingList.remove(booking);
    }

}
