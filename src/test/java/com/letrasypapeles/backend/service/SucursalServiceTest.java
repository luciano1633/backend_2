package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Sucursal;
import com.letrasypapeles.backend.repository.SucursalRepository;
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
public class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @InjectMocks
    private SucursalService sucursalService;

    @Test
    public void testObtenerTodasLasSucursales() {
        // Arrange
        List<Sucursal> sucursales = new ArrayList<>();
        sucursales.add(Sucursal.builder().id(1L).nombre("Sucursal 1").direccion("Dirección 1").build());
        sucursales.add(Sucursal.builder().id(2L).nombre("Sucursal 2").direccion("Dirección 2").build());

        when(sucursalRepository.findAll()).thenReturn(sucursales);

        // Act
        List<Sucursal> sucursalesObtenidas = sucursalService.obtenerTodas();

        // Assert
        assertNotNull(sucursalesObtenidas);
        assertEquals(2, sucursalesObtenidas.size());
    }

    @Test
    public void testObtenerSucursalPorId() {
        // Arrange
        Long sucursalId = 1L;
        Sucursal sucursal = Sucursal.builder().id(sucursalId).nombre("Sucursal 1").direccion("Dirección 1").build();

        when(sucursalRepository.findById(sucursalId)).thenReturn(Optional.of(sucursal));

        // Act
        Optional<Sucursal> sucursalObtenida = sucursalService.obtenerPorId(sucursalId);

        // Assert
        assertTrue(sucursalObtenida.isPresent());
        assertEquals(sucursalId, sucursalObtenida.get().getId());
        assertEquals("Sucursal 1", sucursalObtenida.get().getNombre());
        assertEquals("Dirección 1", sucursalObtenida.get().getDireccion());
    }

    @Test
    public void testGuardarSucursal() {
        // Arrange
        Sucursal sucursal = Sucursal.builder().nombre("Sucursal Nueva").direccion("Dirección Nueva").build();

        when(sucursalRepository.save(sucursal)).thenReturn(sucursal);

        // Act
        Sucursal sucursalGuardada = sucursalService.guardar(sucursal);

        // Assert
        assertNotNull(sucursalGuardada);
        assertEquals("Sucursal Nueva", sucursalGuardada.getNombre());
        assertEquals("Dirección Nueva", sucursalGuardada.getDireccion());
    }

    @Test
    public void testEliminarSucursal() {
        // Arrange
        Long sucursalId = 1L;

        // Act
        sucursalService.eliminar(sucursalId);

        // Assert
        verify(sucursalRepository, times(1)).deleteById(sucursalId);
    }
}
