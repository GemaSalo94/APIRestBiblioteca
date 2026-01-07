package com.biblioteca.controllers;

import com.biblioteca.dto.LibroYNumEjemplaresDTO;
import com.biblioteca.entities.Autor;
import com.biblioteca.entities.Ejemplar;
import com.biblioteca.entities.Libro;
import com.biblioteca.repositories.AutorRepository;
import com.biblioteca.repositories.EjemplarRepository;
import com.biblioteca.repositories.LibroRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private EjemplarRepository ejemplarRepository;

    @GetMapping
    public ResponseEntity<?> listarLibros(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) LocalDate fecha,
            Pageable pageable) {

        if (titulo != null) {
            List<Libro> libros = libroRepository.findByTituloContainingIgnoreCase(titulo);
            return ResponseEntity.ok(libros);
        }

        if (fecha != null) {
            List<Libro> libros = libroRepository.findByFechaPublicacion(fecha);
            return ResponseEntity.ok(libros);
        }

        Page<Libro> libros = libroRepository.findAll(pageable);
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/resumen")
    public ResponseEntity<List<LibroYNumEjemplaresDTO>> listarLibrosResumen() {
        List<LibroYNumEjemplaresDTO> libros = libroRepository.findLibrosConNumEjemplares();
        return ResponseEntity.ok(libros);
    }

    @PostMapping
    public ResponseEntity<?> crearLibro(@Valid @RequestBody Libro libro) {
        try {
            Libro nuevoLibro = libroRepository.save(libro);
            return new ResponseEntity<>(nuevoLibro, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "isbn repetido");
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerLibroPorId(@PathVariable Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        return ResponseEntity.ok(libro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarLibro(@PathVariable Long id) {
        libroRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/autor/{autorId}")
    public ResponseEntity<Libro> añadirAutorALibro(@PathVariable Long id, @PathVariable Long autorId) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        Autor autor = autorRepository.findById(autorId)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));

        libro.getAutores().add(autor);
        libroRepository.save(libro);
        return ResponseEntity.ok(libro);
    }

    @DeleteMapping("/{id}/autor/{autorId}")
    public ResponseEntity<Libro> quitarAutorDeLibro(@PathVariable Long id, @PathVariable Long autorId) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        Autor autor = autorRepository.findById(autorId)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));

        libro.getAutores().remove(autor);
        libroRepository.save(libro);
        return ResponseEntity.ok(libro);
    }

    @PostMapping("/{id}/ejemplares")
    public ResponseEntity<Libro> crearYAñadirEjemplar(@PathVariable Long id, @Valid @RequestBody Ejemplar ejemplar) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        ejemplar.setLibro(libro);
        ejemplarRepository.save(ejemplar);

        return ResponseEntity.status(HttpStatus.CREATED).body(libro);
    }

    @DeleteMapping("/{id}/ejemplares/{ejemplarId}")
    public ResponseEntity<Void> quitarYBorrarEjemplar(@PathVariable Long id, @PathVariable Long ejemplarId) {
        Ejemplar ejemplar = ejemplarRepository.findById(ejemplarId)
                .orElseThrow(() -> new RuntimeException("Ejemplar no encontrado"));

        ejemplarRepository.delete(ejemplar);
        return ResponseEntity.noContent().build();
    }
}