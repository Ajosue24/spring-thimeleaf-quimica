package com.proasecal.software.web.repository.administrar;


import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.administrar.Programas;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MuestraRepository extends CrudRepository<Muestras, Long> {


    Optional<Muestras> findBynumeroMuestra(Integer numeroMuestra);
    Optional<Muestras> findBynumeroMuestraAndIdPrograma_ProgramaId(Integer numeroMuestra,Long idPrograma);
    Optional<Muestras> findByNumeroMuestraAndIdPrograma(Integer NumeroMuestra, Programas programas);
    Optional<Muestras> findByNumeroMuestraAndIdProgramaAndMuestraIdIsNot(Integer NumeroMuestra, Programas programas,Long muestra);

    @Query("SELECT u FROM Muestras u WHERE CONCAT(lower(u.idPrograma.nombrePrograma),'') LIKE CONCAT('%',lower(:programaFront),'%') AND CONCAT(lower(u.idTipoMuestra.nombre),'') LIKE CONCAT('%',lower(:tipoMuestraFront),'%') " 
           + "AND CAST(u.numeroMuestra as text) LIKE CONCAT('%',lower(:numeroFront),'%') "
            + "AND CONCAT(u.fechaInicial,'') LIKE CONCAT('%',:fechaFront,'%')")
    Page<Muestras> filtro(@Param("programaFront") String programaFront, @Param("tipoMuestraFront") String tipoMuestraFront, @Param("numeroFront") String numeroFront, @Param("fechaFront") String fechaFront, Pageable pageable);

    @Query("SELECT u FROM Muestras u WHERE CONCAT(lower(u.idPrograma.nombrePrograma),'') LIKE CONCAT('%',lower(:programaFront),'%') AND CONCAT(lower(u.idTipoMuestra.nombre),'') LIKE CONCAT('%',lower(:tipoMuestraFront),'%') " 
             + "AND CONCAT(u.fechaInicial,'') LIKE CONCAT('%',:fechaFront,'%')")
     Page<Muestras> filtro2(@Param("programaFront") String programaFront, @Param("tipoMuestraFront") String tipoMuestraFront, @Param("fechaFront") String fechaFront, Pageable pageable);

    
    List<Muestras> findByFechaInicialBetween(Date fechaInicial, Date fechaFinal);

    List<Muestras> findByFechaInicialBetweenAndFechaFinalAfterAndIdPrograma
            (Date fechaInicial, Date fechaFinal, Date fechaActual, Programas programas);

    @Query("select distinct m from Muestras m "
    		+ "inner join InscripcionMuestras im on m.muestraId=im.idMuestras "
            +" where CONCAT(im.inscripProgramaId.programaId.nombrePrograma,'')  LIKE CONCAT('%',:programa,'%') "
            + "and CONCAT(m.fechaInicial,'') LIKE CONCAT('%',:ano,'%') order by m.numeroMuestra asc")
    List<Muestras> obtMuestrasxAno(@Param("programa") String programa, @Param("ano") Integer ano);
}
