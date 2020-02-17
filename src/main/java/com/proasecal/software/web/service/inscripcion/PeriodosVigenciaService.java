package com.proasecal.software.web.service.inscripcion;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

import com.proasecal.software.web.entity.DAO.PeriodosVigenciaModal;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import com.proasecal.software.web.entity.inscripcion.InscripcionProgramas;
import com.proasecal.software.web.entity.seguridad.AuditoriaWeb;
import com.proasecal.software.web.entity.seguridad.EnumAccionAuditoria;
import com.proasecal.software.web.service.seguridad.AuditoriaWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proasecal.software.web.entity.inscripcion.PeriodosVigencia;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import com.proasecal.software.web.repository.inscripcion.InscripcionMuestrasRepository;
import com.proasecal.software.web.repository.inscripcion.PeriodosVigenciaRepository;


@Service
@Transactional(rollbackFor = Exception.class)
public class PeriodosVigenciaService {
	
    @Autowired
    private PeriodosVigenciaRepository periodosVigenciaRepository;
    @Autowired
    InscripcionMuestrasRepository inscripcionMuestrasRepository;
    @PersistenceContext
	private EntityManager em;
    @Autowired
    AuditoriaWebService auditoriaWebService;
    private String tableName;
    @PostConstruct
    private void init(){
        Class<?> c = PeriodosVigencia.class;
        Table table = c.getAnnotation(Table.class);
        this.tableName = table.name();
    }

    public void save(PeriodosVigencia periodosVigencia) throws Exception{
        AuditoriaWeb auditoriaWeb =new AuditoriaWeb(tableName,periodosVigencia.getPeriodosvigenciaId(),periodosVigencia.getPeriodosvigenciaId()!=null? EnumAccionAuditoria.ACTUALIZAR.getNombreAccion():EnumAccionAuditoria.CREAR.getNombreAccion());
        this.periodosVigenciaRepository.save(periodosVigencia);
        auditoriaWeb.setIdElementoTabla(periodosVigencia.getPeriodosvigenciaId());
        auditoriaWebService.save(auditoriaWeb);
    }

	public Optional<PeriodosVigencia> find(Long id) {
		return this.periodosVigenciaRepository.findById(id);
	}   

   public Page<PeriodosVigencia> ListPaginated(String laboratorio,String sede,String usuario,String programa,String modalidad,String muestra,String enmora,String patrocinada,String clientePatrocinador,String fechaInicio,String fechaFin, Pageable pageable){
	   Page<PeriodosVigencia> lista;

       if (enmora.equalsIgnoreCase("false"))
           enmora="";
       if (patrocinada.equalsIgnoreCase("false"))
           patrocinada = "";

	   
      if(muestra!=null&&(!muestra.isEmpty()&&!muestra.equalsIgnoreCase("_"))&&clientePatrocinador!=null&&(!clientePatrocinador.isEmpty()&&!clientePatrocinador.equalsIgnoreCase("_"))){
           lista = periodosVigenciaRepository.filtroMuestraCliente(muestra,laboratorio, sede, usuario, programa, modalidad, enmora, patrocinada, clientePatrocinador, fechaInicio, fechaFin,pageable);
           return lista;
       }
      if(muestra!=null&&(!muestra.isEmpty()&&!muestra.equalsIgnoreCase("_"))){
          lista = periodosVigenciaRepository.filtroMuestra(muestra,laboratorio, sede, usuario, programa, modalidad, enmora, patrocinada, fechaInicio, fechaFin,pageable);
          return lista;
      }   
      if(clientePatrocinador!=null&&(!clientePatrocinador.isEmpty()&&!clientePatrocinador.equalsIgnoreCase("_"))){
          lista = periodosVigenciaRepository.filtroCliente(laboratorio, sede, usuario, programa, modalidad, enmora, patrocinada, clientePatrocinador, fechaInicio, fechaFin,pageable);
          return lista;
      }      
       lista  =periodosVigenciaRepository.filtro(laboratorio, sede, usuario, programa, modalidad, enmora, patrocinada, fechaInicio, fechaFin,pageable); 

        return lista;
    }
   
   public Boolean borrarXId(PeriodosVigencia periodosVigencia) {
       Long se =periodosVigencia.getPeriodosvigenciaId();
       try{
           InscripcionMuestras inscripcionMuestras =  inscripcionMuestrasRepository.findByInscripProgramaIdAndFechaCreacionBetween(periodosVigencia.getInscripProgramaId(),periodosVigencia.getFechaInicio(),periodosVigencia.getFechaFin());
           if ( inscripcionMuestras != null ){
               return false;
           }
           periodosVigenciaRepository.delete(periodosVigencia);
           auditoriaWebService.save(new AuditoriaWeb(tableName,se,EnumAccionAuditoria.ELIMINAR.getNombreAccion()));
           return true;
       }catch (Exception e){
           return false;
       }
   }
   
   public boolean validDate(PeriodosVigencia periodosVigencia) {
       if (periodosVigencia.getFechaInicio().compareTo(periodosVigencia.getFechaFin()) <= 0) {
           return false;
       } else {
           return true;
       }
   }	

    public PeriodosVigenciaModal findModal(Long id) {
        Optional <PeriodosVigencia> periodosVigencia = find(id);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        PeriodosVigenciaModal periodosVigenciaModal= new PeriodosVigenciaModal(
                periodosVigencia.get().getPeriodosvigenciaId(),
                periodosVigencia.get().getInscripProgramaId(),
                periodosVigencia.get().getPerVigenciaRegistrados(),
                periodosVigencia.get().getModalidad(),
                sdf.format(periodosVigencia.get().getFechaInicio()),
                sdf.format(periodosVigencia.get().getFechaFin())
        );
        return periodosVigenciaModal;
    }

    public List<PeriodosVigencia> periodosVigenciasInscritos(InscripcionProgramas inscripcionProgramas,UsuariosLabSedes usuariosLabSedes,Long Id){
        return periodosVigenciaRepository.findByInscripProgramaIdAndInscripProgramaId_IdUsuarioLabSedesAndPeriodosvigenciaIdIsNot(inscripcionProgramas,usuariosLabSedes,Id);
    }
    
    public List<PeriodosVigencia> periodosVigenciasInscritos(InscripcionProgramas inscripcionProgramas,UsuariosLabSedes usuariosLabSedes){
        return periodosVigenciaRepository.findByInscripProgramaIdAndInscripProgramaId_IdUsuarioLabSedes(inscripcionProgramas,usuariosLabSedes);
    }
        
    
    public List<PeriodosVigencia> periodosVigenciasInscritos2(InscripcionProgramas inscripcionProgramas){
        return periodosVigenciaRepository.findByInscripProgramaId(inscripcionProgramas);
    }
}
