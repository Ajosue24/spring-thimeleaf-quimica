package com.proasecal.software.web.service.seguridad;

import com.proasecal.software.web.entity.seguridad.Secciones;
import com.proasecal.software.web.repository.seguridad.SeccionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SeccionesService {

    @Autowired
    SeccionesRepository seccionesRepository;


   public Secciones findByName(String nombreSeccion){
        return seccionesRepository.findByNombreEquals(nombreSeccion);
    }

    public List<Secciones> list(){return (List<Secciones>)seccionesRepository.findAll();}
}
