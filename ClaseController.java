
package com.fidness.web.controller;

import com.fidness.web.domain.Clase;
import com.fidness.web.domain.Entrenador;
import com.fidness.web.repository.EntrenadorRepository;
import com.fidness.web.service.ClaseService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/entrenador/clases")
public class ClaseController {

    private final ClaseService claseService;
    private final EntrenadorRepository entrenadorRepository;

    public ClaseController(
            ClaseService claseService,
            EntrenadorRepository entrenadorRepository) {

        this.claseService = claseService;
        this.entrenadorRepository = entrenadorRepository;
    }

    // ==========================================
    // OBTENER ENTRENADOR AUTENTICADO
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
    // 1. LISTAR MIS CLASES
    // ==========================================

    @GetMapping
    public String listarClases(
            Authentication authentication,
            Model model) {

        Entrenador entrenador =
                obtenerEntrenador(authentication);

        model.addAttribute(
                "clases",
                claseService.listarClases(
                        entrenador.getIdEntrenador()
                )
        );

        model.addAttribute(
                "nombreEntrenador",
                entrenador.getUsuario().getNombre()
        );

        return "entrenador/clases/listar";
    }

    // ==========================================
    // 2. FORMULARIO PARA NUEVA CLASE
    // ==========================================

    @GetMapping("/nueva")
    public String nuevaClase(
            Authentication authentication,
            Model model) {

        obtenerEntrenador(authentication);

        Clase clase = new Clase();
        clase.setEstado("PROGRAMADA");

        model.addAttribute("clase", clase);
        model.addAttribute("titulo", "Programar nueva clase");

        return "entrenador/clases/formulario";
    }

    // ==========================================
    // 3. FORMULARIO PARA EDITAR CLASE
    // ==========================================

    @GetMapping("/editar/{id}")
    public String editarClase(
            @PathVariable("id") Integer id,
            Authentication authentication,
            Model model,
            RedirectAttributes ra) {

        Entrenador entrenador =
                obtenerEntrenador(authentication);

        try {

            Clase clase = claseService.buscarClase(
                    id,
                    entrenador.getIdEntrenador()
            );

            if (!"PROGRAMADA".equals(clase.getEstado())) {

                ra.addFlashAttribute(
                        "mensajeError",
                        "Solo se pueden editar clases programadas."
                );

                return "redirect:/entrenador/clases";
            }

            model.addAttribute("clase", clase);
            model.addAttribute("titulo", "Editar clase");

            return "entrenador/clases/formulario";

        } catch (IllegalArgumentException ex) {

            ra.addFlashAttribute(
                    "mensajeError",
                    ex.getMessage()
            );

            return "redirect:/entrenador/clases";
        }
    }

    // ==========================================
    // 4. GUARDAR NUEVA CLASE
    // ==========================================

    @PostMapping("/guardar")
    public String guardarClase(
            Authentication authentication,
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime horaInicio,
            @RequestParam Integer duracionMinutos,
            @RequestParam Integer capacidadMaxima,
            RedirectAttributes ra) {

        Entrenador entrenador =
                obtenerEntrenador(authentication);

        try {

            claseService.registrarClase(
                    entrenador.getIdEntrenador(),
                    nombre,
                    descripcion,
                    fecha,
                    horaInicio,
                    duracionMinutos,
                    capacidadMaxima
            );

            ra.addFlashAttribute(
                    "mensaje",
                    "Clase programada correctamente."
            );

        } catch (IllegalArgumentException ex) {

            ra.addFlashAttribute(
                    "mensajeError",
                    ex.getMessage()
            );
        }

        return "redirect:/entrenador/clases";
    }

    // ==========================================
    // 5. ACTUALIZAR CLASE EXISTENTE
    // ==========================================

    @PostMapping("/actualizar/{id}")
    public String actualizarClase(
            @PathVariable("id") Integer id,
            Authentication authentication,
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime horaInicio,
            @RequestParam Integer duracionMinutos,
            @RequestParam Integer capacidadMaxima,
            RedirectAttributes ra) {

        Entrenador entrenador =
                obtenerEntrenador(authentication);

        try {

            claseService.actualizarClase(
                    id,
                    entrenador.getIdEntrenador(),
                    nombre,
                    descripcion,
                    fecha,
                    horaInicio,
                    duracionMinutos,
                    capacidadMaxima
            );

            ra.addFlashAttribute(
                    "mensaje",
                    "Clase actualizada correctamente."
            );

        } catch (IllegalArgumentException ex) {

            ra.addFlashAttribute(
                    "mensajeError",
                    ex.getMessage()
            );
        }

        return "redirect:/entrenador/clases";
    }

    // ==========================================
    // 6. CANCELAR CLASE
    // ==========================================

    @PostMapping("/cancelar/{id}")
    public String cancelarClase(
            @PathVariable("id") Integer id,
            Authentication authentication,
            RedirectAttributes ra) {

        Entrenador entrenador =
                obtenerEntrenador(authentication);

        try {

            claseService.cancelarClase(
                    id,
                    entrenador.getIdEntrenador()
            );

            ra.addFlashAttribute(
                    "mensaje",
                    "Clase cancelada correctamente."
            );

        } catch (IllegalArgumentException ex) {

            ra.addFlashAttribute(
                    "mensajeError",
                    ex.getMessage()
            );
        }

        return "redirect:/entrenador/clases";
    }

    // ==========================================
    // 7. CONSULTAR AGENDA SEMANAL
    // ==========================================

    @GetMapping("/agenda")
    public String consultarAgenda(
            Authentication authentication,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha,
            Model model) {

        // Obtener entrenador autenticado
        Entrenador entrenador =
                obtenerEntrenador(authentication);

        // Utilizar la fecha actual si no se proporciona otra
        LocalDate fechaReferencia =
                fecha == null ? LocalDate.now() : fecha;

        // Calcular el lunes de la semana seleccionada
        LocalDate inicioSemana =
                fechaReferencia.with(
                        TemporalAdjusters.previousOrSame(
                                DayOfWeek.MONDAY
                        )
                );

        // Calcular el domingo de esa semana
        LocalDate finSemana =
                inicioSemana.plusDays(6);

        // Obtener las clases del entrenador en esa semana
        model.addAttribute(
                "clases",
                claseService.consultarAgenda(
                        entrenador.getIdEntrenador(),
                        inicioSemana,
                        finSemana
                )
        );

        // Fechas para mostrar en la interfaz
        model.addAttribute(
                "inicioSemana",
                inicioSemana
        );

        model.addAttribute(
                "finSemana",
                finSemana
        );

        // Navegación entre semanas
        model.addAttribute(
                "semanaAnterior",
                inicioSemana.minusWeeks(1)
        );

        model.addAttribute(
                "semanaSiguiente",
                inicioSemana.plusWeeks(1)
        );

        // Información del entrenador
        model.addAttribute(
                "nombreEntrenador",
                entrenador.getUsuario().getNombre()
        );

        return "entrenador/agenda";
    }
}
