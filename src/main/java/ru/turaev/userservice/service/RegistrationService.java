package ru.turaev.userservice.service;

import ru.turaev.userservice.dto.UserDto;
import ru.turaev.userservice.entity.User;

public interface RegistrationService {
    UserDto registerUserOfAnyType(User user);
}
