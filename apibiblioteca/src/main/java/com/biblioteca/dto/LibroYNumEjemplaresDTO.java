package com.biblioteca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroYNumEjemplaresDTO {

    private Long id;
    private String titulo;
    private String isbn;
    private LocalDate fechaPublicacion;
    private Integer numEjemplares;

    // Constructor adicional útil para consultas JPQL/HQL
    public LibroYNumEjemplaresDTO(Long id, String titulo, String isbn, LocalDate fechaPublicacion, Long numEjemplares) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
        this.fechaPublicacion = fechaPublicacion;
        this.numEjemplares = numEjemplares != null ? numEjemplares.intValue() : 0;
    }
}