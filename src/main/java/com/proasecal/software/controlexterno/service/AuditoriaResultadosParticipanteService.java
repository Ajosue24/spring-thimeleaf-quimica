package com.proasecal.software.controlexterno.service;

import com.proasecal.software.controlexterno.entity.AuditoriaResultadosParticipante;
import com.proasecal.software.controlexterno.repository.AuditoriaResultadosParticipanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuditoriaResultadosParticipanteService {

    @Autowired
    AuditoriaResultadosParticipanteRepository auditoriaResultadosParticipanteRepository;

    public void save(AuditoriaResultadosParticipante auditoriaResultadosParticipante){
        auditoriaResultadosParticipanteRepository.save(auditoriaResultadosParticipante);
    }

}
