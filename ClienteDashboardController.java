
package com.fidness.web.controller;

import com.fidness.web.domain.Cliente;
import com.fidness.web.domain.Reserva;
import com.fidness.web.repository.ClienteRepository;
import com.fidness.web.repository.ReservaRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class ClienteDashboardController {

    private final ClienteRepository clienteRepository;
    private final ReservaRepository reservaRepository;

    public ClienteDashboardController(
            ClienteRepository clienteRepository,
            ReservaRepository reservaRepository) {

        this.clienteRepository = clienteRepository;
        this.reservaRepository = reservaRepository;
    }

    // ==========================================
    // 1. MOSTRAR DASHBOARD DEL CLIENTE
    // ==========================================

    @GetMapping("/dashboard")
    public String mostrarDashboard(
            Authentication authentication,
            Model model) {

        if (authentication == null
                || !authentication.isAuthenticated()) {

            return "redirect:/login";
        }

        String correo = authentication.getName();

        Cliente cliente = clienteRepository
                .findByUsuarioCorreoIgnoreCase(correo)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No se encontró el cliente autenticado."
                        )
                );

        // ==========================================
        // 2. DATOS PERSONALES
        // ==========================================

        String nombreCompleto =
                cliente.getUsuario().getNombre()
                + " "
                + cliente.getUsuario().getApellidos();

        model.addAttribute("correo", correo);
        model.addAttribute("cliente", cliente);
        model.addAttribute("nombreCompleto", nombreCompleto);
        model.addAttribute("titulo", "Panel del cliente");

        // ==========================================
        // 3. CONSULTAR RESERVAS
        // ==========================================

        List<Reserva> reservas = reservaRepository
                .findByClienteIdClienteOrderByFechaReservaDesc(
                        cliente.getIdCliente()
                );

        // ==========================================
        // 4. CONTAR RESERVAS POR ESTADO
        // ==========================================

        long reservasPendientes = reservas.stream()
                .filter(r -> "PENDIENTE".equals(r.getEstado()))
                .count();

        long reservasConfirmadas = reservas.stream()
                .filter(r -> "CONFIRMADA".equals(r.getEstado()))
                .count();

        long totalReservas = reservas.size();

        model.addAttribute(
                "reservasPendientes",
                reservasPendientes
        );

        model.addAttribute(
                "reservasConfirmadas",
                reservasConfirmadas
        );

        model.addAttribute(
                "totalReservas",
                totalReservas
        );

        // ==========================================
        // 5. BUSCAR PRÓXIMA CLASE CONFIRMADA
        // ==========================================

        LocalDateTime ahora = LocalDateTime.now();

        Optional<Reserva> proximaReserva = reservas.stream()
                .filter(r -> "CONFIRMADA".equals(r.getEstado()))
                .filter(r -> r.getClase() != null)
                .filter(r -> r.getClase().getFecha() != null)
                .filter(r -> r.getClase().getHoraInicio() != null)
                .filter(r -> "PROGRAMADA".equals(
                        r.getClase().getEstado()
                ))
                .filter(r -> {

                    LocalDateTime inicioClase =
                            LocalDateTime.of(
                                    r.getClase().getFecha(),
                                    r.getClase().getHoraInicio()
                            );

                    return inicioClase.isAfter(ahora);
                })
                .min(
                        Comparator.comparing(
                                r -> LocalDateTime.of(
                                        r.getClase().getFecha(),
                                        r.getClase().getHoraInicio()
                                )
                        )
                );

        model.addAttribute(
                "proximaReserva",
                proximaReserva.orElse(null)
        );

        // ==========================================
        // 6. CARGAR VISTA
        // ==========================================

        return "cliente/dashboard";
    }
}
