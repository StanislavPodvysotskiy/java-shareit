package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemResponseDto> getAll(@RequestHeader(value = "X-Sharer-User-Id") Integer ownerId,
                                        HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemService.getAll(ownerId)
                .stream().sorted(Comparator.comparing(ItemResponseDto::getId)).collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getById(@RequestHeader(value = "X-Sharer-User-Id") Integer userId,
                           @PathVariable Integer itemId, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemService.getById(itemId, userId);
    }

    @PostMapping
    public ItemResponseDto save(@RequestHeader(value = "X-Sharer-User-Id") Integer ownerId,
                        @RequestBody @Valid ItemDto itemDto, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemService.save(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update(@RequestHeader(value = "X-Sharer-User-Id") Integer ownerId,
                          @PathVariable Integer itemId, @RequestBody ItemDto itemDto, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemService.update(itemDto, itemId, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto saveComment(@RequestHeader(value = "X-Sharer-User-Id") Integer userId,
                                          @PathVariable Integer itemId, @RequestBody @Valid CommentDto commentDto,
                                          HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemService.saveComment(commentDto, itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> search(@RequestParam (value = "text") String text, HttpServletRequest request) {
        log.info("Получен {} запрос {} поиск {}", request.getMethod(), request.getRequestURI(), text);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemService.search(text);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable Integer itemId, HttpServletRequest request) {
        log.info("Function not available now");
    }

}
