package ru.turaev.userservice.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.turaev.userservice.dto.LoginViewModel;
import ru.turaev.userservice.dto.UserDto;
import ru.turaev.userservice.entity.User;
import ru.turaev.userservice.exception.UserAlreadyExistException;
import ru.turaev.userservice.exception.UserNotFoundException;
import ru.turaev.userservice.mapper.UserMapper;
import ru.turaev.userservice.service.RegistrationService;
import ru.turaev.userservice.service.UserService;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public RegistrationServiceImpl(UserService userService, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto registerUser(LoginViewModel loginViewModel) {
        User user = userMapper.fromLoginViewModel(loginViewModel);
        return registerUserOfAnyType(user);
    }

    @Override
    public UserDto registerUserOfAnyType(User user) {
        if (!isUsernameUnused(user.getUsername())) {
            throw new UserAlreadyExistException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public boolean isUsernameUnused(String username) {
        try {
            userService.findByUsername(username);
            return false;
        } catch (UserNotFoundException ex) {
            return true;
        }
    }
}
