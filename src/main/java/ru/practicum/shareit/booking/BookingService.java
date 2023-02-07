package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;
import java.util.List;

public interface BookingService {

    Collection<Booking> getAll();
    Booking create(Booking booking);
    Booking update(Booking booking);
    void delete(Booking id);
}
