package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    public void testLoadUserByUsername_UsuarioEncontrado() {
        // Arrange
        String email = "juan.perez@example.com";
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().nombre("USER").build());

        Cliente cliente = Cliente.builder()
                .nombre("Juan Perez")
                .email(email)
                .contraseña("password123")
                .puntosFidelidad(0)
                .roles(roles)
                .build();

        when(clienteRepository.findByEmail(email)).thenReturn(Optional.of(cliente));

        // Act
        UserDetails userDetails = usuarioService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
    }

    @Test
    public void testLoadUserByUsername_UsuarioNoEncontrado() {
        // Arrange
        String email = "usuario.noexiste@example.com";

        when(clienteRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            usuarioService.loadUserByUsername(email);
        });
    }
}
