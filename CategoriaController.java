
package com.fidness.web.controller;

import com.fidness.web.domain.Categoria;
import com.fidness.web.repository.CategoriaRepository;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(
            CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Listar todas las categorías
    @GetMapping
    public String listarCategorias(Model model) {

        model.addAttribute(
                "categorias",
                categoriaRepository.findAll()
        );

        return "admin/categorias/listar";
    }

    // Mostrar formulario para nueva categoría
    @GetMapping("/nueva")
    public String nuevaCategoria(Model model) {

        Categoria categoria = new Categoria();
        categoria.setEstado(true);

        model.addAttribute("categoria", categoria);
        model.addAttribute("titulo", "Nueva categoría");

        return "admin/categorias/formulario";
    }

    // Guardar categoría nueva o editada
    @PostMapping("/guardar")
    public String guardarCategoria(
            @Valid @ModelAttribute("categoria") Categoria formulario,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        Categoria categoria;

        // Distinguir creación y edición
        if (formulario.getIdCategoria() == null) {

            categoria = new Categoria();
            categoria.setEstado(true);

        } else {

            categoria = categoriaRepository
                    .findById(formulario.getIdCategoria())
                    .orElse(null);

            if (categoria == null) {
                redirectAttributes.addFlashAttribute(
                        "error",
                        "La categoría que intentas editar no existe."
                );

                return "redirect:/admin/categorias";
            }
        }

        // Validar campos
        if (resultado.hasErrors()) {

            model.addAttribute(
                    "titulo",
                    formulario.getIdCategoria() == null
                            ? "Nueva categoría"
                            : "Editar categoría"
            );

            return "admin/categorias/formulario";
        }

        String nombre = formulario.getNombre().trim();

        // Evitar nombres duplicados
        boolean duplicado = categoriaRepository.findAll()
                .stream()
                .anyMatch(c ->
                        c.getNombre().equalsIgnoreCase(nombre)
                        && !c.getIdCategoria()
                                .equals(categoria.getIdCategoria())
                );

        if (duplicado) {

            resultado.rejectValue(
                    "nombre",
                    "duplicado",
                    "Ya existe una categoría con ese nombre."
            );

            model.addAttribute(
                    "titulo",
                    formulario.getIdCategoria() == null
                            ? "Nueva categoría"
                            : "Editar categoría"
            );

            return "admin/categorias/formulario";
        }

        // Actualizar únicamente campos permitidos
        categoria.setNombre(nombre);
        categoria.setDescripcion(
                formulario.getDescripcion()
        );
        categoria.setSeriesRecomendadas(
                formulario.getSeriesRecomendadas()
        );
        categoria.setRepeticionesRecomendadas(
                formulario.getRepeticionesRecomendadas()
        );

        // El estado se conserva en edición
        // y es true para categorías nuevas
        categoriaRepository.save(categoria);

        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Categoría guardada correctamente."
        );

        return "redirect:/admin/categorias";
    }

    // Mostrar formulario para editar
    @GetMapping("/editar/{id}")
    public String editarCategoria(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes) {

        Categoria categoria = categoriaRepository
                .findById(id)
                .orElse(null);

        if (categoria == null) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "La categoría solicitada no existe."
            );

            return "redirect:/admin/categorias";
        }

        model.addAttribute("categoria", categoria);
        model.addAttribute("titulo", "Editar categoría");

        return "admin/categorias/formulario";
    }

    // Activar o desactivar categoría
    @PostMapping("/estado/{id}")
    public String cambiarEstado(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        Categoria categoria = categoriaRepository
                .findById(id)
                .orElse(null);

        if (categoria == null) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "No se encontró la categoría."
            );

            return "redirect:/admin/categorias";
        }

        categoria.setEstado(
                !Boolean.TRUE.equals(categoria.getEstado())
        );

        categoriaRepository.save(categoria);

        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Estado de la categoría actualizado."
        );

        return "redirect:/admin/categorias";
    }
}

