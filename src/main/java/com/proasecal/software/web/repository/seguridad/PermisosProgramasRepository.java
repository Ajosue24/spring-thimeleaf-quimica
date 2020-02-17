package com.proasecal.software.web.repository.seguridad;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.entity.seguridad.PermisoPrograma;
import com.proasecal.software.web.entity.seguridad.Usuarios;

public interface PermisosProgramasRepository  extends CrudRepository<PermisoPrograma,Long> {

	List<PermisoPrograma> findByUsuarioId(Usuarios usuarios);
	
	Optional <PermisoPrograma> findByIdProgramaAndUsuarioId(Programas programas,Usuarios usuario);
}
