package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.utility.Create;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<BookingResponseDto> findByBookerId(@RequestHeader(value = "X-Sharer-User-Id") Integer userId,
                                    @RequestParam(value = "state", defaultValue = "ALL", required = false) String state,
                                    HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        if (!state.equals("ALL")) {
            return bookingService.findByState(userId, state)
                    .stream().filter(booking -> booking.getBooker().getId().equals(userId))
                    .sorted(Comparator.comparing(BookingResponseDto::getEnd).reversed())
                    .collect(Collectors.toList());
        }
        return bookingService.findByBookerId(userId)
                .stream().sorted(Comparator.comparing(BookingResponseDto::getEnd).reversed())
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> findByOwnerId(@RequestHeader(value = "X-Sharer-User-Id") Integer ownerId,
                                    @RequestParam(value = "state", defaultValue = "ALL", required = false) String state,
                                    HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        if (!state.equals("ALL")) {
            return bookingService.findByState(ownerId, state)
                    .stream().filter(booking -> booking.getItem().getOwner().getId().equals(ownerId))
                    .sorted(Comparator.comparing(BookingResponseDto::getEnd).reversed())
                    .collect(Collectors.toList());
        }
        return bookingService.findByOwnerId(ownerId)
                .stream().sorted(Comparator.comparing(BookingResponseDto::getEnd).reversed())
                .collect(Collectors.toList());
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

}
