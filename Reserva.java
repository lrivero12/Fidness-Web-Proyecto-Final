
package com.fidness.web.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reserva {

    // ==========================================
    // 1. IDENTIFICADOR DE LA RESERVA
    // ==========================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer idReserva;

    // ==========================================
    // 2. CLIENTE QUE REALIZA LA RESERVA
    // ==========================================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    // ==========================================
    // 3. CLASE RESERVADA
    // ==========================================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_clase", nullable = false)
    private Clase clase;

    // ==========================================
    // 4. FECHA DE REGISTRO
    // ==========================================

    @Column(name = "fecha_reserva",
            insertable = false,
            updatable = false)
    private LocalDateTime fechaReserva;

    // ==========================================
    // 5. ESTADO DE LA RESERVA
    // ==========================================

    @Column(name = "estado",
            nullable = false,
            length = 20)
    private String estado = "PENDIENTE";

    // ==========================================
    // 6. CONSTRUCTOR VACÍO
    // ==========================================

    public Reserva() {
    }

    // ==========================================
    // 7. GETTERS Y SETTERS
    // ==========================================

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
