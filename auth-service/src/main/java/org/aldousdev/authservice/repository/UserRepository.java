package org.aldousdev.authservice.repository;

import org.aldousdev.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); //Найти пользователя по email (уникальное поле) Это Optional
    boolean existsByEmail(String email);
}
