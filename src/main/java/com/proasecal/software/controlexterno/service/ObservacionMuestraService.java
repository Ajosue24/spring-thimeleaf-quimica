package com.proasecal.software.controlexterno.service;

import com.proasecal.software.controlexterno.entity.ObservacionMuestra;
import com.proasecal.software.controlexterno.entity.TipoObservacion;
import com.proasecal.software.controlexterno.repository.ObservacionMuestraRepository;
import com.proasecal.software.web.entity.administrar.Muestras;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ObservacionMuestraService {

    @Autowired
    ObservacionMuestraRepository observacionMuestraRepository;

    public void save(ObservacionMuestra observacionMuestra){
        observacionMuestraRepository.save(observacionMuestra);
    }

    public Optional<ObservacionMuestra> encontrarTipoMuestra(Muestras muestras, TipoObservacion tipoObservacion){
        return observacionMuestraRepository.findAllByMuestraIdAndTipoObservacionId(muestras, tipoObservacion);
    }
}