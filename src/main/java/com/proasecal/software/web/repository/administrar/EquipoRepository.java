package com.proasecal.software.web.repository.administrar;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.proasecal.software.web.entity.administrar.Equipos;
import com.proasecal.software.web.entity.administrar.Programas;

@Repository
public interface EquipoRepository extends CrudRepository<Equipos, Long> {

	List<Equipos> findAllByEstado(boolean estado);

	Optional<Equipos> findByCodProasecalAndIdPrograma(String CodProasecal, Programas programas);

	Optional<Equipos> findByCodProasecalAndIdProgramaAndEquipoIdIsNot(String CodProasecal, Programas programas, Long equipo);
	
	Page<Equipos> findByIdPrograma_NombreProgramaContainingIgnoreCaseAndNombreEquipoContainingIgnoreCaseAndGrupoContainingIgnoreCaseAndCodProasecalContainingIgnoreCaseAndEstado
	(String programa,String nombre,String grupo,String codProasecal,Boolean estado, Pageable pageable);

	List<Equipos> findByIdPrograma_ProgramaIdAndEstadoTrue(Long inscripcionMuestras);
}
