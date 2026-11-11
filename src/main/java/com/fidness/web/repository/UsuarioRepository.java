
package com.fidness.web.repository;

import com.fidness.web.domain.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Buscar usuario por correo electrónico
    // Utilizado por el inicio de sesión
    Optional<Usuario> findByCorreo(String correo);

    // Comprobar si un correo ya está registrado
    boolean existsByCorreo(String correo);

    // Validar correos sin distinguir mayúsculas y minúsculas
    boolean existsByCorreoIgnoreCase(String correo);

    // Comprobar si otro usuario tiene el correo al editar
    boolean existsByCorreoIgnoreCaseAndIdUsuarioNot(
            String correo,
            Integer idUsuario
    );
}
