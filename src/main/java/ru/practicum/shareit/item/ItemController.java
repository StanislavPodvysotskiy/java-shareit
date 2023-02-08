package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        List<ItemDto> itemList = new ArrayList<>();
        for (Item item : itemService.getAll(ownerId)) {
            itemList.add(ItemMapper.makeItemDto(item));
        }
        return itemList;
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader(value = "X-Sharer-User-Id") Integer ownerId,
                           @PathVariable Integer itemId, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return ItemMapper.makeItemDto(itemService.getById(itemId, ownerId));
    }

    @PostMapping
    public ItemDto save(@RequestHeader(value = "X-Sharer-User-Id") Integer ownerId,
                        @RequestBody @Valid ItemDto itemDto, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return ItemMapper.makeItemDto(itemService.save(ItemMapper.makeItem(new Item(), itemDto), ownerId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(value = "X-Sharer-User-Id") Integer ownerId,
                          @PathVariable Integer itemId, @RequestBody ItemDto itemDto, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return ItemMapper.makeItemDto(itemService.update(ItemMapper.makeItem(new Item(), itemDto), itemId, ownerId));
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam (value = "text") String text, HttpServletRequest request) {
        log.info("Получен {} запрос {} поиск {}", request.getMethod(), request.getRequestURI(), text);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : itemService.search(text)) {
            itemsDto.add(ItemMapper.makeItemDto(item));
        }
        return itemsDto;
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable Integer itemId, HttpServletRequest request) {
        log.info("Function not available now");
    }
}
