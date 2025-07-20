package com.letrasypapeles.backend.config;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.ClienteRepository;
import com.letrasypapeles.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear Roles si no existen
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("CLIENTE"));
            roleRepository.save(new Role("EMPLEADO"));
            roleRepository.save(new Role("GERENTE"));
        }

        // Crear usuarios de prueba si no existen
        if (clienteRepository.count() == 0) {
            Role clienteRole = roleRepository.findById("CLIENTE").orElseThrow(() -> new RuntimeException("Error: Role CLIENTE is not found."));
            Role empleadoRole = roleRepository.findById("EMPLEADO").orElseThrow(() -> new RuntimeException("Error: Role EMPLEADO is not found."));
            Role gerenteRole = roleRepository.findById("GERENTE").orElseThrow(() -> new RuntimeException("Error: Role GERENTE is not found."));

            // Usuario Cliente
            Cliente cliente = new Cliente();
            cliente.setNombre("Cliente de Prueba");
            cliente.setEmail("cliente@test.com");
            cliente.setContraseña(passwordEncoder.encode("password123"));
            cliente.setPuntosFidelidad(100);
            cliente.setRoles(Set.of(clienteRole));
            clienteRepository.save(cliente);

            // Usuario Empleado
            Cliente empleado = new Cliente();
            empleado.setNombre("Empleado de Prueba");
            empleado.setEmail("empleado@test.com");
            empleado.setContraseña(passwordEncoder.encode("password123"));
            empleado.setPuntosFidelidad(0);
            empleado.setRoles(Set.of(empleadoRole));
            clienteRepository.save(empleado);

            // Usuario Gerente
            Cliente gerente = new Cliente();
            gerente.setNombre("Gerente de Prueba");
            gerente.setEmail("gerente@test.com");
            gerente.setContraseña(passwordEncoder.encode("password123"));
            gerente.setPuntosFidelidad(0);
            gerente.setRoles(Set.of(gerenteRole));
            clienteRepository.save(gerente);
        }
    }
}
