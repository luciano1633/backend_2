package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @Test
    public void testObtenerTodosLosClientes() {
        // Arrange
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(Cliente.builder().id(1L).nombre("Juan Perez").email("juan.perez@example.com").build());
        clientes.add(Cliente.builder().id(2L).nombre("Maria Gomez").email("maria.gomez@example.com").build());

        when(clienteService.obtenerTodos()).thenReturn(clientes);

        // Act
        ResponseEntity<List<Cliente>> response = clienteController.obtenerTodos();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testObtenerClientePorId() {
        // Arrange
        Long clienteId = 1L;
        Cliente cliente = Cliente.builder().id(clienteId).nombre("Juan Perez").email("juan.perez@example.com").build();

        when(clienteService.obtenerPorId(clienteId)).thenReturn(java.util.Optional.of(cliente));

        // Act
        ResponseEntity<Cliente> response = clienteController.obtenerPorId(clienteId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(clienteId, response.getBody().getId());
        assertEquals("Juan Perez", response.getBody().getNombre());
    }

    @Test
    public void testRegistrarCliente() {
        // Arrange
        Cliente cliente = Cliente.builder()
                .nombre("Juan Perez")
                .email("juan.perez@example.com")
                .contraseña("password123")
                .puntosFidelidad(0)
                .build();

        when(clienteService.registrarCliente(cliente)).thenReturn(cliente);

        // Act
        ResponseEntity<Cliente> response = clienteController.registrarCliente(cliente);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Juan Perez", response.getBody().getNombre());
        assertEquals("juan.perez@example.com", response.getBody().getEmail());
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

        when(clienteService.obtenerPorId(clienteId)).thenReturn(java.util.Optional.of(Cliente.builder().id(clienteId).nombre("Juan Perez").email("juan.perez@example.com").contraseña("password123").puntosFidelidad(0).build()));
        when(clienteService.actualizarCliente(cliente)).thenReturn(cliente);

        // Act
        ResponseEntity<Cliente> response = clienteController.actualizarCliente(clienteId, cliente);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(clienteId, response.getBody().getId());
        assertEquals("Juan Perez Actualizado", response.getBody().getNombre());
        assertEquals("juan.perez@example.com", response.getBody().getEmail());
    }

    @Test
    public void testEliminarCliente() {
        // Arrange
        Long clienteId = 1L;

        when(clienteService.obtenerPorId(clienteId)).thenReturn(java.util.Optional.of(Cliente.builder().id(clienteId).nombre("Juan Perez").email("juan.perez@example.com").contraseña("password123").puntosFidelidad(0).build()));

        // Act
        ResponseEntity<Void> response = clienteController.eliminarCliente(clienteId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
