package com.proasecal.software.web.repository.seguridad;

import com.proasecal.software.web.entity.seguridad.FiltrosSecciones;
import com.proasecal.software.web.entity.seguridad.Secciones;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FiltrosSeccionesRepository extends CrudRepository<FiltrosSecciones,Long> {

    List<FiltrosSecciones> findAllByIdSecciones(Secciones secciones);
}
