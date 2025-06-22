package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Inventario;
import com.letrasypapeles.backend.repository.InventarioRepository;
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
public class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    @Test
    public void testObtenerTodosLosInventarios() {
        // Arrange
        List<Inventario> inventarios = new ArrayList<>();
        inventarios.add(Inventario.builder().id(1L).cantidad(10).build());
        inventarios.add(Inventario.builder().id(2L).cantidad(20).build());

        when(inventarioRepository.findAll()).thenReturn(inventarios);

        // Act
        List<Inventario> inventariosObtenidos = inventarioService.obtenerTodos();

        // Assert
        assertNotNull(inventariosObtenidos);
        assertEquals(2, inventariosObtenidos.size());
    }

    @Test
    public void testObtenerInventarioPorId() {
        // Arrange
        Long inventarioId = 1L;
        Inventario inventario = Inventario.builder().id(inventarioId).cantidad(10).build();

        when(inventarioRepository.findById(inventarioId)).thenReturn(Optional.of(inventario));

        // Act
        Optional<Inventario> inventarioObtenido = inventarioService.obtenerPorId(inventarioId);

        // Assert
        assertTrue(inventarioObtenido.isPresent());
        assertEquals(inventarioId, inventarioObtenido.get().getId());
        assertEquals(10, inventarioObtenido.get().getCantidad());
    }

    @Test
    public void testGuardarInventario() {
        // Arrange
        Inventario inventario = Inventario.builder().cantidad(10).build();

        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        // Act
        Inventario inventarioGuardado = inventarioService.guardar(inventario);

        // Assert
        assertNotNull(inventarioGuardado);
        assertEquals(10, inventarioGuardado.getCantidad());
    }

    @Test
    public void testEliminarInventario() {
        // Arrange
        Long inventarioId = 1L;

        // Act
        inventarioService.eliminar(inventarioId);

        // Assert
        verify(inventarioRepository, times(1)).deleteById(inventarioId);
    }

    @Test
    public void testObtenerInventarioPorProductoId() {
        // Arrange
        Long productoId = 1L;
        List<Inventario> inventarios = new ArrayList<>();
        inventarios.add(Inventario.builder().id(1L).cantidad(10).build());
        inventarios.add(Inventario.builder().id(2L).cantidad(20).build());

        when(inventarioRepository.findByProductoId(productoId)).thenReturn(inventarios);

        // Act
        List<Inventario> inventariosObtenidos = inventarioService.obtenerPorProductoId(productoId);

        // Assert
        assertNotNull(inventariosObtenidos);
        assertEquals(2, inventariosObtenidos.size());
    }

    @Test
    public void testObtenerInventarioPorSucursalId() {
        // Arrange
        Long sucursalId = 1L;
        List<Inventario> inventarios = new ArrayList<>();
        inventarios.add(Inventario.builder().id(1L).cantidad(10).build());
        inventarios.add(Inventario.builder().id(2L).cantidad(20).build());

        when(inventarioRepository.findBySucursalId(sucursalId)).thenReturn(inventarios);

        // Act
        List<Inventario> inventariosObtenidos = inventarioService.obtenerPorSucursalId(sucursalId);

        // Assert
        assertNotNull(inventariosObtenidos);
        assertEquals(2, inventariosObtenidos.size());
    }

    @Test
    public void testObtenerInventarioBajoUmbral() {
        // Arrange
        Integer umbral = 15;
        List<Inventario> inventarios = new ArrayList<>();
        inventarios.add(Inventario.builder().id(1L).cantidad(10).build());
        inventarios.add(Inventario.builder().id(2L).cantidad(12).build());

        when(inventarioRepository.findByCantidadLessThan(umbral)).thenReturn(inventarios);

        // Act
        List<Inventario> inventariosObtenidos = inventarioService.obtenerInventarioBajoUmbral(umbral);

        // Assert
        assertNotNull(inventariosObtenidos);
        assertEquals(2, inventariosObtenidos.size());
    }
}
