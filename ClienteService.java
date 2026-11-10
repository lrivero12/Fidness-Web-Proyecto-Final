
package com.fidness.web.service;

import com.fidness.web.domain.Cliente;
import com.fidness.web.domain.Rol;
import com.fidness.web.domain.Usuario;

import com.fidness.web.repository.ClienteRepository;
import com.fidness.web.repository.RolRepository;
import com.fidness.web.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor e inyección de dependencias
    public ClienteService(
            ClienteRepository clienteRepository,
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {

        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =====================================================
    // 1. LISTAR TODOS LOS CLIENTES
    // =====================================================

    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() {

        return clienteRepository
                .findAllByOrderByUsuarioNombreAsc();
    }

    // =====================================================
    // 2. BUSCAR CLIENTE POR ID
    // =====================================================

    @Transactional(readOnly = true)
    public Cliente buscarCliente(Integer idCliente) {

        return clienteRepository.findById(idCliente)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El cliente solicitado no existe."
                        )
                );
    }

    // =====================================================
    // 3. REGISTRAR UN CLIENTE NUEVO
    // =====================================================

    @Transactional
    public Cliente registrarCliente(
            String nombre,
            String apellidos,
            String correo,
            String contrasena,
            String telefono,
            LocalDate fechaNacimiento) {

        // Validar los datos personales
        validarDatos(
                nombre,
                apellidos,
                correo,
                fechaNacimiento
        );

        // Validar contraseña obligatoria
        if (contrasena == null || contrasena.isBlank()) {

            throw new IllegalArgumentException(
                    "La contraseña es obligatoria."
            );
        }

        // Validar longitud mínima de contraseña
        if (contrasena.length() < 8) {

            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres."
            );
        }

        // BCrypt utiliza como máximo 72 bytes
        if (contrasena.getBytes(
                java.nio.charset.StandardCharsets.UTF_8
        ).length > 72) {

            throw new IllegalArgumentException(
                    "La contraseña no puede superar los 72 bytes."
            );
        }

        // Validar teléfono
        validarTelefono(telefono);

        // Normalizar correo electrónico
        String correoNormalizado =
                correo.trim().toLowerCase(Locale.ROOT);

        // Comprobar si el correo ya está registrado
        if (usuarioRepository
                .existsByCorreoIgnoreCase(correoNormalizado)) {

            throw new IllegalArgumentException(
                    "Ya existe un usuario registrado con ese correo."
            );
        }

        // Recuperar el rol CLIENTE desde MySQL
        Rol rolCliente = rolRepository
                .findByNombre("CLIENTE")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No se encontró el rol CLIENTE."
                        )
                );

        // Crear el usuario
        Usuario usuario = new Usuario();

        usuario.setNombre(nombre.trim());
        usuario.setApellidos(apellidos.trim());
        usuario.setCorreo(correoNormalizado);

        // Cifrar la contraseña mediante BCrypt
        usuario.setContrasena(
                passwordEncoder.encode(contrasena)
        );

        usuario.setTelefono(
                telefono == null ? null : telefono.trim()
        );

        usuario.setFechaNacimiento(fechaNacimiento);
        usuario.setEstado(true);
        usuario.setRol(rolCliente);

        // Guardar usuario en la tabla usuarios
        Usuario usuarioGuardado =
                usuarioRepository.save(usuario);

        // Crear el registro correspondiente en clientes
        Cliente cliente = new Cliente();

        cliente.setUsuario(usuarioGuardado);

        // Guardar cliente y devolver el registro
        return clienteRepository.save(cliente);
    }

    // =====================================================
    // 4. ACTUALIZAR LOS DATOS DE UN CLIENTE
    // =====================================================

    @Transactional
    public Cliente actualizarCliente(
            Integer idCliente,
            String nombre,
            String apellidos,
            String correo,
            String telefono,
            LocalDate fechaNacimiento) {

        // Buscar cliente existente
        Cliente cliente = buscarCliente(idCliente);

        Usuario usuario = cliente.getUsuario();

        // Validar los datos personales
        validarDatos(
                nombre,
                apellidos,
                correo,
                fechaNacimiento
        );

        // Validar teléfono
        validarTelefono(telefono);

        // Normalizar correo
        String correoNormalizado =
                correo.trim().toLowerCase(Locale.ROOT);

        // Comprobar que otro usuario no tenga el correo
        if (usuarioRepository
                .existsByCorreoIgnoreCaseAndIdUsuarioNot(
                        correoNormalizado,
                        usuario.getIdUsuario())) {

            throw new IllegalArgumentException(
                    "Ya existe otro usuario registrado con ese correo."
            );
        }

        // Actualizar información personal
        usuario.setNombre(nombre.trim());
        usuario.setApellidos(apellidos.trim());
        usuario.setCorreo(correoNormalizado);

        usuario.setTelefono(
                telefono == null ? null : telefono.trim()
        );

        usuario.setFechaNacimiento(fechaNacimiento);

        // No modificar contraseña, rol ni estado
        usuarioRepository.save(usuario);

        return cliente;
    }

    // =====================================================
    // 5. ACTIVAR O DESACTIVAR CLIENTE
    // =====================================================

    @Transactional
    public boolean cambiarEstado(Integer idCliente) {

        // Buscar cliente
        Cliente cliente = buscarCliente(idCliente);

        Usuario usuario = cliente.getUsuario();

        // Invertir estado actual
        boolean nuevoEstado =
                !Boolean.TRUE.equals(usuario.getEstado());

        usuario.setEstado(nuevoEstado);

        // Guardar el nuevo estado
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
            LocalDate fechaNacimiento) {

        // Comprobar campos obligatorios
        if (nombre == null || nombre.isBlank()
                || apellidos == null || apellidos.isBlank()
                || correo == null || correo.isBlank()) {

            throw new IllegalArgumentException(
                    "Nombre, apellidos y correo son obligatorios."
            );
        }

        // Validar longitud del nombre
        if (nombre.trim().length() > 80) {

            throw new IllegalArgumentException(
                    "El nombre no puede superar los 80 caracteres."
            );
        }

        // Validar longitud de apellidos
        if (apellidos.trim().length() > 100) {

            throw new IllegalArgumentException(
                    "Los apellidos no pueden superar los 100 caracteres."
            );
        }

        // Validar longitud y formato del correo
        String correoNormalizado = correo.trim();

        if (correoNormalizado.length() > 150
                || !correoNormalizado.matches(
                        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

            throw new IllegalArgumentException(
                    "Introduce un correo electrónico válido."
            );
        }

        // Validar fecha de nacimiento
        if (fechaNacimiento != null
                && fechaNacimiento.isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "La fecha de nacimiento no puede ser futura."
            );
        }
    }

    // =====================================================
    // 7. VALIDAR TELÉFONO
    // =====================================================

    private void validarTelefono(String telefono) {

        if (telefono != null
                && telefono.trim().length() > 20) {

            throw new IllegalArgumentException(
                    "El teléfono no puede superar los 20 caracteres."
            );
        }
    }
}
