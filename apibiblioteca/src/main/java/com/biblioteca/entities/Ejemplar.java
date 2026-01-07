package com.biblioteca.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ejemplares")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ejemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String descripcion;

    @NotNull(message = "El campo disponible es obligatorio")
    @Column(nullable = false)
    private Boolean disponible;

    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    @JsonIgnore
    private Libro libro;
}