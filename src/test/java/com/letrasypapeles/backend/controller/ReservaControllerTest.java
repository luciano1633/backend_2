package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Reserva;
import com.letrasypapeles.backend.service.ReservaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservaController.class)
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testObtenerTodasLasReservas() throws Exception {
        when(reservaService.obtenerTodas()).thenReturn(List.of(new Reserva()));
        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser
    public void testObtenerReservaPorId() throws Exception {
        Long reservaId = 1L;
        Reserva reserva = Reserva.builder().id(reservaId).build();
        when(reservaService.obtenerPorId(reservaId)).thenReturn(Optional.of(reserva));

        mockMvc.perform(get("/api/reservas/{id}", reservaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservaId));
    }

    @Test
    @WithMockUser
    public void testCrearReserva() throws Exception {
        Reserva reserva = Reserva.builder().id(1L).fechaReserva(LocalDateTime.now()).build();
        when(reservaService.guardar(any(Reserva.class))).thenReturn(reserva);

        mockMvc.perform(post("/api/reservas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reserva)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testActualizarReserva() throws Exception {
        Long reservaId = 1L;
        Reserva reservaActualizada = Reserva.builder().id(reservaId).build();
        when(reservaService.obtenerPorId(reservaId)).thenReturn(Optional.of(new Reserva()));
        when(reservaService.guardar(any(Reserva.class))).thenReturn(reservaActualizada);

        mockMvc.perform(put("/api/reservas/{id}", reservaId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaActualizada)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testEliminarReserva() throws Exception {
        Long reservaId = 1L;
        when(reservaService.obtenerPorId(reservaId)).thenReturn(Optional.of(new Reserva()));

        mockMvc.perform(delete("/api/reservas/{id}", reservaId).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testObtenerReservasPorClienteId() throws Exception {
        Long clienteId = 1L;
        when(reservaService.obtenerPorClienteId(clienteId)).thenReturn(List.of(new Reserva()));

        mockMvc.perform(get("/api/reservas/cliente/{clienteId}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }
}
