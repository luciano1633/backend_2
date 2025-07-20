package com.letrasypapeles.backend.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JwtAuthenticationResponseTest {

    @Test
    void testJwtAuthenticationResponseCreation() {
        JwtAuthenticationResponse response = new JwtAuthenticationResponse("testToken");
        assertNotNull(response);
        assertEquals("testToken", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
    }

    @Test
    void testSetters() {
        JwtAuthenticationResponse response = new JwtAuthenticationResponse("initialToken");
        response.setAccessToken("newToken");
        response.setTokenType("Custom");

        assertEquals("newToken", response.getAccessToken());
        assertEquals("Custom", response.getTokenType());
    }

    @Test
    void testEquality() {
        JwtAuthenticationResponse response1 = new JwtAuthenticationResponse("token1");
        JwtAuthenticationResponse response2 = new JwtAuthenticationResponse("token1");
        JwtAuthenticationResponse response3 = new JwtAuthenticationResponse("token2");

        // Verify that objects with the same accessToken are considered "equal" by comparing their fields
        assertEquals(response1.getAccessToken(), response2.getAccessToken());
        assertEquals(response1.getTokenType(), response2.getTokenType());

        // Verify that objects with different accessToken are considered "not equal"
        assertNotEquals(response1.getAccessToken(), response3.getAccessToken());
    }
}
