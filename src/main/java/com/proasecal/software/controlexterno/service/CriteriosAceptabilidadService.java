package com.proasecal.software.controlexterno.service;

import com.proasecal.software.controlexterno.entity.CriteriosAceptabilidad;
import com.proasecal.software.controlexterno.repository.CriteriosAceptabilidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CriteriosAceptabilidadService {

    @Autowired
    CriteriosAceptabilidadRepository criteriosAceptabilidadRepository;

    public List<CriteriosAceptabilidad> getAll(){
        return (List<CriteriosAceptabilidad>)criteriosAceptabilidadRepository.findAll();
    }
}
