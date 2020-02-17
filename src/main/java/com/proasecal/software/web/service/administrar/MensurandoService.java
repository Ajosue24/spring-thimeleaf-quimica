package com.proasecal.software.web.service.administrar;
import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.repository.administrar.MensurandoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class MensurandoService {
    @Autowired
    MensurandoRepository mensurandoRepository;

    public List<Mensurandos> list(){
        return (List<Mensurandos>) mensurandoRepository.findAll();
    }

    public Mensurandos getMensurandos(Long id){
        return mensurandoRepository.findById(id).get();
    }
    
    public List<Mensurandos> obtenerMensurandosxPrograma(Programas Programa) {
        return mensurandoRepository.findMensurandosByidProgramaOrderByOrdenAsc(Programa);
    }

    public List<Mensurandos> listHabilitado(Long inscripcionMuestras){
        return (List<Mensurandos>) mensurandoRepository.findByIdPrograma_ProgramaIdAndEstadoTrueOrderByOrdenAsc(inscripcionMuestras);
    }
    
}
