package com.example.productoApi.controller;

import com.example.productoApi.model.Categoria;
import com.example.productoApi.model.Producto;
import com.example.productoApi.service.CategoriaService;
import com.example.productoApi.service.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Slf4j
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    @Autowired
    public ProductoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    // 1. LISTAR TODOS
    @GetMapping
    public List<Producto> listaProductos() {
        log.info("Listando todos los productos");
        return productoService.obtenerProductos();
    }

    // 2. OBTENER UNO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> productoConcreto(@PathVariable Long id) {
        Producto producto = productoService.productoConcreto(id);
        if (producto == null) {
            log.warn("Producto con ID {} no encontrado", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El producto con ID: " + id + " no existe");
        }
        log.info("Buscando el producto: {}", producto.getNombre());
        return ResponseEntity.ok(producto);
    }

    // 3. CREAR PRODUCTO (POST)
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        // Validamos que la categoría venga en el JSON y exista en la DB
        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            return ResponseEntity.badRequest().body("Debe especificar una categoría válida con su ID");
        }

        Categoria cat = categoriaService.obtenerCategoria(producto.getCategoria().getId());
        if (cat == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La categoría seleccionada no existe");
        }

        producto.setCategoria(cat);
        Producto nuevo = productoService.guardar(producto);
        log.info("Producto creado: {}", nuevo.getNombre());
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // 4. EDITAR PRODUCTO (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<?> editarProducto(@PathVariable Long id, @RequestBody Producto detalles) {
        Producto existente = productoService.productoConcreto(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        // Actualizar campos básicos
        existente.setNombre(detalles.getNombre());
        existente.setPrecio(detalles.getPrecio()); // Asumiendo que tienes este campo

        // Si intentan cambiar la categoría
        if (detalles.getCategoria() != null && detalles.getCategoria().getId() != null) {
            Categoria nuevaCat = categoriaService.obtenerCategoria(detalles.getCategoria().getId());
            if (nuevaCat != null) {
                existente.setCategoria(nuevaCat);
            }
        }

        Producto actualizado = productoService.guardar(existente);
        log.info("Producto ID {} actualizado correctamente", id);
        return ResponseEntity.ok(actualizado);
    }

    // 5. BORRAR PRODUCTO (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarProducto(@PathVariable Long id) {
        Producto producto = productoService.productoConcreto(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        productoService.borrarProducto(id);
        log.warn("Producto con ID {} eliminado", id);
        return ResponseEntity.noContent().build();
    }
}