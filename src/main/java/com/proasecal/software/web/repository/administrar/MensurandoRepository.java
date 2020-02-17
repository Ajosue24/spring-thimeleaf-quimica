package com.proasecal.software.web.repository.administrar;
import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.administrar.Programas;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface MensurandoRepository extends CrudRepository<Mensurandos, Long> {
	  List<Mensurandos> findMensurandosByidProgramaOrderByOrdenAsc(Programas Programa);

	  
	List<Mensurandos> findByIdPrograma_ProgramaIdAndEstadoTrueOrderByOrdenAsc(Long inscripcionMuestras);
}
