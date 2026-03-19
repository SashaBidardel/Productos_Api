package com.example.productoApi.security;
import com.example.productoApi.model.Role;
import lombok.extern.slf4j.Slf4j;

import com.example.productoApi.model.Categoria;
import com.example.productoApi.model.Producto;
import com.example.productoApi.model.Usuario;
import com.example.productoApi.repository.CategoriaRepository;
import com.example.productoApi.repository.ProductoRepository;
import com.example.productoApi.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
@Slf4j

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(CategoriaRepository catRepo, UsuarioRepository usuRepo,
                                   ProductoRepository prodRepo, PasswordEncoder encoder) {
        return args -> {
            // 1. Asegurar Categoría Principal
            Categoria general = catRepo.findByNombre("General");
            if (general == null) {
                general = new Categoria();
                general.setNombre("General");
                general = catRepo.save(general);
                log.info("Check: Categoría General creada");
            }

            // 2. Usuarios (Usando el Enum Role)
            if (usuRepo.findByUsername("admin").isEmpty()) {
                usuRepo.save(Usuario.builder()
                        .username("admin")
                        .password(encoder.encode("1234"))
                        .role(Role.ADMIN) // Cambiado a Enum
                        .build());
                log.info("Check: Admin creado");
            }

            if (usuRepo.findByUsername("usuario").isEmpty()) {
                usuRepo.save(Usuario.builder()
                        .username("usuario")
                        .password(encoder.encode("1234"))
                        .role(Role.USER) // Nuevo usuario estándar
                        .build());
                log.info("Check: Usuario estándar creado");
            }

            // 3. Productos variados
            if (prodRepo.findAll().isEmpty()) { // Si la lista está vacía, llenamos
                Producto p1 = new Producto();
                p1.setNombre("Teclado Mecánico");
                p1.setPrecio(85);
                p1.setCategoria(general);

                Producto p2 = new Producto();
                p2.setNombre("Ratón Gaming");
                p2.setPrecio(45);
                p2.setCategoria(general);

                Producto p3 = new Producto();
                p3.setNombre("Monitor");
                p3.setPrecio(150);
                p3.setCategoria(general);

                prodRepo.saveAll(java.util.List.of(p1, p2, p3));
                log.info("Check: Productos inicializados (Teclado, Ratón, Monitor)");
            }
        };
    }
}