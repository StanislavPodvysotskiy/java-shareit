package ru.practicum.shareit.user.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserRepository;
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
        checkUserById(userId);
        return userMap.get(userId);
    }

    @Override
    public User save(User user) {
        checkUserByEmail(user.getEmail());
        user.setId(id++);
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user, Integer userId) {
        checkUserById(userId);
        for (User u : userMap.values()) {
            if (u.getEmail().equals(user.getEmail()) && !Objects.equals(u.getId(), userId)) {
                throw new AlreadyExistException("User with email " + u.getEmail());
            }
        }
        User savedUser = userMap.get(userId);
        if (user.getName() != null && !user.getName().isBlank()) {
            savedUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            savedUser.setEmail(user.getEmail());
        }
        return savedUser;
    }

    @Override
    public void delete(Integer id) {
        checkUserById(id);
        userMap.remove(id);
    }

    private void checkUserById(Integer id) {
        if (!userMap.containsKey(id)) {
            throw new NotFoundException("User");
        }
    }

    private void checkUserByEmail(String email) {
        for (User u : userMap.values()) {
            if (u.getEmail().equals(email)) {
                throw new AlreadyExistException("User");
            }
        }
    }
}
