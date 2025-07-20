package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Sucursal;
import com.letrasypapeles.backend.service.SucursalService;
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

@WebMvcTest(SucursalController.class)
public class SucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SucursalService sucursalService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testObtenerTodasLasSucursales() throws Exception {
        when(sucursalService.obtenerTodas()).thenReturn(List.of(new Sucursal()));
        mockMvc.perform(get("/api/sucursales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser
    public void testObtenerSucursalPorId() throws Exception {
        Long sucursalId = 1L;
        Sucursal sucursal = Sucursal.builder().id(sucursalId).nombre("Sucursal 1").build();
        when(sucursalService.obtenerPorId(sucursalId)).thenReturn(Optional.of(sucursal));

        mockMvc.perform(get("/api/sucursales/{id}", sucursalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Sucursal 1"));
    }

    @Test
    @WithMockUser
    public void testCrearSucursal() throws Exception {
        Sucursal sucursal = Sucursal.builder().nombre("Sucursal Nueva").build();
        when(sucursalService.guardar(any(Sucursal.class))).thenReturn(sucursal);

        mockMvc.perform(post("/api/sucursales")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sucursal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Sucursal Nueva"));
    }

    @Test
    @WithMockUser
    public void testActualizarSucursal() throws Exception {
        Long sucursalId = 1L;
        Sucursal sucursalActualizada = Sucursal.builder().id(sucursalId).nombre("Sucursal Actualizada").build();
        when(sucursalService.obtenerPorId(sucursalId)).thenReturn(Optional.of(new Sucursal()));
        when(sucursalService.guardar(any(Sucursal.class))).thenReturn(sucursalActualizada);

        mockMvc.perform(put("/api/sucursales/{id}", sucursalId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sucursalActualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Sucursal Actualizada"));
    }

    @Test
    @WithMockUser
    public void testEliminarSucursal() throws Exception {
        Long sucursalId = 1L;
        when(sucursalService.obtenerPorId(sucursalId)).thenReturn(Optional.of(new Sucursal()));

        mockMvc.perform(delete("/api/sucursales/{id}", sucursalId).with(csrf()))
                .andExpect(status().isOk());
    }
}
