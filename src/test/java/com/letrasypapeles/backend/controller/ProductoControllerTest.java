package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testObtenerTodosLosProductos() throws Exception {
        Producto producto = Producto.builder().id(1L).nombre("Producto 1").build();
        when(productoService.obtenerTodos()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productoList[0].nombre").value("Producto 1"));
    }

    @Test
    @WithMockUser
    public void testObtenerProductoPorId() throws Exception {
        Long productoId = 1L;
        Producto producto = Producto.builder().id(productoId).nombre("Producto 1").build();
        when(productoService.obtenerPorId(productoId)).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/api/productos/{id}", productoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Producto 1"));
    }

    @Test
    @WithMockUser
    public void testCrearProducto() throws Exception {
        Producto producto = Producto.builder().nombre("Producto Nuevo").precio(BigDecimal.TEN).build();
        when(productoService.guardar(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Producto Nuevo"));
    }

    @Test
    @WithMockUser
    public void testActualizarProducto() throws Exception {
        Long productoId = 1L;
        Producto productoActualizado = Producto.builder().id(productoId).nombre("Producto Actualizado").build();
        when(productoService.obtenerPorId(productoId)).thenReturn(Optional.of(new Producto()));
        when(productoService.guardar(any(Producto.class))).thenReturn(productoActualizado);

        mockMvc.perform(put("/api/productos/{id}", productoId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Producto Actualizado"));
    }

    @Test
    @WithMockUser
    public void testEliminarProducto() throws Exception {
        Long productoId = 1L;
        when(productoService.obtenerPorId(productoId)).thenReturn(Optional.of(new Producto()));

        mockMvc.perform(delete("/api/productos/{id}", productoId).with(csrf()))
                .andExpect(status().isOk());
    }
}
