package com.proasecal.software.controlexterno.service;

import com.proasecal.software.controlexterno.entity.Informes;
import com.proasecal.software.controlexterno.repository.InformesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class InformesService {

    @Autowired
    InformesRepository informesRepository;

    public void save(Informes informes){
        informesRepository.save(informes);
    }

    public Informes informeXId(long informe) {
        return informesRepository.findById(informe).get();
    }

    public void saveAll(List<Informes> informesList){
        informesRepository.saveAll(informesList);
    }


}
