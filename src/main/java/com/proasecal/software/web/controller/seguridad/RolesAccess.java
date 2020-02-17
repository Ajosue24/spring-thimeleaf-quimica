package com.proasecal.software.web.controller.seguridad;

import com.proasecal.software.web.entity.seguridad.Modulos;
import com.proasecal.software.web.entity.seguridad.Permisos;
import com.proasecal.software.web.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RolesAccess {

    /**
     * Atributo Que indica si usuario puede ver el modulo
     */
    public static List<String> rolesPermitidos(String nombreModulo){
        Optional<List<Modulos>> collectModulos = Util.userModules();
        List<String> lista = new ArrayList<>();
        lista.add("ADMIN");
        if(collectModulos.isPresent()) {
            Boolean isPermission = collectModulos.get().stream().anyMatch(modulos -> modulos.getNombreModulo().equals(nombreModulo)||modulos.getIdSecciones().getNombre().equals(nombreModulo));
            if(!isPermission) {
            }else {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                authentication.getAuthorities().forEach(l->lista.add(l.getAuthority()));
                for(GrantedAuthority g :authentication.getAuthorities()){
                    lista.add(g.getAuthority());
                }

                return lista;
            }
        }

        return lista;
    }

    /**
     * Se valida si usuario puede obtener acceso a funcionalidades o PERMISOS
     * Atributo Que indica si usuario puede ver el modulo
     */
    public static List<String> permisosPermitidos(String url){
        Optional<List<Permisos>> collectPermissions = Util.userPermissions();
        List<String> lista = new ArrayList<>();
        lista.add("ADMIN");
        if(collectPermissions.isPresent()) {
            Boolean isPermission = collectPermissions.get().stream().anyMatch(permisos -> permisos.getUrl().equals(url));
            if(!isPermission) {
            }else {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                authentication.getAuthorities().forEach(l->lista.add(l.getAuthority()));
                for(GrantedAuthority g :authentication.getAuthorities()){
                    lista.add(g.getAuthority());
                }
                return lista;
            }
        }

        return lista;
    }
}
