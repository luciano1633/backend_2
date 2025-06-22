package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Pedido;
import com.letrasypapeles.backend.service.PedidoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    @Test
    public void testObtenerTodosLosPedidos() {
        // Arrange
        List<Pedido> pedidos = new ArrayList<>();
        pedidos.add(Pedido.builder().id(1L).fecha(LocalDateTime.now()).build());
        pedidos.add(Pedido.builder().id(2L).fecha(LocalDateTime.now()).build());

        when(pedidoService.obtenerTodos()).thenReturn(pedidos);

        // Act
        ResponseEntity<List<Pedido>> response = pedidoController.obtenerTodos();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testObtenerPedidoPorId() {
        // Arrange
        Long pedidoId = 1L;
        Pedido pedido = Pedido.builder().id(pedidoId).fecha(LocalDateTime.now()).build();

        when(pedidoService.obtenerPorId(pedidoId)).thenReturn(Optional.of(pedido));

        // Act
        ResponseEntity<Pedido> response = pedidoController.obtenerPorId(pedidoId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(pedidoId, response.getBody().getId());
    }

    @Test
    public void testCrearPedido() {
        // Arrange
        Pedido pedido = Pedido.builder().fecha(LocalDateTime.now()).build();

        when(pedidoService.guardar(pedido)).thenReturn(pedido);

        // Act
        ResponseEntity<Pedido> response = pedidoController.crearPedido(pedido);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testActualizarPedido() {
        // Arrange
        Long pedidoId = 1L;
        Pedido pedido = Pedido.builder().id(pedidoId).fecha(LocalDateTime.now()).build();

        when(pedidoService.obtenerPorId(pedidoId)).thenReturn(Optional.of(Pedido.builder().id(pedidoId).fecha(LocalDateTime.now()).build()));
        when(pedidoService.guardar(pedido)).thenReturn(pedido);

        // Act
        ResponseEntity<Pedido> response = pedidoController.actualizarPedido(pedidoId, pedido);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(pedidoId, response.getBody().getId());
    }

    @Test
    public void testEliminarPedido() {
        // Arrange
        Long pedidoId = 1L;

        when(pedidoService.obtenerPorId(pedidoId)).thenReturn(Optional.of(Pedido.builder().id(pedidoId).fecha(LocalDateTime.now()).build()));

        // Act
        ResponseEntity<Void> response = pedidoController.eliminarPedido(pedidoId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testObtenerPedidosPorClienteId() {
        // Arrange
        Long clienteId = 1L;
        List<Pedido> pedidos = new ArrayList<>();
        pedidos.add(Pedido.builder().id(1L).fecha(LocalDateTime.now()).build());
        pedidos.add(Pedido.builder().id(2L).fecha(LocalDateTime.now()).build());

        when(pedidoService.obtenerPorClienteId(clienteId)).thenReturn(pedidos);

        // Act
        ResponseEntity<List<Pedido>> response = pedidoController.obtenerPorClienteId(clienteId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }
}
