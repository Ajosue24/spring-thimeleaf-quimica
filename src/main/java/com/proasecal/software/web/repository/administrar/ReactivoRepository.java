package com.proasecal.software.web.repository.administrar;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.entity.administrar.Reactivos;

@Repository
public interface ReactivoRepository extends CrudRepository<Reactivos, Long>{
	
	Optional<Reactivos> findByNombreReactivoAndIdPrograma(String nombre, Programas programaId);

	Optional<Reactivos> findByCodProasecalAndIdPrograma(Integer CodProasecal, Programas programas);
	Optional<Reactivos> findByCodProasecalAndIdProgramaAndReactivoIdIsNot(Integer CodProasecal, Programas programas,Long reactivo);

	List<Reactivos> findAllByEstado(Boolean estado);

	@Query("SELECT u FROM Reactivos u WHERE CONCAT(u.codProasecal,'') LIKE CONCAT('%',:codPro,'%')" + "AND CONCAT(u.grupo,'') LIKE CONCAT('%',:grupo,'%')"+
			"AND CONCAT(u.idPrograma.nombrePrograma,'') LIKE CONCAT('%',:programas,'%')"+"AND CONCAT(lower(u.nombreReactivo),'') LIKE CONCAT('%',lower( :nombreReactivo),'%')"+"AND CONCAT(u.estado,'') LIKE CONCAT('%', :estado,'%')")
	Page<Reactivos> filtro(@Param("codPro") String codPro, @Param("grupo") String grupo, @Param("programas") String programas, @Param("nombreReactivo") String nombreReactivo,@Param("estado") String estado, Pageable pageable);

	List<Reactivos> findByIdPrograma_ProgramaIdAndEstadoTrue(Long inscripcionMuestras);
}
