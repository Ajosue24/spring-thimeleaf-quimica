package com.proasecal.software.web.repository.administrar;


import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.entity.administrar.TiposMuestras;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface TipoMuestraRepository extends CrudRepository<TiposMuestras, Long>{

    List<TiposMuestras> findByNombreContainingIgnoreCase(String nombre);
    
    List<TiposMuestras> findTiposMuestrasByidPrograma(Programas Programa);
}
