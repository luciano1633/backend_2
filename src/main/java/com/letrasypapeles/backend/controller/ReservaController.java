package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Reserva;
import com.letrasypapeles.backend.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLEADO', 'GERENTE')")
    public ResponseEntity<List<Reserva>> obtenerTodas() {
        List<Reserva> reservas = reservaService.obtenerTodas();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Reserva> obtenerPorId(@PathVariable Long id) {
        return reservaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Reserva>> obtenerPorClienteId(@PathVariable Long clienteId) {
        List<Reserva> reservas = reservaService.obtenerPorClienteId(clienteId);
        return ResponseEntity.ok(reservas);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Reserva> crearReserva(@RequestBody Reserva reserva) {
        Reserva nuevaReserva = reservaService.guardar(reserva);
        return ResponseEntity.ok(nuevaReserva);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLEADO', 'GERENTE')")
    public ResponseEntity<Reserva> actualizarReserva(@PathVariable Long id, @RequestBody Reserva reserva) {
        return reservaService.obtenerPorId(id)
                .map(r -> {
                    reserva.setId(id);
                    Reserva reservaActualizada = reservaService.guardar(reserva);
                    return ResponseEntity.ok(reservaActualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLEADO', 'GERENTE')")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long id) {
        return reservaService.obtenerPorId(id)
                .map(r -> {
                    reservaService.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
