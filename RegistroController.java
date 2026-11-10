
package com.fidness.web.controller;

import com.fidness.web.service.ClienteService;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistroController {

    private final ClienteService clienteService;

    public RegistroController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // ==========================================
    // 1. MOSTRAR FORMULARIO DE REGISTRO
    // ==========================================

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {

        model.addAttribute(
                "titulo",
                "Registro de clientes"
        );

        return "registro";
    }

    // ==========================================
    // 2. PROCESAR REGISTRO DE CLIENTE
    // ==========================================

    @PostMapping("/registro")
    public String registrarCliente(
            @RequestParam String nombre,
            @RequestParam String apellidos,
            @RequestParam String correo,
            @RequestParam String contrasena,
            @RequestParam String confirmarContrasena,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false)
            LocalDate fechaNacimiento,
            RedirectAttributes ra) {

        // Validar confirmación de contraseña
        if (contrasena == null
                || !contrasena.equals(confirmarContrasena)) {

            ra.addFlashAttribute(
                    "mensajeError",
                    "Las contraseñas no coinciden."
            );

            return "redirect:/registro";
        }

        try {

            // Reutilizar el servicio existente
            clienteService.registrarCliente(
                    nombre,
                    apellidos,
                    correo,
                    contrasena,
                    telefono,
                    fechaNacimiento
            );

            // Mensaje de registro exitoso
            ra.addFlashAttribute(
                    "mensaje",
                    "Tu cuenta fue creada correctamente. "
                    + "Ahora puedes iniciar sesión."
            );

            return "redirect:/login";

        } catch (IllegalArgumentException
                | IllegalStateException ex) {

            ra.addFlashAttribute(
                    "mensajeError",
                    ex.getMessage()
            );

            return "redirect:/registro";
        }
    }
}
