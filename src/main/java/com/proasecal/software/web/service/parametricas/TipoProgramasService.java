package com.proasecal.software.web.service.parametricas;

import com.proasecal.software.web.entity.parametricas.TipoProgramas;
import com.proasecal.software.web.repository.parametricas.TipoProgramasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class TipoProgramasService {
    @Autowired
    TipoProgramasRepository tipoProgramasRepository;

    public List<TipoProgramas> list() {
        return (List<TipoProgramas>) tipoProgramasRepository.findAll();
    }
}
