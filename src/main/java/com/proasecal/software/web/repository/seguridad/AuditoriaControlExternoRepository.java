package com.proasecal.software.web.repository.seguridad;

import com.proasecal.software.web.entity.seguridad.AuditoriaControlExterno;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface AuditoriaControlExternoRepository extends CrudRepository<AuditoriaControlExterno,Long> {

    @Query("SELECT ard FROM AuditoriaControlExterno ard "
            + "WHERE  ard.fecha  BETWEEN :desde AND :hasta order by idAuditoriaControlExterno asc")
    List<AuditoriaControlExterno> informeAuditoria(@Param("desde") Date desde, @Param("hasta") Date hasta);
}
