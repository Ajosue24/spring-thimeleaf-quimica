package com.proasecal.software.web.service.parametricas;


import com.proasecal.software.web.entity.parametricas.Pais;
import com.proasecal.software.web.repository.parametricas.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PaisService {

    @Autowired
    PaisRepository paisRepository;

    public List<Pais> obtenerPaises() {
        return (List<Pais>) paisRepository.findAll();
    }

    public List<Pais> filtrarPaisLike(String nombrePais) {
        return paisRepository.findByNombrePaisContainingIgnoreCase(nombrePais);
    }
}
