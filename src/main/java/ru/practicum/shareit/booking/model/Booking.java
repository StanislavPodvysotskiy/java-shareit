package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@NoArgsConstructor
public class Booking {

    private Integer itemId;
    private Integer ownerId;
    private Integer bookerId;
    private LocalDateTime dateOfStartBooking;
    private LocalDateTime dateOfEndBooking;
    private Boolean isApproved;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(itemId, booking.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }
}
