package ru.turaev.userservice.controller;

import org.springframework.web.bind.annotation.*;
import ru.turaev.userservice.dto.UserDto;
import ru.turaev.userservice.service.UserService;

import java.util.List;

@RestController
@RequestMapping("userservice/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable long id) {
        return userService.findById(id);
    }

    @GetMapping("/any")
    public List<UserDto> getAllUsersOfAnyTypes() {
        return userService.findAllUsersOfAnyTypes();
    }

    @GetMapping("/any/{id}")
    public UserDto getUserOfAnyTypes(@PathVariable long id) {
        return userService.findUserOfAnyTypes(id);
    }

    @PutMapping("/{id}")
    public void giveAdminRootToUser(@PathVariable long id) {
        userService.giveAdminRootToUser(id);
    }

    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable long id) {
        return userService.deleteById(id);
    }
}
