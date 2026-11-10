
package com.fidness.web.controller;

import com.fidness.web.domain.Cliente;
import com.fidness.web.repository.ClienteRepository;
import com.fidness.web.service.ReservaService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente/reservas")
public class ReservaClienteController {

    private final ReservaService reservaService;
    private final ClienteRepository clienteRepository;

    public ReservaClienteController(
            ReservaService reservaService,
            ClienteRepository clienteRepository) {

        this.reservaService = reservaService;
        this.clienteRepository = clienteRepository;
    }

    // ==========================================
    // 1. OBTENER CLIENTE AUTENTICADO
    // ==========================================

    private Cliente obtenerCliente(
            Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new IllegalStateException(
                    "Debes iniciar sesión."
            );
        }

        String correo = authentication.getName();

        return clienteRepository
                .findByUsuarioCorreoIgnoreCase(correo)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No se encontró el cliente autenticado."
                        )
                );
    }

    // ==========================================
    // 2. MOSTRAR CLASES DISPONIBLES
    // ==========================================

    @GetMapping("/disponibles")
    public String clasesDisponibles(
            Authentication authentication,
            Model model) {

        Cliente cliente = obtenerCliente(authentication);

        model.addAttribute(
                "clases",
                reservaService.listarClasesDisponibles()
        );

        model.addAttribute(
                "nombreCliente",
                cliente.getUsuario().getNombre()
        );

        return "cliente/reservas/disponibles";
    }

    // ==========================================
    // 3. REGISTRAR UNA RESERVA
    // ==========================================

    @PostMapping("/registrar/{idClase}")
    public String registrarReserva(
            @PathVariable("idClase") Integer idClase,
            Authentication authentication,
            RedirectAttributes ra) {

        Cliente cliente = obtenerCliente(authentication);

        try {

            reservaService.registrarReserva(
                    cliente.getIdCliente(),
                    idClase
            );

            ra.addFlashAttribute(
                    "mensaje",
                    "Reserva solicitada correctamente. "
                    + "Está pendiente de confirmación."
            );

            return "redirect:/cliente/reservas/mis-reservas";

        } catch (IllegalArgumentException ex) {

            ra.addFlashAttribute(
                    "mensajeError",
                    ex.getMessage()
            );

            return "redirect:/cliente/reservas/disponibles";
        }
    }

    // ==========================================
    // 4. MOSTRAR MIS RESERVAS
    // ==========================================

    @GetMapping("/mis-reservas")
    public String misReservas(
            Authentication authentication,
            Model model) {

        Cliente cliente = obtenerCliente(authentication);

        model.addAttribute(
                "reservas",
                reservaService.listarReservasCliente(
                        cliente.getIdCliente()
                )
        );

        model.addAttribute(
                "nombreCliente",
                cliente.getUsuario().getNombre()
        );

        return "cliente/reservas/mis-reservas";
    }

    // ==========================================
    // 5. CANCELAR UNA RESERVA
    // ==========================================

    @PostMapping("/cancelar/{idReserva}")
    public String cancelarReserva(
            @PathVariable("idReserva") Integer idReserva,
            Authentication authentication,
            RedirectAttributes ra) {

        Cliente cliente = obtenerCliente(authentication);

        try {

            reservaService.cancelarReservaCliente(
                    idReserva,
                    cliente.getIdCliente()
            );

            ra.addFlashAttribute(
                    "mensaje",
                    "Reserva cancelada correctamente."
            );

        } catch (IllegalArgumentException ex) {

            ra.addFlashAttribute(
                    "mensajeError",
                    ex.getMessage()
            );
        }

        return "redirect:/cliente/reservas/mis-reservas";
    }
}
