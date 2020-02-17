package com.proasecal.software.web.repository.administrar;

import com.proasecal.software.web.entity.administrar.Programas;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProgramasRepository extends CrudRepository<Programas,Long> {


    String FIND_PROJECTS = "SELECT * FROM PROGRAMAS WHERE v_nombre_programa LIKE '%Programa1%'";

    @Query(value = FIND_PROJECTS, nativeQuery = true)
   List<Programas> selectFromProgramasWhereLike(@Param("frase") String frase, Pageable pageable);

    String consultarTablas = "SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name   = 'programas'";

    @Query(value = consultarTablas, nativeQuery = true)
    List<String> getColumnsTable();

    @Query("select u from Programas u where :variable LIKE CONCAT('%',:frase,'%')")
    List<Programas> busqueda(@Param("frase") String frase,@Param("variable") String variable);
    
    
    List<Programas> findByNombreProgramaContainingIgnoreCase(String nombrePrograma);

    List<Programas> findProgramasByTipoProgramaId_NombreTipoProgramaContainingIgnoreCase(String s,Sort sort);
    
    Programas findProgramasByNombreProgramaEqualsIgnoreCase(String nombrePrograma);




}
