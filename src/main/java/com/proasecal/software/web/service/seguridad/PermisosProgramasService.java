package com.proasecal.software.web.service.seguridad;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.entity.seguridad.PermisoPrograma;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import com.proasecal.software.web.repository.seguridad.PermisosProgramasRepository;

@Service
@Transactional(rollbackFor = Exception.class)
public class PermisosProgramasService {
	@Autowired
	PermisosProgramasRepository permisosProgramasRepository;

	public List<PermisoPrograma>obtListPermisosProgramas(Usuarios usuario) {
		return (List<PermisoPrograma>)permisosProgramasRepository.findByUsuarioId(usuario);
	}
	
	public Optional<PermisoPrograma> buscarPermisoPrograma(Programas programas,Usuarios usuario){
		return this.permisosProgramasRepository.findByIdProgramaAndUsuarioId(programas,usuario);
	}
	
	public void save(PermisoPrograma permisoPrograma) {
		this.permisosProgramasRepository.save(permisoPrograma);
	}
	
}
