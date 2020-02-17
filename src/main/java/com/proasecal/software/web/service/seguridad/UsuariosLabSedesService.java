package com.proasecal.software.web.service.seguridad;

import com.proasecal.software.web.entity.seguridad.Usuarios;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import com.proasecal.software.web.repository.seguridad.UsuariosLabSedesRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuariosLabSedesService {

    @Autowired
    UsuariosLabSedesRespository usuariosLabSedesRespository;

    public void save(UsuariosLabSedes usuariosLabSedes){
        usuariosLabSedesRespository.save(usuariosLabSedes);
    }

    public List<UsuariosLabSedes> obtenerUsuariosPorSedes(Long sedesId){
        return usuariosLabSedesRespository.findByIdSedes_IdSedes(sedesId);
    }

    public  List<UsuariosLabSedes> list() {
        return (List<UsuariosLabSedes>) this.usuariosLabSedesRespository.findAll();
    }
    public  Optional<UsuariosLabSedes> get(Long id) {
        return  this.usuariosLabSedesRespository.findById(id);
    }

    public UsuariosLabSedes buscarXUsuario(Long idUsuario){
        return usuariosLabSedesRespository.findByUsuarioId_IdUsuario(idUsuario);
    }
    public UsuariosLabSedes buscarXUsuarioID(Usuarios usuarios) {
        Optional<UsuariosLabSedes> userLabSedes = this.usuariosLabSedesRespository.findByUsuarioId(usuarios);
        if (userLabSedes.isPresent()){
            return userLabSedes.get();
        }
        return null;
    }

   public UsuariosLabSedes buscarPorNombreUsuario(String nombreUsuario) {
       return usuariosLabSedesRespository.findByUsuarioId_NombreUsuario(nombreUsuario);
   }

   public UsuariosLabSedes findTest(Long idUsuariosLabSedes){
       return usuariosLabSedesRespository.findTest(idUsuariosLabSedes);
   }

}
