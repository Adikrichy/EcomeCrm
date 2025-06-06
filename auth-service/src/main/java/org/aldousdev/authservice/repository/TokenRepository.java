package org.aldousdev.authservice.repository;

import org.aldousdev.authservice.entity.Token;
import org.aldousdev.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
     List<Token> findByUser(User user); //Найти всех пользователей с ролью WORKER Это про List тут только по одному
     Optional<Token> findByToken(String token);
     // Есть set для получения все роли
}
