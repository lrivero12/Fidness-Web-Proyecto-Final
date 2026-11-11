package com.fidness.web.repository;

import com.fidness.web.domain.Rol;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    // Buscar un rol por su nombre
    Optional<Rol> findByNombre(String nombre);
}