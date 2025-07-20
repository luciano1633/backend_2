package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.service.ClienteService;
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

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testObtenerTodosLosClientes() throws Exception {
        Cliente cliente = Cliente.builder().id(1L).nombre("Juan Perez").email("juan.perez@example.com").build();
        when(clienteService.obtenerTodos()).thenReturn(List.of(cliente));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.clienteList[0].nombre").value("Juan Perez"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @WithMockUser
    public void testObtenerClientePorId() throws Exception {
        Long clienteId = 1L;
        Cliente cliente = Cliente.builder().id(clienteId).nombre("Juan Perez").email("juan.perez@example.com").build();
        when(clienteService.obtenerPorId(clienteId)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/clientes/{id}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Perez"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @WithMockUser
    public void testRegistrarCliente() throws Exception {
        Cliente cliente = Cliente.builder().nombre("Juan Perez").email("juan.perez@example.com").build();
        when(clienteService.registrarCliente(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/clientes/registro")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Perez"));
    }

    @Test
    @WithMockUser
    public void testActualizarCliente() throws Exception {
        Long clienteId = 1L;
        Cliente clienteActualizado = Cliente.builder().id(clienteId).nombre("Juan Actualizado").build();
        when(clienteService.obtenerPorId(clienteId)).thenReturn(Optional.of(new Cliente()));
        when(clienteService.actualizarCliente(any(Cliente.class))).thenReturn(clienteActualizado);

        mockMvc.perform(put("/api/clientes/{id}", clienteId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Actualizado"));
    }

    @Test
    @WithMockUser
    public void testEliminarCliente() throws Exception {
        Long clienteId = 1L;
        when(clienteService.obtenerPorId(clienteId)).thenReturn(Optional.of(new Cliente()));

        mockMvc.perform(delete("/api/clientes/{id}", clienteId).with(csrf()))
                .andExpect(status().isOk());
    }
}
