package com.lottofun.lottofunrest.security;

import com.lottofun.lottofunrest.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final JwtConfig jwtConfig;

    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    /**
     * Returns the secret key used for signing the JWT tokens.
     *
     * @return the signing {@link Key} derived from the secret
     */
    private Key getKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
    }

    /**
     * Generates a JWT token containing the username as subject,
     * with an expiration time defined in the configuration.
     *
     * @param username the username to be included in the token subject
     * @return the generated JWT token as a String
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username (subject) from the given JWT token.
     *
     * @param token the JWT token to parse
     * @return the username contained in the token subject
     * @throws JwtException if the token is invalid or signature verification fails
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Extract claims from Jwt Token
     *
     * @param token the JWT token to get claims
     * @return Claims if the token is valid
     */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Validates the JWT token by verifying its signature and expiration date.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
