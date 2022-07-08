package ru.turaev.userservice.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import ru.turaev.userservice.dto.LoginViewModel;
import ru.turaev.userservice.entity.User;
import ru.turaev.userservice.exception.UserNotFoundException;
import ru.turaev.userservice.repository.UserRepository;
import ru.turaev.userservice.securityutil.JwtTokenProvider;
import ru.turaev.userservice.service.LoginService;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public ResponseEntity<?> authenticate(LoginViewModel model) throws UserNotFoundException, AuthenticationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(model.getUsername(), model.getPassword()));
        User user = userRepository.findByUsername(model.getUsername()).orElseThrow(() -> new UserNotFoundException("Пользователь с данным id не найден"));
        String token = jwtTokenProvider.createJwtToken(user.getUsername(), user.getRole());
        Map<Object, Object> response = new HashMap<>();
        response.put("login", model.getUsername());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
}
