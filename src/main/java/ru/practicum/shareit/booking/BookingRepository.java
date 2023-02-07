package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingRepository {

    Collection<Booking> getAll();
    Booking create(Booking booking);
    Booking update(Booking booking);
    void delete(Booking id);
}
