package com.proasecal.software.web.service.administrar;

import com.proasecal.software.web.entity.administrar.Laboratorios;
import com.proasecal.software.web.entity.administrar.Sedes;
import com.proasecal.software.web.entity.seguridad.AuditoriaWeb;
import com.proasecal.software.web.entity.seguridad.EnumAccionAuditoria;
import com.proasecal.software.web.repository.administrar.SedesRepository;
import com.proasecal.software.web.service.seguridad.AuditoriaWebService;
import com.proasecal.software.web.service.seguridad.UsuariosLabSedesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.Table;
import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class SedesService {

    @Autowired
    SedesRepository sedesRepository;

    @Autowired
    UsuariosLabSedesService usuariosLabSedesService;
    @Autowired
    AuditoriaWebService auditoriaWebService;
    private String tableName;
    @PostConstruct
    private void init(){
        Class<?> c = Sedes.class;
        Table table = c.getAnnotation(Table.class);
        this.tableName = table.name();
    }

    public Page<Sedes> filtro(String usuario,String pais, String nombreSede, String laboratorioId,String laboratorioRz, String fechaCreacion, String impResultados, Pageable pageable){
        Page<Sedes> lista;
        if(usuario!=null&&(!usuario.isEmpty()&&!usuario.equalsIgnoreCase("_"))){
            lista = sedesRepository.filtroUsuario(usuario,pais,nombreSede,laboratorioId,laboratorioRz,fechaCreacion,impResultados,pageable);//
            return lista;
        }
        lista = sedesRepository.filtro(pais,nombreSede,laboratorioId,laboratorioRz,fechaCreacion,impResultados,pageable);

        return lista;
    }

    public Sedes get(Long id){
        return sedesRepository.findById(id).get();
    }

    
    public List<Sedes> list() {
        return (List<Sedes>) this.sedesRepository.findAll();
    }
    
    
    public Boolean save(Sedes sedes){
        try {
            AuditoriaWeb auditoriaWeb =new AuditoriaWeb(tableName,sedes.getIdSedes(),sedes.getIdSedes()!=0? EnumAccionAuditoria.ACTUALIZAR.getNombreAccion():EnumAccionAuditoria.CREAR.getNombreAccion());
            this.sedesRepository.save(sedes);
            auditoriaWeb.setIdElementoTabla(sedes.getIdSedes());
            auditoriaWebService.save(auditoriaWeb);
            return true;
        }catch (Exception e){
            return false;
        }

    }
    public List<Sedes> listSedesxIdlab(Laboratorios laboratorios) {
        return (List<Sedes>) this.sedesRepository.findByIdLaboratoriosSedes(laboratorios);
    }

    public Boolean borrarXId(long id) {
        try{
            sedesRepository.deleteById(id);
            auditoriaWebService.save(new AuditoriaWeb(tableName,id,EnumAccionAuditoria.ELIMINAR.getNombreAccion()));
            return true;
        } catch (RuntimeException e) {
            return false;
        }
        catch (Exception e){
            return false;
        }

    }

    public List<Sedes> obtSedexMuestra(String muestra){
        return sedesRepository.obtSedexMuestra(muestra);
    }
}
