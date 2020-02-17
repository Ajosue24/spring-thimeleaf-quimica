package com.proasecal.software.controlexterno.service;


import com.proasecal.software.controlexterno.entity.AuditoriaResultadosDirector;
import com.proasecal.software.controlexterno.repository.AuditoriaResultadosDirectorRepository;
import com.proasecal.software.web.entity.administrar.Muestras;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuditoriaResultadosDirectorService {
    @Autowired
    AuditoriaResultadosDirectorRepository auditoriaResultadosDirectorRepository;


    public void save(AuditoriaResultadosDirector auditoriaResultadosDirector){
        auditoriaResultadosDirectorRepository.save(auditoriaResultadosDirector);
    }

    public List<AuditoriaResultadosDirector> findByMuestras(Muestras muestras){
        return auditoriaResultadosDirectorRepository.findByIdMuestrasOrderByIdAuditResDirectorDesc(muestras);
    }
    
    public List<AuditoriaResultadosDirector> informeAuditoria(Date desde, Date hasta){
        return auditoriaResultadosDirectorRepository.informeAuditoria(desde,hasta);
    }
}
