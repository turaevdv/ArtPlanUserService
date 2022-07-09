package ru.turaev.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.turaev.userservice.entity.LoginAttempt;
import ru.turaev.userservice.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
    int countLoginAttemptsByUserAndTimeGreaterThan(User user, LocalDateTime time);

    List<LoginAttempt> getLoginAttemptByUser(User user);

    @Modifying
    @Query("delete from LoginAttempt a where a.user.id = :userId")
    void deleteAllByUserId(long userId);
}
