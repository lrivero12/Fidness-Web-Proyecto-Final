
package com.fidness.web.service;

import com.fidness.web.domain.Entrenador;
import com.fidness.web.domain.Rol;
import com.fidness.web.domain.Usuario;

import com.fidness.web.repository.EntrenadorRepository;
import com.fidness.web.repository.RolRepository;
import com.fidness.web.repository.UsuarioRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EntrenadorService {

    private final EntrenadorRepository entrenadorRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public EntrenadorService(
            EntrenadorRepository entrenadorRepository,
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {

        this.entrenadorRepository = entrenadorRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =====================================================
    // 1. LISTAR ENTRENADORES
    // =====================================================

    @Transactional(readOnly = true)
    public List<Entrenador> listarEntrenadores() {

        return entrenadorRepository
                .findAllByOrderByUsuarioNombreAsc();
    }

    // =====================================================
    // 2. BUSCAR ENTRENADOR POR ID
    // =====================================================

    @Transactional(readOnly = true)
    public Entrenador buscarEntrenador(Integer idEntrenador) {

        return entrenadorRepository.findById(idEntrenador)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El entrenador solicitado no existe."
                        )
                );
    }

    // =====================================================
    // 3. REGISTRAR ENTRENADOR
    // =====================================================

    @Transactional
    public Entrenador registrarEntrenador(
            String nombre,
            String apellidos,
            String correo,
            String contrasena,
            String telefono,
            LocalDate fechaNacimiento) {

        validarDatos(
                nombre,
                apellidos,
                correo,
                telefono,
                fechaNacimiento
        );

        validarContrasena(contrasena);

        String correoNormalizado =
                correo.trim().toLowerCase(Locale.ROOT);

        if (usuarioRepository
                .existsByCorreoIgnoreCase(correoNormalizado)) {

            throw new IllegalArgumentException(
                    "Ya existe un usuario registrado con ese correo."
            );
        }

        // Obtener el rol ENTRENADOR
        Rol rolEntrenador = rolRepository
                .findByNombre("ENTRENADOR")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No se encontró el rol ENTRENADOR."
                        )
                );

        // Crear usuario
        Usuario usuario = new Usuario();

        usuario.setNombre(nombre.trim());
        usuario.setApellidos(apellidos.trim());
        usuario.setCorreo(correoNormalizado);

        // Cifrar contraseña
        usuario.setContrasena(
                passwordEncoder.encode(contrasena)
        );

        usuario.setTelefono(
                telefono == null ? null : telefono.trim()
        );

        usuario.setFechaNacimiento(fechaNacimiento);
        usuario.setEstado(true);
        usuario.setRol(rolEntrenador);

        // Guardar usuario
        Usuario usuarioGuardado =
                usuarioRepository.save(usuario);

        // Crear entrenador asociado
        Entrenador entrenador = new Entrenador();
        entrenador.setUsuario(usuarioGuardado);

        return entrenadorRepository.save(entrenador);
    }

    // =====================================================
    // 4. ACTUALIZAR ENTRENADOR
    // =====================================================

    @Transactional
    public Entrenador actualizarEntrenador(
            Integer idEntrenador,
            String nombre,
            String apellidos,
            String correo,
            String telefono,
            LocalDate fechaNacimiento) {

        Entrenador entrenador = buscarEntrenador(idEntrenador);
        Usuario usuario = entrenador.getUsuario();

        validarDatos(
                nombre,
                apellidos,
                correo,
                telefono,
                fechaNacimiento
        );

        String correoNormalizado =
                correo.trim().toLowerCase(Locale.ROOT);

        if (usuarioRepository
                .existsByCorreoIgnoreCaseAndIdUsuarioNot(
                        correoNormalizado,
                        usuario.getIdUsuario())) {

            throw new IllegalArgumentException(
                    "Ya existe otro usuario registrado con ese correo."
            );
        }

        usuario.setNombre(nombre.trim());
        usuario.setApellidos(apellidos.trim());
        usuario.setCorreo(correoNormalizado);

        usuario.setTelefono(
                telefono == null ? null : telefono.trim()
        );

        usuario.setFechaNacimiento(fechaNacimiento);

        // Conservar contraseña, rol y estado
        usuarioRepository.save(usuario);

        return entrenador;
    }

    // =====================================================
    // 5. ACTIVAR O DESACTIVAR ENTRENADOR
    // =====================================================

    @Transactional
    public boolean cambiarEstado(Integer idEntrenador) {

        Entrenador entrenador = buscarEntrenador(idEntrenador);
        Usuario usuario = entrenador.getUsuario();

        boolean nuevoEstado =
                !Boolean.TRUE.equals(usuario.getEstado());

        usuario.setEstado(nuevoEstado);
        usuarioRepository.save(usuario);

        return nuevoEstado;
    }

    // =====================================================
    // 6. VALIDAR DATOS PERSONALES
    // =====================================================

    private void validarDatos(
            String nombre,
            String apellidos,
            String correo,
            String telefono,
            LocalDate fechaNacimiento) {

        if (nombre == null || nombre.isBlank()
                || apellidos == null || apellidos.isBlank()
                || correo == null || correo.isBlank()) {

            throw new IllegalArgumentException(
                    "Nombre, apellidos y correo son obligatorios."
            );
        }

        if (nombre.trim().length() > 80) {

            throw new IllegalArgumentException(
                    "El nombre no puede superar los 80 caracteres."
            );
        }

        if (apellidos.trim().length() > 100) {

            throw new IllegalArgumentException(
                    "Los apellidos no pueden superar los 100 caracteres."
            );
        }

        String correoValidado = correo.trim();

        if (correoValidado.length() > 150
                || !correoValidado.matches(
                        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

            throw new IllegalArgumentException(
                    "Introduce un correo electrónico válido."
            );
        }

        if (telefono != null && telefono.trim().length() > 20) {

            throw new IllegalArgumentException(
                    "El teléfono no puede superar los 20 caracteres."
            );
        }

        if (fechaNacimiento != null
                && fechaNacimiento.isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "La fecha de nacimiento no puede ser futura."
            );
        }
    }

    // =====================================================
    // 7. VALIDAR CONTRASEÑA
    // =====================================================

    private void validarContrasena(String contrasena) {

        if (contrasena == null || contrasena.isBlank()) {

            throw new IllegalArgumentException(
                    "La contraseña es obligatoria."
            );
        }

        if (contrasena.length() < 8) {

            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres."
            );
        }

        if (contrasena.getBytes(StandardCharsets.UTF_8).length > 72) {

            throw new IllegalArgumentException(
                    "La contraseña no puede superar los 72 bytes."
            );
        }
    }
}
