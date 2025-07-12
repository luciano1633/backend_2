package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.service.ClienteService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void testObtenerTodosLosClientes() {
        // Arrange
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(Cliente.builder().id(1L).nombre("Juan Perez").email("juan.perez@example.com").build());
        clientes.add(Cliente.builder().id(2L).nombre("Maria Gomez").email("maria.gomez@example.com").build());

        when(clienteService.obtenerTodos()).thenReturn(clientes);

        // Act
        ResponseEntity<CollectionModel<EntityModel<Cliente>>> response = clienteController.obtenerTodos();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertTrue(response.getBody().hasLink("self"));
    }

    @Test
    public void testObtenerTodosLosClientes_LanzaExcepcion() {
        // Arrange
        when(clienteService.obtenerTodos()).thenReturn(List.of(new Cliente()));
        try (var mocked = mockStatic(WebMvcLinkBuilder.class)) {
            mocked.when(() -> WebMvcLinkBuilder.linkTo(any(Class.class))).thenThrow(new RuntimeException("Test Exception"));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> {
                clienteController.obtenerTodos();
            });
        }
    }

    @Test
    public void testObtenerClientePorId() {
        // Arrange
        Long clienteId = 1L;
        Cliente cliente = Cliente.builder().id(clienteId).nombre("Juan Perez").email("juan.perez@example.com").build();

        when(clienteService.obtenerPorId(clienteId)).thenReturn(Optional.of(cliente));

        // Act
        ResponseEntity<EntityModel<Cliente>> response = clienteController.obtenerPorId(clienteId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getContent());
        assertEquals(clienteId, response.getBody().getContent().getId());
        assertEquals("Juan Perez", response.getBody().getContent().getNombre());
        assertTrue(response.getBody().hasLink("self"));
        assertTrue(response.getBody().hasLink("todos-los-clientes"));
    }

    @Test
    public void testRegistrarCliente() {
        // Arrange
        Cliente cliente = Cliente.builder()
                .id(1L)
                .nombre("Juan Perez")
                .email("juan.perez@example.com")
                .contraseña("password123")
                .puntosFidelidad(0)
                .build();

        when(clienteService.registrarCliente(cliente)).thenReturn(cliente);

        // Act
        ResponseEntity<EntityModel<Cliente>> response = clienteController.registrarCliente(cliente);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getContent());
        assertEquals("Juan Perez", response.getBody().getContent().getNombre());
        assertTrue(response.getBody().hasLink("self"));
        assertTrue(response.getBody().hasLink("todos-los-clientes"));
    }

    @Test
    public void testActualizarCliente() {
        // Arrange
        Long clienteId = 1L;
        Cliente cliente = Cliente.builder()
                .id(clienteId)
                .nombre("Juan Perez Actualizado")
                .email("juan.perez@example.com")
                .contraseña("password123")
                .puntosFidelidad(0)
                .build();

        when(clienteService.obtenerPorId(clienteId)).thenReturn(Optional.of(Cliente.builder().id(clienteId).nombre("Juan Perez").email("juan.perez@example.com").contraseña("password123").puntosFidelidad(0).build()));
        when(clienteService.actualizarCliente(cliente)).thenReturn(cliente);

        // Act
        ResponseEntity<EntityModel<Cliente>> response = clienteController.actualizarCliente(clienteId, cliente);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getContent());
        assertEquals("Juan Perez Actualizado", response.getBody().getContent().getNombre());
        assertTrue(response.getBody().hasLink("self"));
        assertTrue(response.getBody().hasLink("todos-los-clientes"));
    }

    @Test
    public void testEliminarCliente() {
        // Arrange
        Long clienteId = 1L;

        when(clienteService.obtenerPorId(clienteId)).thenReturn(Optional.of(Cliente.builder().id(clienteId).nombre("Juan Perez").email("juan.perez@example.com").contraseña("password123").puntosFidelidad(0).build()));

        // Act
        ResponseEntity<Void> response = clienteController.eliminarCliente(clienteId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
