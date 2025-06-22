package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void testObtenerTodosLosRoles() {
        // Arrange
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().nombre("ADMIN").build());
        roles.add(Role.builder().nombre("USER").build());

        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        List<Role> rolesObtenidos = roleService.obtenerTodos();

        // Assert
        assertNotNull(rolesObtenidos);
        assertEquals(2, rolesObtenidos.size());
    }

    @Test
    public void testObtenerRolePorNombre() {
        // Arrange
        String roleNombre = "ADMIN";
        Role role = Role.builder().nombre("ADMIN").build();

        when(roleRepository.findByNombre(roleNombre)).thenReturn(Optional.of(role));

        // Act
        Optional<Role> roleObtenido = roleService.obtenerPorNombre(roleNombre);

        // Assert
        assertTrue(roleObtenido.isPresent());
        assertEquals(roleNombre, roleObtenido.get().getNombre());
    }

    @Test
    public void testGuardarRole() {
        // Arrange
        Role role = Role.builder().nombre("ADMIN").build();

        when(roleRepository.save(role)).thenReturn(role);

        // Act
        Role roleGuardado = roleService.guardar(role);

        // Assert
        assertNotNull(roleGuardado);
        assertEquals("ADMIN", roleGuardado.getNombre());
    }

    @Test
    public void testEliminarRole() {
        // Arrange
        String roleNombre = "ADMIN";

        // Act
        roleService.eliminar(roleNombre);

        // Assert
        verify(roleRepository, times(1)).deleteById(roleNombre);
    }
}
