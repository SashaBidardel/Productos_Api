package com.example.productoApi.controller;
import com.example.productoApi.model.Usuario;
import com.example.productoApi.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Slf4j
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // 1. LISTAR TODOS
    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listarUsuarios();
    }

    // 2. BUSCAR POR ID (Útil para cargar datos antes de editar)
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

    // 3. CREAR (POST)
    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        Usuario nuevo = usuarioService.guardarUsuario(usuario);
        log.info("Usuario creado con ID: {}", nuevo.getId());
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // 4. EDITAR (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> editar(@PathVariable Long id, @RequestBody Usuario usuarioDetalles) {
        Usuario usuarioExistente = usuarioService.obtenerUsuarioPorId(id);

        if (usuarioExistente == null) {
            return ResponseEntity.notFound().build();
        }

        // Actualizamos los campos necesarios
        usuarioExistente.setUsername(usuarioDetalles.getUsername());
        usuarioExistente.setRole(usuarioDetalles.getRole());


        Usuario actualizado = usuarioService.guardarUsuario(usuarioExistente);
        log.info("Usuario ID {} actualizado", id);
        return ResponseEntity.ok(actualizado);
    }

    // 5. BORRAR (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);

        if (usuario != null) {
            usuarioService.borrarUsuario(id);
            log.info("Usuario ID {} eliminado", id);
            return ResponseEntity.noContent().build(); // Retorna 204 (sin contenido)
        }

        return ResponseEntity.notFound().build();
    }
}