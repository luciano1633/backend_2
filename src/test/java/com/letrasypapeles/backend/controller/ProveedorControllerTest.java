package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Proveedor;
import com.letrasypapeles.backend.service.ProveedorService;
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

@WebMvcTest(ProveedorController.class)
public class ProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProveedorService proveedorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testObtenerTodosLosProveedores() throws Exception {
        when(proveedorService.obtenerTodos()).thenReturn(List.of(new Proveedor()));
        mockMvc.perform(get("/api/proveedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser
    public void testObtenerProveedorPorId() throws Exception {
        Long proveedorId = 1L;
        Proveedor proveedor = Proveedor.builder().id(proveedorId).nombre("Proveedor 1").build();
        when(proveedorService.obtenerPorId(proveedorId)).thenReturn(Optional.of(proveedor));

        mockMvc.perform(get("/api/proveedores/{id}", proveedorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Proveedor 1"));
    }

    @Test
    @WithMockUser
    public void testCrearProveedor() throws Exception {
        Proveedor proveedor = Proveedor.builder().nombre("Proveedor Nuevo").build();
        when(proveedorService.guardar(any(Proveedor.class))).thenReturn(proveedor);

        mockMvc.perform(post("/api/proveedores")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proveedor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Proveedor Nuevo"));
    }

    @Test
    @WithMockUser
    public void testActualizarProveedor() throws Exception {
        Long proveedorId = 1L;
        Proveedor proveedorActualizado = Proveedor.builder().id(proveedorId).nombre("Proveedor Actualizado").build();
        when(proveedorService.obtenerPorId(proveedorId)).thenReturn(Optional.of(new Proveedor()));
        when(proveedorService.guardar(any(Proveedor.class))).thenReturn(proveedorActualizado);

        mockMvc.perform(put("/api/proveedores/{id}", proveedorId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proveedorActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Proveedor Actualizado"));
    }

    @Test
    @WithMockUser
    public void testEliminarProveedor() throws Exception {
        Long proveedorId = 1L;
        when(proveedorService.obtenerPorId(proveedorId)).thenReturn(Optional.of(new Proveedor()));

        mockMvc.perform(delete("/api/proveedores/{id}", proveedorId).with(csrf()))
                .andExpect(status().isOk());
    }
}
