
package com.fidness.web.controller;

import com.fidness.web.domain.Cliente;
import com.fidness.web.domain.Usuario;
import com.fidness.web.service.ClienteService;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Mostrar listado de clientes
    @GetMapping
    public String listarClientes(Model model) {

        model.addAttribute(
                "clientes",
                clienteService.listarClientes()
        );

        return "admin/clientes/listar";
    }

    // Mostrar formulario para registrar cliente
    @GetMapping("/nuevo")
    public String nuevoCliente(Model model) {

        model.addAttribute("usuario", new Usuario());
        model.addAttribute("titulo", "Registrar cliente");

        return "admin/clientes/formulario";
    }

    // Mostrar formulario para editar cliente
    @GetMapping("/editar/{id}")
    public String editarCliente(
            @PathVariable("id") Integer id,
            Model model,
            RedirectAttributes ra) {

        try {

            Cliente cliente = clienteService.buscarCliente(id);

            model.addAttribute("usuario", cliente.getUsuario());
            model.addAttribute("idCliente", cliente.getIdCliente());
            model.addAttribute("titulo", "Editar cliente");

            return "admin/clientes/formulario";

        } catch (IllegalArgumentException ex) {

            ra.addFlashAttribute("mensajeError", ex.getMessage());

            return "redirect:/admin/clientes";
        }
    }

    // Guardar cliente nuevo
    @PostMapping("/guardar")
    public String guardarCliente(
            @RequestParam String nombre,
            @RequestParam String apellidos,
            @RequestParam String correo,
            @RequestParam String contrasena,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) LocalDate fechaNacimiento,
            RedirectAttributes ra) {

        try {

            clienteService.registrarCliente(
                    nombre,
                    apellidos,
                    correo,
                    contrasena,
                    telefono,
                    fechaNacimiento
            );

            ra.addFlashAttribute(
                    "mensaje",
                    "Cliente registrado correctamente."
            );

        } catch (IllegalArgumentException | IllegalStateException ex) {

            ra.addFlashAttribute(
                    "mensajeError",
                    ex.getMessage()
            );
        }

        return "redirect:/admin/clientes";
    }

    // Actualizar información del cliente
    @PostMapping("/actualizar/{id}")
    public String actualizarCliente(
            @PathVariable("id") Integer id,
            @RequestParam String nombre,
            @RequestParam String apellidos,
            @RequestParam String correo,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) LocalDate fechaNacimiento,
            RedirectAttributes ra) {

        try {

            clienteService.actualizarCliente(
                    id,
                    nombre,
                    apellidos,
                    correo,
                    telefono,
                    fechaNacimiento
            );

            ra.addFlashAttribute(
                    "mensaje",
                    "Cliente actualizado correctamente."
            );

        } catch (IllegalArgumentException ex) {

            ra.addFlashAttribute(
                    "mensajeError",
                    ex.getMessage()
            );
        }

        return "redirect:/admin/clientes";
    }

    // Activar o desactivar cliente
    @PostMapping("/estado/{id}")
    public String cambiarEstado(
            @PathVariable("id") Integer id,
            RedirectAttributes ra) {

        try {

            boolean activo = clienteService.cambiarEstado(id);

            ra.addFlashAttribute(
                    "mensaje",
                    activo
                            ? "Cliente activado correctamente."
                            : "Cliente desactivado correctamente."
            );

        } catch (IllegalArgumentException ex) {

            ra.addFlashAttribute(
                    "mensajeError",
                    ex.getMessage()
            );
        }

        return "redirect:/admin/clientes";
    }
}
