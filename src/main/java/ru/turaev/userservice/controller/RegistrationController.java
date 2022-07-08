package ru.turaev.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.turaev.userservice.dto.LoginViewModel;
import ru.turaev.userservice.dto.UserDto;
import ru.turaev.userservice.entity.User;
import ru.turaev.userservice.service.LoginService;
import ru.turaev.userservice.service.RegistrationService;

import java.util.Map;

@RestController
@RequestMapping("userservice/api/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;
    private final LoginService loginService;

    @GetMapping
    public ResponseEntity<?> isUsernameUnused(@RequestParam("username") String username) {
        if (username.isBlank()) {
            return new ResponseEntity<>("The request parameter is empty", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(registrationService.isUsernameUnused(username));
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody LoginViewModel loginViewModel) {
         UserDto userDto = registrationService.registerUser(loginViewModel);
         ResponseEntity<Map<Object, Object>> responseEntity = loginService.authenticate(loginViewModel);
         responseEntity.getBody().put("id", userDto.getId());
         return responseEntity;
    }

    @PostMapping("/any")
    public UserDto registerUserOfAnyType(@RequestBody User user) {
        return registrationService.registerUserOfAnyType(user);
    }
}