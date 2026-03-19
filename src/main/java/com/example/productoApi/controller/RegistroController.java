package com.example.productoApi.controller;

import com.example.productoApi.model.Role;
import com.example.productoApi.model.Usuario;
import com.example.productoApi.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/registro")
@Slf4j
public class RegistroController {

    private final UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // 1. Ya no necesitamos el @GetMapping para mostrar el formulario.
    // El formulario ahora vive en el Frontend (React, Angular, HTML simple, etc.)

    // 2. Procesar el registro
    @PostMapping
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            // Seteamos el rol por defecto
            usuario.setRole(Role.USER);

            // Guardamos el usuario
            Usuario usuarioCreado = usuarioService.guardarUsuario(usuario);

            log.info("Nuevo usuario registrado: {}", usuarioCreado.getUsername());

            // En una API, en lugar de redirigir, devolvemos un mensaje de éxito
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "mensaje", "Usuario registrado correctamente",
                            "username", usuarioCreado.getUsername()
                    ));

        } catch (Exception e) {
            log.error("Error al registrar usuario: {}", e.getMessage());
            // Devolvemos el error en formato JSON
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error en el registro: " + e.getMessage()));
        }
    }
}