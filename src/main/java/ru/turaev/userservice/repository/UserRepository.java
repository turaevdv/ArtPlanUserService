package ru.turaev.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.turaev.userservice.entity.User;
import ru.turaev.userservice.enums.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndIsActiveIsTrue(long id);
    List<User> findAllByIsActiveIsTrue();
    Optional<User> findByUsername(String username);
    Optional<User> findByIdAndIsActiveIsTrueAndRoleIs(long id, Role role);
    List<User> findAllByIsActiveIsTrueAndRoleIs(Role role);
}
