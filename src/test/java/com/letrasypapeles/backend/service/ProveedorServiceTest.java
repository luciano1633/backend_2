package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Proveedor;
import com.letrasypapeles.backend.repository.ProveedorRepository;
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
public class ProveedorServiceTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorService proveedorService;

    @Test
    public void testObtenerTodosLosProveedores() {
        // Arrange
        List<Proveedor> proveedores = new ArrayList<>();
        proveedores.add(Proveedor.builder().id(1L).nombre("Proveedor 1").contacto("Contacto 1").build());
        proveedores.add(Proveedor.builder().id(2L).nombre("Proveedor 2").contacto("Contacto 2").build());

        when(proveedorRepository.findAll()).thenReturn(proveedores);

        // Act
        List<Proveedor> proveedoresObtenidos = proveedorService.obtenerTodos();

        // Assert
        assertNotNull(proveedoresObtenidos);
        assertEquals(2, proveedoresObtenidos.size());
    }

    @Test
    public void testObtenerProveedorPorId() {
        // Arrange
        Long proveedorId = 1L;
        Proveedor proveedor = Proveedor.builder().id(proveedorId).nombre("Proveedor 1").contacto("Contacto 1").build();

        when(proveedorRepository.findById(proveedorId)).thenReturn(Optional.of(proveedor));

        // Act
        Optional<Proveedor> proveedorObtenido = proveedorService.obtenerPorId(proveedorId);

        // Assert
        assertTrue(proveedorObtenido.isPresent());
        assertEquals(proveedorId, proveedorObtenido.get().getId());
        assertEquals("Proveedor 1", proveedorObtenido.get().getNombre());
        assertEquals("Contacto 1", proveedorObtenido.get().getContacto());
    }

    @Test
    public void testGuardarProveedor() {
        // Arrange
        Proveedor proveedor = Proveedor.builder().nombre("Proveedor Nuevo").contacto("Contacto Nuevo").build();

        when(proveedorRepository.save(proveedor)).thenReturn(proveedor);

        // Act
        Proveedor proveedorGuardado = proveedorService.guardar(proveedor);

        // Assert
        assertNotNull(proveedorGuardado);
        assertEquals("Proveedor Nuevo", proveedorGuardado.getNombre());
        assertEquals("Contacto Nuevo", proveedorGuardado.getContacto());
    }

    @Test
    public void testEliminarProveedor() {
        // Arrange
        Long proveedorId = 1L;

        // Act
        proveedorService.eliminar(proveedorId);

        // Assert
        verify(proveedorRepository, times(1)).deleteById(proveedorId);
    }
}
