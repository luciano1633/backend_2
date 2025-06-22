package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @Test
    public void testCrearRole() {
        // Arrange
        Role role = Role.builder().nombre("ADMIN").build();

        when(roleService.guardar(role)).thenReturn(role);

        // Act
        ResponseEntity<Role> response = roleController.crearRole(role);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ADMIN", response.getBody().getNombre());
    }

    @Test
    public void testObtenerRolePorNombre() {
        // Arrange
        String roleNombre = "ADMIN";
        Role role = Role.builder().nombre(roleNombre).build();

        when(roleService.obtenerPorNombre(roleNombre)).thenReturn(java.util.Optional.of(role));

        // Act
        ResponseEntity<Role> response = roleController.obtenerPorNombre(roleNombre);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(roleNombre, response.getBody().getNombre());
    }

    @Test
    public void testEliminarRole() {
        // Arrange
        String roleNombre = "ADMIN";

        when(roleService.obtenerPorNombre(roleNombre)).thenReturn(java.util.Optional.of(Role.builder().nombre(roleNombre).build()));

        // Act
        ResponseEntity<Void> response = roleController.eliminarRole(roleNombre);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testObtenerTodosLosRoles() {
        // Arrange
        java.util.List<Role> roles = new java.util.ArrayList<>();
        roles.add(Role.builder().nombre("ADMIN").build());
        roles.add(Role.builder().nombre("USER").build());

        when(roleService.obtenerTodos()).thenReturn(roles);

        // Act
        ResponseEntity<java.util.List<Role>> response = roleController.obtenerTodos();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }
}
