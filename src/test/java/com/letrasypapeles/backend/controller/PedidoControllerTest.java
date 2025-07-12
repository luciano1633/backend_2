package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.entity.Pedido;
import com.letrasypapeles.backend.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        cliente = Cliente.builder().id(1L).nombre("Test Cliente").build();
    }

    private Pedido createPedido(Long id) {
        return Pedido.builder().id(id).fecha(LocalDateTime.now()).cliente(cliente).build();
    }

    @Test
    public void testObtenerTodosLosPedidos() {
        // Arrange
        List<Pedido> pedidos = new ArrayList<>();
        pedidos.add(createPedido(1L));
        pedidos.add(createPedido(2L));

        when(pedidoService.obtenerTodos()).thenReturn(pedidos);

        // Act
        ResponseEntity<CollectionModel<EntityModel<Pedido>>> response = pedidoController.obtenerTodos();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertTrue(response.getBody().hasLink("self"));
    }

    @Test
    public void testObtenerTodosLosPedidos_LanzaExcepcion() {
        // Arrange
        when(pedidoService.obtenerTodos()).thenReturn(List.of(createPedido(1L)));
        try (var mocked = mockStatic(WebMvcLinkBuilder.class)) {
            mocked.when(() -> WebMvcLinkBuilder.linkTo(any(Class.class))).thenThrow(new RuntimeException("Test Exception"));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> {
                pedidoController.obtenerTodos();
            });
        }
    }

    @Test
    public void testObtenerPedidoPorId() {
        // Arrange
        Long pedidoId = 1L;
        Pedido pedido = createPedido(pedidoId);

        when(pedidoService.obtenerPorId(pedidoId)).thenReturn(Optional.of(pedido));

        // Act
        ResponseEntity<EntityModel<Pedido>> response = pedidoController.obtenerPorId(pedidoId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getContent());
        assertEquals(pedidoId, response.getBody().getContent().getId());
        assertTrue(response.getBody().hasLink("self"));
        assertTrue(response.getBody().hasLink("cliente"));
        assertTrue(response.getBody().hasLink("todos-los-pedidos"));
    }

    @Test
    public void testCrearPedido() {
        // Arrange
        Pedido pedido = createPedido(1L);

        when(pedidoService.guardar(pedido)).thenReturn(pedido);

        // Act
        ResponseEntity<EntityModel<Pedido>> response = pedidoController.crearPedido(pedido);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getContent());
        assertTrue(response.getBody().hasLink("self"));
    }

    @Test
    public void testActualizarPedido() {
        // Arrange
        Long pedidoId = 1L;
        Pedido pedido = createPedido(pedidoId);

        when(pedidoService.obtenerPorId(pedidoId)).thenReturn(Optional.of(createPedido(pedidoId)));
        when(pedidoService.guardar(pedido)).thenReturn(pedido);

        // Act
        ResponseEntity<EntityModel<Pedido>> response = pedidoController.actualizarPedido(pedidoId, pedido);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getContent());
        assertEquals(pedidoId, response.getBody().getContent().getId());
        assertTrue(response.getBody().hasLink("self"));
    }

    @Test
    public void testEliminarPedido() {
        // Arrange
        Long pedidoId = 1L;

        when(pedidoService.obtenerPorId(pedidoId)).thenReturn(Optional.of(createPedido(pedidoId)));

        // Act
        ResponseEntity<Void> response = pedidoController.eliminarPedido(pedidoId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testObtenerPedidosPorClienteId() {
        // Arrange
        Long clienteId = 1L;
        List<Pedido> pedidos = new ArrayList<>();
        pedidos.add(createPedido(1L));
        pedidos.add(createPedido(2L));

        when(pedidoService.obtenerPorClienteId(clienteId)).thenReturn(pedidos);

        // Act
        ResponseEntity<CollectionModel<EntityModel<Pedido>>> response = pedidoController.obtenerPorClienteId(clienteId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertTrue(response.getBody().hasLink("self"));
    }
}
