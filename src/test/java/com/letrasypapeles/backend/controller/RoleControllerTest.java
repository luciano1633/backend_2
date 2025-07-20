package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.service.RoleService;
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

@WebMvcTest(RoleController.class)
@WithMockUser(roles = "ADMIN")
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCrearRole() throws Exception {
        Role role = Role.builder().nombre("ADMIN").build();
        when(roleService.guardar(any(Role.class))).thenReturn(role);

        mockMvc.perform(post("/api/roles")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("ADMIN"));
    }

    @Test
    public void testObtenerRolePorNombre() throws Exception {
        String roleNombre = "ADMIN";
        Role role = Role.builder().nombre(roleNombre).build();
        when(roleService.obtenerPorNombre(roleNombre)).thenReturn(Optional.of(role));

        mockMvc.perform(get("/api/roles/{nombre}", roleNombre))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value(roleNombre));
    }

    @Test
    public void testEliminarRole() throws Exception {
        String roleNombre = "ADMIN";
        when(roleService.obtenerPorNombre(roleNombre)).thenReturn(Optional.of(new Role()));

        mockMvc.perform(delete("/api/roles/{nombre}", roleNombre).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void testObtenerTodosLosRoles() throws Exception {
        when(roleService.obtenerTodos()).thenReturn(List.of(new Role("ADMIN"), new Role("USER")));

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }
}
