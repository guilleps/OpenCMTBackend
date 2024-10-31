package com.open.cmt.service;

import com.open.cmt.controller.request.AuthRequest;
import com.open.cmt.entity.Usuario;
import com.open.cmt.enumeration.Role;
import com.open.cmt.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public void crearUsuario(AuthRequest request) {
        Usuario usuario = Usuario.builder()
                .uuid(UUID.randomUUID())
                .usuario(request.getUsuario())
                .contrasenia(passwordEncoder.encode(request.getContrasenia()))
                .role(Role.ADMIN)
                .build();
        usuarioRepository.save(usuario);
    }
}
