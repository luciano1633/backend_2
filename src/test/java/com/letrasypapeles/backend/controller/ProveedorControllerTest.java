package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Proveedor;
import com.letrasypapeles.backend.service.ProveedorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProveedorControllerTest {

    @Mock
    private ProveedorService proveedorService;

    @InjectMocks
    private ProveedorController proveedorController;

    @Test
    public void testObtenerTodosLosProveedores() {
        // Arrange
        List<Proveedor> proveedores = new ArrayList<>();
        proveedores.add(Proveedor.builder().id(1L).nombre("Proveedor 1").contacto("Contacto 1").build());
        proveedores.add(Proveedor.builder().id(2L).nombre("Proveedor 2").contacto("Contacto 2").build());

        when(proveedorService.obtenerTodos()).thenReturn(proveedores);

        // Act
        ResponseEntity<List<Proveedor>> response = proveedorController.obtenerTodos();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testObtenerProveedorPorId() {
        // Arrange
        Long proveedorId = 1L;
        Proveedor proveedor = Proveedor.builder().id(proveedorId).nombre("Proveedor 1").contacto("Contacto 1").build();

        when(proveedorService.obtenerPorId(proveedorId)).thenReturn(Optional.of(proveedor));

        // Act
        ResponseEntity<Proveedor> response = proveedorController.obtenerPorId(proveedorId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(proveedorId, response.getBody().getId());
        assertEquals("Proveedor 1", response.getBody().getNombre());
        assertEquals("Contacto 1", response.getBody().getContacto());
    }

    @Test
    public void testCrearProveedor() {
        // Arrange
        Proveedor proveedor = Proveedor.builder().nombre("Proveedor Nuevo").contacto("Contacto Nuevo").build();

        when(proveedorService.guardar(proveedor)).thenReturn(proveedor);

        // Act
        ResponseEntity<Proveedor> response = proveedorController.crearProveedor(proveedor);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Proveedor Nuevo", response.getBody().getNombre());
        assertEquals("Contacto Nuevo", response.getBody().getContacto());
    }

    @Test
    public void testActualizarProveedor() {
        // Arrange
        Long proveedorId = 1L;
        Proveedor proveedor = Proveedor.builder().id(proveedorId).nombre("Proveedor Actualizado").contacto("Contacto Actualizado").build();

        when(proveedorService.obtenerPorId(proveedorId)).thenReturn(Optional.of(Proveedor.builder().id(proveedorId).nombre("Proveedor 1").contacto("Contacto 1").build()));
        when(proveedorService.guardar(proveedor)).thenReturn(proveedor);

        // Act
        ResponseEntity<Proveedor> response = proveedorController.actualizarProveedor(proveedorId, proveedor);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(proveedorId, response.getBody().getId());
        assertEquals("Proveedor Actualizado", response.getBody().getNombre());
        assertEquals("Contacto Actualizado", response.getBody().getContacto());
    }

    @Test
    public void testEliminarProveedor() {
        // Arrange
        Long proveedorId = 1L;

        when(proveedorService.obtenerPorId(proveedorId)).thenReturn(Optional.of(Proveedor.builder().id(proveedorId).nombre("Proveedor 1").contacto("Contacto 1").build()));

        // Act
        ResponseEntity<Void> response = proveedorController.eliminarProveedor(proveedorId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
