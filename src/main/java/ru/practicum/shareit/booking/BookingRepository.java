package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Override
    @Query("select b from Booking b where b.id = ?1")
    Booking getById(Integer id);

    List<Booking> findByBookerId(Integer bookerId);

    @Query("select b from Booking b where b.item.owner.id = ?1")
    List<Booking> findByOwnerId(Integer ownerId);

    @Query("select b from Booking b where b.start < ?1 and b.end > ?1 and b.booker.id = ?2 " +
            "or b.start < ?1 and b.end > ?1 and b.item.owner.id = ?2")
    List<Booking> findCurrentBooking(LocalDateTime localDateTime, Integer userId);

    @Query("select b from Booking b where b.end < ?1 and b.booker.id = ?2 or b.end < ?1 and b.item.owner.id = ?2")
    List<Booking> findPastBooking(LocalDateTime localDateTime, Integer bookerId);

    @Query("select b from Booking b where b.start > ?1 and b.booker.id = ?2 or b.start > ?1 and b.item.owner.id = ?2")
    List<Booking> findFutureBooking(LocalDateTime localDateTime, Integer bookerId);

    @Query("select b from Booking b where b.status = ?1 and b.booker.id = ?2 or b.status = ?1 and b.item.owner.id = ?2")
    List<Booking> findByStatusAndBookerId(String status, Integer bookerId);

}
