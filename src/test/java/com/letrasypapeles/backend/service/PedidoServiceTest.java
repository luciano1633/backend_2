package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Pedido;
import com.letrasypapeles.backend.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    public void testObtenerTodosLosPedidos() {
        // Arrange
        List<Pedido> pedidos = new ArrayList<>();
        pedidos.add(Pedido.builder().id(1L).fecha(LocalDateTime.now()).estado("Pendiente").build());
        pedidos.add(Pedido.builder().id(2L).fecha(LocalDateTime.now()).estado("Entregado").build());

        when(pedidoRepository.findAll()).thenReturn(pedidos);

        // Act
        List<Pedido> pedidosObtenidos = pedidoService.obtenerTodos();

        // Assert
        assertNotNull(pedidosObtenidos);
        assertEquals(2, pedidosObtenidos.size());
    }

    @Test
    public void testObtenerPedidoPorId() {
        // Arrange
        Long pedidoId = 1L;
        Pedido pedido = Pedido.builder().id(pedidoId).fecha(LocalDateTime.now()).estado("Pendiente").build();

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        // Act
        Optional<Pedido> pedidoObtenido = pedidoService.obtenerPorId(pedidoId);

        // Assert
        assertTrue(pedidoObtenido.isPresent());
        assertEquals(pedidoId, pedidoObtenido.get().getId());
    }

    @Test
    public void testGuardarPedido() {
        // Arrange
        Pedido pedido = Pedido.builder().fecha(LocalDateTime.now()).estado("Pendiente").build();

        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        // Act
        Pedido pedidoGuardado = pedidoService.guardar(pedido);

        // Assert
        assertNotNull(pedidoGuardado);
    }

    @Test
    public void testEliminarPedido() {
        // Arrange
        Long pedidoId = 1L;

        // Act
        pedidoService.eliminar(pedidoId);

        // Assert
        verify(pedidoRepository, times(1)).deleteById(pedidoId);
    }

    @Test
    public void testObtenerPedidosPorClienteId() {
        // Arrange
        Long clienteId = 1L;
        List<Pedido> pedidos = new ArrayList<>();
        pedidos.add(Pedido.builder().id(1L).fecha(LocalDateTime.now()).estado("Pendiente").build());
        pedidos.add(Pedido.builder().id(2L).fecha(LocalDateTime.now()).estado("Entregado").build());

        when(pedidoRepository.findByClienteId(clienteId)).thenReturn(pedidos);

        // Act
        List<Pedido> pedidosObtenidos = pedidoService.obtenerPorClienteId(clienteId);

        // Assert
        assertNotNull(pedidosObtenidos);
        assertEquals(2, pedidosObtenidos.size());
    }

    @Test
    public void testObtenerPedidosPorEstado() {
        // Arrange
        String estado = "Pendiente";
        List<Pedido> pedidos = new ArrayList<>();
        pedidos.add(Pedido.builder().id(1L).fecha(LocalDateTime.now()).estado(estado).build());

        when(pedidoRepository.findByEstado(estado)).thenReturn(pedidos);

        // Act
        List<Pedido> pedidosObtenidos = pedidoService.obtenerPorEstado(estado);

        // Assert
        assertNotNull(pedidosObtenidos);
        assertEquals(1, pedidosObtenidos.size());
    }
}
