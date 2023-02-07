package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(value = "X-Sharer-User-Id") Integer ownerId,
                                HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemService.getAll(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader(value = "X-Sharer-User-Id") Integer ownerId,
                           @PathVariable Integer itemId, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemService.getById(itemId, ownerId);
    }

    @PostMapping
    public ItemDto save(@RequestHeader(value = "X-Sharer-User-Id") Integer ownerId,
                        @RequestBody @Valid ItemDto itemDto, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemService.save(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(value = "X-Sharer-User-Id") Integer ownerId,
                          @PathVariable Integer itemId, @RequestBody ItemDto itemDto, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemService.update(itemDto, itemId, ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam (value = "text") String text, HttpServletRequest request) {
        log.info("Получен {} запрос {} поиск {}", request.getMethod(), request.getRequestURI(), text);
        if(text.isBlank()) {
            return new ArrayList<>();
        }
        return itemService.search(text);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable Integer itemId, HttpServletRequest request) {
        System.out.println("Function not available now");
    }
}
