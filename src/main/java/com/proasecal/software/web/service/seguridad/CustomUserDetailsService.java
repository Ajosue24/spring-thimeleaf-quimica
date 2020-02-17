package com.proasecal.software.web.service.seguridad;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proasecal.software.web.entity.seguridad.CustomUserDetail;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import com.proasecal.software.web.repository.seguridad.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuarios> optionalUser = this.usuarioRepository.findByNombreUsuario(username);

        optionalUser.orElseThrow(() -> new UsernameNotFoundException("Nombre de usuario no encontrado"));
        if (!optionalUser.get().getEstado()) {
            throw new UsernameNotFoundException("Usuario inactivo");
        }
        return optionalUser.map(CustomUserDetail::new).get();
    }

}
