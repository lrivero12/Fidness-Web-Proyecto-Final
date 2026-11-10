
package com.fidness.web.repository;

import com.fidness.web.domain.Reserva;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository
        extends JpaRepository<Reserva, Integer> {

    // ==========================================
    // 1. LISTAR RESERVAS DE UN CLIENTE
    // ==========================================

    List<Reserva> findByClienteIdClienteOrderByFechaReservaDesc(
            Integer idCliente
    );

    // ==========================================
    // 2. LISTAR RESERVAS DE UNA CLASE
    // ==========================================

    List<Reserva> findByClaseIdClaseOrderByFechaReservaAsc(
            Integer idClase
    );

    // ==========================================
    // 3. VERIFICAR RESERVAS ACTIVAS DUPLICADAS
    // ==========================================

    boolean existsByClienteIdClienteAndClaseIdClaseAndEstadoIn(
            Integer idCliente,
            Integer idClase,
            List<String> estados
    );

    // ==========================================
    // 4. CONTAR RESERVAS CONFIRMADAS
    // ==========================================

    long countByClaseIdClaseAndEstado(
            Integer idClase,
            String estado
    );

    // ==========================================
    // 5. CONTAR RESERVAS PENDIENTES Y CONFIRMADAS
    // ==========================================

    long countByClaseIdClaseAndEstadoIn(
            Integer idClase,
            List<String> estados
    );

    // ==========================================
    // 6. CONSULTAR SOLICITUDES DE UN ENTRENADOR
    // ==========================================

    List<Reserva> findByClaseEntrenadorIdEntrenadorAndEstadoOrderByFechaReservaAsc(
            Integer idEntrenador,
            String estado
    );

    // ==========================================
    // 7. LISTAR TODAS LAS RESERVAS DEL ENTRENADOR
    // ==========================================

    List<Reserva> findByClaseEntrenadorIdEntrenadorOrderByFechaReservaDesc(
            Integer idEntrenador
    );

    // ==========================================
    // 8. LISTAR ASISTENTES CONFIRMADOS
    //    DE LAS CLASES DEL ENTRENADOR
    // ==========================================

    List<Reserva> findByClaseEntrenadorIdEntrenadorAndEstadoOrderByClaseFechaAscClaseHoraInicioAsc(
            Integer idEntrenador,
            String estado
    );
}
