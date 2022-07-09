package ru.turaev.userservice.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.turaev.userservice.entity.User;
import ru.turaev.userservice.enums.Role;
import ru.turaev.userservice.repository.UserRepository;
import ru.turaev.userservice.service.RegistrationService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ContextListener implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final RegistrationService registrationService;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Optional<User> optionalAdmin = userRepository.findByUsernameAndIsActiveIsTrue("admin");
        if (optionalAdmin.isEmpty()) {
            registerAdmin();
            return;
        }
        User admin = optionalAdmin.get();
        if (!admin.isNonLocked()) {
            admin.setNonLocked(true);
        }
    }

    private void registerAdmin() {
        User newAdmin = User.builder()
                .username("admin")
                .password("admin")
                .role(Role.ADMIN)
                .isActive(true)
                .isNonLocked(true)
                .build();
        registrationService.registerUserOfAnyType(newAdmin);
    }
}