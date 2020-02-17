package com.proasecal.software.controlexterno.service;

import com.proasecal.software.controlexterno.entity.TipoObservacion;
import com.proasecal.software.controlexterno.repository.TipoObservacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class TipoObservacionService {

    @Autowired
    TipoObservacionRepository tipoObservacionRepository;

    public Optional<TipoObservacion> encontrarId (Long id){
        return tipoObservacionRepository.findById(id);
    }
}
