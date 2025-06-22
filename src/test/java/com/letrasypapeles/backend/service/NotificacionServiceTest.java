package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Notificacion;
import com.letrasypapeles.backend.repository.NotificacionRepository;
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
public class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;

    @Test
    public void testObtenerTodasLasNotificaciones() {
        // Arrange
        List<Notificacion> notificaciones = new ArrayList<>();
        notificaciones.add(Notificacion.builder().id(1L).mensaje("Notificación 1").build());
        notificaciones.add(Notificacion.builder().id(2L).mensaje("Notificación 2").build());

        when(notificacionRepository.findAll()).thenReturn(notificaciones);

        // Act
        List<Notificacion> notificacionesObtenidas = notificacionService.obtenerTodas();

        // Assert
        assertNotNull(notificacionesObtenidas);
        assertEquals(2, notificacionesObtenidas.size());
    }

    @Test
    public void testObtenerNotificacionPorId() {
        // Arrange
        Long notificacionId = 1L;
        Notificacion notificacion = Notificacion.builder().id(notificacionId).mensaje("Notificación 1").build();

        when(notificacionRepository.findById(notificacionId)).thenReturn(Optional.of(notificacion));

        // Act
        Optional<Notificacion> notificacionObtenida = notificacionService.obtenerPorId(notificacionId);

        // Assert
        assertTrue(notificacionObtenida.isPresent());
        assertEquals(notificacionId, notificacionObtenida.get().getId());
    }

    @Test
    public void testGuardarNotificacion() {
        // Arrange
        Notificacion notificacion = Notificacion.builder().mensaje("Nueva Notificación").build();

        when(notificacionRepository.save(notificacion)).thenReturn(notificacion);

        // Act
        Notificacion notificacionGuardada = notificacionService.guardar(notificacion);

        // Assert
        assertNotNull(notificacionGuardada);
        assertEquals("Nueva Notificación", notificacionGuardada.getMensaje());
    }

    @Test
    public void testEliminarNotificacion() {
        // Arrange
        Long notificacionId = 1L;

        // Act
        notificacionService.eliminar(notificacionId);

        // Assert
        verify(notificacionRepository, times(1)).deleteById(notificacionId);
    }

    @Test
    public void testObtenerNotificacionesPorClienteId() {
        // Arrange
        Long clienteId = 1L;
        List<Notificacion> notificaciones = new ArrayList<>();
        notificaciones.add(Notificacion.builder().id(1L).mensaje("Notificación 1").build());
        notificaciones.add(Notificacion.builder().id(2L).mensaje("Notificación 2").build());

        when(notificacionRepository.findByClienteId(clienteId)).thenReturn(notificaciones);

        // Act
        List<Notificacion> notificacionesObtenidas = notificacionService.obtenerPorClienteId(clienteId);

        // Assert
        assertNotNull(notificacionesObtenidas);
        assertEquals(2, notificacionesObtenidas.size());
    }

    @Test
    public void testObtenerNotificacionesPorFechaEntre() {
        // Arrange
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fechaFin = LocalDateTime.now();
        List<Notificacion> notificaciones = new ArrayList<>();
        notificaciones.add(Notificacion.builder().id(1L).mensaje("Notificación 1").build());
        notificaciones.add(Notificacion.builder().id(2L).mensaje("Notificación 2").build());

        when(notificacionRepository.findByFechaBetween(fechaInicio, fechaFin)).thenReturn(notificaciones);

        // Act
        List<Notificacion> notificacionesObtenidas = notificacionService.obtenerPorFechaEntre(fechaInicio, fechaFin);

        // Assert
        assertNotNull(notificacionesObtenidas);
        assertEquals(2, notificacionesObtenidas.size());
    }
}
