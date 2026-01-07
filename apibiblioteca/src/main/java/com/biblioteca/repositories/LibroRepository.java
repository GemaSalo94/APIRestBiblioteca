package com.biblioteca.repositories;

import com.biblioteca.dto.LibroYNumEjemplaresDTO;
import com.biblioteca.entities.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    // Consultas derivadas
    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    List<Libro> findByFechaPublicacion(LocalDate fechaPublicacion);

    // Consultas personalizadas con @Query y JPQL
    @Query("SELECT new com.biblioteca.dto.LibroYNumEjemplaresDTO(l.id, l.titulo, l.isbn, l.fechaPublicacion, COUNT(e)) " +
            "FROM Libro l LEFT JOIN l.ejemplares e " +
            "GROUP BY l.id, l.titulo, l.isbn, l.fechaPublicacion")
    List<LibroYNumEjemplaresDTO> findLibrosConNumEjemplares();

    @Query("SELECT COUNT(l) FROM Libro l WHERE YEAR(l.fechaPublicacion) < :year")
    Long countLibrosAntesDeYear(@Param("year") int year);
}