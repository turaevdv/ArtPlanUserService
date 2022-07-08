package ru.turaev.userservice.service;

import ru.turaev.userservice.dto.LoginViewModel;
import ru.turaev.userservice.dto.UserDto;
import ru.turaev.userservice.entity.User;

public interface RegistrationService {
    UserDto registerUser(LoginViewModel loginViewModel);
    UserDto registerUserOfAnyType(User user);
    boolean isUsernameUnused(String username);
}
