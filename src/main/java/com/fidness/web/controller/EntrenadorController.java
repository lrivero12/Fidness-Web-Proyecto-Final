
package com.fidness.web.controller;

import com.fidness.web.domain.Entrenador;
import com.fidness.web.domain.Usuario;
import com.fidness.web.service.EntrenadorService;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/entrenadores")
public class EntrenadorController {

    private final EntrenadorService entrenadorService;

    public EntrenadorController(
            EntrenadorService entrenadorService) {

        this.entrenadorService = entrenadorService;
    }

    // =====================================================
    // 1. LISTAR ENTRENADORES
    // =====================================================

    @GetMapping
    public String listarEntrenadores(Model model) {

        model.addAttribute(
                "entrenadores",
                entrenadorService.listarEntrenadores()
        );

        return "admin/entrenadores/listar";
    }

    // =====================================================
    // 2. FORMULARIO DE NUEVO ENTRENADOR
    // =====================================================

    @GetMapping("/nuevo")
    public String nuevoEntrenador(Model model) {

        model.addAttribute("usuario", new Usuario());
        model.addAttribute("titulo", "Registrar entrenador");

        return "admin/entrenadores/formulario";
    }

    // =====================================================
    // 3. FORMULARIO DE EDICIÓN
    // =====================================================

    @GetMapping("/editar/{id}")
    public String editarEntrenador(
            @PathVariable("id") Integer id,
            Model model,
            RedirectAttributes ra) {

        try {

            Entrenador entrenador =
                    entrenadorService.buscarEntrenador(id);

            model.addAttribute(
                    "usuario",
                    entrenador.getUsuario()
            );

            model.addAttribute(
                    "idEntrenador",
                    entrenador.getIdEntrenador()
            );

            model.addAttribute(
                    "titulo",
                    "Editar entrenador"
            );

            return "admin/entrenadores/formulario";

        } catch (IllegalArgumentException ex) {

            ra.addFlashAttribute(
                    "mensajeError",
                    ex.getMessage()
            );

            return "redirect:/admin/entrenadores";
        }
    }

    // =====================================================
    // 4. REGISTRAR ENTRENADOR
    // =====================================================

    @PostMapping("/guardar")
    public String guardarEntrenador(
            @RequestParam String nombre,
            @RequestParam String apellidos,
            @RequestParam String correo,
            @RequestParam String contrasena,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaNacimiento,
            RedirectAttributes ra) {

        try {

            entrenadorService.registrarEntrenador(
                    nombre,
                    apellidos,
                    correo,
                    contrasena,
                    telefono,
                    fechaNacimiento
            );

            ra.addFlashAttribute(
                    "mensaje",
                    "Entrenador registrado correctamente."
            );

        } catch (IllegalArgumentException | IllegalStateException ex) {

            ra.addFlashAttribute(
                    "mensajeError",
                    ex.getMessage()
            );
        }

        return "redirect:/admin/entrenadores";
    }

    // =====================================================
    // 5. ACTUALIZAR ENTRENADOR
    // =====================================================

    @PostMapping("/actualizar/{id}")
    public String actualizarEntrenador(
            @PathVariable("id") Integer id,
            @RequestParam String nombre,
            @RequestParam String apellidos,
            @RequestParam String correo,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaNacimiento,
            RedirectAttributes ra) {

        try {

            entrenadorService.actualizarEntrenador(
                    id,
                    nombre,
                    apellidos,
                    correo,
                    telefono,
                    fechaNacimiento
            );

            ra.addFlashAttribute(
                    "mensaje",
                    "Entrenador actualizado correctamente."
            );

        } catch (IllegalArgumentException ex) {

            ra.addFlashAttribute(
                    "mensajeError",
                    ex.getMessage()
            );
        }

        return "redirect:/admin/entrenadores";
    }

    // =====================================================
    // 6. ACTIVAR O DESACTIVAR ENTRENADOR
    // =====================================================

    @PostMapping("/estado/{id}")
    public String cambiarEstado(
            @PathVariable("id") Integer id,
            RedirectAttributes ra) {

        try {

            boolean activo =
                    entrenadorService.cambiarEstado(id);

            ra.addFlashAttribute(
                    "mensaje",
                    activo
                            ? "Entrenador activado correctamente."
                            : "Entrenador desactivado correctamente."
            );

        } catch (IllegalArgumentException ex) {

            ra.addFlashAttribute(
                    "mensajeError",
                    ex.getMessage()
            );
        }

        return "redirect:/admin/entrenadores";
    }
}
