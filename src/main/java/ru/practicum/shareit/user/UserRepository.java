package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll();
    User getById(Integer userId);
    User save(User user);
    User update(UserDto userDto, Integer userId);
    void delete(Integer id);

}
