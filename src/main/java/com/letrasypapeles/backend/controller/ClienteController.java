package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Obtener todos los clientes", description = "Retorna una lista de todos los clientes con enlaces HATEOAS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente.")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLEADO', 'GERENTE')")
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> obtenerTodos() {
        List<EntityModel<Cliente>> clientes = clienteService.obtenerTodos().stream()
                .map(cliente -> {
                    cliente.setContraseña(null);
                    try {
                        return EntityModel.of(cliente,
                                linkTo(methodOn(ClienteController.class).obtenerPorId(cliente.getId())).withSelfRel());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        try {
            return ResponseEntity.ok(
                    CollectionModel.of(clientes, linkTo(methodOn(ClienteController.class).obtenerTodos()).withSelfRel()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "Obtener un cliente por su ID", description = "Retorna un cliente específico según su ID con enlaces HATEOAS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado."),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EntityModel<Cliente>> obtenerPorId(@PathVariable Long id) {
        return clienteService.obtenerPorId(id)
                .map(cliente -> {
                    cliente.setContraseña(null);
                    try {
                        return EntityModel.of(cliente,
                                linkTo(methodOn(ClienteController.class).obtenerPorId(id)).withSelfRel(),
                                linkTo(methodOn(ClienteController.class).obtenerTodos()).withRel("todos-los-clientes"));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar un nuevo cliente", description = "Registra un nuevo cliente y lo retorna con enlaces HATEOAS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente registrado exitosamente.")
    })
    @PostMapping("/registro")
    @PreAuthorize("permitAll()")
    public ResponseEntity<EntityModel<Cliente>> registrarCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.registrarCliente(cliente);
        nuevoCliente.setContraseña(null);
        try {
            return ResponseEntity.ok(
                    EntityModel.of(nuevoCliente,
                            linkTo(methodOn(ClienteController.class).obtenerPorId(nuevoCliente.getId())).withSelfRel(),
                            linkTo(methodOn(ClienteController.class).obtenerTodos()).withRel("todos-los-clientes")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "Actualizar un cliente existente", description = "Actualiza un cliente existente según su ID y lo retorna con enlaces HATEOAS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EntityModel<Cliente>> actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        return clienteService.obtenerPorId(id)
                .map(c -> {
                    cliente.setId(id);
                    Cliente clienteActualizado = clienteService.actualizarCliente(cliente);
                    clienteActualizado.setContraseña(null);
                    try {
                        return EntityModel.of(clienteActualizado,
                                linkTo(methodOn(ClienteController.class).obtenerPorId(id)).withSelfRel(),
                                linkTo(methodOn(ClienteController.class).obtenerTodos()).withRel("todos-los-clientes"));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un cliente", description = "Elimina un cliente según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente eliminado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        return clienteService.obtenerPorId(id)
                .map(c -> {
                    clienteService.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
