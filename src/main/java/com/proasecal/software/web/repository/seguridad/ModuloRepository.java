package com.proasecal.software.web.repository.seguridad;

import com.proasecal.software.web.entity.seguridad.Modulos;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ModuloRepository extends CrudRepository<Modulos,Long> {

    Modulos findByNombreModulo(String nombreModulo);

    List<Modulos> findByIdSecciones_IdSecciones(Long idSecciones);
}
