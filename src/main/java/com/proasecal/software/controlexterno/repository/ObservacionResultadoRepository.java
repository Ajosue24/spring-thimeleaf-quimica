package com.proasecal.software.controlexterno.repository;

import com.proasecal.software.controlexterno.entity.ObservacionResultado;
import com.proasecal.software.controlexterno.entity.Resultados;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ObservacionResultadoRepository extends CrudRepository <ObservacionResultado, Long> {

    Optional<ObservacionResultado> findByResultadosId (Resultados resultados);

}
