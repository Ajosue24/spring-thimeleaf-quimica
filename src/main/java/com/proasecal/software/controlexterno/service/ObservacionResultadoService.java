package com.proasecal.software.controlexterno.service;

import com.proasecal.software.controlexterno.entity.ObservacionResultado;
import com.proasecal.software.controlexterno.entity.Resultados;
import com.proasecal.software.controlexterno.repository.ObservacionResultadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ObservacionResultadoService {

    @Autowired
    ObservacionResultadoRepository observacionResultadoRepository;

    public void guardarObservacion(ObservacionResultado observacionResultado){
        observacionResultadoRepository.save(observacionResultado);
    }

    public Optional<ObservacionResultado> getObservacionResultadoByResultado(Resultados resultados){
        return observacionResultadoRepository.findByResultadosId(resultados);
    }
}

