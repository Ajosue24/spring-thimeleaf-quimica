package com.proasecal.software.controlexterno.service;

import com.proasecal.software.controlexterno.entity.DAO.Aberrantes;
import com.proasecal.software.controlexterno.entity.Resultados;
import com.proasecal.software.controlexterno.entity.ResultadosDetallados;
import com.proasecal.software.controlexterno.repository.ResultadosDetalladosRepository;
import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.administrar.Metodos;
import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class ResultadosDetalladosService {

    @Autowired
    ResultadosDetalladosRepository resultadosDetalladosRepository;

    
    public Boolean eliminarDetalle(Resultados resultado) {
    	try {
            resultadosDetalladosRepository.deleteByIdResultados(resultado);
            return true;
		} catch (Exception e) {
			 return false;
		}
    }

    
    public void saveAll(List<ResultadosDetallados> resultadosDetalladosList) {
        resultadosDetalladosRepository.saveAll(resultadosDetalladosList);
    }

    public List<ResultadosDetallados> resultados(Boolean fecha, Muestras muestras, Mensurandos mensurandos) {
        return resultadosDetalladosRepository.findByIdResultados_ResultadoFechaAndIdResultados_IdInscripcionMuestras_IdMuestrasAndMensurandosId(fecha, muestras, mensurandos);
    }

    public Long countByResultadoFechaAndMuestrasAndMensurando(Boolean fecha, Muestras muestras, Mensurandos mensurandos){
        return resultadosDetalladosRepository.countAllByIdResultados_ResultadoFechaAndIdResultados_IdInscripcionMuestras_IdMuestrasAndMensurandosId(fecha,muestras,mensurandos);
    }


    public String resetAberrantes(Integer idMuestras,Integer idMensurandos,Integer idMetodos,Integer idEquipo,Integer idReactivo){
        try {
         return resultadosDetalladosRepository.resetAberrantes(idMuestras,idMensurandos,idMetodos,idEquipo,idReactivo);
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    public String getConsensoInicial(Integer idMuestras,Integer idMensurandos,Integer idMetodos,Integer idEquipo,Integer idReactivo){
        try {
            return resultadosDetalladosRepository.consensoInicial(idMuestras,idMensurandos,idMetodos,idEquipo,idReactivo);
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    public String getConsensoInicialInicial(Integer idMuestras,Integer idMensurandos,Integer idMetodos,Integer idEquipo,Integer idReactivo){
        try {
            return resultadosDetalladosRepository.consensoInicialInicial(idMuestras,idMensurandos,idMetodos,idEquipo,idReactivo);
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    public String getIteracionGrubbs(Integer idMuestras,Integer idMensurandos,Integer idMetodos,Integer idEquipo,Integer idReactivo,Double grubbs){
        try {
            return resultadosDetalladosRepository.iteracionGrubbs(idMuestras,idMensurandos,idMetodos,idEquipo,idReactivo,grubbs);
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    public String getAlgoritmoA(Integer idMuestras,Integer idMensurandos,Integer idMetodos,Integer idEquipo,Integer idReactivo){
        try {
            return resultadosDetalladosRepository.algoritmoA(idMuestras,idMensurandos,idMetodos,idEquipo,idReactivo);
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    public String getAlgoritmoDS(Integer idMuestras,Integer idMensurandos,Integer idMetodos,Integer idEquipo,Integer idReactivo){
        try {
            return resultadosDetalladosRepository.algoritmoDS(idMuestras,idMensurandos,idMetodos,idEquipo,idReactivo);
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    public List<ResultadosDetallados> obtenerListaPorResultadosyMensurando(List<Resultados> resultadosList,Mensurandos mensurandos){
    return resultadosDetalladosRepository.findByIdResultadosInAndMensurandosId(resultadosList,mensurandos);
    }

    public List<ResultadosDetallados> obtenerListaPorResultadosyMensurandoyMetodos(List<Resultados> resultadosList, Mensurandos mensurandos, Metodos metodos){
        return resultadosDetalladosRepository.findByIdResultadosInAndMensurandosIdAndMetodoIdAndIdResultados_ResultadoFecha(resultadosList,mensurandos,metodos,false);
    }

    public Optional<ResultadosDetallados> get(Long id){
        return resultadosDetalladosRepository.findById(id);
    }

    public void deleteIfExist(List<ResultadosDetallados> resultadosDetalladosList)throws Exception{
            resultadosDetalladosRepository.deleteAll(resultadosDetalladosList);
    }

    public List<Aberrantes> findAberrantes(Long id){
        return resultadosDetalladosRepository.findByIdResultadosDetallados(id);
    }

    public List<ResultadosDetallados> findLast12xUsuarioAndMensurandoAndFechaResBefore(UsuariosLabSedes usuariosLabSedes,Mensurandos mensurandos,Date fechaCreacionR){
        return resultadosDetalladosRepository.findTop12ByIdResultados_IdInscripcionMuestras_IdUsuariosAndMensurandosIdAndIdResultados_ResultadoFechaAndIdResultados_FechaCreacionBefore(usuariosLabSedes,mensurandos,false,fechaCreacionR);
    }

    public List<ResultadosDetallados> findLast12xUsuarioAndMensurando(UsuariosLabSedes usuariosLabSedes,Mensurandos mensurandos){
        return resultadosDetalladosRepository.findTop12ByIdResultados_IdInscripcionMuestras_IdUsuariosAndMensurandosIdAndIdResultados_ResultadoFecha(usuariosLabSedes,mensurandos,false);
    }

    public List<ResultadosDetallados> findLast12xUsuarioAndMensurandoAndIdResultadosLessThanEqualsOrderByFechaInicial(UsuariosLabSedes usuariosLabSedes,Mensurandos mensurandos, Resultados resultados){
        return resultadosDetalladosRepository.findTop12ByIdResultados_IdInscripcionMuestras_IdUsuariosAndMensurandosIdAndIdResultados_ResultadoFechaAndIdResultadosIsLessThanEqualOrderByIdResultados_IdInscripcionMuestras_IdMuestras_FechaInicialAsc(
                usuariosLabSedes,mensurandos,false,resultados);
    }

    public List<ResultadosDetallados> findByMuestrasAndMensurando(Muestras muestras, Mensurandos mensurandos){
        return resultadosDetalladosRepository.findByIdResultados_IdInscripcionMuestras_IdMuestrasAndMensurandosIdAndIdResultados_ResultadoFecha(muestras,mensurandos,false);
    }

    public List<ResultadosDetallados> findByMuestrasAndMensurandoAndMetodo(Muestras muestras, Mensurandos mensurandos, Metodos metodos){
        return resultadosDetalladosRepository.findByIdResultados_IdInscripcionMuestras_IdMuestrasAndMensurandosIdAndMetodoIdAndIdResultados_ResultadoFecha(muestras,mensurandos,metodos,false);
    }

    public Integer findByMuestrasAndMensurandoCount(Muestras muestras,Mensurandos mensurandos){
        return resultadosDetalladosRepository.findAllByIdResultados_IdInscripcionMuestras_IdMuestrasAndMensurandosIdAndIdResultados_ResultadoFecha(muestras,mensurandos,false);
    }
}

