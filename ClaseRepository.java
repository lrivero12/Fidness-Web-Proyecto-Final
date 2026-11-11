
package com.fidness.web.repository;

import com.fidness.web.domain.Clase;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaseRepository
        extends JpaRepository<Clase, Integer> {

    // ==========================================
    // 1. LISTAR CLASES DE UN ENTRENADOR
    // ==========================================

    List<Clase> findByEntrenadorIdEntrenadorOrderByFechaAscHoraInicioAsc(
            Integer idEntrenador
    );

    // ==========================================
    // 2. CONSULTAR CLASES POR ESTADO
    // ==========================================

    List<Clase> findByEstadoOrderByFechaAscHoraInicioAsc(
            String estado
    );

    // ==========================================
    // 3. CONSULTAR CLASES POR FECHA
    // ==========================================

    List<Clase> findByFechaOrderByHoraInicioAsc(
            LocalDate fecha
    );

    // ==========================================
    // 4. CONSULTAR AGENDA SEMANAL
    // ==========================================

    List<Clase> findByEntrenadorIdEntrenadorAndFechaBetweenOrderByFechaAscHoraInicioAsc(
            Integer idEntrenador,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    // ==========================================
    // 5. CONSULTAR CLASES FUTURAS
    // ==========================================

    List<Clase> findByEntrenadorIdEntrenadorAndFechaGreaterThanEqualOrderByFechaAscHoraInicioAsc(
            Integer idEntrenador,
            LocalDate fecha
    );

    // ==========================================
    // 6. CONSULTAR CLASES DISPONIBLES
    // ==========================================

    List<Clase> findByEstadoAndFechaGreaterThanEqualOrderByFechaAscHoraInicioAsc(
            String estado,
            LocalDate fecha
    );

    // ==========================================
    // 7. CONTAR CLASES DE UN ENTRENADOR
    // ==========================================

    long countByEntrenadorIdEntrenador(
            Integer idEntrenador
    );
}
