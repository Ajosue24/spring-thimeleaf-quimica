package com.proasecal.software.controlexterno.service;

import com.proasecal.software.controlexterno.entity.EscenariosFijos;
import com.proasecal.software.controlexterno.entity.EscenariosResultados;
import com.proasecal.software.controlexterno.repository.EscenarioResultadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class EscenarioResultadoService {

    @Autowired
    EscenarioResultadoRepository escenarioResultadoRepository;

    public List<EscenariosResultados> escenariosResultadosXescenariosFijosYAberrante(EscenariosFijos escenariosFijos, boolean b){
        return escenarioResultadoRepository.findAllByIdEscenarioFijoAndAberrante(escenariosFijos, b);
    }
    
    
    public EscenariosResultados guardar(EscenariosResultados escenariosResultados) {
    	
    	this.escenarioResultadoRepository.save(escenariosResultados);
    	return escenariosResultados;
    	
    }
}
