package org.aldousdev.authservice.service;

import org.aldousdev.authservice.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
     String extractUsername(String token);
     String generateToken(UserDetails userDetails); //генерация токена
     boolean isValidToken(String token,UserDetails userDetails); // Проверка токена

     //Класс User может реализовывать UserDetails, но если ты хочешь разделить сущность
    // и Spring Security-логику, то правильнее — оставить User отдельно, а UserDetailsImpl сделать отдельным классом-обёрткой (это более чистая архитектура).
}
