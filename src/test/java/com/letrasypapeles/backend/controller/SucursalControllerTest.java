package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Sucursal;
import com.letrasypapeles.backend.service.SucursalService;
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
public class SucursalControllerTest {

    @Mock
    private SucursalService sucursalService;

    @InjectMocks
    private SucursalController sucursalController;

    @Test
    public void testObtenerTodasLasSucursales() {
        // Arrange
        List<Sucursal> sucursales = new ArrayList<>();
        sucursales.add(Sucursal.builder().id(1L).nombre("Sucursal 1").direccion("Dirección 1").build());
        sucursales.add(Sucursal.builder().id(2L).nombre("Sucursal 2").direccion("Dirección 2").build());

        when(sucursalService.obtenerTodas()).thenReturn(sucursales);

        // Act
        ResponseEntity<List<Sucursal>> response = sucursalController.obtenerTodas();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testObtenerSucursalPorId() {
        // Arrange
        Long sucursalId = 1L;
        Sucursal sucursal = Sucursal.builder().id(sucursalId).nombre("Sucursal 1").direccion("Dirección 1").build();

        when(sucursalService.obtenerPorId(sucursalId)).thenReturn(Optional.of(sucursal));

        // Act
        ResponseEntity<Sucursal> response = sucursalController.obtenerPorId(sucursalId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(sucursalId, response.getBody().getId());
        assertEquals("Sucursal 1", response.getBody().getNombre());
        assertEquals("Dirección 1", response.getBody().getDireccion());
    }

    @Test
    public void testCrearSucursal() {
        // Arrange
        Sucursal sucursal = Sucursal.builder().nombre("Sucursal Nueva").direccion("Dirección Nueva").build();

        when(sucursalService.guardar(sucursal)).thenReturn(sucursal);

        // Act
        ResponseEntity<Sucursal> response = sucursalController.crearSucursal(sucursal);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Sucursal Nueva", response.getBody().getNombre());
        assertEquals("Dirección Nueva", response.getBody().getDireccion());
    }

    @Test
    public void testActualizarSucursal() {
        // Arrange
        Long sucursalId = 1L;
        Sucursal sucursal = Sucursal.builder().id(sucursalId).nombre("Sucursal Actualizada").direccion("Dirección Actualizada").build();

        when(sucursalService.obtenerPorId(sucursalId)).thenReturn(Optional.of(Sucursal.builder().id(sucursalId).nombre("Sucursal 1").direccion("Dirección 1").build()));
        when(sucursalService.guardar(sucursal)).thenReturn(sucursal);

        // Act
        ResponseEntity<Sucursal> response = sucursalController.actualizarSucursal(sucursalId, sucursal);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(sucursalId, response.getBody().getId());
        assertEquals("Sucursal Actualizada", response.getBody().getNombre());
        assertEquals("Dirección Actualizada", response.getBody().getDireccion());
    }

    @Test
    public void testEliminarSucursal() {
        // Arrange
        Long sucursalId = 1L;

        when(sucursalService.obtenerPorId(sucursalId)).thenReturn(Optional.of(Sucursal.builder().id(sucursalId).nombre("Sucursal 1").direccion("Dirección 1").build()));

        // Act
        ResponseEntity<Void> response = sucursalController.eliminarSucursal(sucursalId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
