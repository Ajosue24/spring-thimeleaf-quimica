package com.proasecal.software.web.service.seguridad;

import com.proasecal.software.web.entity.seguridad.FiltrosSecciones;
import com.proasecal.software.web.entity.seguridad.Secciones;
import com.proasecal.software.web.repository.seguridad.FiltrosSeccionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class FiltrosSeccionesService {

    @Autowired
    FiltrosSeccionesRepository filtrosSeccionesRepository;


    public List<FiltrosSecciones> getListFiltros(Secciones secciones) {
        return (List<FiltrosSecciones>) filtrosSeccionesRepository.findAllByIdSecciones(secciones);
    }

}
