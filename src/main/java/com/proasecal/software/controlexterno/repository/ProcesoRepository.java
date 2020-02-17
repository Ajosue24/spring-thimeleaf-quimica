package com.proasecal.software.controlexterno.repository;

import com.proasecal.software.controlexterno.entity.Proceso;
import com.proasecal.software.web.entity.administrar.Muestras;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProcesoRepository extends CrudRepository<Proceso, Long> {

    Optional<Proceso> findByMuestraId(Muestras muestras);

    Optional<Proceso> findTopByMuestraIdAndTipoProcesoOrderByIdProcesoDesc (Muestras muestras, int proceso);

}
