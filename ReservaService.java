
package com.fidness.web.service;

import com.fidness.web.domain.Clase;
import com.fidness.web.domain.Cliente;
import com.fidness.web.domain.Reserva;

import com.fidness.web.repository.ClaseRepository;
import com.fidness.web.repository.ClienteRepository;
import com.fidness.web.repository.ReservaRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClaseRepository claseRepository;
    private final ClienteRepository clienteRepository;

    public ReservaService(
            ReservaRepository reservaRepository,
            ClaseRepository claseRepository,
            ClienteRepository clienteRepository) {

        this.reservaRepository = reservaRepository;
        this.claseRepository = claseRepository;
        this.clienteRepository = clienteRepository;
    }

    // ==========================================
    // 1. CONSULTAR CLASES DISPONIBLES
    // ==========================================

    @Transactional(readOnly = true)
    public List<Clase> listarClasesDisponibles() {

        return claseRepository
                .findByEstadoAndFechaGreaterThanEqualOrderByFechaAscHoraInicioAsc(
                        "PROGRAMADA",
                        LocalDateTime.now().toLocalDate()
                )
                .stream()
                .filter(clase -> LocalDateTime.of(
                        clase.getFecha(),
                        clase.getHoraInicio()
                ).isAfter(LocalDateTime.now()))
                .toList();
    }

    // ==========================================
    // 2. CONSULTAR RESERVAS DE UN CLIENTE
    // ==========================================

    @Transactional(readOnly = true)
    public List<Reserva> listarReservasCliente(
            Integer idCliente) {

        return reservaRepository
                .findByClienteIdClienteOrderByFechaReservaDesc(
                        idCliente
                );
    }

    // ==========================================
    // 3. CONSULTAR RESERVAS DE UNA CLASE
    // ==========================================

    @Transactional(readOnly = true)
    public List<Reserva> listarReservasClase(
            Integer idClase) {

        return reservaRepository
                .findByClaseIdClaseOrderByFechaReservaAsc(
                        idClase
                );
    }

    // ==========================================
    // 4. CONSULTAR SOLICITUDES PENDIENTES
    // ==========================================

    @Transactional(readOnly = true)
    public List<Reserva> listarSolicitudesPendientes(
            Integer idEntrenador) {

        return reservaRepository
                .findByClaseEntrenadorIdEntrenadorAndEstadoOrderByFechaReservaAsc(
                        idEntrenador,
                        "PENDIENTE"
                );
    }

    // ==========================================
    // 5. CONSULTAR TODAS LAS SOLICITUDES
    // ==========================================

    @Transactional(readOnly = true)
    public List<Reserva> listarReservasEntrenador(
            Integer idEntrenador) {

        return reservaRepository
                .findByClaseEntrenadorIdEntrenadorOrderByFechaReservaDesc(
                        idEntrenador
                );
    }

    // ==========================================
    // 6. CONTAR CUPOS OCUPADOS
    // ==========================================

    @Transactional(readOnly = true)
    public long contarCuposOcupados(Integer idClase) {

        return reservaRepository
                .countByClaseIdClaseAndEstadoIn(
                        idClase,
                        List.of("PENDIENTE", "CONFIRMADA")
                );
    }

    // ==========================================
    // 7. CONSULTAR CUPOS DISPONIBLES
    // ==========================================

    @Transactional(readOnly = true)
    public long consultarCuposDisponibles(
            Integer idClase) {

        Clase clase = claseRepository.findById(idClase)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La clase solicitada no existe."
                        )
                );

        long ocupados = contarCuposOcupados(idClase);

        return Math.max(
                0,
                clase.getCapacidadMaxima() - ocupados
        );
    }

    // ==========================================
    // 8. REGISTRAR UNA RESERVA
    // ==========================================

    @Transactional
    public Reserva registrarReserva(
            Integer idCliente,
            Integer idClase) {

        Cliente cliente = clienteRepository
                .findById(idCliente)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El cliente solicitado no existe."
                        )
                );

        if (!Boolean.TRUE.equals(
                cliente.getUsuario().getEstado())) {

            throw new IllegalArgumentException(
                    "Tu cuenta se encuentra desactivada."
            );
        }

        Clase clase = claseRepository
                .findById(idClase)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La clase seleccionada no existe."
                        )
                );

        if (!"PROGRAMADA".equals(clase.getEstado())) {

            throw new IllegalArgumentException(
                    "La clase no está disponible para reservas."
            );
        }

        LocalDateTime inicioClase = LocalDateTime.of(
                clase.getFecha(),
                clase.getHoraInicio()
        );

        if (!inicioClase.isAfter(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "No puedes reservar una clase que ya comenzó."
            );
        }

        boolean reservaExistente = reservaRepository
                .existsByClienteIdClienteAndClaseIdClaseAndEstadoIn(
                        idCliente,
                        idClase,
                        List.of("PENDIENTE", "CONFIRMADA")
                );

        if (reservaExistente) {

            throw new IllegalArgumentException(
                    "Ya tienes una reserva activa para esta clase."
            );
        }

        if (contarCuposOcupados(idClase)
                >= clase.getCapacidadMaxima()) {

            throw new IllegalArgumentException(
                    "La clase ya alcanzó su capacidad máxima."
            );
        }

        Reserva reserva = new Reserva();

        reserva.setCliente(cliente);
        reserva.setClase(clase);
        reserva.setEstado("PENDIENTE");

        return reservaRepository.save(reserva);
    }

    // ==========================================
    // 9. CANCELAR RESERVA DEL CLIENTE
    // ==========================================

    @Transactional
    public void cancelarReservaCliente(
            Integer idReserva,
            Integer idCliente) {

        Reserva reserva = reservaRepository
                .findById(idReserva)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La reserva solicitada no existe."
                        )
                );

        if (!reserva.getCliente()
                .getIdCliente().equals(idCliente)) {

            throw new IllegalArgumentException(
                    "No tienes permiso para cancelar esta reserva."
            );
        }

        if (!List.of("PENDIENTE", "CONFIRMADA")
                .contains(reserva.getEstado())) {

            throw new IllegalArgumentException(
                    "Esta reserva ya no puede cancelarse."
            );
        }

        LocalDateTime inicioClase = LocalDateTime.of(
                reserva.getClase().getFecha(),
                reserva.getClase().getHoraInicio()
        );

        if (!inicioClase.isAfter(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "No puedes cancelar una reserva de una clase que ya comenzó."
            );
        }

        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);
    }

    // ==========================================
    // 10. CONFIRMAR SOLICITUD
    // ==========================================

    @Transactional
    public void confirmarReserva(
            Integer idReserva,
            Integer idEntrenador) {

        Reserva reserva = buscarReservaEntrenador(
                idReserva,
                idEntrenador
        );

        if (!"PENDIENTE".equals(reserva.getEstado())) {

            throw new IllegalArgumentException(
                    "Solo se pueden confirmar reservas pendientes."
            );
        }

        if (!"PROGRAMADA".equals(
                reserva.getClase().getEstado())) {

            throw new IllegalArgumentException(
                    "La clase ya no se encuentra programada."
            );
        }

        if (!LocalDateTime.of(
                reserva.getClase().getFecha(),
                reserva.getClase().getHoraInicio()
        ).isAfter(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "No se puede confirmar una clase que ya comenzó."
            );
        }

        reserva.setEstado("CONFIRMADA");
        reservaRepository.save(reserva);
    }

    // ==========================================
    // 11. RECHAZAR SOLICITUD
    // ==========================================

    @Transactional
    public void rechazarReserva(
            Integer idReserva,
            Integer idEntrenador) {

        Reserva reserva = buscarReservaEntrenador(
                idReserva,
                idEntrenador
        );

        if (!"PENDIENTE".equals(reserva.getEstado())) {

            throw new IllegalArgumentException(
                    "Solo se pueden rechazar reservas pendientes."
            );
        }

        reserva.setEstado("RECHAZADA");
        reservaRepository.save(reserva);
    }

    // ==========================================
    // 12. VALIDAR PROPIEDAD DE LA RESERVA
    // ==========================================

    private Reserva buscarReservaEntrenador(
            Integer idReserva,
            Integer idEntrenador) {

        Reserva reserva = reservaRepository
                .findById(idReserva)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La reserva solicitada no existe."
                        )
                );

        if (!reserva.getClase()
                .getEntrenador()
                .getIdEntrenador()
                .equals(idEntrenador)) {

            throw new IllegalArgumentException(
                    "No tienes permiso para administrar esta reserva."
            );
        }

        return reserva;
    }
}
