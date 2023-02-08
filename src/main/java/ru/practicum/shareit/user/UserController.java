package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll(HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        List<UserDto> usersDto = new ArrayList<>();
        for (User u : userService.getAll()) {
            usersDto.add(UserMapper.makeUserDto(u));
        }
        return usersDto;
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable Integer userId, HttpServletRequest request) {
        log.info("Получен {} запрос {} пользователя с ID {}", request.getMethod(), request.getRequestURI(), userId);
        return UserMapper.makeUserDto(userService.getById(userId));
    }

    @PostMapping
    public UserDto save(@RequestBody @Validated(Create.class) UserDto userDto, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return UserMapper.makeUserDto(userService.save(UserMapper.makeUser(new User(), userDto)));
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody @Valid UserDto userDto,
                          @PathVariable Integer userId, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return UserMapper.makeUserDto(userService.update(UserMapper.makeUser(new User(), userDto), userId));
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Integer userId, HttpServletRequest request) {
        log.info("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        userService.delete(userId);
    }
}
