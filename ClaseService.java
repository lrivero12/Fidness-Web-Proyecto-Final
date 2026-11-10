
package com.fidness.web.service;

import com.fidness.web.domain.Clase;
import com.fidness.web.domain.Entrenador;

import com.fidness.web.repository.ClaseRepository;
import com.fidness.web.repository.EntrenadorRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClaseService {

    private final ClaseRepository claseRepository;
    private final EntrenadorRepository entrenadorRepository;

    public ClaseService(
            ClaseRepository claseRepository,
            EntrenadorRepository entrenadorRepository) {

        this.claseRepository = claseRepository;
        this.entrenadorRepository = entrenadorRepository;
    }

    // ==========================================
    // 1. LISTAR CLASES DEL ENTRENADOR
    // ==========================================

    @Transactional(readOnly = true)
    public List<Clase> listarClases(Integer idEntrenador) {

        return claseRepository
                .findByEntrenadorIdEntrenadorOrderByFechaAscHoraInicioAsc(
                        idEntrenador
                );
    }

    // ==========================================
    // 2. BUSCAR UNA CLASE DEL ENTRENADOR
    // ==========================================

    @Transactional(readOnly = true)
    public Clase buscarClase(
            Integer idClase,
            Integer idEntrenador) {

        Clase clase = claseRepository.findById(idClase)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La clase solicitada no existe."
                        )
                );

        // Evitar acceso a clases de otros entrenadores
        if (!clase.getEntrenador()
                .getIdEntrenador()
                .equals(idEntrenador)) {

            throw new IllegalArgumentException(
                    "No tienes permiso para administrar esta clase."
            );
        }

        return clase;
    }

    // ==========================================
    // 3. REGISTRAR UNA CLASE
    // ==========================================

    @Transactional
    public Clase registrarClase(
            Integer idEntrenador,
            String nombre,
            String descripcion,
            LocalDate fecha,
            LocalTime horaInicio,
            Integer duracionMinutos,
            Integer capacidadMaxima) {

        Entrenador entrenador = entrenadorRepository
                .findById(idEntrenador)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El entrenador no existe."
                        )
                );

        if (!Boolean.TRUE.equals(
                entrenador.getUsuario().getEstado())) {

            throw new IllegalArgumentException(
                    "El entrenador se encuentra inactivo."
            );
        }

        validarDatos(
                nombre,
                fecha,
                horaInicio,
                duracionMinutos,
                capacidadMaxima
        );

        validarHorario(fecha, horaInicio, duracionMinutos);

        Clase clase = new Clase();

        clase.setNombre(nombre.trim());
        clase.setDescripcion(
                descripcion == null ? null : descripcion.trim()
        );
        clase.setFecha(fecha);
        clase.setHoraInicio(horaInicio);
        clase.setDuracionMinutos(duracionMinutos);
        clase.setCapacidadMaxima(capacidadMaxima);
        clase.setEntrenador(entrenador);
        clase.setEstado("PROGRAMADA");

        return claseRepository.save(clase);
    }

    // ==========================================
    // 4. ACTUALIZAR UNA CLASE
    // ==========================================

    @Transactional
    public Clase actualizarClase(
            Integer idClase,
            Integer idEntrenador,
            String nombre,
            String descripcion,
            LocalDate fecha,
            LocalTime horaInicio,
            Integer duracionMinutos,
            Integer capacidadMaxima) {

        Clase clase = buscarClase(idClase, idEntrenador);

        if (!"PROGRAMADA".equals(clase.getEstado())) {

            throw new IllegalArgumentException(
                    "Solo se pueden editar clases programadas."
            );
        }

        validarDatos(
                nombre,
                fecha,
                horaInicio,
                duracionMinutos,
                capacidadMaxima
        );

        validarHorario(fecha, horaInicio, duracionMinutos);

        clase.setNombre(nombre.trim());
        clase.setDescripcion(
                descripcion == null ? null : descripcion.trim()
        );
        clase.setFecha(fecha);
        clase.setHoraInicio(horaInicio);
        clase.setDuracionMinutos(duracionMinutos);
        clase.setCapacidadMaxima(capacidadMaxima);

        return claseRepository.save(clase);
    }

    // ==========================================
    // 5. CANCELAR UNA CLASE
    // ==========================================

    @Transactional
    public void cancelarClase(
            Integer idClase,
            Integer idEntrenador) {

        Clase clase = buscarClase(idClase, idEntrenador);

        if (!"PROGRAMADA".equals(clase.getEstado())) {

            throw new IllegalArgumentException(
                    "Solo se pueden cancelar clases programadas."
            );
        }

        clase.setEstado("CANCELADA");
        claseRepository.save(clase);
    }

    // ==========================================
    // 6. CONSULTAR AGENDA SEMANAL
    // ==========================================

    @Transactional(readOnly = true)
    public List<Clase> consultarAgenda(
            Integer idEntrenador,
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        if (fechaInicio == null || fechaFin == null
                || fechaFin.isBefore(fechaInicio)) {

            throw new IllegalArgumentException(
                    "El rango de fechas no es válido."
            );
        }

        return claseRepository
                .findByEntrenadorIdEntrenadorAndFechaBetweenOrderByFechaAscHoraInicioAsc(
                        idEntrenador,
                        fechaInicio,
                        fechaFin
                );
    }

    // ==========================================
    // 7. VALIDAR DATOS DE LA CLASE
    // ==========================================

    private void validarDatos(
            String nombre,
            LocalDate fecha,
            LocalTime horaInicio,
            Integer duracionMinutos,
            Integer capacidadMaxima) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la clase es obligatorio."
            );
        }

        if (nombre.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "El nombre no puede superar los 100 caracteres."
            );
        }

        if (fecha == null || horaInicio == null) {
            throw new IllegalArgumentException(
                    "Debes indicar la fecha y hora de la clase."
            );
        }

        if (duracionMinutos == null || duracionMinutos <= 0) {
            throw new IllegalArgumentException(
                    "La duración debe ser mayor que cero."
            );
        }

        if (capacidadMaxima == null || capacidadMaxima <= 0) {
            throw new IllegalArgumentException(
                    "La capacidad máxima debe ser mayor que cero."
            );
        }
    }

    // ==========================================
    // 8. VALIDAR FECHA Y HORARIO
    // ==========================================

    private void validarHorario(
            LocalDate fecha,
            LocalTime horaInicio,
            Integer duracionMinutos) {

        LocalDateTime inicio = LocalDateTime.of(
                fecha,
                horaInicio
        );

        if (!inicio.isAfter(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "La clase debe programarse para una fecha y hora futuras."
            );
        }

        LocalDateTime fin = inicio.plusMinutes(duracionMinutos);

        if (!fin.toLocalDate().equals(fecha)) {

            throw new IllegalArgumentException(
                    "La clase debe comenzar y finalizar el mismo día."
            );
        }
    }
}
