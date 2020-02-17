package com.proasecal.software.web.service.administrar;

import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.administrar.Metodos;
import com.proasecal.software.web.entity.seguridad.AuditoriaWeb;
import com.proasecal.software.web.entity.seguridad.EnumAccionAuditoria;
import com.proasecal.software.web.repository.administrar.MetodoRepository;
import com.proasecal.software.web.service.seguridad.AuditoriaWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

@Service
@Transactional(rollbackFor = Exception.class)
public class MetodoService {

    @Autowired
    private MetodoRepository metodoRepository;
	@PersistenceContext
	private EntityManager em;
	@Autowired
	AuditoriaWebService auditoriaWebService;
	private String tableName;
	@PostConstruct
	private void init(){
		Class<?> c = Metodos.class;
		Table table = c.getAnnotation(Table.class);
		this.tableName = table.name();
	}
    public void save(Metodos metodos) throws Exception{
		AuditoriaWeb auditoriaWeb =new AuditoriaWeb(tableName,metodos.getMetodoId(),metodos.getMetodoId()!=null? EnumAccionAuditoria.ACTUALIZAR.getNombreAccion():EnumAccionAuditoria.CREAR.getNombreAccion());
		this.metodoRepository.save(metodos);
		auditoriaWeb.setIdElementoTabla(metodos.getMetodoId());
		auditoriaWebService.save(auditoriaWeb);
    }

    
	public Optional<Metodos> find(Long id) {
		Optional<Metodos> metodo = this.metodoRepository.findById(id);
		return metodo;
	}
	
	public Boolean exist(Metodos metodos) {

		if (metodos.getMetodoId()==null){
			Optional<Metodos> optional = this.metodoRepository.findByCodProasecalAndIdMensurandos(metodos.getCodProasecal(), metodos.getIdMensurandos());
			if (optional.isPresent()) {
				return true;
			} else {
				return false;
			}
		}
		else{
			Optional<Metodos> optional = this.metodoRepository.findByCodProasecalAndIdMensurandosAndMetodoIdIsNot(metodos.getCodProasecal(), metodos.getIdMensurandos(),metodos.getMetodoId());
			if (optional.isPresent()) {
				return true;
			} else {
				return false;
			}
		}
	}

    public Page<Metodos> ListPaginated(String programaFront,String mensurandoFront,String nombreFront,String grupoFront,String codProsecalFront,Boolean estadoFront, Pageable pageable){
 	   Page<Metodos> lista;
    	
        if(grupoFront!=null&&(!grupoFront.isEmpty()&&!grupoFront.equalsIgnoreCase("_"))){
            lista = metodoRepository.findByIdMensurandos_IdPrograma_NombreProgramaContainingIgnoreCaseAndIdMensurandos_NombreMensurandoContainingIgnoreCaseAndNombreMetodoContainingIgnoreCaseAndGrupoContainingIgnoreCaseAndCodProasecalContainingIgnoreCaseAndEstado(programaFront,mensurandoFront,nombreFront,grupoFront,codProsecalFront,estadoFront,pageable);        		
            return lista;
        }     
        lista = metodoRepository.findByIdMensurandos_IdPrograma_NombreProgramaContainingIgnoreCaseAndIdMensurandos_NombreMensurandoContainingIgnoreCaseAndNombreMetodoContainingIgnoreCaseAndCodProasecalContainingIgnoreCaseAndEstado(programaFront,mensurandoFront,nombreFront,codProsecalFront,estadoFront,pageable);   
        return lista;        
        
    }

    public Boolean borrarXId(long id) {
        try{
            Metodos m=new Metodos();
                    m.setMetodoId(id);
            metodoRepository.delete(m);
			auditoriaWebService.save(new AuditoriaWeb(tableName,id,EnumAccionAuditoria.ELIMINAR.getNombreAccion()));
            return true;
        }catch (Exception e){
            return false;
        }

    }

    public List<Metodos> list(){
		return (List<Metodos>) metodoRepository.findAll();
	}

	public List<Metodos> buscarXMensurando(Mensurandos mensurando){
		return metodoRepository.findAllByIdMensurandos(mensurando);
	}
}
