package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByBookerId(Integer bookerId, Sort sort);

    @Query("select b from Booking b where b.item.owner.id = ?1")
    List<Booking> findByOwnerId(Integer ownerId, Sort sort);

    @Query("select b from Booking b where b.start < ?1 and b.end > ?1 and b.booker.id = ?2")
    List<Booking> findCurrentBookingUser(LocalDateTime localDateTime, Integer userId);

    @Query("select b from Booking b where b.start < ?1 and b.end > ?1 and b.item.owner.id = ?2")
    List<Booking> findCurrentBookingOwner(LocalDateTime localDateTime, Integer ownerId);

    @Query("select b from Booking b where b.start <= ?1 and b.booker.id = ?2 and b.status = 'APPROVED'")
    List<Booking> findPastBookingUser(LocalDateTime localDateTime, Integer bookerId, Sort sort);

    @Query("select b from Booking b where b.start <= ?1 and b.item.id = ?2 and b.status = 'APPROVED'")
    List<Booking> findPastBookingByItemId(LocalDateTime localDateTime, Integer itemId);

    @Query("select b from Booking b where b.start <= ?1 and b.item.owner.id = ?2 and b.status = 'APPROVED'")
    List<Booking> findPastBookingOwner(LocalDateTime localDateTime, Integer ownerId, Sort sort);

    @Query("select b from Booking b where b.start > ?1 and b.booker.id = ?2")
    List<Booking> findFutureBookingUser(LocalDateTime localDateTime, Integer bookerId, Sort sort);

    @Query("select b from Booking b where b.start > ?1 and b.item.owner.id = ?2")
    List<Booking> findFutureBookingOwner(LocalDateTime localDateTime, Integer ownerId, Sort sort);

    @Query("select b from Booking b where b.start > ?1 and b.item.owner.id = ?2 and b.status = 'APPROVED'")
    List<Booking> findFutureBookingOwnerApproved(LocalDateTime localDateTime, Integer ownerId, Sort sort);

    List<Booking> findByStatusAndBookerId(Status status, Integer bookerId, Sort sort);

    @Query("select b from Booking b where b.status = ?1 and b.item.owner.id = ?2")
    List<Booking> findByStatusAndOwnerId(Status status, Integer bookerId, Sort sort);

}
