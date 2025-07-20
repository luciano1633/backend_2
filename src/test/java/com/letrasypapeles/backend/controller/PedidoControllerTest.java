package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.entity.Pedido;
import com.letrasypapeles.backend.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
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

@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder().id(1L).nombre("Test Cliente").build();
    }

    private Pedido createPedido(Long id) {
        return Pedido.builder().id(id).cliente(cliente).build();
    }

    @Test
    @WithMockUser
    public void testObtenerTodosLosPedidos() throws Exception {
        when(pedidoService.obtenerTodos()).thenReturn(List.of(createPedido(1L)));
        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pedidoList").exists());
    }

    @Test
    @WithMockUser
    public void testObtenerPedidoPorId() throws Exception {
        Long pedidoId = 1L;
        Pedido pedido = createPedido(pedidoId);
        when(pedidoService.obtenerPorId(pedidoId)).thenReturn(Optional.of(pedido));

        mockMvc.perform(get("/api/pedidos/{id}", pedidoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pedidoId));
    }

    @Test
    @WithMockUser
    public void testCrearPedido() throws Exception {
        Pedido pedido = createPedido(1L);
        when(pedidoService.guardar(any(Pedido.class))).thenReturn(pedido);

        mockMvc.perform(post("/api/pedidos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void testActualizarPedido() throws Exception {
        Long pedidoId = 1L;
        Pedido pedido = createPedido(pedidoId);
        when(pedidoService.obtenerPorId(pedidoId)).thenReturn(Optional.of(createPedido(pedidoId)));
        when(pedidoService.guardar(any(Pedido.class))).thenReturn(pedido);

        mockMvc.perform(put("/api/pedidos/{id}", pedidoId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testEliminarPedido() throws Exception {
        Long pedidoId = 1L;
        when(pedidoService.obtenerPorId(pedidoId)).thenReturn(Optional.of(createPedido(pedidoId)));

        mockMvc.perform(delete("/api/pedidos/{id}", pedidoId).with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    public void testObtenerPedidosPorClienteId() throws Exception {
        Long clienteId = 1L;
        when(pedidoService.obtenerPorClienteId(clienteId)).thenReturn(List.of(createPedido(1L)));

        mockMvc.perform(get("/api/pedidos/cliente/{clienteId}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pedidoList").exists());
    }
}
