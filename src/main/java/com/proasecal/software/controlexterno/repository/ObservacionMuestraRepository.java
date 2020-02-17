package com.proasecal.software.controlexterno.repository;

import com.proasecal.software.controlexterno.entity.ObservacionMuestra;
import com.proasecal.software.controlexterno.entity.TipoObservacion;
import com.proasecal.software.web.entity.administrar.Muestras;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ObservacionMuestraRepository extends CrudRepository<ObservacionMuestra,Long> {

    Optional<ObservacionMuestra> findAllByMuestraIdAndTipoObservacionId(Muestras muestras, TipoObservacion tipoObservacion);
}
