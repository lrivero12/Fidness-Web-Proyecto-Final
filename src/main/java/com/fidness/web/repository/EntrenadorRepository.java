
package com.fidness.web.repository;

import com.fidness.web.domain.Entrenador;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrenadorRepository
        extends JpaRepository<Entrenador, Integer> {

    // Buscar entrenador por el ID del usuario
    Optional<Entrenador> findByUsuarioIdUsuario(Integer idUsuario);

    // Buscar entrenadores por nombre
    List<Entrenador> findByUsuarioNombreContainingIgnoreCase(
            String nombre
    );

    // Buscar entrenador por correo electrónico
    Optional<Entrenador> findByUsuarioCorreoIgnoreCase(
            String correo
    );

    // Listar entrenadores ordenados por nombre
    List<Entrenador> findAllByOrderByUsuarioNombreAsc();
}
