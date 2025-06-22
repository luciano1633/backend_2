package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Reserva;
import com.letrasypapeles.backend.service.ReservaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservaControllerTest {

    @Mock
    private ReservaService reservaService;

    @InjectMocks
    private ReservaController reservaController;

    @Test
    public void testObtenerTodasLasReservas() {
        // Arrange
        List<Reserva> reservas = new ArrayList<>();
        reservas.add(Reserva.builder().id(1L).fechaReserva(LocalDateTime.now()).build());
        reservas.add(Reserva.builder().id(2L).fechaReserva(LocalDateTime.now()).build());

        when(reservaService.obtenerTodas()).thenReturn(reservas);

        // Act
        ResponseEntity<List<Reserva>> response = reservaController.obtenerTodas();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testObtenerReservaPorId() {
        // Arrange
        Long reservaId = 1L;
        Reserva reserva = Reserva.builder().id(reservaId).fechaReserva(LocalDateTime.now()).build();

        when(reservaService.obtenerPorId(reservaId)).thenReturn(Optional.of(reserva));

        // Act
        ResponseEntity<Reserva> response = reservaController.obtenerPorId(reservaId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(reservaId, response.getBody().getId());
    }

    @Test
    public void testCrearReserva() {
        // Arrange
        Reserva reserva = Reserva.builder().fechaReserva(LocalDateTime.now()).build();

        when(reservaService.guardar(reserva)).thenReturn(reserva);

        // Act
        ResponseEntity<Reserva> response = reservaController.crearReserva(reserva);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testActualizarReserva() {
        // Arrange
        Long reservaId = 1L;
        Reserva reserva = Reserva.builder().id(reservaId).fechaReserva(LocalDateTime.now()).build();

        when(reservaService.obtenerPorId(reservaId)).thenReturn(Optional.of(Reserva.builder().id(reservaId).fechaReserva(LocalDateTime.now()).build()));
        when(reservaService.guardar(reserva)).thenReturn(reserva);

        // Act
        ResponseEntity<Reserva> response = reservaController.actualizarReserva(reservaId, reserva);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(reservaId, response.getBody().getId());
    }

    @Test
    public void testEliminarReserva() {
        // Arrange
        Long reservaId = 1L;

        when(reservaService.obtenerPorId(reservaId)).thenReturn(Optional.of(Reserva.builder().id(reservaId).fechaReserva(LocalDateTime.now()).build()));

        // Act
        ResponseEntity<Void> response = reservaController.eliminarReserva(reservaId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testObtenerReservasPorClienteId() {
        // Arrange
        Long clienteId = 1L;
        List<Reserva> reservas = new ArrayList<>();
        reservas.add(Reserva.builder().id(1L).fechaReserva(LocalDateTime.now()).build());
        reservas.add(Reserva.builder().id(2L).fechaReserva(LocalDateTime.now()).build());

        when(reservaService.obtenerPorClienteId(clienteId)).thenReturn(reservas);

        // Act
        ResponseEntity<List<Reserva>> response = reservaController.obtenerPorClienteId(clienteId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }
}
