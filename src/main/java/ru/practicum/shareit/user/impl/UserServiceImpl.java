package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;

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
        User user = userRepository.getById(userId);
        if (user == null) {
            throw new NotFoundException("User");
        }
        return user;
    }

    @Override
    @Transactional
    public UserDto save(UserDto userdto) {
        /*User checkEmail = userRepository.checkEmail(user.getEmail());
        if (checkEmail != null) {
            throw new AlreadyExistException("User with email " + user.getEmail());
        }*/
        return UserMapper.makeUserDto(userRepository.save(UserMapper.makeUser(userdto)));
    }

    @Override
    @Transactional
    public User update(User user, Integer userId) {
        User savedUser = userRepository.getById(userId);
        User checkEmail = userRepository.checkEmail(user.getEmail());
        if (user.getEmail() != null) {
            savedUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            savedUser.setName(user.getName());
        }
        if (checkEmail != null && checkEmail.getEmail().equals(savedUser.getEmail())
                && !Objects.equals(checkEmail.getId(), userId)) {
            throw new AlreadyExistException("User with email " + user.getEmail());
        }
        return userRepository.save(savedUser);
    }

    @Override
    public void delete(Integer userId) {
        userRepository.deleteById(userId);
    }

}
