package com.proasecal.software.controlexterno.controller;

import com.proasecal.software.controlexterno.entity.DAO.ResultadosFueraDeFecha;
import com.proasecal.software.controlexterno.entity.DAO.MensurandosXSimular;
import com.proasecal.software.controlexterno.entity.Resultados;
import com.proasecal.software.controlexterno.entity.ResultadosDetallados;
import com.proasecal.software.controlexterno.service.EscenariosFijosService;
import com.proasecal.software.controlexterno.service.ResultadosDetalladosService;
import com.proasecal.software.controlexterno.service.ResultadosService;
import com.proasecal.software.web.controller.administrar.AdministrarClientes;
import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import com.proasecal.software.web.service.administrar.MensurandoService;
import com.proasecal.software.web.service.administrar.MuestraService;
import com.proasecal.software.web.service.seguridad.UsuariosLabSedesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "controlexterno")
public class GestionDeMuestrasController {

    private final Logger LOG = LoggerFactory.getLogger(GestionDeMuestrasController.class);

    @Autowired
    MuestraService muestraService;
    @Autowired
    MensurandoService mensurandoService;
    @Autowired
    ResultadosService resultadosService;
    @Autowired
    UsuariosLabSedesService usuariosLabSedesService;
    @Autowired
    ResultadosDetalladosService resultadosDetalladosService;
    @Autowired
    EscenariosFijosService escenariosFijosService;

    /* Inicio interfaz grafica inicial */

    @RequestMapping(value = "/gestionMuestras/{id}", method = RequestMethod.GET)
    public ModelAndView gestionMuestras(@PathVariable("id") String id) {
        Muestras muestras;
        ModelAndView modelAndView;

        try {
            muestras = muestraService.find(Long.valueOf(id)).get();
            modelAndView = new ModelAndView("controlexterno/gestionDeMuestras/gestionDeMuestras");

        } catch (Exception e) {
            return new ModelAndView("redirect:/controlexterno/listResultadosDirector");
        }
        Integer resultadosList = resultadosPorMuestra(muestras).intValue();
        List<Resultados> resultadosFueraDeFecha = resultadosService.resultadosFueraDeFecha(muestras);
        List<Mensurandos> mensurandosList = mensurandoService.obtenerMensurandosxPrograma(muestras.getIdPrograma());
        modelAndView.addObject("muestraInfo", muestras);
        modelAndView.addObject("pendientes", muestras.getInscripcionMuestrasList().size() - resultadosList);
        modelAndView.addObject("fueraDeFecha", resultadosFueraDeFecha.size());
        modelAndView.addObject("fueraDeFechaList", resultadosFueraDeFecha(resultadosFueraDeFecha));
        LOG.info("Inicio mensurando x simular");
        modelAndView.addObject("mensurandosXSimular", mensurandosXSimular(muestras, mensurandosList));
        LOG.info("FIN mensurando x simular");
        return modelAndView;
    }

    private Long resultadosPorMuestra(Muestras muestras) {
        try {
            return resultadosService.countByIdInscripcionMuestras_IdMuestras(muestras);
        } catch (Exception e) {
            return 0l;
        }
    }

    private List<ResultadosFueraDeFecha> resultadosFueraDeFecha(List<Resultados> resultados) {
        List<ResultadosFueraDeFecha> resultadosFueraDeFechas = new ArrayList<>();

        for (int i = 0; i < resultados.size(); i++) {

            UsuariosLabSedes usuariosLabSedes = usuariosLabSedesService.buscarXUsuario(resultados.get(i).getIdInscripcionMuestras().getIdUsuarios().getUsuarioId().getIdUsuario());

            ResultadosFueraDeFecha resultadosFueraDeFecha = new ResultadosFueraDeFecha(
                    usuariosLabSedes.getIdLaboratoriosSedes().getRazonSocial(),
                    usuariosLabSedes.getIdSedes().getNombreSede(),
                    usuariosLabSedes.getUsuarioId().getCodProasecal().toString(),
                    resultados.get(i).getFechaCreacion()
            );

            resultadosFueraDeFechas.add(resultadosFueraDeFecha);
        }
        return resultadosFueraDeFechas;
    }

    private List<MensurandosXSimular> mensurandosXSimular(Muestras muestras, List<Mensurandos> mensurandosList) {

        List<MensurandosXSimular> mensurandosXSimularList = new ArrayList<>();

        for (Mensurandos mensurando : mensurandosList) {
            Long resultadosDetallados = resultadosDetalladosService.countByResultadoFechaAndMuestrasAndMensurando(false, muestras, mensurando);
            Long resultadosDetalladosFuera = resultadosDetalladosService.countByResultadoFechaAndMuestrasAndMensurando(true, muestras, mensurando);
            Boolean simulado = escenariosFijosService.countByIdMuestrasAndIdMensurandosAndEscenariosFijosIdIsNull(muestras, mensurando).intValue() > 0 ? true : false;

            MensurandosXSimular mensurandosXSimular = new MensurandosXSimular(
                    simulado,
                    mensurando,
                    resultadosDetallados.intValue() + resultadosDetalladosFuera.intValue(),
                    resultadosDetallados.intValue(),
                    resultadosDetalladosFuera.intValue()
            );
            mensurandosXSimularList.add(mensurandosXSimular);
        }

        return mensurandosXSimularList;
    }

    /* Fin interfaz grafica inicial */


    /* Inicio actualización de fechas */

    @RequestMapping(value = "/gestionMuestras/fechas", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity fechas(@RequestParam("id") String id, @RequestParam("tipo") String tipo, @RequestParam("fecha") String fecha) {
        Muestras muestra;

        try {
            Date date = null;
            muestra = muestraService.find(Long.valueOf(id)).get();

            if (!fecha.isEmpty()) {
                date = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(fecha);
            }

            if (tipo.equals("0")) {
                muestra.setFechaLibResultado(date);
            } else {
                muestra.setFechaCierre(date);
                actualizarEstados(muestra, date);
            }

            muestraService.save(muestra);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    private void actualizarEstados(Muestras muestras, Date fecha) {
        List<Resultados> resultadosList = resultadosService.resultadosPorMuestra(muestras);

        for (Resultados resultado : resultadosList) {
            if (resultado.getFechaCreacion().after(fecha)) {
                resultado.setResultadoFecha(true);
            } else {
                resultado.setResultadoFecha(false);
            }
            resultadosService.save(resultado);
        }
    }

    /* Fin actualización de fechas */
}