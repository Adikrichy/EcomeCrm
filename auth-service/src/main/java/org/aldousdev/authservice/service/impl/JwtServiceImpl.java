package org.aldousdev.authservice.service.impl;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.aldousdev.authservice.exception.JwtTokenException;
import org.aldousdev.authservice.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    private SecretKey secretKey;

    @PostConstruct
    public void init(){
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

     @Override
     public String generateToken(UserDetails userDetails){
         Map<String,Object> claims = new HashMap<>(); //создали claims
         claims.put("role",userDetails.getAuthorities()); // Положили роль
         claims.put("email",userDetails.getUsername()); // Положили email

         return Jwts.builder()
                 .setClaims(claims)
                 .setSubject(userDetails.getUsername())
                 .setIssuedAt(new Date(System.currentTimeMillis()))
                 .setExpiration(new Date(System.currentTimeMillis()+ expiration))
                 .signWith(secretKey,SignatureAlgorithm.HS256)
                 .compact();

     }
     @Override
     public String extractUsername(String token){
         try {
             return Jwts.parserBuilder()
                     .setSigningKey(secretKey)
                     .build()
                     .parseClaimsJws(token)
                     .getBody()
                     .getSubject();

         }catch (Exception e){
              throw new RuntimeException("JWT token extraction failed",e);
         }
     }

     @Override
     public boolean isValidToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        }catch (JwtException | IllegalArgumentException e) {
            throw new JwtTokenException("JWT token extraction failed",e);
        }

     }

     private boolean isTokenExpired(String token) {
         Date expirationDate = Jwts.parserBuilder()
                 .setSigningKey(secretKey)
                 .build()
                 .parseClaimsJws(token)
                 .getBody()
                 .getExpiration();
         return expirationDate.before(new Date());
     }


}
