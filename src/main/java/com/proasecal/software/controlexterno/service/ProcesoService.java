package com.proasecal.software.controlexterno.service;

import com.proasecal.software.controlexterno.entity.Proceso;
import com.proasecal.software.controlexterno.repository.ProcesoRepository;
import com.proasecal.software.web.entity.administrar.Muestras;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProcesoService {

    @Autowired
    ProcesoRepository procesoRepository;

    public void save (Proceso proceso){
        procesoRepository.save(proceso);
    }

    public Optional<Proceso> muestraVersionGenerada(Muestras muestras){
        return procesoRepository.findTopByMuestraIdAndTipoProcesoOrderByIdProcesoDesc(muestras, 2);
    }
}
