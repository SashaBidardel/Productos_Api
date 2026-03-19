package com.example.productoApi.service;

import com.example.productoApi.exceptions.ProductoException;
import com.example.productoApi.exceptions.UsuarioException;
import com.example.productoApi.model.Producto;

import com.example.productoApi.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service

public class ProductoService {
    @Autowired
    ProductoRepository productoRepository;
    public List<Producto> obtenerProductos(){
        return productoRepository.findAll();
    }
    public Producto productoConcreto(Long id){
        return productoRepository.findById(id).orElseThrow(() -> new ProductoException("Producto no encontrado, no existe el id:"+ id));
    }
    public Producto productoNombre(String nombre){
        return productoRepository.findByNombre(nombre).orElseThrow(() -> new ProductoException("Producto no encontrado,no existe el nombre:" + nombre));
    }
    public Producto guardar(Producto producto) {
        // Comprobar si ya existe
        Optional<Producto> existente = productoRepository.findByNombre(producto.getNombre());
        if(existente.isPresent() && !existente.get().getId().equals(producto.getId())){
            throw new UsuarioException("El nombre de usuario '" +producto.getNombre()  + "' ya existe");
        }
        return productoRepository.save(producto);
    }
    public void borrarProducto(Long id){
        productoRepository.deleteById(id);
    }
}
