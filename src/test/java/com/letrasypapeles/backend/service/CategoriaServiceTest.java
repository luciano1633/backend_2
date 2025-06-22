package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Categoria;
import com.letrasypapeles.backend.repository.CategoriaRepository;
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
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    public void testObtenerTodasLasCategorias() {
        // Arrange
        List<Categoria> categorias = new ArrayList<>();
        categorias.add(Categoria.builder().nombre("Libros").build());
        categorias.add(Categoria.builder().nombre("Música").build());

        when(categoriaRepository.findAll()).thenReturn(categorias);

        // Act
        List<Categoria> categoriasObtenidas = categoriaService.obtenerTodas();

        // Assert
        assertNotNull(categoriasObtenidas);
        assertEquals(2, categoriasObtenidas.size());
    }

    @Test
    public void testObtenerCategoriaPorId() {
        // Arrange
        Long categoriaId = 1L;
        Categoria categoria = Categoria.builder().id(categoriaId).nombre("Libros").build();

        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));

        // Act
        Optional<Categoria> categoriaObtenida = categoriaService.obtenerPorId(categoriaId);

        // Assert
        assertTrue(categoriaObtenida.isPresent());
        assertEquals(categoriaId, categoriaObtenida.get().getId());
        assertEquals("Libros", categoriaObtenida.get().getNombre());
    }

    @Test
    public void testGuardarCategoria() {
        // Arrange
        Categoria categoria = Categoria.builder().nombre("Libros").build();

        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        // Act
        Categoria categoriaGuardada = categoriaService.guardar(categoria);

        // Assert
        assertNotNull(categoriaGuardada);
        assertEquals("Libros", categoriaGuardada.getNombre());
    }

    @Test
    public void testEliminarCategoria() {
        // Arrange
        Long categoriaId = 1L;

        // Act
        categoriaService.eliminar(categoriaId);

        // Assert
        verify(categoriaRepository, times(1)).deleteById(categoriaId);
    }
}
