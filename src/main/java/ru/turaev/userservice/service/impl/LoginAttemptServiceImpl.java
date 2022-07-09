package ru.turaev.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.turaev.userservice.entity.LoginAttempt;
import ru.turaev.userservice.entity.User;
import ru.turaev.userservice.repository.LoginAttemptRepository;
import ru.turaev.userservice.service.LoginAttemptService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginAttemptServiceImpl implements LoginAttemptService {
    private final LoginAttemptRepository loginAttemptRepository;

    @Override
    public void saveAttempt(User user) {
        LoginAttempt attempt = LoginAttempt.builder()
                .user(user)
                .time(LocalDateTime.now())
                .build();
        loginAttemptRepository.save(attempt);
    }

    @Override
    public int numberOfUserAttempt(User user) {
        return loginAttemptRepository.countLoginAttemptsByUserAndTimeGreaterThan(user, LocalDateTime.now().minusHours(1));
    }

    @Override
    public void deleteUserAttempts(User user) {
        loginAttemptRepository.deleteAllByUserId(user.getId());
    }
}
