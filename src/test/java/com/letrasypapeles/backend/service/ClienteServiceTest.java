package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.letrasypapeles.backend.repository.RoleRepository;
import com.letrasypapeles.backend.entity.Role;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    public void testObtenerClientePorId() {
        // Arrange
        Long clienteId = 1L;
        Cliente cliente = Cliente.builder()
                .id(clienteId)
                .nombre("Juan Perez")
                .email("juan.perez@example.com")
                .contraseña("password123")
                .puntosFidelidad(0)
                .build();

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));

        // Act
        Cliente clienteObtenido = clienteService.obtenerPorId(clienteId).orElse(null);

        // Assert
        assertNotNull(clienteObtenido);
        assertEquals(clienteId, clienteObtenido.getId());
        assertEquals("Juan Perez", clienteObtenido.getNombre());
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

        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);
        when(roleRepository.findByNombre("CLIENTE")).thenReturn(Optional.of(Role.builder().nombre("CLIENTE").build()));
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        // Act
        Cliente clienteRegistrado = clienteService.registrarCliente(cliente);

        // Assert
        assertNotNull(clienteRegistrado);
        assertEquals("Juan Perez", clienteRegistrado.getNombre());
        assertEquals("juan.perez@example.com", clienteRegistrado.getEmail());
    }

    @Test
    public void testRegistrarCliente_EmailYaExiste() {
        // Arrange
        String email = "juan.perez@example.com";
        Cliente cliente = Cliente.builder()
                .nombre("Juan Perez")
                .email(email)
                .contraseña("password123")
                .puntosFidelidad(0)
                .build();

        when(clienteRepository.existsByEmail(email)).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            clienteService.registrarCliente(cliente);
        });
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

        when(clienteRepository.save(cliente)).thenReturn(cliente);

        // Act
        Cliente clienteActualizado = clienteService.actualizarCliente(cliente);

        // Assert
        assertNotNull(clienteActualizado);
        assertEquals(clienteId, clienteActualizado.getId());
        assertEquals("Juan Perez Actualizado", clienteActualizado.getNombre());
        assertEquals("juan.perez@example.com", clienteActualizado.getEmail());
    }

    @Test
    public void testObtenerClientePorEmail() {
        // Arrange
        String clienteEmail = "juan.perez@example.com";
        Cliente cliente = Cliente.builder()
                .id(1L)
                .nombre("Juan Perez")
                .email(clienteEmail)
                .contraseña("password123")
                .puntosFidelidad(0)
                .build();

        when(clienteRepository.findByEmail(clienteEmail)).thenReturn(Optional.of(cliente));

        // Act
        Cliente clienteObtenido = clienteService.obtenerPorEmail(clienteEmail).orElse(null);

        // Assert
        assertNotNull(clienteObtenido);
        assertEquals(clienteEmail, clienteObtenido.getEmail());
        assertEquals("Juan Perez", clienteObtenido.getNombre());
    }

    @Test
    public void testEliminarCliente() {
        // Arrange
        Long clienteId = 1L;

        // Act
        clienteService.eliminar(clienteId);

        // Assert
        verify(clienteRepository, times(1)).deleteById(clienteId);
    }

    @Test
    public void testObtenerTodosLosClientes() {
        // Arrange
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(Cliente.builder().id(1L).nombre("Juan Perez").email("juan.perez@example.com").build());
        clientes.add(Cliente.builder().id(2L).nombre("Maria Gomez").email("maria.gomez@example.com").build());

        when(clienteRepository.findAll()).thenReturn(clientes);

        // Act
        List<Cliente> clientesObtenidos = clienteService.obtenerTodos();

        // Assert
        assertNotNull(clientesObtenidos);
        assertEquals(2, clientesObtenidos.size());
    }
}
