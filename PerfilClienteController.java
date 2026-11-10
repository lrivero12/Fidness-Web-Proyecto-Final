
package com.fidness.web.controller;

import com.fidness.web.domain.Usuario;
import com.fidness.web.repository.UsuarioRepository;

import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente/perfil")
public class PerfilClienteController {

    private final UsuarioRepository usuarioRepository;

    public PerfilClienteController(
            UsuarioRepository usuarioRepository) {

        this.usuarioRepository = usuarioRepository;
    }

    // Mostrar perfil del cliente autenticado
    @GetMapping
    public String mostrarPerfil(
            Authentication authentication,
            Model model) {

        String correo = authentication.getName();

        Usuario usuario = usuarioRepository
                .findByCorreo(correo)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No se encontró el usuario."
                        )
                );

        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Mi perfil");

        return "cliente/perfil";
    }

    // Actualizar información personal
    @PostMapping("/actualizar")
    public String actualizarPerfil(
            Authentication authentication,
            @RequestParam String nombre,
            @RequestParam String apellidos,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) LocalDate fechaNacimiento,
            RedirectAttributes ra) {

        String correo = authentication.getName();

        Usuario usuario = usuarioRepository
                .findByCorreo(correo)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No se encontró el usuario."
                        )
                );

        // Validar campos obligatorios
        if (nombre == null || nombre.isBlank()
                || apellidos == null || apellidos.isBlank()) {

            ra.addFlashAttribute(
                    "mensajeError",
                    "Nombre y apellidos son obligatorios."
            );

            return "redirect:/cliente/perfil";
        }

        // Validar longitud de campos
        if (nombre.trim().length() > 80
                || apellidos.trim().length() > 100
                || (telefono != null
                    && telefono.trim().length() > 20)) {

            ra.addFlashAttribute(
                    "mensajeError",
                    "Uno de los campos supera la longitud permitida."
            );

            return "redirect:/cliente/perfil";
        }

        // Validar fecha de nacimiento
        if (fechaNacimiento != null
                && fechaNacimiento.isAfter(LocalDate.now())) {

            ra.addFlashAttribute(
                    "mensajeError",
                    "La fecha de nacimiento no puede ser futura."
            );

            return "redirect:/cliente/perfil";
        }

        // Actualizar únicamente datos personales
        usuario.setNombre(nombre.trim());
        usuario.setApellidos(apellidos.trim());

        usuario.setTelefono(
                telefono == null ? null : telefono.trim()
        );

        usuario.setFechaNacimiento(fechaNacimiento);

        // Guardar cambios
        usuarioRepository.save(usuario);

        ra.addFlashAttribute(
                "mensaje",
                "Perfil actualizado correctamente."
        );

        return "redirect:/cliente/perfil";
    }
}
