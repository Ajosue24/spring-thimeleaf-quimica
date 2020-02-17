package com.proasecal.software.web.service.administrar;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.proasecal.software.web.entity.seguridad.AuditoriaWeb;
import com.proasecal.software.web.entity.seguridad.EnumAccionAuditoria;
import com.proasecal.software.web.service.seguridad.AuditoriaWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.proasecal.software.web.entity.administrar.Reactivos;
import com.proasecal.software.web.repository.administrar.ProgramasRepository;
import com.proasecal.software.web.repository.administrar.ReactivoRepository;

@Service
@Transactional
public class ReactivoService {

	@Autowired
	private ReactivoRepository reactivoRepository;
	@Autowired
	private ProgramasRepository ProgramasRepository;
	
	@PersistenceContext
	private EntityManager em;
	@Autowired
	AuditoriaWebService auditoriaWebService;
	private String tableName;
	@PostConstruct
	private void init(){
		Class<?> c = Reactivos.class;
		Table table = c.getAnnotation(Table.class);
		this.tableName = table.name();
	}
	public void save(Reactivos reactivos) throws Exception {

		AuditoriaWeb auditoriaWeb =new AuditoriaWeb(tableName,reactivos.getReactivoId(),reactivos.getReactivoId()!=null? EnumAccionAuditoria.ACTUALIZAR.getNombreAccion():EnumAccionAuditoria.CREAR.getNombreAccion());
		this.reactivoRepository.save(reactivos);
		auditoriaWeb.setIdElementoTabla(reactivos.getReactivoId());
		auditoriaWebService.save(auditoriaWeb);
	}

	public Optional<Reactivos> find(Long id) {
		Optional<Reactivos> reactivo = this.reactivoRepository.findById(id);
		return reactivo;
	}

	public Page<Reactivos> ListPaginated(String codProasecal,String grupo,String programas,String nombreReactivo,String estado,
			Pageable pageable) {
		Page<Reactivos> reactivosPage =reactivoRepository.filtro(codProasecal,grupo,programas,nombreReactivo,estado,pageable);
		return reactivosPage;
	}	

	public Boolean exist(Reactivos reactivo) {

		if (!this.validateEdit(reactivo)) {
			return false;
		}

		Optional<Reactivos> optionalReactivo = this.reactivoRepository
				.findByNombreReactivoAndIdPrograma(reactivo.getNombreReactivo(), reactivo.getIdPrograma());
		if (optionalReactivo.isPresent()) {
			return true;
		} else {
			return false;
		}

	}

	public Boolean validateEdit(Reactivos reactivo) {
		Optional<Long> optionalReactivo = Optional.ofNullable(reactivo.getReactivoId());
		if (optionalReactivo.isPresent()) {
			return false;
		}
		return true;
	}
	
	public Boolean exist2(Reactivos reactivos) {

		if(reactivos.getReactivoId()==null){
			Optional<Reactivos> optional = this.reactivoRepository.findByCodProasecalAndIdPrograma(reactivos.getCodProasecal(), reactivos.getIdPrograma());
			if (optional.isPresent()) {
				return true;
			} else {
				return false;
			}
		}else{
			Optional<Reactivos> optional = this.reactivoRepository.findByCodProasecalAndIdProgramaAndReactivoIdIsNot(reactivos.getCodProasecal(), reactivos.getIdPrograma(),reactivos.getReactivoId());
			if (optional.isPresent()) {
				return true;
			} else {
				return false;
			}
		}
	}

	public Boolean deleteById(Long id){
		try {
			reactivoRepository.deleteById(id);
			auditoriaWebService.save(new AuditoriaWeb(tableName,id, EnumAccionAuditoria.ELIMINAR.getNombreAccion()));
			return true;
		}catch (Exception e){
			return false;
		}
	}

	public List<Reactivos> listHabilitado(Long inscripcionMuestras){
		return reactivoRepository.findByIdPrograma_ProgramaIdAndEstadoTrue(inscripcionMuestras);
	}

}
