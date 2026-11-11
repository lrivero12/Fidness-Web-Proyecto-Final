
package com.fidness.web.domain;

import jakarta.persistence.*;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    // Nombre obligatorio y máximo 80 caracteres
    @NotBlank(message = "El nombre de la categoría es obligatorio.")
    @Size(max = 80, message = "El nombre no puede superar los 80 caracteres.")
    @Column(name = "nombre", nullable = false, unique = true, length = 80)
    private String nombre;

    // Descripción opcional, máximo 255 caracteres
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres.")
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    // Las series deben estar entre 1 y 20
    @NotNull(message = "Debes indicar las series recomendadas.")
    @Min(value = 1, message = "El mínimo permitido es 1 serie.")
    @Max(value = 20, message = "El máximo permitido es 20 series.")
    @Column(name = "series_recomendadas")
    private Integer seriesRecomendadas;

    // Repeticiones obligatorias
    @NotBlank(message = "Debes indicar las repeticiones recomendadas.")
    @Size(max = 30, message = "Las repeticiones no pueden superar los 30 caracteres.")
    @Column(name = "repeticiones_recomendadas", length = 30)
    private String repeticionesRecomendadas;

    // Estado de la categoría
    @NotNull(message = "El estado de la categoría es obligatorio.")
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;

    public Categoria() {
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getSeriesRecomendadas() {
        return seriesRecomendadas;
    }

    public void setSeriesRecomendadas(Integer seriesRecomendadas) {
        this.seriesRecomendadas = seriesRecomendadas;
    }

    public String getRepeticionesRecomendadas() {
        return repeticionesRecomendadas;
    }

    public void setRepeticionesRecomendadas(String repeticionesRecomendadas) {
        this.repeticionesRecomendadas = repeticionesRecomendadas;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
