
package com.fidness.web.controller;

import com.fidness.web.domain.Clase;
import com.fidness.web.domain.Entrenador;
import com.fidness.web.domain.Reserva;
import com.fidness.web.domain.Usuario;

import com.fidness.web.repository.ClaseRepository;
import com.fidness.web.repository.EntrenadorRepository;
import com.fidness.web.repository.ReservaRepository;
import com.fidness.web.repository.UsuarioRepository;

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
@RequestMapping("/entrenador")
public class EntrenadorDashboardController {

    private final UsuarioRepository usuarioRepository;
    private final EntrenadorRepository entrenadorRepository;
    private final ClaseRepository claseRepository;
    private final ReservaRepository reservaRepository;

    public EntrenadorDashboardController(
            UsuarioRepository usuarioRepository,
            EntrenadorRepository entrenadorRepository,
            ClaseRepository claseRepository,
            ReservaRepository reservaRepository) {

        this.usuarioRepository = usuarioRepository;
        this.entrenadorRepository = entrenadorRepository;
        this.claseRepository = claseRepository;
        this.reservaRepository = reservaRepository;
    }

    // ==========================================
    // 1. MOSTRAR DASHBOARD DEL ENTRENADOR
    // ==========================================

    @GetMapping("/dashboard")
    public String mostrarDashboard(
            Authentication authentication,
            Model model) {

        if (authentication == null
                || !authentication.isAuthenticated()) {

            return "redirect:/login";
        }

        // ==========================================
        // 2. OBTENER USUARIO AUTENTICADO
        // ==========================================

        String correo = authentication.getName();

        Usuario usuario = usuarioRepository
                .findByCorreo(correo)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No se encontró el usuario autenticado."
                        )
                );

        Entrenador entrenador = entrenadorRepository
                .findByUsuarioCorreoIgnoreCase(correo)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No se encontró el entrenador autenticado."
                        )
                );

        Integer idEntrenador = entrenador.getIdEntrenador();

        // ==========================================
        // 3. INFORMACIÓN PERSONAL
        // ==========================================

        String nombreCompleto =
                usuario.getNombre() + " " + usuario.getApellidos();

        model.addAttribute("usuario", usuario);
        model.addAttribute("entrenador", entrenador);
        model.addAttribute("nombreCompleto", nombreCompleto);
        model.addAttribute("titulo", "Panel del entrenador");

        // ==========================================
        // 4. CONSULTAR CLASES DEL ENTRENADOR
        // ==========================================

        List<Clase> clases = claseRepository
                .findByEntrenadorIdEntrenadorOrderByFechaAscHoraInicioAsc(
                        idEntrenador
                );

        long clasesProgramadas = clases.stream()
                .filter(c -> "PROGRAMADA".equals(c.getEstado()))
                .count();

        long totalClases = clases.size();

        model.addAttribute("totalClases", totalClases);
        model.addAttribute(
                "clasesProgramadas",
                clasesProgramadas
        );

        // ==========================================
        // 5. CONSULTAR RESERVAS Y SOLICITUDES
        // ==========================================

        List<Reserva> reservas = reservaRepository
                .findByClaseEntrenadorIdEntrenadorOrderByFechaReservaDesc(
                        idEntrenador
                );

        long solicitudesPendientes = reservas.stream()
                .filter(r -> "PENDIENTE".equals(r.getEstado()))
                .count();

        long reservasConfirmadas = reservas.stream()
                .filter(r -> "CONFIRMADA".equals(r.getEstado()))
                .count();

        model.addAttribute(
                "solicitudesPendientes",
                solicitudesPendientes
        );

        model.addAttribute(
                "reservasConfirmadas",
                reservasConfirmadas
        );

        // ==========================================
        // 6. BUSCAR PRÓXIMA CLASE PROGRAMADA
        // ==========================================

        LocalDateTime ahora = LocalDateTime.now();

        Optional<Clase> proximaClase = clases.stream()
                .filter(c -> "PROGRAMADA".equals(c.getEstado()))
                .filter(c -> c.getFecha() != null)
                .filter(c -> c.getHoraInicio() != null)
                .filter(c -> {

                    LocalDateTime inicioClase =
                            LocalDateTime.of(
                                    c.getFecha(),
                                    c.getHoraInicio()
                            );

                    return inicioClase.isAfter(ahora);
                })
                .min(
                        Comparator.comparing(
                                c -> LocalDateTime.of(
                                        c.getFecha(),
                                        c.getHoraInicio()
                                )
                        )
                );

        model.addAttribute(
                "proximaClase",
                proximaClase.orElse(null)
        );

        // ==========================================
        // 7. CARGAR DASHBOARD
        // ==========================================

        return "entrenador/dashboard";
    }
}
