package ru.practicum.shareit.user.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class UserRepositoryInMemory implements UserRepository {

    private Integer id = 1;
    private final Map<Integer, User> userMap = new HashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User getById(Integer userId) {
        if (!userMap.containsKey(userId)) {
            throw new NotFoundException("User");
        }
        return userMap.get(userId);
    }

    @Override
    public User save(User user) {
        for (User u : userMap.values()) {
            if (u.getEmail().equals(user.getEmail())) {
                throw new AlreadyExistException("User");
            }
        }
        user.setId(id++);
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(UserDto userDto, Integer userId) {
        if (!userMap.containsKey(userId)) {
            throw new NotFoundException("User");
        }
        for (User u : userMap.values()) {
            if (u.getEmail().equals(userDto.getEmail()) && !Objects.equals(u.getId(), userId)) {
                throw new AlreadyExistException("User with email " + u.getEmail());
            }
        }
        User user = UserMapper.makeUser(userMap.get(userId), userDto, userId);
        userMap.put(userId, user);
        return user;
    }

    @Override
    public void delete(Integer id) {
        if (!userMap.containsKey(id)) {
            throw new NotFoundException("User");
        }
        userMap.remove(id);
    }
}
