package com.proasecal.software.web.service.inscripcion;

import com.proasecal.software.web.entity.administrar.Laboratorios;
import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.entity.administrar.Sedes;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import com.proasecal.software.web.entity.inscripcion.InscripcionProgramas;
import com.proasecal.software.web.entity.seguridad.AuditoriaWeb;
import com.proasecal.software.web.entity.seguridad.EnumAccionAuditoria;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import com.proasecal.software.web.repository.administrar.LaboratorioRepository;
import com.proasecal.software.web.repository.inscripcion.InscripcionMuestrasRepository;
import com.proasecal.software.web.repository.inscripcion.InscripcionProgramasRepository;
import com.proasecal.software.web.service.seguridad.AuditoriaWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.Table;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class InscripcionProgramasService {
    @Autowired
    LaboratorioRepository laboratorioRepository;

    @Autowired
    InscripcionMuestrasRepository inscripcionMuestrasRepository;

    @Autowired
    InscripcionProgramasRepository inscripcionProgramasRepository;
    @Autowired
    AuditoriaWebService auditoriaWebService;
    private String tableName;
    @PostConstruct
    private void init(){
        Class<?> c = InscripcionProgramas.class;
        Table table = c.getAnnotation(Table.class);
        this.tableName = table.name();
    }
    public void save(InscripcionProgramas inscripcionProgramas) {
        try{
            UsuariosLabSedes usuariosLabSedes = inscripcionProgramas.getIdUsuarioLabSedes();
            Usuarios usuarios = usuariosLabSedes.getUsuarioId();
            if (usuarios.getCodProasecal()!= null){
                long codProsecal = usuarios.getCodProasecal();
                inscripcionProgramas.setCodProasecal(codProsecal);
            }
            AuditoriaWeb auditoriaWeb =new AuditoriaWeb(tableName,inscripcionProgramas.getInscripProgramaId(),inscripcionProgramas.getInscripProgramaId()!=null? EnumAccionAuditoria.ACTUALIZAR.getNombreAccion():EnumAccionAuditoria.CREAR.getNombreAccion());
            this.inscripcionProgramasRepository.save(inscripcionProgramas);
            auditoriaWeb.setIdElementoTabla(inscripcionProgramas.getInscripProgramaId());
            auditoriaWebService.save(auditoriaWeb);
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
    public boolean borrarXId(Long inscripProgramaId){
        try {
            InscripcionProgramas inscripPrograma = inscripcionProgramasRepository.findInscripcionProgramasByInscripProgramaIdEquals(inscripProgramaId);
            InscripcionMuestras inscripcionMuestras =  inscripcionMuestrasRepository.findByInscripProgramaId(inscripPrograma);
            if ( inscripcionMuestras != null ){
                return false;
            }
            inscripcionProgramasRepository.delete(inscripPrograma);
            auditoriaWebService.save(new AuditoriaWeb(tableName,inscripProgramaId, EnumAccionAuditoria.ELIMINAR.getNombreAccion()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public  List<InscripcionProgramas> obtenerPorUsuario(UsuariosLabSedes idUsuarioLabSedes){
        return  (List<InscripcionProgramas>) inscripcionProgramasRepository.findByIdUsuarioLabSedes(idUsuarioLabSedes);
    }

    public boolean valExist(InscripcionProgramas inscripcionProgramas){
        if(!this.valEdit(inscripcionProgramas)){
            return false;
        }
        Optional<InscripcionProgramas> optional = this.inscripcionProgramasRepository
                .findByIdSedesAndIdUsuarioLabSedesAndProgramaId(inscripcionProgramas.getIdSedes()
                        ,inscripcionProgramas.getIdUsuarioLabSedes(),inscripcionProgramas.getProgramaId());
        if(optional.isPresent()){
            return true;
        }else{
            return false;
        }
    }
    public boolean valEdit(InscripcionProgramas inscripcionProgramas){
        Optional<Long> optionalInscripProg = Optional.ofNullable(inscripcionProgramas.getInscripProgramaId());
        if(optionalInscripProg.isPresent()){
            return false;
        }
        return true;
    }
    public List<InscripcionProgramas> list() {
        return (List<InscripcionProgramas>) this.inscripcionProgramasRepository.findAll();
    }

    public Page<InscripcionProgramas> filtro(String muestra, String laboratorio, String sede, String usuario, String programaFront, Pageable pageable){
        Page<InscripcionProgramas> lista = this.inscripcionProgramasRepository.filtro( muestra, laboratorio, sede, usuario, programaFront, pageable);
        return lista ;
    }

    public Page<InscripcionProgramas> filtro1(String laboratorio, String sede, String usuario, String programaFront, Pageable pageable){
        Page<InscripcionProgramas> lista = this.inscripcionProgramasRepository.filtro1( laboratorio, sede, usuario, programaFront, pageable);
        return lista ;
    }


    public InscripcionProgramas optenerUsuarioXProgramasIns(UsuariosLabSedes usuariosLabSedes,Programas programas){
        return this.inscripcionProgramasRepository.findByIdUsuarioLabSedesAndProgramaId(usuariosLabSedes,programas);
        }

    public Optional<InscripcionProgramas> findById(Long id){
        return  inscripcionProgramasRepository.findById(id);
    }


    public Boolean validarSiUsuarioPoseePrograma(Long usuario){
        return inscripcionProgramasRepository.existsByIdUsuarioLabSedes_IdUsuarioLabSedes(usuario);
    }

    public List<InscripcionProgramas> listaXprogramasYUsuarios(Programas programas,UsuariosLabSedes usuariosLabSedes){
        return this.inscripcionProgramasRepository.findByProgramaIdAndIdUsuarioLabSedes(programas,usuariosLabSedes);
    }

}