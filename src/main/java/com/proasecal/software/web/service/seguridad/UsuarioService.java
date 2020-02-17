package com.proasecal.software.web.service.seguridad;

import com.proasecal.software.web.entity.seguridad.AuditoriaWeb;
import com.proasecal.software.web.entity.seguridad.EnumAccionAuditoria;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import com.proasecal.software.web.repository.seguridad.UsuarioRepository;
import com.proasecal.software.web.service.inscripcion.InscripcionProgramasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UsuarioService {

    @Autowired
    UsuarioRepository usuariosRepository;

    @Autowired
    InscripcionProgramasService inscripcionProgramasService;
    @Autowired
    AuditoriaWebService auditoriaWebService;
    private String tableName;

    @PostConstruct
    private void init() {
        Class<?> c = Usuarios.class;
        Table table = c.getAnnotation(Table.class);
        this.tableName = table.name();
    }

    public List<Usuarios> list() {
        return (List<Usuarios>) usuariosRepository.findAll();
    }

    public Usuarios get(Long id) {
        return usuariosRepository.findById(id).get();
    }

    public void save(Usuarios usuarios) {
        AuditoriaWeb auditoriaWeb = new AuditoriaWeb(tableName, usuarios.getIdUsuario(), usuarios.getIdUsuario() != 0 ? EnumAccionAuditoria.ACTUALIZAR.getNombreAccion() : EnumAccionAuditoria.CREAR.getNombreAccion());
        usuariosRepository.save(usuarios);
        auditoriaWeb.setIdElementoTabla(usuarios.getIdUsuario());
        auditoriaWebService.save(auditoriaWeb);
    }


    public Usuarios findByUserName(String userName) {
        Optional<Usuarios> usuario = usuariosRepository.findByNombreUsuario(userName);
        if (usuario.isPresent()) {
            return usuario.get();
        }
        return null;
    }

    public Boolean validarUsuarioExistente(String nombreUsuario) {
        return usuariosRepository.existsByNombreUsuario(nombreUsuario);
    }

    public Boolean validarCodProasecalExistente(Integer codigoProasecal) {
        return usuariosRepository.existsByCodProasecal(codigoProasecal);
    }

    public Page<Usuarios> ListPaginated(String nombreUsuario, String nombres, String apellidos, String codProasecal, String estado, Pageable pageable) {
        Page<Usuarios> lista;
        if (codProasecal != null && (!codProasecal.isEmpty() && !codProasecal.equalsIgnoreCase("_"))) {
            lista = usuariosRepository.filtro(nombreUsuario, nombres, apellidos, codProasecal, estado, pageable);
            return lista;
        }
        lista = usuariosRepository.filtroSinCodProasecal(nombreUsuario, nombres, apellidos, estado, pageable);
        return lista;
    }

    public Boolean deleteById(Long idUsuario) {
        try {
            Usuarios usuarios = usuariosRepository.findById(idUsuario).get();
            UsuariosLabSedes user = usuarios.getUsuariosLabSedesSet().stream().filter(u -> u.getUsuarioId().getIdUsuario() == idUsuario).findFirst().get();

            if (inscripcionProgramasService.validarSiUsuarioPoseePrograma(user.getIdUsuarioLabSedes())) {
                return false;
            }
            ;
            usuarios.setRolesList(new HashSet<>());
            usuariosRepository.save(usuarios);
            usuariosRepository.deleteById(idUsuario);
            auditoriaWebService.save(new AuditoriaWeb(tableName, idUsuario, EnumAccionAuditoria.ELIMINAR.getNombreAccion()));
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Usuarios findByCodProasecal(String codProasecal) {
        Optional<Usuarios> user = usuariosRepository.findByCodProasecal(Integer.valueOf(codProasecal));
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public List<Usuarios> obtUsuxMuestra(String muestra) {
        return usuariosRepository.obtUsuxMuestra(muestra);
    }

}
