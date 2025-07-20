package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Inventario;
import com.letrasypapeles.backend.service.InventarioService;
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

@WebMvcTest(InventarioController.class)
public class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventarioService inventarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testObtenerTodosLosInventarios() throws Exception {
        when(inventarioService.obtenerTodos()).thenReturn(List.of(new Inventario()));
        mockMvc.perform(get("/api/inventarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser
    public void testObtenerInventarioPorId() throws Exception {
        Long inventarioId = 1L;
        Inventario inventario = Inventario.builder().id(inventarioId).cantidad(10).build();
        when(inventarioService.obtenerPorId(inventarioId)).thenReturn(Optional.of(inventario));

        mockMvc.perform(get("/api/inventarios/{id}", inventarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(10));
    }

    @Test
    @WithMockUser
    public void testCrearInventario() throws Exception {
        Inventario inventario = Inventario.builder().cantidad(10).build();
        when(inventarioService.guardar(any(Inventario.class))).thenReturn(inventario);

        mockMvc.perform(post("/api/inventarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(10));
    }

    @Test
    @WithMockUser
    public void testActualizarInventario() throws Exception {
        Long inventarioId = 1L;
        Inventario inventarioActualizado = Inventario.builder().id(inventarioId).cantidad(20).build();
        when(inventarioService.obtenerPorId(inventarioId)).thenReturn(Optional.of(new Inventario()));
        when(inventarioService.guardar(any(Inventario.class))).thenReturn(inventarioActualizado);

        mockMvc.perform(put("/api/inventarios/{id}", inventarioId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventarioActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(20));
    }

    @Test
    @WithMockUser
    public void testEliminarInventario() throws Exception {
        Long inventarioId = 1L;
        when(inventarioService.obtenerPorId(inventarioId)).thenReturn(Optional.of(new Inventario()));

        mockMvc.perform(delete("/api/inventarios/{id}", inventarioId).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testObtenerInventarioPorProductoId() throws Exception {
        Long productoId = 1L;
        when(inventarioService.obtenerPorProductoId(productoId)).thenReturn(List.of(new Inventario()));

        mockMvc.perform(get("/api/inventarios/producto/{productoId}", productoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser
    public void testObtenerInventarioPorSucursalId() throws Exception {
        Long sucursalId = 1L;
        when(inventarioService.obtenerPorSucursalId(sucursalId)).thenReturn(List.of(new Inventario()));

        mockMvc.perform(get("/api/inventarios/sucursal/{sucursalId}", sucursalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }
}
