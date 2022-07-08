package ru.turaev.userservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.turaev.userservice.dto.UserDto;
import ru.turaev.userservice.entity.User;
import ru.turaev.userservice.service.RegistrationService;

@RestController
@RequestMapping("userservice/api/registration")
public class RegistrationController {
   private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public UserDto registerUserOfAnyType(@RequestBody User user) {
        return registrationService.registerUserOfAnyType(user);
    }
}
