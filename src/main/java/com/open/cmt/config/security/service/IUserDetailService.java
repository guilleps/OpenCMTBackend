package com.open.cmt.config.security.service;

import com.open.cmt.entity.Usuario;
import com.open.cmt.enumeration.Role;
import com.open.cmt.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class IUserDetailService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        if (usuario.getRole() != Role.ADMIN)
            throw new UsernameNotFoundException("Acceso denegado: El usuario no tiene privilegios de ADMIN");

        return new User(usuario.getUsuario(), usuario.getContrasenia(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name())));
    }
}
