package org.aldousdev.authservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация JWT токенов
 * Загружает настройки из application.yml
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtConfig {
    private String secret; // Секретный ключ
    private long accessTokenExpiration = 90000; //15 минут
    private long refreshTokenExpiration = 604800000; //7 days
    private CookieConfig cookie = new CookieConfig();

    @Getter
    @Setter
    public static class CookieConfig {
        private String accessTokenName = "access_token"; //Имя cookie для access токена
        private String refreshTokenName = "refresh_token"; // Name for cookie for refresh token
        private String domain = "localhost"; // Domain for cookie
        private boolean secure = false; // only HTTPS
        private String sameSite = "Strict"; //SameSite
        private int maxAge = 900; // 15 minutes
        private int refreshMaxAge = 604800; // 7 days
        private String path = "/"; //path for token
        private String refreshPath = "/auth/refresh"; // path for refresh

    }
    /**
     * Получить время жизни access токена в секундах
     */
    public int getAccessTokenExpirationInSeconds() {
        return(int)(accessTokenExpiration/1000);
    }
    public int getRefreshTokenExpirationInSeconds() {
        return(int)(refreshTokenExpiration/1000); // get refresh token in seconds
    }
}
