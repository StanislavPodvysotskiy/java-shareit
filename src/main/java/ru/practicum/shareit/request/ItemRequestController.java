package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.utility.Create;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @GetMapping
    public List<ItemRequestResponseDto> findAllOwn(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                   HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemRequestService.findAllOwn(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> findAll(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size,
                                         HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemRequestService.findAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto findById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                           @PathVariable Integer requestId,
                                           HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemRequestService.findById(userId, requestId);
    }

    @PostMapping
    public ItemRequestResponseDto save(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                       @RequestBody @Validated(Create.class) ItemRequestDto itemRequestDto,
                                       HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemRequestService.save(userId, itemRequestDto);
    }
}
