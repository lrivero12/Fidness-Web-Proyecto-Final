
package com.fidness.web.controller;

import com.fidness.web.domain.Entrenador;
import com.fidness.web.domain.Reserva;
import com.fidness.web.repository.EntrenadorRepository;
import com.fidness.web.repository.ReservaRepository;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/entrenador/asistentes")
public class AsistenteController {

    private final ReservaRepository reservaRepository;
    private final EntrenadorRepository entrenadorRepository;

    // ==========================================
    // 1. CONSTRUCTOR
    // ==========================================

    public AsistenteController(
            ReservaRepository reservaRepository,
            EntrenadorRepository entrenadorRepository) {

        this.reservaRepository = reservaRepository;
        this.entrenadorRepository = entrenadorRepository;
    }

    // ==========================================
    // 2. OBTENER ENTRENADOR AUTENTICADO
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
    // 3. LISTAR ASISTENTES CONFIRMADOS
    // ==========================================

    @GetMapping
    public String listarAsistentes(
            Authentication authentication,
            Model model) {

        Entrenador entrenador =
                obtenerEntrenador(authentication);

        // Consultar solamente reservas confirmadas
        // de las clases del entrenador autenticado.

        List<Reserva> asistentes = reservaRepository
                .findByClaseEntrenadorIdEntrenadorAndEstadoOrderByClaseFechaAscClaseHoraInicioAsc(
                        entrenador.getIdEntrenador(),
                        "CONFIRMADA"
                );

        // Datos para la vista HTML

        model.addAttribute(
                "asistentes",
                asistentes
        );

        model.addAttribute(
                "totalAsistentes",
                asistentes.size()
        );

        model.addAttribute(
                "nombreEntrenador",
                entrenador.getUsuario().getNombre()
        );

        return "entrenador/asistentes/listar";
    }
}
