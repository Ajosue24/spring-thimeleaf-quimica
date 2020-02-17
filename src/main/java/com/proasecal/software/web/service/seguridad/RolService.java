package com.proasecal.software.web.service.seguridad;

import com.proasecal.software.web.entity.seguridad.AuditoriaWeb;
import com.proasecal.software.web.entity.seguridad.EnumAccionAuditoria;
import com.proasecal.software.web.entity.seguridad.Roles;
import com.proasecal.software.web.repository.seguridad.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.Table;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class RolService {

    @Autowired
    RolRepository rolesRepository;
    @Autowired
    AuditoriaWebService auditoriaWebService;
    private String tableName;
    @PostConstruct
    private void init(){
        Class<?> c = Roles.class;
        Table table = c.getAnnotation(Table.class);
        this.tableName = table.name();
    }
    public List<Roles> obtenerListaRoles(){
        return (List<Roles>)rolesRepository.findAll();
    }

    public Roles obtenerRol(Long id){
       return rolesRepository.findById(id).get();
    }
    
    
    public List <Roles> listUsuariosRolAsesor(){
   	return (List<Roles>)rolesRepository.listUsuariosRolAsesor();
   }

    public void guardarRol(Roles roles){
        AuditoriaWeb auditoriaWeb =new AuditoriaWeb(tableName,roles.getIdRoles(),roles.getIdRoles()!=0? EnumAccionAuditoria.ACTUALIZAR.getNombreAccion():EnumAccionAuditoria.CREAR.getNombreAccion());
        rolesRepository.save(roles);
        auditoriaWeb.setIdElementoTabla(roles.getIdRoles());
        auditoriaWebService.save(auditoriaWeb);
    }

    public Boolean validarSiExisteRol(String nombreRol){
        return rolesRepository.existsByNombreRol(nombreRol);
    }

    public Roles obtenerPorNombreRol(String nombreRol){
        return rolesRepository.findByNombreRolIgnoreCase(nombreRol);
    }

    public Boolean validarSiExisteRolParaLab(){
        return rolesRepository.existsByCodigoProasecalIsTrue();
    }


    public Page<Roles> listPaginated(String nombreRol, String descripcion, Boolean estado, Pageable pageable){

        Page<Roles> rolesPage = rolesRepository.findByNombreRolContainingIgnoreCaseAndDescripcionContainingIgnoreCaseAndEstado(nombreRol, descripcion, estado, pageable);
        return rolesPage;
    }
}
