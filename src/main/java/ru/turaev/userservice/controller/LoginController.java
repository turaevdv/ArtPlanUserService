package ru.turaev.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.turaev.userservice.dto.LoginViewModel;
import ru.turaev.userservice.exception.JwtAuthenticationException;
import ru.turaev.userservice.service.LoginService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("userservice/api/auth")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginViewModel model) {
        return loginService.authenticate(model);
    }

    @GetMapping("/token-verification")
    public boolean verifyToken(HttpServletRequest request) {
        try {
            return loginService.verifyToken(request);
        } catch (JwtAuthenticationException ex) {
            return false;
        }
    }
}
