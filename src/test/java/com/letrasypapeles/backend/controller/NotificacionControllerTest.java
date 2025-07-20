package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Notificacion;
import com.letrasypapeles.backend.service.NotificacionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificacionController.class)
public class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificacionService notificacionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testObtenerTodasLasNotificaciones() throws Exception {
        when(notificacionService.obtenerTodas()).thenReturn(List.of(new Notificacion()));
        mockMvc.perform(get("/api/notificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser
    public void testObtenerNotificacionPorId() throws Exception {
        Long notificacionId = 1L;
        Notificacion notificacion = Notificacion.builder().id(notificacionId).mensaje("Notificación 1").build();
        when(notificacionService.obtenerPorId(notificacionId)).thenReturn(Optional.of(notificacion));

        mockMvc.perform(get("/api/notificaciones/{id}", notificacionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Notificación 1"));
    }

    @Test
    @WithMockUser
    public void testCrearNotificacion() throws Exception {
        Notificacion notificacion = Notificacion.builder().mensaje("Nueva Notificación").build();
        when(notificacionService.guardar(any(Notificacion.class))).thenReturn(notificacion);

        mockMvc.perform(post("/api/notificaciones")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificacion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Nueva Notificación"));
    }

    @Test
    @WithMockUser
    public void testEliminarNotificacion() throws Exception {
        Long notificacionId = 1L;
        when(notificacionService.obtenerPorId(notificacionId)).thenReturn(Optional.of(new Notificacion()));

        mockMvc.perform(delete("/api/notificaciones/{id}", notificacionId).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testObtenerNotificacionesPorClienteId() throws Exception {
        Long clienteId = 1L;
        when(notificacionService.obtenerPorClienteId(clienteId)).thenReturn(List.of(new Notificacion()));

        mockMvc.perform(get("/api/notificaciones/cliente/{clienteId}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }
}
