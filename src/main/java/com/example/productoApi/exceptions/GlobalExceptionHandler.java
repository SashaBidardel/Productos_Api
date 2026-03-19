package com.example.productoApi.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice // 1. Cambiado de @ControllerAdvice a @RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CategoriaException.class, ProductoException.class, UsuarioException.class})
    public ResponseEntity<Map<String, String>> manejarExcepciones(RuntimeException ex) {

        log.error("Se ha producido una excepción: {}", ex.getMessage());

        // 2. Creamos un JSON de respuesta dinámico
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", "Error en la operación");
        respuesta.put("mensaje", ex.getMessage());
        respuesta.put("status", "400");

        // 3. Retornamos un 400 Bad Request con el JSON
        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    // Opcional: Manejar cualquier otro error inesperado (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> manejarErroresGenerales(Exception ex) {
        log.error("Error no controlado: ", ex);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", "Internal Server Error");
        respuesta.put("mensaje", "Ocurrió un error inesperado en el servidor");

        return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
