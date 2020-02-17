package com.proasecal.software.controlexterno.service;

import java.util.List;
import java.util.Optional;

import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proasecal.software.controlexterno.entity.Resultados;
import com.proasecal.software.controlexterno.repository.ResultadosRepository;


@Service
@Transactional
public class ResultadosService {
	
	@Autowired
	ResultadosRepository resultadosRepository;

    public List<Resultados> obtListResultados(String muestraAudi){
        return resultadosRepository.obtListResultados(muestraAudi);
    }
	public Optional<Resultados> find(Long id){
		return this.resultadosRepository.findById(id);
	}
	
    public List<Resultados> resultadosPorMuestra(Muestras muestras){
        return resultadosRepository.findByIdInscripcionMuestras_IdMuestras(muestras);
    }

    public List<Resultados> findByUserAndInscripcionMuestras(Usuarios usuarios, InscripcionMuestras inscripcionMuestras){
        return resultadosRepository.findByIdUsuariosAndIdInscripcionMuestras(usuarios,inscripcionMuestras);
    }

    public void save(Resultados resultados){
        resultadosRepository.save(resultados);
    }

    public Optional<Resultados> findByIdInscripcionMuestras(InscripcionMuestras inscripcionMuestras){
        return resultadosRepository.findByIdInscripcionMuestras(inscripcionMuestras);
    }

    public List<Resultados>resultadosFueraDeFecha(Muestras muestras){
        return resultadosRepository.findByIdInscripcionMuestras_IdMuestrasAndResultadoFechaIsTrue(muestras);
    }

    public Boolean borrarXId(long id) {
        try {
        	Resultados r = new Resultados();
            r.setIdResultados(id);
            resultadosRepository.delete(r);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Resultados findLastResultByUser(Usuarios usuarios){
        return resultadosRepository.findFirstByIdInscripcionMuestras_IdUsuarios_UsuarioIdOrderByIdResultadosDesc(usuarios);
    }

    public List<Resultados> resultadosMuestraMensurandoMetodo(String muestra, String mensurando, String metodo){
        return resultadosRepository.resultadosMuestraMensurandoMetodo(muestra, mensurando, metodo);
    }

    public List<Resultados> findResultadosByInscripcionMuestras(InscripcionMuestras inscripcionMuestras){
        return resultadosRepository.findAllByIdInscripcionMuestras(inscripcionMuestras);
    }


    public Resultados findLastByInscripcionMuestras(InscripcionMuestras inscripcionMuestras){
        return resultadosRepository.findFirstByIdInscripcionMuestrasOrderByIdInscripcionMuestrasDesc(inscripcionMuestras);
    }

    public Long countByIdInscripcionMuestras_IdMuestras(Muestras muestras){
        return resultadosRepository.countAllByIdInscripcionMuestras_IdMuestras(muestras);
    }

}
