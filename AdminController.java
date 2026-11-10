
package com.fidness.web.controller;

import com.fidness.web.repository.UsuarioRepository;
import com.fidness.web.repository.ClienteRepository;
import com.fidness.web.repository.EntrenadorRepository;
import com.fidness.web.repository.CategoriaRepository;
import com.fidness.web.repository.EjercicioRepository;
import com.fidness.web.repository.ClaseRepository;
import com.fidness.web.repository.ReservaRepository;

import com.fidness.web.domain.Clase;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final EntrenadorRepository entrenadorRepository;
    private final CategoriaRepository categoriaRepository;
    private final EjercicioRepository ejercicioRepository;
    private final ClaseRepository claseRepository;
    private final ReservaRepository reservaRepository;

    public AdminController(
            UsuarioRepository usuarioRepository,
            ClienteRepository clienteRepository,
            EntrenadorRepository entrenadorRepository,
            CategoriaRepository categoriaRepository,
            EjercicioRepository ejercicioRepository,
            ClaseRepository claseRepository,
            ReservaRepository reservaRepository) {

        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.entrenadorRepository = entrenadorRepository;
        this.categoriaRepository = categoriaRepository;
        this.ejercicioRepository = ejercicioRepository;
        this.claseRepository = claseRepository;
        this.reservaRepository = reservaRepository;
    }

    // ==========================================
    // MOSTRAR DASHBOARD DEL ADMINISTRADOR
    // ==========================================

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {

        // ==========================================
        // 1. ESTADÍSTICAS DE USUARIOS
        // ==========================================

        model.addAttribute(
                "totalUsuarios",
                usuarioRepository.count()
        );

        model.addAttribute(
                "totalClientes",
                clienteRepository.count()
        );

        model.addAttribute(
                "totalEntrenadores",
                entrenadorRepository.count()
        );

        // ==========================================
        // 2. CATEGORÍAS Y EJERCICIOS
        // ==========================================

        model.addAttribute(
                "totalCategorias",
                categoriaRepository.count()
        );

        model.addAttribute(
                "totalEjercicios",
                ejercicioRepository.count()
        );

        // ==========================================
        // 3. ESTADÍSTICAS DE CLASES
        // ==========================================

        List<Clase> clases = claseRepository.findAll();

        long totalClases = clases.size();

        long clasesProgramadas = clases.stream()
                .filter(c ->
                        "PROGRAMADA".equals(c.getEstado()))
                .count();

        long clasesCanceladas = clases.stream()
                .filter(c ->
                        "CANCELADA".equals(c.getEstado()))
                .count();

        model.addAttribute(
                "totalClases",
                totalClases
        );

        model.addAttribute(
                "clasesProgramadas",
                clasesProgramadas
        );

        model.addAttribute(
                "clasesCanceladas",
                clasesCanceladas
        );

        // ==========================================
        // 4. ESTADÍSTICAS DE RESERVAS
        // ==========================================

        model.addAttribute(
                "totalReservas",
                reservaRepository.count()
        );

        // ==========================================
        // 5. BUSCAR PRÓXIMA CLASE PROGRAMADA
        // ==========================================

        LocalDateTime ahora = LocalDateTime.now();

        Optional<Clase> proximaClase = clases.stream()
                .filter(c ->
                        "PROGRAMADA".equals(c.getEstado()))
                .filter(c -> c.getFecha() != null)
                .filter(c -> c.getHoraInicio() != null)
                .filter(c -> {

                    LocalDateTime inicio =
                            LocalDateTime.of(
                                    c.getFecha(),
                                    c.getHoraInicio()
                            );

                    return inicio.isAfter(ahora);
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
        // 6. TÍTULO DEL DASHBOARD
        // ==========================================

        model.addAttribute(
                "titulo",
                "Panel del administrador"
        );

        return "admin/dashboard";
    }
}
