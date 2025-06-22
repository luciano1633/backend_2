package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    @Test
    public void testObtenerTodosLosProductos() {
        // Arrange
        List<Producto> productos = new ArrayList<>();
        productos.add(Producto.builder().id(1L).nombre("Producto 1").descripcion("Descripción 1").precio(BigDecimal.valueOf(10.0)).build());
        productos.add(Producto.builder().id(2L).nombre("Producto 2").descripcion("Descripción 2").precio(BigDecimal.valueOf(20.0)).build());

        when(productoService.obtenerTodos()).thenReturn(productos);

        // Act
        ResponseEntity<List<Producto>> response = productoController.obtenerTodos();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testObtenerProductoPorId() {
        // Arrange
        Long productoId = 1L;
        Producto producto = Producto.builder().id(productoId).nombre("Producto 1").descripcion("Descripción 1").precio(BigDecimal.valueOf(10.0)).build();

        when(productoService.obtenerPorId(productoId)).thenReturn(Optional.of(producto));

        // Act
        ResponseEntity<Producto> response = productoController.obtenerPorId(productoId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(productoId, response.getBody().getId());
        assertEquals("Producto 1", response.getBody().getNombre());
        assertEquals("Descripción 1", response.getBody().getDescripcion());
        assertEquals(BigDecimal.valueOf(10.0), response.getBody().getPrecio());
    }

    @Test
    public void testCrearProducto() {
        // Arrange
        Producto producto = Producto.builder().nombre("Producto Nuevo").descripcion("Descripción Nueva").precio(BigDecimal.valueOf(10.0)).build();

        when(productoService.guardar(producto)).thenReturn(producto);

        // Act
        ResponseEntity<Producto> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Producto Nuevo", response.getBody().getNombre());
        assertEquals("Descripción Nueva", response.getBody().getDescripcion());
        assertEquals(BigDecimal.valueOf(10.0), response.getBody().getPrecio());
    }

    @Test
    public void testActualizarProducto() {
        // Arrange
        Long productoId = 1L;
        Producto producto = Producto.builder().id(productoId).nombre("Producto Actualizado").descripcion("Descripción Actualizada").precio(BigDecimal.valueOf(20.0)).build();

        when(productoService.obtenerPorId(productoId)).thenReturn(Optional.of(Producto.builder().id(productoId).nombre("Producto 1").descripcion("Descripción 1").precio(BigDecimal.valueOf(10.0)).build()));
        when(productoService.guardar(producto)).thenReturn(producto);

        // Act
        ResponseEntity<Producto> response = productoController.actualizarProducto(productoId, producto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(productoId, response.getBody().getId());
        assertEquals("Producto Actualizado", response.getBody().getNombre());
        assertEquals("Descripción Actualizada", response.getBody().getDescripcion());
        assertEquals(BigDecimal.valueOf(20.0), response.getBody().getPrecio());
    }

    @Test
    public void testEliminarProducto() {
        // Arrange
        Long productoId = 1L;

        when(productoService.obtenerPorId(productoId)).thenReturn(Optional.of(Producto.builder().id(productoId).nombre("Producto 1").descripcion("Descripción 1").precio(BigDecimal.valueOf(10.0)).build()));

        // Act
        ResponseEntity<Void> response = productoController.eliminarProducto(productoId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
