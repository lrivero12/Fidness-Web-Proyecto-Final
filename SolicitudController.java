package com.fidness.web.controller;

import com.fidness.web.domain.Entrenador;
import com.fidness.web.repository.EntrenadorRepository;
import com.fidness.web.service.ReservaService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/entrenador/solicitudes")
public class SolicitudController {

    private final ReservaService reservaService;
    private final EntrenadorRepository entrenadorRepository;

    public SolicitudController(
            ReservaService reservaService,
            EntrenadorRepository entrenadorRepository) {

        this.reservaService = reservaService;
        this.entrenadorRepository = entrenadorRepository;
    }

    // ==========================================
    // 1. OBTENER ENTRENADOR AUTENTICADO
    // ==========================================

    private Entrenador obtenerEntrenador(
            Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new IllegalStateException(
                    "Debes iniciar sesión."
            );
        }

        String correo = authentication.getName();

        return entrenadorRepository
                .findByUsuarioCorreoIgnoreCase(correo)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No se encontró el entrenador autenticado."
                        )
                );
    }

    // ==========================================
    // 2. LISTAR SOLICITUDES
    // ==========================================

    @GetMapping
    public String listarSolicitudes(
            Authentication authentication,
            Model model) {

        Entrenador entrenador =
                obtenerEntrenador(authentication);

        Integer idEntrenador =
                entrenador.getIdEntrenador();

        model.addAttribute(
                "solicitudes",
                reservaService.listarReservasEntrenador(
                        idEntrenador
                )
        );

        model.addAttribute(
                "solicitudesPendientes",
                reservaService.listarSolicitudesPendientes(
                        idEntrenador
                )
        );

        model.addAttribute(
                "nombreEntrenador",
                entrenador.getUsuario().getNombre()
        );

        return "entrenador/solicitudes/listar";
    }

    // ==========================================
    // 3. CONFIRMAR SOLICITUD
    // ==========================================

    @PostMapping("/confirmar/{id}")
    public String confirmarSolicitud(
            @PathVariable("id") Integer id,
            Authentication authentication,
            RedirectAttributes ra) {

        Entrenador entrenador =
                obtenerEntrenador(authentication);

        try {

            reservaService.confirmarReserva(
                    id,
                    entrenador.getIdEntrenador()
            );

            ra.addFlashAttribute(
                    "mensaje",
                    "Solicitud confirmada correctamente."
            );

        } catch (IllegalArgumentException ex) {

            ra.addFlashAttribute(
                    "mensajeError",
                    ex.getMessage()
            );
        }

        return "redirect:/entrenador/solicitudes";
    }

    // ==========================================
    // 4. RECHAZAR SOLICITUD
    // ==========================================

    @PostMapping("/rechazar/{id}")
    public String rechazarSolicitud(
            @PathVariable("id") Integer id,
            Authentication authentication,
            RedirectAttributes ra) {

        Entrenador entrenador =
                obtenerEntrenador(authentication);

        try {

            reservaService.rechazarReserva(
                    id,
                    entrenador.getIdEntrenador()
            );

            ra.addFlashAttribute(
                    "mensaje",
                    "Solicitud rechazada correctamente."
            );

        } catch (IllegalArgumentException ex) {

            ra.addFlashAttribute(
                    "mensajeError",
                    ex.getMessage()
            );
        }

        return "redirect:/entrenador/solicitudes";
    }
}