package com.example.productoApi.repository;

import com.example.productoApi.model.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria,Long> {

    List<Categoria> findByIdBetween(int num1, int num2);

    Categoria findByNombre(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    Optional<Categoria> findByNombreIgnoreCase(String nombre);
}
