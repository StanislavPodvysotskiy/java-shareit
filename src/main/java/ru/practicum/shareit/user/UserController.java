package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAll(HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable Integer userId, HttpServletRequest request) {
        log.info("Получен {} запрос {} пользователя с ID {}", request.getMethod(), request.getRequestURI(), userId);
        return userService.getById(userId);
    }

    @PostMapping
    public User save(@RequestBody @Valid User user, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return userService.save(user);
    }

    @PatchMapping("/{userId}")
    public User update(@RequestBody @Valid UserDto userDto, @PathVariable Integer userId, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return userService.update(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Integer userId, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        userService.delete(userId);
    }
}
