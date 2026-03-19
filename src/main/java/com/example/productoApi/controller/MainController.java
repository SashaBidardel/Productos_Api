package com.example.productoApi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Centralizamos todo lo relacionado con seguridad
@Slf4j
public class MainController {

    // 1. El GET /login ya no hace falta en la API.
    // El formulario de login lo tendrá tu Frontend (React/Angular/HTML).

    // 2. Procesar el Login (POST)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        log.info("Intento de login para el usuario: {}", username);

        // Aquí iría tu lógica de validación con Spring Security
        // Por ahora, simulamos una respuesta exitosa
        return ResponseEntity.ok(Map.of(
                "mensaje", "Login exitoso",
                "token", "aqui-iria-tu-jwt-token",
                "username", username
        ));
    }


}
