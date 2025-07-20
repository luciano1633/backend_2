package com.letrasypapeles.backend.config;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.ClienteRepository;
import com.letrasypapeles.backend.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DataInitializerTest {

    @InjectMocks
    private DataInitializer dataInitializer;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRun_initializesDataWhenRepositoriesAreEmpty() throws Exception {
        // Simular que los repositorios están vacíos
        when(roleRepository.count()).thenReturn(0L);
        when(clienteRepository.count()).thenReturn(0L);

        // Simular el guardado de roles
        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(roleRepository.findById("CLIENTE")).thenReturn(Optional.of(new Role("CLIENTE")));
        when(roleRepository.findById("EMPLEADO")).thenReturn(Optional.of(new Role("EMPLEADO")));
        when(roleRepository.findById("GERENTE")).thenReturn(Optional.of(new Role("GERENTE")));

        // Simular el codificador de contraseñas
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Ejecutar el inicializador
        dataInitializer.run();

        // Verificar que los roles se guardaron
        verify(roleRepository, times(3)).save(any(Role.class)); // CLIENTE, EMPLEADO, GERENTE

        // Verificar que los clientes se guardaron
        verify(clienteRepository, times(3)).save(any(Cliente.class)); // Cliente, Empleado, Gerente

        // Verificar que las contraseñas se codificaron
        verify(passwordEncoder, times(3)).encode(anyString());
    }

    @Test
    void testRun_doesNotInitializeDataWhenRepositoriesAreNotEmpty() throws Exception {
        // Simular que los repositorios no están vacíos
        when(roleRepository.count()).thenReturn(3L);
        when(clienteRepository.count()).thenReturn(3L);

        // Ejecutar el inicializador
        dataInitializer.run();

        // Verificar que no se llamó a save en ningún repositorio
        verify(roleRepository, never()).save(any(Role.class));
        verify(clienteRepository, never()).save(any(Cliente.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
}
