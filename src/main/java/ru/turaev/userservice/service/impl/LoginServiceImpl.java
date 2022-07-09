package ru.turaev.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.turaev.userservice.dto.LoginViewModel;
import ru.turaev.userservice.entity.User;
import ru.turaev.userservice.exception.CredentialException;
import ru.turaev.userservice.exception.IllegalUserException;
import ru.turaev.userservice.exception.UserNotFoundException;
import ru.turaev.userservice.mapper.UserMapper;
import ru.turaev.userservice.repository.UserRepository;
import ru.turaev.userservice.securityutil.JwtTokenProvider;
import ru.turaev.userservice.service.LoginAttemptService;
import ru.turaev.userservice.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final SessionFactory sessionFactory;

    @Value("${login.attempt.count}")
    private int ATTEMPT_COUNT;

    @Transactional(noRollbackFor = CredentialException.class)
    @Override
    public ResponseEntity<Map<Object, Object>> authenticate(LoginViewModel model) {
        User user = userRepository.findByUsernameAndIsActiveIsTrue(model.getUsername())
                .orElseThrow(() -> new UserNotFoundException("The user with this username was not found"));

        if (!user.isNonLocked()) {
            throw new IllegalUserException("The user is blocked", HttpStatus.FORBIDDEN, userMapper.toDto(user));
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(model.getUsername(), model.getPassword()));
            loginAttemptService.deleteUserAttempts(user);
        } catch (AuthenticationException ex) {
            loginAttemptService.saveAttempt(user);
            if (loginAttemptService.numberOfUserAttempt(user) >= ATTEMPT_COUNT) {
                user.setNonLocked(false);
            }
            throw new CredentialException("Password is incorrect");
        }

        String token = jwtTokenProvider.createJwtToken(user.getUsername(), user.getRole());
        Map<Object, Object> response = new HashMap<>();
        response.put("login", model.getUsername());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @Override
    public boolean verifyToken(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token.isBlank()) {
            return false;
        }
        return jwtTokenProvider.validateToken(token);
    }
}
