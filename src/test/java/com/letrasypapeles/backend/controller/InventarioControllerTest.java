package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Inventario;
import com.letrasypapeles.backend.service.InventarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InventarioControllerTest {

    @Mock
    private InventarioService inventarioService;

    @InjectMocks
    private InventarioController inventarioController;

    @Test
    public void testObtenerTodosLosInventarios() {
        // Arrange
        List<Inventario> inventarios = new ArrayList<>();
        inventarios.add(Inventario.builder().id(1L).cantidad(10).build());
        inventarios.add(Inventario.builder().id(2L).cantidad(20).build());

        when(inventarioService.obtenerTodos()).thenReturn(inventarios);

        // Act
        ResponseEntity<List<Inventario>> response = inventarioController.obtenerTodos();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testObtenerInventarioPorId() {
        // Arrange
        Long inventarioId = 1L;
        Inventario inventario = Inventario.builder().id(inventarioId).cantidad(10).build();

        when(inventarioService.obtenerPorId(inventarioId)).thenReturn(java.util.Optional.of(inventario));

        // Act
        ResponseEntity<Inventario> response = inventarioController.obtenerPorId(inventarioId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(inventarioId, response.getBody().getId());
        assertEquals(10, response.getBody().getCantidad());
    }

    @Test
    public void testCrearInventario() {
        // Arrange
        Inventario inventario = Inventario.builder().cantidad(10).build();

        when(inventarioService.guardar(inventario)).thenReturn(inventario);

        // Act
        ResponseEntity<Inventario> response = inventarioController.crearInventario(inventario);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().getCantidad());
    }

    @Test
    public void testActualizarInventario() {
        // Arrange
        Long inventarioId = 1L;
        Inventario inventario = Inventario.builder().id(inventarioId).cantidad(20).build();

        when(inventarioService.obtenerPorId(inventarioId)).thenReturn(java.util.Optional.of(Inventario.builder().id(inventarioId).cantidad(10).build()));
        when(inventarioService.guardar(inventario)).thenReturn(inventario);

        // Act
        ResponseEntity<Inventario> response = inventarioController.actualizarInventario(inventarioId, inventario);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(inventarioId, response.getBody().getId());
        assertEquals(20, response.getBody().getCantidad());
    }

    @Test
    public void testEliminarInventario() {
        // Arrange
        Long inventarioId = 1L;

        when(inventarioService.obtenerPorId(inventarioId)).thenReturn(java.util.Optional.of(Inventario.builder().id(inventarioId).cantidad(10).build()));

        // Act
        ResponseEntity<Void> response = inventarioController.eliminarInventario(inventarioId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testObtenerInventarioPorProductoId() {
        // Arrange
        Long productoId = 1L;
        List<Inventario> inventarios = new ArrayList<>();
        inventarios.add(Inventario.builder().id(1L).cantidad(10).build());
        inventarios.add(Inventario.builder().id(2L).cantidad(20).build());

        when(inventarioService.obtenerPorProductoId(productoId)).thenReturn(inventarios);

        // Act
        ResponseEntity<List<Inventario>> response = inventarioController.obtenerPorProductoId(productoId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testObtenerInventarioPorSucursalId() {
        // Arrange
        Long sucursalId = 1L;
        List<Inventario> inventarios = new ArrayList<>();
        inventarios.add(Inventario.builder().id(1L).cantidad(10).build());
        inventarios.add(Inventario.builder().id(2L).cantidad(20).build());

        when(inventarioService.obtenerPorSucursalId(sucursalId)).thenReturn(inventarios);

        // Act
        ResponseEntity<List<Inventario>> response = inventarioController.obtenerPorSucursalId(sucursalId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }
}
