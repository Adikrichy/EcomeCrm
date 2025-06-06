package org.aldousdev.authservice.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aldousdev.authservice.config.JwtConfig;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CookieUtil {
    private final JwtConfig jwtConfig;
    /**
     * Создает HTTP-only cookie для access токена
     */
    public ResponseCookie createAccessTokenCookie (String token){
        return ResponseCookie.from(jwtConfig.getCookie().getAccessTokenName(),token)
                .httpOnly(true)
                .secure(jwtConfig.getCookie().isSecure())
                .sameSite(jwtConfig.getCookie().getSameSite())
                .domain(jwtConfig.getCookie().getDomain())
                .path(jwtConfig.getCookie().getPath())
                .maxAge(jwtConfig.getCookie().getMaxAge())
                .build();
    }
    /**
     * Создает HTTP-only cookie для refresh токена
     */
    public ResponseCookie createRefreshTokenCookie (String token){
        return ResponseCookie.from(jwtConfig.getCookie().getRefreshTokenName(),token)
                .httpOnly(true)
                .secure(jwtConfig.getCookie().isSecure())
                .sameSite(jwtConfig.getCookie().getSameSite())
                .domain(jwtConfig.getCookie().getDomain())
                .path(jwtConfig.getCookie().getPath())
                .maxAge(jwtConfig.getCookie().getMaxAge())
                .build();
    }
    /**
     * Создает cookie для удаления токена (logout)
     */
    public ResponseCookie createLogoutCookie( String cookieName){
        return ResponseCookie.from(cookieName,"")
                .httpOnly(true)
                .secure(jwtConfig.getCookie().isSecure())
                .sameSite(jwtConfig.getCookie().getSameSite())
                .domain(jwtConfig.getCookie().getDomain())
                .path("/")
                .maxAge(0)
                .build();
    }
    /**
     * Создает cookie для удаления access токена
     */
    public ResponseCookie createAccessTokenLogoutCookie(){
        return createLogoutCookie(jwtConfig.getCookie().getAccessTokenName());
    }
    public ResponseCookie createRefreshTokenLogoutCookie(){
        return createLogoutCookie(jwtConfig.getCookie().getRefreshTokenName());
    }
    /**
     * Извлекает значение cookie по имени из HTTP запроса
     */
    public Optional<String> getCookieValue(HttpServletRequest request, String cookieName) {
        if(request.getCookies() != null){
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(StringUtils::hasText)
                .findFirst();
    }
    public Optional<String> getAccessTokenFromCookies(HttpServletRequest request) {
        return getCookieValue(request, jwtConfig.getCookie().getAccessTokenName());
    }

    /**
     * Извлекает refresh токен из cookies
     */
    public Optional<String> getRefreshTokenFromCookies(HttpServletRequest request) {
        return getCookieValue(request, jwtConfig.getCookie().getRefreshTokenName());
    }

    /**
     * Добавляет cookie в HTTP ответ
     */
    public void addCookieToResponse(HttpServletResponse response, ResponseCookie cookie) {
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * Добавляет access токен cookie в ответ
     */
    public void addAccessTokenCookieToResponse(HttpServletResponse response, String token) {
        ResponseCookie cookie = createAccessTokenCookie(token);
        addCookieToResponse(response, cookie);
        log.debug("Access token cookie added to response");
    }

    /**
     * Добавляет refresh токен cookie в ответ
     */
    public void addRefreshTokenCookieToResponse(HttpServletResponse response, String token) {
        ResponseCookie cookie = createRefreshTokenCookie(token);
        addCookieToResponse(response, cookie);
        log.debug("Refresh token cookie added to response");
    }

    /**
     * Добавляет оба токена (access и refresh) в ответ
     */
    public void addTokenCookiesToResponse(HttpServletResponse response, String accessToken, String refreshToken) {
        addAccessTokenCookieToResponse(response, accessToken);
        addRefreshTokenCookieToResponse(response, refreshToken);
    }

    /**
     * Очищает все токены из cookies (logout)
     */
    public void clearTokenCookies(HttpServletResponse response) {
        ResponseCookie accessTokenCookie = createAccessTokenLogoutCookie();
        ResponseCookie refreshTokenCookie = createRefreshTokenLogoutCookie();

        addCookieToResponse(response, accessTokenCookie);
        addCookieToResponse(response, refreshTokenCookie);

        log.debug("Token cookies cleared from response");
    }

    /**
     * Проверяет, есть ли access токен в cookies
     */
    public boolean hasAccessToken(HttpServletRequest request) {
        return getAccessTokenFromCookies(request).isPresent();
    }

    /**
     * Проверяет, есть ли refresh токен в cookies
     */
    public boolean hasRefreshToken(HttpServletRequest request) {
        return getRefreshTokenFromCookies(request).isPresent();
    }

    /**
     * Получает имя cookie для access токена
     */
    public String getAccessTokenCookieName() {
        return jwtConfig.getCookie().getAccessTokenName();
    }
}
