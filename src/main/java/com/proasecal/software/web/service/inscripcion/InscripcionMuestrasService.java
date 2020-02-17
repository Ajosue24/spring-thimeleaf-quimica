package com.proasecal.software.web.service.inscripcion;

import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import com.proasecal.software.web.entity.inscripcion.InscripcionProgramas;
import com.proasecal.software.web.entity.seguridad.AuditoriaWeb;
import com.proasecal.software.web.entity.seguridad.EnumAccionAuditoria;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import com.proasecal.software.web.repository.inscripcion.InscripcionMuestrasRepository;
import com.proasecal.software.web.service.seguridad.AuditoriaWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InscripcionMuestrasService {

    @Autowired
    InscripcionMuestrasRepository inscripcionMuestrasRepository;
    @Autowired
    AuditoriaWebService auditoriaWebService;
    private String tableName;
    @PostConstruct
    private void init(){
        Class<?> c = InscripcionMuestras.class;
        Table table = c.getAnnotation(Table.class);
        this.tableName = table.name();
    }
    public void save(InscripcionMuestras inscripcionMuestras) {
        try {
            AuditoriaWeb auditoriaWeb =new AuditoriaWeb(tableName,inscripcionMuestras.getIdInscripcionMuestras(),inscripcionMuestras.getIdInscripcionMuestras()!=0? EnumAccionAuditoria.ACTUALIZAR.getNombreAccion():EnumAccionAuditoria.CREAR.getNombreAccion());
            inscripcionMuestrasRepository.save(inscripcionMuestras);
            auditoriaWeb.setIdElementoTabla(inscripcionMuestras.getIdInscripcionMuestras());
            auditoriaWebService.save(auditoriaWeb);
        } catch (Exception e) {
        }
    }

    public void delete(InscripcionMuestras inscripcionMuestras) {
        try {
            Long se =inscripcionMuestras.getIdInscripcionMuestras();
            inscripcionMuestrasRepository.delete(inscripcionMuestras);
            auditoriaWebService.save(new AuditoriaWeb(tableName,se, EnumAccionAuditoria.ELIMINAR.getNombreAccion()));
        }catch (Exception e){
        }
    }

    public Optional<InscripcionMuestras> findMuestraInscrita(Muestras idMuestra, InscripcionProgramas idPrograma, UsuariosLabSedes idUsuario) {
        return inscripcionMuestrasRepository.findByInscripProgramaIdAndIdMuestrasAndIdUsuarios(idPrograma, idMuestra, idUsuario);
    }

    public List<InscripcionMuestras> findByUsuarioAndFechaIncialDesc(UsuariosLabSedes usuariosLabSedes) {
        return inscripcionMuestrasRepository.findByIdUsuariosOrderByIdMuestras_FechaInicialAsc(usuariosLabSedes);
    }

    public Page<InscripcionMuestras> findByUsuarioAndFechaIncialDescConFiltro(UsuariosLabSedes usuariosLabSedes, String desde, String hasta, String numero, Pageable pageable) {
        return inscripcionMuestrasRepository.findByUsuarioAndFechaIncialDescConFiltro(usuariosLabSedes, desde, hasta, numero, pageable);
    }

    public InscripcionMuestras findByUsuarioAndMuestras(UsuariosLabSedes usuarios, Muestras muestras) {
        return inscripcionMuestrasRepository.findByIdUsuariosAndIdMuestras(usuarios, muestras);
    }


    public boolean inscrito(Muestras idMuestra, InscripcionProgramas idPrograma, UsuariosLabSedes idUsuario) {
        return inscripcionMuestrasRepository.existsByInscripProgramaIdAndIdMuestrasAndIdUsuarios(idPrograma, idMuestra, idUsuario);
    }

    public List<InscripcionMuestras> obtListUsuarios(String muestra, String laboratorio, String sede, String usuario, String estado) {
        if (estado.equalsIgnoreCase("Todos")) {
            return inscripcionMuestrasRepository.obtListUsuarios(muestra, laboratorio, sede, usuario);
        }
        if (estado.equalsIgnoreCase("Informados")) {
            return inscripcionMuestrasRepository.obtListUsuarios2(muestra, laboratorio, sede, usuario);
        }
        return inscripcionMuestrasRepository.obtListUsuarios3(muestra, laboratorio, sede, usuario);
    }

    public Integer obtListUsuariosInscritos(String muestra, String laboratorio, String sede, String usuario) {
        return inscripcionMuestrasRepository.obtListUsuariosInscritos(muestra, laboratorio, sede, usuario);
    }

    public Integer obtListUsuariosNoInscritos(String muestra, String laboratorio, String sede, String usuario) {
        return inscripcionMuestrasRepository.obtListUsuariosNoInscritos(muestra, laboratorio, sede, usuario);
    }

    public InscripcionMuestras findById(Long id) {
        return inscripcionMuestrasRepository.findById(id).get();
    }

    public Optional<InscripcionMuestras> find(Long id) {
    	return this.inscripcionMuestrasRepository.findById(id);
    }
    
    public Optional <InscripcionMuestras> findByUsuario(UsuariosLabSedes usuario,Long inscripcionMuestraId) {
        return inscripcionMuestrasRepository.findByIdUsuariosAndIdInscripcionMuestras(usuario,inscripcionMuestraId);
    }

    public List<InscripcionMuestras> revisionResultados(String muestra, String laboratorio, String sede, String usuario){
        return inscripcionMuestrasRepository.revisionResultados(muestra, laboratorio, sede, usuario);
    }

    public List<InscripcionMuestras> revisionResultadosResultado(String muestra, String laboratorio, String sede, String usuario, String resultado){
        return inscripcionMuestrasRepository.revisionResultadosResultado(muestra, laboratorio, sede, usuario, resultado);
    }

    public List<InscripcionMuestras> revisionResultadosProceso(String muestra, String laboratorio, String sede, String usuario, String proceso){
        return inscripcionMuestrasRepository.revisionResultadosProceso(muestra, laboratorio, sede, usuario, proceso);
    }

    public List<InscripcionMuestras> revisionResultadosFull(String muestra, String laboratorio, String sede, String usuario, String resultado, String proceso){
        return inscripcionMuestrasRepository.revisionResultadosFull(muestra, laboratorio, sede, usuario, resultado, proceso);
    }

    public List<InscripcionMuestras> revisionResultadosEnFecha(String muestra){
        return inscripcionMuestrasRepository.revisionResultadosEnFecha(muestra);
    }

    public Optional<InscripcionMuestras> find(long id) {
        Optional<InscripcionMuestras> insmuestra = this.inscripcionMuestrasRepository.findById(id);
        return insmuestra;
    }
}