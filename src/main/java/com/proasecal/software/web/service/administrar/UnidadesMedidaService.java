package com.proasecal.software.web.service.administrar;

import com.proasecal.software.web.entity.administrar.UnidadesMedidas;
import com.proasecal.software.web.repository.administrar.UnidadesMedidasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UnidadesMedidaService {

    @Autowired
    UnidadesMedidasRepository unidadesMedidasRepository;

    public UnidadesMedidas get(Long id){
        return unidadesMedidasRepository.findById(id).get();
    }
}
