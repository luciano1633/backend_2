package com.letrasypapeles.backend.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginRequestTest {

    @Test
    void testLoginRequestCreation() {
        LoginRequest loginRequest = new LoginRequest("testuser", "password123");
        assertNotNull(loginRequest);
        assertEquals("testuser", loginRequest.getUsername());
        assertEquals("password123", loginRequest.getPassword());
    }

    @Test
    void testSetters() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("anotheruser");
        loginRequest.setPassword("newpassword");

        assertEquals("anotheruser", loginRequest.getUsername());
        assertEquals("newpassword", loginRequest.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        LoginRequest loginRequest1 = new LoginRequest("user1", "pass1");
        LoginRequest loginRequest2 = new LoginRequest("user1", "pass1");
        LoginRequest loginRequest3 = new LoginRequest("user2", "pass2");

        assertEquals(loginRequest1, loginRequest2);
        assertNotEquals(loginRequest1, loginRequest3);
        assertEquals(loginRequest1.hashCode(), loginRequest2.hashCode());
        assertNotEquals(loginRequest1.hashCode(), loginRequest3.hashCode());
    }
}
