package com.example.productoApi.controller;

import com.example.productoApi.model.Categoria;
import com.example.productoApi.service.CategoriaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias") // Convención /api/...
@Slf4j // Reemplaza la creación manual del Logger
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // 1. LISTAR TODAS
    @GetMapping
    public List<Categoria> listaCategorias() {
        log.info("Listando todas las categorías");
        return categoriaService.listaCategorias();
    }

    // 2. OBTENER UNA POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoria(@PathVariable Long id) {
        Categoria categoria = categoriaService.obtenerCategoria(id);
        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }
        log.info("Buscando categoría con id: {}", id);
        return ResponseEntity.ok(categoria);
    }

    // 3. CREAR CATEGORÍA
    @PostMapping
    public ResponseEntity<Categoria> guardar(@RequestBody Categoria categoria) {
        Categoria nueva = categoriaService.guardarCategoria(categoria);
        log.info("Categoría guardada correctamente: {}", nueva.getNombre());
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // 4. EDITAR CATEGORÍA
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Categoria detalles) {
        Categoria existente = categoriaService.obtenerCategoria(id);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        // Validación lógica: No editar la categoría "General"
        if (existente.getNombre().equalsIgnoreCase("General")) {
            log.warn("Intento fallido de editar la categoría General");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No se puede editar la categoría General");
        }

        existente.setNombre(detalles.getNombre());


        Categoria actualizada = categoriaService.guardarCategoria(existente);
        return ResponseEntity.ok(actualizada);
    }

    // 5. BORRAR CATEGORÍA
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id) {
        Categoria categoria = categoriaService.obtenerCategoria(id);

        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }

        if (categoria.getNombre().equalsIgnoreCase("General")) {
            log.warn("No se puede borrar la categoría General");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Operación no permitida: Categoría General protegida");
        }

        categoriaService.borrarCategoria(id);
        log.warn("Eliminada categoría con ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}