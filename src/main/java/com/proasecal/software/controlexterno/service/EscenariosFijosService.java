package com.proasecal.software.controlexterno.service;

import com.proasecal.software.controlexterno.entity.EscenariosFijos;
import com.proasecal.software.controlexterno.entity.EscenariosResultados;
import com.proasecal.software.controlexterno.entity.DAO.ValoresConsenso;
import com.proasecal.software.controlexterno.repository.EscenarioResultadoRepository;
import com.proasecal.software.controlexterno.repository.EscenariosFijosRepository;
import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.administrar.Metodos;
import com.proasecal.software.web.entity.administrar.Muestras;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EscenariosFijosService {

    @Autowired
    EscenariosFijosRepository escenariosFijosRepository;
    @Autowired
    EscenarioResultadoRepository escenarioResultadoRepository;

    private final Logger LOG = LoggerFactory.getLogger(EscenariosFijosService.class);

    public Optional<EscenariosFijos> escenarioFijoXId(long id) {
        return escenariosFijosRepository.findById(id);
    }

    public List<EscenariosFijos> escenariosXMuestraYMensurando(Muestras muestra, Mensurandos mensurando) {
        return escenariosFijosRepository.findByIdMuestrasAndIdMensurandosAndEscenariosFijosIdIsNull(muestra, mensurando);
    }

    public List<EscenariosFijos> escenariosXMuestraMensurandoMetodo(Muestras muestra, Mensurandos mensurando, Metodos metodos) {
        return escenariosFijosRepository.findByIdMuestrasAndIdMensurandosAndMetodoIdAndEscenariosFijosIdIsNull(muestra, mensurando, metodos);
    }

    public void eliminarEscenarios(Muestras muestras, Mensurandos mensurandos, Metodos metodos) {
        escenariosFijosRepository.deleteAllByIdMuestrasAndIdMensurandosAndMetodoId(muestras, mensurandos, metodos);
    }

    public EscenariosFijos guardar(EscenariosFijos escenariosFijos) {
        this.escenariosFijosRepository.save(escenariosFijos);
        return escenariosFijos;
    }

    public EscenariosFijos fijarEscenario(EscenariosFijos escenariosFijos, List<ValoresConsenso> valoresConsenso, List<ValoresConsenso> valoresAtipicosList) {

        try {
            this.escenariosFijosRepository.save(escenariosFijos);
            LOG.info("Se guardo escenario fijo exitosamente");
            //Salvamos Escenarios Fijos
        } catch (Exception e) {
            LOG.error("error al guardar escenario fijo" + e);
        }


        try {
            List<EscenariosResultados> escenariosResultadosList = new ArrayList<>();
            valoresConsenso.stream().forEach(item -> {

                EscenariosResultados escenariosResultados = new EscenariosResultados();

                escenariosResultados.setAberrante(item.getIsAberrante());
                escenariosResultados.setDesviacionValorAsignado(item.getDesviacionValorAsignadoClia());
                escenariosResultados.setIndiceDesviacionEstandar(item.getIndiceDesviacionEstandarClia());
                escenariosResultados.setIndiceVarianza(item.getIndiceVarianzaClia());
                escenariosResultados.setIdEscenarioFijo(escenariosFijos);
                escenariosResultados.setIdResultadosDetallados(item.getResultadosDetallados());
                escenariosResultados.setAberrante(item.getIsAberrante());
                escenariosResultadosList.add(escenariosResultados);
            });
            this.escenarioResultadoRepository.saveAll(escenariosResultadosList);
            LOG.info("escenarios resultados CONSENSO Guardados Exitosamente");
        } catch (Exception e) {
            LOG.error("error al guardar ESCENARIOS_RESULTADOS CONSENSO" + e);

        }

        /**
         * No modifico solo agrego...
         */

        try {
            List<EscenariosResultados> escenariosResultadosAtipicosList = new ArrayList<>();
            valoresAtipicosList.stream().forEach(item -> {

                EscenariosResultados escenariosResultados = new EscenariosResultados();

                escenariosResultados.setAberrante(item.getIsAberrante());
                escenariosResultados.setDesviacionValorAsignado(item.getDesviacionValorAsignadoClia());
                escenariosResultados.setIndiceDesviacionEstandar(item.getIndiceDesviacionEstandarClia());
                escenariosResultados.setIndiceVarianza(item.getIndiceVarianzaClia());
                escenariosResultados.setIdEscenarioFijo(escenariosFijos);
                escenariosResultados.setIdResultadosDetallados(item.getResultadosDetallados());
                escenariosResultados.setAberrante(item.getIsAberrante());
                escenariosResultadosAtipicosList.add(escenariosResultados);
            });
            if (!escenariosResultadosAtipicosList.isEmpty()) {
                this.escenarioResultadoRepository.saveAll(escenariosResultadosAtipicosList);
                LOG.info("escenarios resultados ATIPICOS Guardados Exitosamente");
            }
        } catch (Exception e) {
            LOG.error("Error al guardar ESCENARIOS_RESULTADOS ATIPICOS", e);
        }

        return escenariosFijos;
    }


    public void simulacionesHijas(Long idEscenariosFijos, Long idMuestra, Long idMensurando, Long idMetodo) {
        this.escenariosFijosRepository.calculoSimulacionesHijas(
                Math.toIntExact(idEscenariosFijos),
                Math.toIntExact(idMuestra),
                Math.toIntExact(idMensurando),
                Math.toIntExact(idMetodo));
        /*this.escenariosFijosRepository.calculoSimulacionesHijasAberrantes(
                Math.toIntExact(idEscenariosFijos),
                Math.toIntExact(idMuestra),
                Math.toIntExact(idMensurando),
                Math.toIntExact(idMetodo));*/
    }



    public EscenariosFijos ultimoEscenarioFijo(Muestras muestra, Mensurandos mensurando, Metodos metodo) {
        return this.escenariosFijosRepository.findTop1ByIdMuestrasAndIdMensurandosAndMetodoIdAndEscenariosFijosIdIsNullOrderByFechaCreacionDesc(muestra, mensurando, metodo);
    }

    public void actualizarVersion(Integer numeroVersion,Integer resultado) {
        escenariosFijosRepository.actualizarVersionProcedure(numeroVersion, resultado);
    }
    public Long countByIdMuestrasAndIdMensurandosAndEscenariosFijosIdIsNull(Muestras muestra, Mensurandos mensurando){
       return escenariosFijosRepository.countAllByIdMuestrasAndIdMensurandosAndEscenariosFijosIdIsNull(muestra,mensurando);
    }
}