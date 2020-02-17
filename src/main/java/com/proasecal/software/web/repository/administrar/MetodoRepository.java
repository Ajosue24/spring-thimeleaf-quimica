package com.proasecal.software.web.repository.administrar;
import com.proasecal.software.web.entity.administrar.Metodos;
import com.proasecal.software.web.entity.administrar.Mensurandos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface MetodoRepository extends CrudRepository<Metodos,Long> {

	Optional<Metodos> findByCodProasecalAndIdMensurandos(String CodProasecal, Mensurandos mensurandos);
	Optional<Metodos> findByCodProasecalAndIdMensurandosAndMetodoIdIsNot(String CodProasecal, Mensurandos mensurandos,Long metodo);
	
	Page<Metodos> findByIdMensurandos_IdPrograma_NombreProgramaContainingIgnoreCaseAndIdMensurandos_NombreMensurandoContainingIgnoreCaseAndNombreMetodoContainingIgnoreCaseAndGrupoContainingIgnoreCaseAndCodProasecalContainingIgnoreCaseAndEstado
	(String programa,String ensurando,String nombre,String grupo,String codProasecal,Boolean estado, Pageable pageable);
	
	Page<Metodos> findByIdMensurandos_IdPrograma_NombreProgramaContainingIgnoreCaseAndIdMensurandos_NombreMensurandoContainingIgnoreCaseAndNombreMetodoContainingIgnoreCaseAndCodProasecalContainingIgnoreCaseAndEstado
	(String programa,String ensurando,String nombre,String codProasecal,Boolean estado, Pageable pageable);

	List<Metodos> findAllByEstadoTrue();

	List<Metodos> findAllByIdMensurandos(Mensurandos mensurandos);
    
}
