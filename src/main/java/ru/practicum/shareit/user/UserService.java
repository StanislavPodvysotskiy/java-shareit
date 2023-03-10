package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(Integer userId);

    UserDto save(UserDto userDto);

    User update(User user, Integer userId);

    void delete(Integer id);

}
