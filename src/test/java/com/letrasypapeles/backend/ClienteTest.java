package com.letrasypapeles.backend;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.entity.Role;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class ClienteTest {

    @Test
    public void testCrearCliente() {
        Set<Role> roles = Set.of(Role.builder().nombre("USER").build());
        Cliente cliente = Cliente.builder()
                .nombre("Juan Perez")
                .email("juan.perez@example.com")
                .contraseña("password123")
                .puntosFidelidad(0)
                .roles(roles)
                .build();

        assertNotNull(cliente);
        assertEquals("Juan Perez", cliente.getNombre());
        assertEquals("juan.perez@example.com", cliente.getEmail());
        assertEquals("password123", cliente.getContraseña());
        assertEquals(0, cliente.getPuntosFidelidad());
        assertEquals(1, cliente.getRoles().size());
    }

    @Test
    public void testEquals() {
        Cliente cliente1 = Cliente.builder()
                .id(1L)
                .nombre("Juan Perez")
                .email("juan.perez@example.com")
                .contraseña("password123")
                .puntosFidelidad(0)
                .build();

        Cliente cliente2 = Cliente.builder()
                .id(1L)
                .nombre("Juan Perez")
                .email("juan.perez@example.com")
                .contraseña("password123")
                .puntosFidelidad(0)
                .build();

        assertEquals(cliente1, cliente2);
    }

    @Test
    public void testHashCode() {
        Cliente cliente1 = Cliente.builder()
                .id(1L)
                .nombre("Juan Perez")
                .email("juan.perez@example.com")
                .contraseña("password123")
                .puntosFidelidad(0)
                .build();

        Cliente cliente2 = Cliente.builder()
                .id(1L)
                .nombre("Juan Perez")
                .email("juan.perez@example.com")
                .contraseña("password123")
                .puntosFidelidad(0)
                .build();

        assertEquals(cliente1.hashCode(), cliente2.hashCode());
    }

    @Test
    public void testAgregarRole() {
        Role role = Role.builder().nombre("ADMIN").build();
        Cliente cliente = Cliente.builder()
                .nombre("Juan Perez")
                .email("juan.perez@example.com")
                .contraseña("password123")
                .puntosFidelidad(0)
                .roles(new java.util.HashSet<>())
                .build();

        cliente.getRoles().add(role);
        assertEquals(1, cliente.getRoles().size());
        assertTrue(cliente.getRoles().contains(role));
    }
}
