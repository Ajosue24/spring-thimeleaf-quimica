package com.proasecal.software.controlexterno.repository;

import com.proasecal.software.controlexterno.entity.EscenariosFijos;
import com.proasecal.software.controlexterno.entity.EscenariosResultados;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EscenarioResultadoRepository extends CrudRepository<EscenariosResultados, Long> {

    List<EscenariosResultados> findAllByIdEscenarioFijoAndAberrante(EscenariosFijos escenariosFijos, boolean b);

}
