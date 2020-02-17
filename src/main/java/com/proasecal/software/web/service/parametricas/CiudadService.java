package com.proasecal.software.web.service.parametricas;

import com.proasecal.software.web.entity.parametricas.Ciudad;
import com.proasecal.software.web.entity.parametricas.Departamentos;
import com.proasecal.software.web.repository.parametricas.CiudadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CiudadService {

    @Autowired
    CiudadRepository ciudadRepository;

    public List<Ciudad> obtenerCiudadxDepartamento(Departamentos departamentos) {
        return ciudadRepository.findCiudadByIdDepartamentos(departamentos);
    }
}

