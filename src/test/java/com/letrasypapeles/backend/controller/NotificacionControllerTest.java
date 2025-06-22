package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Notificacion;
import com.letrasypapeles.backend.service.NotificacionService;
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
public class NotificacionControllerTest {

    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private NotificacionController notificacionController;

    @Test
    public void testObtenerTodasLasNotificaciones() {
        // Arrange
        List<Notificacion> notificaciones = new ArrayList<>();
        notificaciones.add(Notificacion.builder().id(1L).mensaje("Notificación 1").build());
        notificaciones.add(Notificacion.builder().id(2L).mensaje("Notificación 2").build());

        when(notificacionService.obtenerTodas()).thenReturn(notificaciones);

        // Act
        ResponseEntity<List<Notificacion>> response = notificacionController.obtenerTodas();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testObtenerNotificacionPorId() {
        // Arrange
        Long notificacionId = 1L;
        Notificacion notificacion = Notificacion.builder().id(notificacionId).mensaje("Notificación 1").build();

        when(notificacionService.obtenerPorId(notificacionId)).thenReturn(Optional.of(notificacion));

        // Act
        ResponseEntity<Notificacion> response = notificacionController.obtenerPorId(notificacionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(notificacionId, response.getBody().getId());
        assertEquals("Notificación 1", response.getBody().getMensaje());
    }

    @Test
    public void testCrearNotificacion() {
        // Arrange
        Notificacion notificacion = Notificacion.builder().mensaje("Nueva Notificación").build();

        when(notificacionService.guardar(notificacion)).thenReturn(notificacion);

        // Act
        ResponseEntity<Notificacion> response = notificacionController.crearNotificacion(notificacion);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Nueva Notificación", response.getBody().getMensaje());
    }

    @Test
    public void testEliminarNotificacion() {
        // Arrange
        Long notificacionId = 1L;

        when(notificacionService.obtenerPorId(notificacionId)).thenReturn(Optional.of(Notificacion.builder().id(notificacionId).mensaje("Notificación 1").build()));

        // Act
        ResponseEntity<Void> response = notificacionController.eliminarNotificacion(notificacionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testObtenerNotificacionesPorClienteId() {
        // Arrange
        Long clienteId = 1L;
        List<Notificacion> notificaciones = new ArrayList<>();
        notificaciones.add(Notificacion.builder().id(1L).mensaje("Notificación 1").build());
        notificaciones.add(Notificacion.builder().id(2L).mensaje("Notificación 2").build());

        when(notificacionService.obtenerPorClienteId(clienteId)).thenReturn(notificaciones);

        // Act
        ResponseEntity<List<Notificacion>> response = notificacionController.obtenerPorClienteId(clienteId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }
}
