package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    @BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void testObtenerTodosLosProductos() {
        // Arrange
        List<Producto> productos = new ArrayList<>();
        productos.add(Producto.builder().id(1L).nombre("Producto 1").descripcion("Descripción 1").precio(BigDecimal.valueOf(10.0)).build());
        productos.add(Producto.builder().id(2L).nombre("Producto 2").descripcion("Descripción 2").precio(BigDecimal.valueOf(20.0)).build());

        when(productoService.obtenerTodos()).thenReturn(productos);

        // Act
        ResponseEntity<CollectionModel<EntityModel<Producto>>> response = productoController.obtenerTodos();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertTrue(response.getBody().hasLink("self"));
    }

    @Test
    public void testObtenerTodosLosProductos_LanzaExcepcion() {
        // Arrange
        when(productoService.obtenerTodos()).thenReturn(List.of(new Producto()));
        try (var mocked = mockStatic(WebMvcLinkBuilder.class)) {
            mocked.when(() -> WebMvcLinkBuilder.linkTo(any(Class.class))).thenThrow(new RuntimeException("Test Exception"));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> {
                productoController.obtenerTodos();
            });
        }
    }

    @Test
    public void testObtenerProductoPorId() {
        // Arrange
        Long productoId = 1L;
        Producto producto = Producto.builder().id(productoId).nombre("Producto 1").descripcion("Descripción 1").precio(BigDecimal.valueOf(10.0)).build();

        when(productoService.obtenerPorId(productoId)).thenReturn(Optional.of(producto));

        // Act
        ResponseEntity<EntityModel<Producto>> response = productoController.obtenerPorId(productoId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getContent());
        assertEquals(productoId, response.getBody().getContent().getId());
        assertEquals("Producto 1", response.getBody().getContent().getNombre());
        assertTrue(response.getBody().hasLink("self"));
        assertTrue(response.getBody().hasLink("todos-los-productos"));
    }

    @Test
    public void testCrearProducto() {
        // Arrange
        Producto producto = Producto.builder().id(1L).nombre("Producto Nuevo").descripcion("Descripción Nueva").precio(BigDecimal.valueOf(10.0)).build();

        when(productoService.guardar(producto)).thenReturn(producto);

        // Act
        ResponseEntity<EntityModel<Producto>> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getContent());
        assertEquals("Producto Nuevo", response.getBody().getContent().getNombre());
        assertTrue(response.getBody().hasLink("self"));
        assertTrue(response.getBody().hasLink("todos-los-productos"));
    }

    @Test
    public void testActualizarProducto() {
        // Arrange
        Long productoId = 1L;
        Producto producto = Producto.builder().id(productoId).nombre("Producto Actualizado").descripcion("Descripción Actualizada").precio(BigDecimal.valueOf(20.0)).build();

        when(productoService.obtenerPorId(productoId)).thenReturn(Optional.of(Producto.builder().id(productoId).nombre("Producto 1").descripcion("Descripción 1").precio(BigDecimal.valueOf(10.0)).build()));
        when(productoService.guardar(producto)).thenReturn(producto);

        // Act
        ResponseEntity<EntityModel<Producto>> response = productoController.actualizarProducto(productoId, producto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getContent());
        assertEquals("Producto Actualizado", response.getBody().getContent().getNombre());
        assertTrue(response.getBody().hasLink("self"));
        assertTrue(response.getBody().hasLink("todos-los-productos"));
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
