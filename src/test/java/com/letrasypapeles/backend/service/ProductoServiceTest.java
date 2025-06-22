package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    public void testObtenerTodosLosProductos() {
        // Arrange
        List<Producto> productos = new ArrayList<>();
        productos.add(Producto.builder().id(1L).nombre("Producto 1").descripcion("Descripción 1").precio(BigDecimal.valueOf(10.0)).build());
        productos.add(Producto.builder().id(2L).nombre("Producto 2").descripcion("Descripción 2").precio(BigDecimal.valueOf(20.0)).build());

        when(productoRepository.findAll()).thenReturn(productos);

        // Act
        List<Producto> productosObtenidos = productoService.obtenerTodos();

        // Assert
        assertNotNull(productosObtenidos);
        assertEquals(2, productosObtenidos.size());
    }

    @Test
    public void testObtenerProductoPorId() {
        // Arrange
        Long productoId = 1L;
        Producto producto = Producto.builder().id(productoId).nombre("Producto 1").descripcion("Descripción 1").precio(BigDecimal.valueOf(10.0)).build();

        when(productoRepository.findById(productoId)).thenReturn(Optional.of(producto));

        // Act
        Optional<Producto> productoObtenido = productoService.obtenerPorId(productoId);

        // Assert
        assertTrue(productoObtenido.isPresent());
        assertEquals(productoId, productoObtenido.get().getId());
        assertEquals("Producto 1", productoObtenido.get().getNombre());
    }

    @Test
    public void testGuardarProducto() {
        // Arrange
        Producto producto = Producto.builder().nombre("Producto Nuevo").descripcion("Descripción Nueva").precio(BigDecimal.valueOf(10.0)).build();

        when(productoRepository.save(producto)).thenReturn(producto);

        // Act
        Producto productoGuardado = productoService.guardar(producto);

        // Assert
        assertNotNull(productoGuardado);
        assertEquals("Producto Nuevo", productoGuardado.getNombre());
        assertEquals("Descripción Nueva", productoGuardado.getDescripcion());
    }

    @Test
    public void testEliminarProducto() {
        // Arrange
        Long productoId = 1L;

        // Act
        productoService.eliminar(productoId);

        // Assert
        verify(productoRepository, times(1)).deleteById(productoId);
    }
}
