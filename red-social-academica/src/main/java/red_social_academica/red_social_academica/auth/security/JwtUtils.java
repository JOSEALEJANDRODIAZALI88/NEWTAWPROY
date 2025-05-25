package red_social_academica.red_social_academica.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Clase utilitaria para la generación y validación de tokens JWT.
 */
@Component
public class JwtUtils {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Genera un token JWT firmado a partir de los datos de autenticación.
     * 
     * @param authentication objeto con el usuario autenticado
     * @return token JWT generado
     */
    public String generateJwtToken(Authentication authentication) {
        String username = authentication.getName();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extrae el username (subject) desde un token JWT.
     * 
     * @param token JWT
     * @return nombre de usuario
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Verifica si un token es válido (firma + expiración).
     * 
     * @param token JWT
     * @return true si es válido
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            System.err.println("Token inválido: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("Token expirado: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("Token no soportado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Token vacío: " + e.getMessage());
        }
        return false;
    }

    /**
     * Extrae el token JWT desde el encabezado Authorization de la request.
     * 
     * @param request HTTP
     * @return el token si existe
     */
    public String parseJwt(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}