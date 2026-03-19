package com.example.productoApi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Centralizamos  seguridad
@Slf4j
public class MainController {



    // Procesar el Login (POST)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        log.info("Intento de login para el usuario: {}", username);


        return ResponseEntity.ok(Map.of(
                "mensaje", "Login exitoso",
                "username", username
        ));
    }


}
