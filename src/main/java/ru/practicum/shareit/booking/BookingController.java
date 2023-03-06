package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.exception.BookingStatusException;
import ru.practicum.shareit.utility.Create;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<BookingResponseDto> findByBookerId(@RequestHeader(value = "X-Sharer-User-Id") Integer userId,
                                    @RequestParam(value = "state", defaultValue = "ALL") String state,
                                    HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        State enumState = getStateOrException(state);
        if (enumState.equals(State.ALL)) {
            return bookingService.findByBookerId(userId);
        }
        return bookingService.findByStateUser(userId, enumState);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> findByOwnerId(@RequestHeader(value = "X-Sharer-User-Id") Integer ownerId,
                                    @RequestParam(value = "state", defaultValue = "ALL") String state,
                                    HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        State enumState = getStateOrException(state);
        if (enumState.equals(State.ALL)) {
            return bookingService.findByOwnerId(ownerId);
        }
        return bookingService.findByStateOwner(ownerId, enumState);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(@RequestHeader(value = "X-Sharer-User-Id") Integer userId,
                           @PathVariable Integer bookingId, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return bookingService.getById(userId, bookingId);
    }

    @PostMapping
    public BookingResponseDto save(@RequestHeader(value = "X-Sharer-User-Id") Integer bookerId,
                           @RequestBody @Validated(Create.class) BookingDto bookingDto, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return bookingService.save(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto update(@RequestHeader(value = "X-Sharer-User-Id") Integer userId,
                           @RequestParam(value = "approved") Boolean isApproved,
                           @PathVariable Integer bookingId, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return bookingService.update(userId, isApproved, bookingId);
    }

    private State getStateOrException(String state) {
        State enumState;
        try {
            enumState = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new BookingStatusException("Unknown state: " + state);
        }
        return enumState;
    }

}
