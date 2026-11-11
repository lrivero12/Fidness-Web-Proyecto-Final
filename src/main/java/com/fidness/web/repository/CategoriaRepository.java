
package com.fidness.web.repository;

import com.fidness.web.domain.Categoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    List<Categoria> findByEstadoTrueOrderByNombreAsc();

    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
}
