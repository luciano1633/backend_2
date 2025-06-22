package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Categoria;
import com.letrasypapeles.backend.service.CategoriaService;
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
public class CategoriaControllerTest {

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    @Test
    public void testObtenerTodasLasCategorias() {
        // Arrange
        List<Categoria> categorias = new ArrayList<>();
        categorias.add(Categoria.builder().nombre("Libros").build());
        categorias.add(Categoria.builder().nombre("Música").build());

        when(categoriaService.obtenerTodas()).thenReturn(categorias);

        // Act
        ResponseEntity<List<Categoria>> response = categoriaController.obtenerTodas();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testObtenerCategoriaPorId() {
        // Arrange
        Long categoriaId = 1L;
        Categoria categoria = Categoria.builder().id(categoriaId).nombre("Libros").build();

        when(categoriaService.obtenerPorId(categoriaId)).thenReturn(java.util.Optional.of(categoria));

        // Act
        ResponseEntity<Categoria> response = categoriaController.obtenerPorId(categoriaId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(categoriaId, response.getBody().getId());
        assertEquals("Libros", response.getBody().getNombre());
    }

    @Test
    public void testCrearCategoria() {
        // Arrange
        Categoria categoria = Categoria.builder().nombre("Libros").build();

        when(categoriaService.guardar(categoria)).thenReturn(categoria);

        // Act
        ResponseEntity<Categoria> response = categoriaController.crearCategoria(categoria);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Libros", response.getBody().getNombre());
    }

    @Test
    public void testActualizarCategoria() {
        // Arrange
        Long categoriaId = 1L;
        Categoria categoria = Categoria.builder().id(categoriaId).nombre("Música").build();

        when(categoriaService.obtenerPorId(categoriaId)).thenReturn(java.util.Optional.of(Categoria.builder().id(categoriaId).nombre("Libros").build()));
        when(categoriaService.guardar(categoria)).thenReturn(categoria);

        // Act
        ResponseEntity<Categoria> response = categoriaController.actualizarCategoria(categoriaId, categoria);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(categoriaId, response.getBody().getId());
        assertEquals("Música", response.getBody().getNombre());
    }

    @Test
    public void testEliminarCategoria() {
        // Arrange
        Long categoriaId = 1L;

        when(categoriaService.obtenerPorId(categoriaId)).thenReturn(java.util.Optional.of(Categoria.builder().id(categoriaId).nombre("Libros").build()));

        // Act
        ResponseEntity<Void> response = categoriaController.eliminarCategoria(categoriaId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
