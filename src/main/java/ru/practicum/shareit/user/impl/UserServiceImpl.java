package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Integer userId) {
        return getUserOrException(userId);
    }

    @Override
    @Transactional
    public UserDto save(UserDto userdto) {
        return UserMapper.makeUserDto(userRepository.save(UserMapper.makeUser(userdto)));
    }

    @Override
    @Transactional
    public User update(User user, Integer userId) {
        User savedUser = getUserOrException(userId);
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            savedUser.setEmail(user.getEmail());
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            savedUser.setName(user.getName());
        }
        return savedUser;
    }

    @Override
    public void delete(Integer userId) {
        userRepository.deleteById(userId);
    }

    private User getUserOrException(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId));
    }

}