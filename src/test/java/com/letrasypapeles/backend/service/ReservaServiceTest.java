package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Reserva;
import com.letrasypapeles.backend.repository.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    public void testObtenerTodasLasReservas() {
        // Arrange
        List<Reserva> reservas = new ArrayList<>();
        reservas.add(Reserva.builder().id(1L).fechaReserva(LocalDateTime.now()).build());
        reservas.add(Reserva.builder().id(2L).fechaReserva(LocalDateTime.now()).build());

        when(reservaRepository.findAll()).thenReturn(reservas);

        // Act
        List<Reserva> reservasObtenidas = reservaService.obtenerTodas();

        // Assert
        assertNotNull(reservasObtenidas);
        assertEquals(2, reservasObtenidas.size());
    }

    @Test
    public void testObtenerReservaPorId() {
        // Arrange
        Long reservaId = 1L;
        Reserva reserva = Reserva.builder().id(reservaId).fechaReserva(LocalDateTime.now()).build();

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));

        // Act
        Optional<Reserva> reservaObtenida = reservaService.obtenerPorId(reservaId);

        // Assert
        assertTrue(reservaObtenida.isPresent());
        assertEquals(reservaId, reservaObtenida.get().getId());
    }

    @Test
    public void testGuardarReserva() {
        // Arrange
        Reserva reserva = Reserva.builder().fechaReserva(LocalDateTime.now()).build();

        when(reservaRepository.save(reserva)).thenReturn(reserva);

        // Act
        Reserva reservaGuardada = reservaService.guardar(reserva);

        // Assert
        assertNotNull(reservaGuardada);
    }

    @Test
    public void testEliminarReserva() {
        // Arrange
        Long reservaId = 1L;

        // Act
        reservaService.eliminar(reservaId);

        // Assert
        verify(reservaRepository, times(1)).deleteById(reservaId);
    }

    @Test
    public void testObtenerReservasPorClienteId() {
        // Arrange
        Long clienteId = 1L;
        List<Reserva> reservas = new ArrayList<>();
        reservas.add(Reserva.builder().id(1L).fechaReserva(LocalDateTime.now()).build());
        reservas.add(Reserva.builder().id(2L).fechaReserva(LocalDateTime.now()).build());

        when(reservaRepository.findByClienteId(clienteId)).thenReturn(reservas);

        // Act
        List<Reserva> reservasObtenidas = reservaService.obtenerPorClienteId(clienteId);

        // Assert
        assertNotNull(reservasObtenidas);
        assertEquals(2, reservasObtenidas.size());
    }

    @Test
    public void testObtenerReservasPorProductoId() {
        // Arrange
        Long productoId = 1L;
        List<Reserva> reservas = new ArrayList<>();
        reservas.add(Reserva.builder().id(1L).fechaReserva(LocalDateTime.now()).build());
        reservas.add(Reserva.builder().id(2L).fechaReserva(LocalDateTime.now()).build());

        when(reservaRepository.findByProductoId(productoId)).thenReturn(reservas);

        // Act
        List<Reserva> reservasObtenidas = reservaService.obtenerPorProductoId(productoId);

        // Assert
        assertNotNull(reservasObtenidas);
        assertEquals(2, reservasObtenidas.size());
    }

    @Test
    public void testObtenerReservasPorEstado() {
        // Arrange
        String estado = "Pendiente";
        List<Reserva> reservas = new ArrayList<>();
        reservas.add(Reserva.builder().id(1L).fechaReserva(LocalDateTime.now()).build());
        reservas.add(Reserva.builder().id(2L).fechaReserva(LocalDateTime.now()).build());

        when(reservaRepository.findByEstado(estado)).thenReturn(reservas);

        // Act
        List<Reserva> reservasObtenidas = reservaService.obtenerPorEstado(estado);

        // Assert
        assertNotNull(reservasObtenidas);
        assertEquals(2, reservasObtenidas.size());
    }
}
