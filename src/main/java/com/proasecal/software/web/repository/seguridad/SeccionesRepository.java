package com.proasecal.software.web.repository.seguridad;

import com.proasecal.software.web.entity.seguridad.Secciones;
import org.springframework.data.repository.CrudRepository;

public interface SeccionesRepository extends CrudRepository<Secciones,Long> {

    Secciones findByNombreEquals(String nombreSeccion);
}
