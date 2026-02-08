package com.pablo.crud_articulos.repository;

import com.pablo.crud_articulos.model.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, Long> {

    // Búsqueda simple en todos los campos
    List<Articulo> findByTituloContainingIgnoreCaseOrAutoresContainingIgnoreCaseOrRevistaContainingIgnoreCaseOrResumenContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCase(
            String titulo, String autores, String revista, String resumen, String palabrasClave);

    // Búsqueda con AND
    @Query("SELECT a FROM Articulo a WHERE " +
           "(LOWER(a.titulo) LIKE LOWER(CONCAT('%', :q1, '%')) OR " +
           "LOWER(a.autores) LIKE LOWER(CONCAT('%', :q1, '%')) OR " +
           "LOWER(a.revista) LIKE LOWER(CONCAT('%', :q1, '%')) OR " +
           "LOWER(a.resumen) LIKE LOWER(CONCAT('%', :q1, '%')) OR " +
           "LOWER(a.palabrasClave) LIKE LOWER(CONCAT('%', :q1, '%'))) " +
           "AND " +
           "(LOWER(a.titulo) LIKE LOWER(CONCAT('%', :q2, '%')) OR " +
           "LOWER(a.autores) LIKE LOWER(CONCAT('%', :q2, '%')) OR " +
           "LOWER(a.revista) LIKE LOWER(CONCAT('%', :q2, '%')) OR " +
           "LOWER(a.resumen) LIKE LOWER(CONCAT('%', :q2, '%')) OR " +
           "LOWER(a.palabrasClave) LIKE LOWER(CONCAT('%', :q2, '%')))")
    List<Articulo> searchWithAnd(String q1, String q2);

    // Búsqueda con OR
    @Query("SELECT a FROM Articulo a WHERE " +
           "LOWER(a.titulo) LIKE LOWER(CONCAT('%', :q1, '%')) OR " +
           "LOWER(a.autores) LIKE LOWER(CONCAT('%', :q1, '%')) OR " +
           "LOWER(a.revista) LIKE LOWER(CONCAT('%', :q1, '%')) OR " +
           "LOWER(a.resumen) LIKE LOWER(CONCAT('%', :q1, '%')) OR " +
           "LOWER(a.palabrasClave) LIKE LOWER(CONCAT('%', :q1, '%')) OR " +
           "LOWER(a.titulo) LIKE LOWER(CONCAT('%', :q2, '%')) OR " +
           "LOWER(a.autores) LIKE LOWER(CONCAT('%', :q2, '%')) OR " +
           "LOWER(a.revista) LIKE LOWER(CONCAT('%', :q2, '%')) OR " +
           "LOWER(a.resumen) LIKE LOWER(CONCAT('%', :q2, '%')) OR " +
           "LOWER(a.palabrasClave) LIKE LOWER(CONCAT('%', :q2, '%'))")
    List<Articulo> searchWithOr(String q1, String q2);
}