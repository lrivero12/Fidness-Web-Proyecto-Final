
package com.fidness.web.repository;

import com.fidness.web.domain.Ejercicio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EjercicioRepository extends JpaRepository<Ejercicio, Integer> {

    List<Ejercicio> findByEstadoTrueOrderByNombreAsc();

    List<Ejercicio> findByCategoriaIdCategoria(Integer idCategoria);

    List<Ejercicio> findByNivelIgnoreCase(String nivel);

    List<Ejercicio> findByNombreContainingIgnoreCase(String nombre);
}
