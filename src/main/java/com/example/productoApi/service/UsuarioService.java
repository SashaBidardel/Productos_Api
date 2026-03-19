package com.example.productoApi.service;

import java.util.Optional;
import com.example.productoApi.exceptions.UsuarioException;
import com.example.productoApi.model.Usuario;
import com.example.productoApi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    // LISTAR TODOS LOS USUARIOS
    public List<Usuario> listarUsuarios() {
        return usuarioRepo.findAll();
    }
    // OBTENER USUARIO POR USERNAME
    public Usuario obtenerUsuarioPorUsername(String username) {
        return usuarioRepo.findByUsername(username).orElseThrow(()-> new UsuarioException("Usuario no encontrado,no existe el nombre:" + username));
    }

    // GUARDAR NUEVO USUARIO O ACTUALIZAR EXISTENTE
    public Usuario guardarUsuario(Usuario usuario) {
        // Comprobar si ya existe
        Optional<Usuario> existente = usuarioRepo.findByUsername(usuario.getUsername());
        if(existente.isPresent() && !existente.get().getId().equals(usuario.getId())){
            throw new UsuarioException("El nombre de usuario '" + usuario.getUsername() + "' ya existe");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        // Si no existe, lo guardamos
        return usuarioRepo.save(usuario);
    }

    // OBTENER USUARIO POR ID
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepo.findById(id).orElse(null);
    }



    // BORRAR USUARIO POR ID
    public void borrarUsuario(Long id) {
        usuarioRepo.deleteById(id);
    }
}

