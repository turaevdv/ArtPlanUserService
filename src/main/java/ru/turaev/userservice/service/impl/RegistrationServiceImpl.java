package ru.turaev.userservice.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.turaev.userservice.dto.UserDto;
import ru.turaev.userservice.entity.User;
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
    public UserDto registerUserOfAnyType(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return userMapper.toDto(user);
    }
}
