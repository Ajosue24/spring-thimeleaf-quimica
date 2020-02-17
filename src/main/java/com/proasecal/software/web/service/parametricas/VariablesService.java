package com.proasecal.software.web.service.parametricas;

import com.proasecal.software.web.entity.parametricas.Variables;
import com.proasecal.software.web.repository.parametricas.VariablesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class VariablesService {

    @Autowired
    VariablesRepository variablesRepository;

   public Variables obtenerValorVariable(String nombreVariable){
        return variablesRepository.findByNombreVariableEqualsIgnoreCase(nombreVariable);
    }

    public List<Variables> getAllVarByProcesosId(Long idProcesos){
       return variablesRepository.findAllByIdProceso_IdProceso(idProcesos);
    }
}
