
package com.fidness.web.controller;

import com.fidness.web.domain.Categoria;
import com.fidness.web.domain.Ejercicio;
import com.fidness.web.repository.CategoriaRepository;
import com.fidness.web.repository.EjercicioRepository;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/ejercicios")
public class EjercicioController {

    private final EjercicioRepository ejercicioRepository;
    private final CategoriaRepository categoriaRepository;

    public EjercicioController(
            EjercicioRepository ejercicioRepository,
            CategoriaRepository categoriaRepository) {

        this.ejercicioRepository = ejercicioRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // Mostrar todos los ejercicios registrados
    @GetMapping
    public String listarEjercicios(Model model) {

        model.addAttribute(
                "ejercicios",
                ejercicioRepository.findAll()
        );

        return "admin/ejercicios/listar";
    }

    // Mostrar formulario para registrar un ejercicio
    @GetMapping("/nuevo")
    public String nuevoEjercicio(Model model) {

        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setCategoria(new Categoria());
        ejercicio.setEstado(true);

        model.addAttribute("ejercicio", ejercicio);
        model.addAttribute("titulo", "Nuevo ejercicio");

        model.addAttribute(
                "categorias",
                categoriaRepository.findAll()
                        .stream()
                        .filter(c -> Boolean.TRUE.equals(c.getEstado()))
                        .toList()
        );

        return "admin/ejercicios/formulario";
    }

    // Mostrar formulario para editar un ejercicio
    @GetMapping("/editar/{id}")
    public String editarEjercicio(
            @PathVariable("id") Integer id,
            Model model,
            RedirectAttributes redirectAttributes) {

        Ejercicio ejercicio = ejercicioRepository
                .findById(id)
                .orElse(null);

        if (ejercicio == null) {

            redirectAttributes.addFlashAttribute(
                    "mensajeError",
                    "El ejercicio solicitado no existe."
            );

            return "redirect:/admin/ejercicios";
        }

        model.addAttribute("ejercicio", ejercicio);
        model.addAttribute("titulo", "Editar ejercicio");

        // Incluir la categoría actual aunque esté inactiva
        List<Categoria> categorias = categoriaRepository.findAll()
                .stream()
                .filter(c ->
                        Boolean.TRUE.equals(c.getEstado())
                        || c.getIdCategoria().equals(
                                ejercicio.getCategoria().getIdCategoria()
                        )
                )
                .toList();

        model.addAttribute("categorias", categorias);

        return "admin/ejercicios/formulario";
    }

    // Guardar un ejercicio nuevo o actualizar uno existente
    @PostMapping("/guardar")
    public String guardarEjercicio(
            @ModelAttribute Ejercicio datos,
            RedirectAttributes redirectAttributes) {

        Integer idCategoria = datos.getCategoria() == null
                ? null
                : datos.getCategoria().getIdCategoria();

        if (idCategoria == null) {

            redirectAttributes.addFlashAttribute(
                    "mensajeError",
                    "Debes seleccionar una categoría."
            );

            return "redirect:/admin/ejercicios";
        }

        Categoria categoria = categoriaRepository
                .findById(idCategoria)
                .orElse(null);

        if (categoria == null) {

            redirectAttributes.addFlashAttribute(
                    "mensajeError",
                    "La categoría seleccionada no existe."
            );

            return "redirect:/admin/ejercicios";
        }

        Ejercicio ejercicio;

        if (datos.getIdEjercicio() != null) {

            ejercicio = ejercicioRepository
                    .findById(datos.getIdEjercicio())
                    .orElse(null);

            if (ejercicio == null) {

                redirectAttributes.addFlashAttribute(
                        "mensajeError",
                        "El ejercicio que intentas editar no existe."
                );

                return "redirect:/admin/ejercicios";
            }

        } else {

            ejercicio = new Ejercicio();
            ejercicio.setEstado(true);
        }

        // Actualizar los datos del ejercicio
        ejercicio.setNombre(datos.getNombre());
        ejercicio.setDescripcion(datos.getDescripcion());
        ejercicio.setInstrucciones(datos.getInstrucciones());
        ejercicio.setNivel(datos.getNivel());
        ejercicio.setImagen(datos.getImagen());
        ejercicio.setCategoria(categoria);

        // Guardar en MySQL
        ejercicioRepository.save(ejercicio);

        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Ejercicio guardado correctamente."
        );

        return "redirect:/admin/ejercicios";
    }

    // Activar o desactivar un ejercicio sin eliminarlo
    @PostMapping("/estado/{id}")
    public String cambiarEstado(
            @PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {

        Ejercicio ejercicio = ejercicioRepository
                .findById(id)
                .orElse(null);

        if (ejercicio == null) {

            redirectAttributes.addFlashAttribute(
                    "mensajeError",
                    "El ejercicio solicitado no existe."
            );

            return "redirect:/admin/ejercicios";
        }

        // Invertir el estado actual
        boolean nuevoEstado = !Boolean.TRUE.equals(
                ejercicio.getEstado()
        );

        ejercicio.setEstado(nuevoEstado);

        // Actualizar el registro en MySQL
        ejercicioRepository.save(ejercicio);

        redirectAttributes.addFlashAttribute(
                "mensaje",
                nuevoEstado
                        ? "Ejercicio activado correctamente."
                        : "Ejercicio desactivado correctamente."
        );

        return "redirect:/admin/ejercicios";
    }
}
