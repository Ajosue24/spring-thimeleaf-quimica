package com.proasecal.software.controlexterno.repository;

import com.proasecal.software.controlexterno.entity.AuditoriaResultadosDirector;
import com.proasecal.software.web.entity.administrar.Muestras;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface AuditoriaResultadosDirectorRepository extends CrudRepository<AuditoriaResultadosDirector,Long> {
    List<AuditoriaResultadosDirector> findByIdMuestrasOrderByIdAuditResDirectorDesc(Muestras muestras);
    
    @Query("SELECT ard FROM AuditoriaResultadosDirector ard "
    		+ "WHERE  ard.fechaModificacion  BETWEEN :desde AND :hasta")
     List<AuditoriaResultadosDirector> informeAuditoria(@Param("desde") Date desde, @Param("hasta") Date hasta);

}




