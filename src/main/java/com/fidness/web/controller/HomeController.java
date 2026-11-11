
package com.fidness.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio(Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        boolean esAdministrador = authentication
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority()
                        .equals("ROLE_ADMINISTRADOR"));

        if (esAdministrador) {
            return "redirect:/admin/dashboard";
        }

        boolean esEntrenador = authentication
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority()
                        .equals("ROLE_ENTRENADOR"));

        if (esEntrenador) {
            return "redirect:/entrenador/dashboard";
        }

        boolean esCliente = authentication
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority()
                        .equals("ROLE_CLIENTE"));

        if (esCliente) {
            return "redirect:/cliente/dashboard";
        }

        return "redirect:/login";
    }
}
