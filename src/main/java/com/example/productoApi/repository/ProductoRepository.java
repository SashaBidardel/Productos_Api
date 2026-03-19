package com.example.productoApi.repository;

import com.example.productoApi.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long> {
    Optional<Producto> findByNombre(String nombre);
}
