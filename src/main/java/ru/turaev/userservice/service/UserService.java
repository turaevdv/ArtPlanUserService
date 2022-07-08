package ru.turaev.userservice.service;

import ru.turaev.userservice.dto.UserDto;
import ru.turaev.userservice.entity.User;

import java.util.List;

public interface UserService {
    void giveAdminRootToUser(long id);

    User save(User user);

    UserDto findById(long id);

    UserDto findByUsername(String username);

    List<UserDto> findAllUsers();

    UserDto findUserOfAnyTypes(long id);

    List<UserDto> findAllUsersOfAnyTypes();

    UserDto deleteById(long id);
}
