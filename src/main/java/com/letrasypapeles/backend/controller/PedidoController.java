package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Pedido;
import com.letrasypapeles.backend.service.PedidoService;
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
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Obtener todos los pedidos", description = "Retorna una lista de todos los pedidos con enlaces HATEOAS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida exitosamente.")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLEADO', 'GERENTE')")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> obtenerTodos() {
        List<EntityModel<Pedido>> pedidos = pedidoService.obtenerTodos().stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        try {
            return ResponseEntity.ok(
                    CollectionModel.of(pedidos, linkTo(methodOn(PedidoController.class).obtenerTodos()).withSelfRel()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "Obtener un pedido por su ID", description = "Retorna un pedido específico según su ID con enlaces HATEOAS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado."),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EntityModel<Pedido>> obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id)
                .map(this::toEntityModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener pedidos por ID de cliente", description = "Retorna una lista de pedidos para un cliente específico con enlaces HATEOAS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados."),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado.")
    })
    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> obtenerPorClienteId(@PathVariable Long clienteId) {
        List<EntityModel<Pedido>> pedidos = pedidoService.obtenerPorClienteId(clienteId).stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        try {
            return ResponseEntity.ok(
                    CollectionModel.of(pedidos, linkTo(methodOn(PedidoController.class).obtenerPorClienteId(clienteId)).withSelfRel()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "Crear un nuevo pedido", description = "Crea un nuevo pedido y lo retorna con enlaces HATEOAS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente.")
    })
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EntityModel<Pedido>> crearPedido(@RequestBody Pedido pedido) {
        Pedido nuevoPedido = pedidoService.guardar(pedido);
        return ResponseEntity.status(201).body(toEntityModel(nuevoPedido));
    }

    @Operation(summary = "Actualizar un pedido existente", description = "Actualiza un pedido existente según su ID y lo retorna con enlaces HATEOAS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido actualizado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLEADO', 'GERENTE')")
    public ResponseEntity<EntityModel<Pedido>> actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        return pedidoService.obtenerPorId(id)
                .map(p -> {
                    pedido.setId(id);
                    Pedido pedidoActualizado = pedidoService.guardar(pedido);
                    return ResponseEntity.ok(toEntityModel(pedidoActualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un pedido", description = "Elimina un pedido según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido eliminado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id)
                .map(p -> {
                    pedidoService.eliminar(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private EntityModel<Pedido> toEntityModel(Pedido pedido) {
        try {
            return EntityModel.of(pedido,
                    linkTo(methodOn(PedidoController.class).obtenerPorId(pedido.getId())).withSelfRel(),
                    linkTo(methodOn(ClienteController.class).obtenerPorId(pedido.getCliente().getId())).withRel("cliente"),
                    linkTo(methodOn(PedidoController.class).obtenerTodos()).withRel("todos-los-pedidos"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
