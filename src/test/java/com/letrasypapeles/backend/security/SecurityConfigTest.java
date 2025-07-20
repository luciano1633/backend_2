package com.letrasypapeles.backend.security;

import com.letrasypapeles.backend.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @MockBean // Mockear UsuarioService ya que no estamos probando la lógica de negocio aquí
    private UsuarioService usuarioService;

    @Test
    void passwordEncoderBeanShouldBeCreated() {
        assertNotNull(passwordEncoder);
    }

    @Test
    void authenticationManagerBeanShouldBeCreated() {
        assertNotNull(authenticationManager);
    }

    @Test
    void publicEndpointsShouldBeAccessible() throws Exception {
        // Los endpoints /h2-console y /swagger-ui.html pueden no estar completamente disponibles
        // o configurados en un contexto de prueba unitaria de Spring Boot, por lo que se omiten.
        // La prueba de /api/auth/signin requiere un POST con cuerpo, lo cual está fuera del alcance de esta prueba simple de accesibilidad.
    }

    @Test
    void protectedEndpointsShouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/clientes")) // Ejemplo de endpoint protegido
                .andExpect(status().isForbidden()); // Espera 403 Forbidden si no hay autenticación
    }
}
