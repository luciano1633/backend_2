package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Categoria;
import com.letrasypapeles.backend.service.CategoriaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaController.class)
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testObtenerTodasLasCategorias() throws Exception {
        List<Categoria> categorias = new ArrayList<>();
        categorias.add(Categoria.builder().nombre("Libros").build());
        when(categoriaService.obtenerTodas()).thenReturn(categorias);

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser
    public void testObtenerCategoriaPorId() throws Exception {
        Long categoriaId = 1L;
        Categoria categoria = Categoria.builder().id(categoriaId).nombre("Libros").build();
        when(categoriaService.obtenerPorId(categoriaId)).thenReturn(Optional.of(categoria));

        mockMvc.perform(get("/api/categorias/{id}", categoriaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Libros"));
    }

    @Test
    @WithMockUser
    public void testCrearCategoria() throws Exception {
        Categoria categoria = Categoria.builder().nombre("Libros").build();
        when(categoriaService.guardar(any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(post("/api/categorias")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Libros"));
    }

    @Test
    @WithMockUser
    public void testActualizarCategoria() throws Exception {
        Long categoriaId = 1L;
        Categoria categoriaActualizada = Categoria.builder().id(categoriaId).nombre("Música").build();
        when(categoriaService.obtenerPorId(categoriaId)).thenReturn(Optional.of(Categoria.builder().id(categoriaId).nombre("Libros").build()));
        when(categoriaService.guardar(any(Categoria.class))).thenReturn(categoriaActualizada);

        mockMvc.perform(put("/api/categorias/{id}", categoriaId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaActualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Música"));
    }

    @Test
    @WithMockUser
    public void testEliminarCategoria() throws Exception {
        Long categoriaId = 1L;
        when(categoriaService.obtenerPorId(categoriaId)).thenReturn(Optional.of(Categoria.builder().id(categoriaId).nombre("Libros").build()));

        mockMvc.perform(delete("/api/categorias/{id}", categoriaId).with(csrf()))
                .andExpect(status().isOk());
    }
}
