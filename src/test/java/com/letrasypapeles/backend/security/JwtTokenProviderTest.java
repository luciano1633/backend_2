package com.letrasypapeles.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    private Key jwtSecretKey; // Cambiado a Key
    private long jwtExpirationInMs;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Generar una clave secreta segura para las pruebas
        jwtSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Genera una clave segura para HS512
        jwtExpirationInMs = 3600000; // 1 hora

        // Usar ReflectionTestUtils para inyectar la clave y la expiración
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", java.util.Base64.getEncoder().encodeToString(jwtSecretKey.getEncoded())); // Inyectar como String
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", jwtExpirationInMs);
    }

    @Test
    void testGenerateToken() {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("testuser", "password", authorities);

        String token = jwtTokenProvider.generateToken(authentication);
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Verificar que el token es válido
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals("testuser", jwtTokenProvider.getUsernameFromJWT(token));
    }

    @Test
    void testGetUsernameFromJWT() {
        String username = "testuser";
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512) // Usar el objeto Key
                .compact();

        assertEquals(username, jwtTokenProvider.getUsernameFromJWT(token));
    }

    @Test
    void testValidateToken_validToken() {
        String username = "testuser";
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512) // Usar el objeto Key
                .compact();

        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void testValidateToken_invalidToken() {
        String invalidToken = "invalid.token.string";
        assertFalse(jwtTokenProvider.validateToken(invalidToken));
    }

    @Test
    void testValidateToken_expiredToken() {
        String username = "testuser";
        String expiredToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - jwtExpirationInMs - 1000)) // Token expirado
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512) // Usar el objeto Key
                .compact();

        assertFalse(jwtTokenProvider.validateToken(expiredToken));
    }

    @Test
    void testValidateToken_unsupportedJwtException() {
        String unsupportedToken = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS256), SignatureAlgorithm.HS256) // Algoritmo diferente
                .compact();

        assertFalse(jwtTokenProvider.validateToken(unsupportedToken));
    }

    @Test
    void testValidateToken_malformedJwtException() {
        String malformedToken = "header.payload"; // Falta la firma
        assertFalse(jwtTokenProvider.validateToken(malformedToken));
    }

    @Test
    void testValidateToken_signatureException() {
        Key wrongSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Generar otra clave segura
        String tokenWithWrongSignature = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(wrongSecretKey, SignatureAlgorithm.HS512) // Usar la clave incorrecta
                .compact();

        assertFalse(jwtTokenProvider.validateToken(tokenWithWrongSignature));
    }
}
