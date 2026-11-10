
package com.fidness.web.repository;

import com.fidness.web.domain.Cliente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    // Buscar un cliente por su usuario
    Optional<Cliente> findByUsuarioIdUsuario(Integer idUsuario);

    // Buscar clientes por nombre
    List<Cliente> findByUsuarioNombreContainingIgnoreCase(String nombre);

    // Buscar clientes por correo electrónico
    Optional<Cliente> findByUsuarioCorreoIgnoreCase(String correo);

    // Listar clientes ordenados por nombre
    List<Cliente> findAllByOrderByUsuarioNombreAsc();
}
