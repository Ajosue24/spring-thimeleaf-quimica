package com.proasecal.software.controlexterno.controller.simulacion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.proasecal.software.controlexterno.entity.*;
import com.proasecal.software.controlexterno.entity.DAO.*;
import com.proasecal.software.controlexterno.entity.EscenariosResultados;
import com.proasecal.software.controlexterno.service.*;
import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.administrar.Metodos;
import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.seguridad.AuditoriaControlExterno;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import com.proasecal.software.web.service.administrar.MensurandoService;
import com.proasecal.software.web.service.administrar.MetodoService;
import com.proasecal.software.web.service.administrar.MuestraService;
import com.proasecal.software.web.service.administrar.UnidadesMedidaService;
import com.proasecal.software.web.service.parametricas.VariablesService;
import com.proasecal.software.web.service.seguridad.AuditoriaControlExternoService;
import com.proasecal.software.web.service.seguridad.UsuarioService;
import com.proasecal.software.web.service.seguridad.UsuariosLabSedesService;
import com.proasecal.software.web.util.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "controlexterno")
public class ModuloSimulacionController {
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
    VariablesService variablesService;
    @Autowired
    CriteriosAceptabilidadService criteriosAceptabilidadService;
    @Autowired
    EscenariosFijosService escenariosFijosService;
    @Autowired
    MetodoService metodoService;
    @Autowired
    EscenarioResultadoService escenarioResultadoService;
    @Autowired
    UnidadesMedidaService unidadesMedidaService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    AuditoriaControlExternoService auditoriaControlExternoService;

    /* Inicio interfaz grafica inicial */

    @RequestMapping(value = "/simulacion")
    public ModelAndView simulacionGet(@RequestParam("muestra") String idMuestra,
                                      @RequestParam("mensurando") String idMensurando, FormSimulacion formSimulacionPost) {

        Muestras muestras;
        Mensurandos mensurandos;
        ModelAndView modelAndView;

        try {
            muestras = muestraService.find(Long.valueOf(idMuestra)).get();
            try {
                mensurandos = mensurandoService.getMensurandos(Long.valueOf(idMensurando));
            } catch (Exception e) {
                return new ModelAndView("redirect:/controlexterno/listResultadosDirector");
            }
            modelAndView = new ModelAndView("controlexterno/simulacion/moduloSimulacion");


        } catch (Exception e) {
            return new ModelAndView("redirect:/controlexterno/listResultadosDirector");
        }

        /*
          Validacion para Consenso Inicial
         */
        //List<Resultados> resultadosList = resultadosService.resultadosPorMuestra(muestras);
        //List<ResultadosDetallados> resultadosDetalladosList = resultadosDetalladosService.findByMuestrasAndMensurando(muestras,mensurandos);
       // List<ResultadosDetallados> resultadosDetalladosList = resultadosDetalladosService.obtenerListaPorResultadosyMensurando(resultadosList, mensurandos);
        List<ConsensoInicial> consensoInicialList = new ArrayList<>();
        //Reiniciamos aberrantes en caso de que ya se haya realizado el calculo para dicha muestra
        resultadosDetalladosService.resetAberrantes(Math.toIntExact(muestras.getMuestraId()), Math.toIntExact(mensurandos.getMensurandosId()), 0, 0, 0);
        //metodo para el consenso inicial
        //Consenso inicial
        if (formSimulacionPost.getConsensoInicialFiltro() != null) {
            consensoInicialList.add(formSimulacionPost.getConsensoInicialFiltro());
        } else {
            consensoInicialList.add(Util.parseStringToConsensoInicial(resultadosDetalladosService.getConsensoInicialInicial(Math.toIntExact(muestras.getMuestraId()), Math.toIntExact(mensurandos.getMensurandosId()), 0, 0, 0)));
        }
        Integer resultadosDetalladosList = consensoInicialList.get(0).getTotalElementos()+consensoInicialList.get(0).getTotalAberrantes();
        //Grubbs
        if (formSimulacionPost.getConsensoInicialList() != null && !formSimulacionPost.getConsensoInicialList().isEmpty()) {
            consensoInicialList.add(formSimulacionPost.getConsensoInicialList().get(0));
        } else {
            if (resultadosDetalladosList != null && resultadosDetalladosList >= 3) {
                consensoInicialList.add(Util.parseStringToConsensoInicialGrubbs(resultadosDetalladosService.getIteracionGrubbs(Math.toIntExact(muestras.getMuestraId()), Math.toIntExact(mensurandos.getMensurandosId()), 0, 0, 0, Util.grubbs(resultadosDetalladosList, variablesService.obtenerValorVariable("N_SIGNIFICANCIA").getValor()))));
            } else {
                consensoInicialList.add(new ConsensoInicial());
            }
        }
        //ALGA
        if (formSimulacionPost.getConsensoInicialList() != null && !formSimulacionPost.getConsensoInicialList().isEmpty()) {
            consensoInicialList.add(formSimulacionPost.getConsensoInicialList().get(1));
        } else {
            if (resultadosDetalladosList != null && resultadosDetalladosList >= 5) {
                consensoInicialList.add(Util.parseStringToConsensoInicial(resultadosDetalladosService.getAlgoritmoA(Math.toIntExact(muestras.getMuestraId()), Math.toIntExact(mensurandos.getMensurandosId()), 0, 0, 0)));
            } else {
                consensoInicialList.add(new ConsensoInicial());
            }
        }
        //3DS
        if (formSimulacionPost.getConsensoInicialList() != null && !formSimulacionPost.getConsensoInicialList().isEmpty()) {
            consensoInicialList.add(formSimulacionPost.getConsensoInicialList().get(2));
        } else {
            if (resultadosDetalladosList != null && resultadosDetalladosList >= 2) {
                consensoInicialList.add(Util.parseStringToConsensoInicial(resultadosDetalladosService.getAlgoritmoDS(Math.toIntExact(muestras.getMuestraId()), Math.toIntExact(mensurandos.getMensurandosId()), 0, 0, 0)));
            } else {
                consensoInicialList.add(new ConsensoInicial());
            }
        }


        //Fin validacion consenso inicial

        /*Objeto que almacena multiples obj para ser mostrados en front End*/
        FormSimulacion formSimulacion;
        if (!(formSimulacionPost.getEscenariosFijos() != null)) {
            formSimulacion = new FormSimulacion();
            formSimulacion.setEscenariosFijos(new EscenariosFijos());
            formSimulacion.getEscenariosFijos().setIdMensurandos(mensurandos);
            formSimulacion.getEscenariosFijos().setIdMuestras(muestras);
        } else {
            formSimulacion = formSimulacionPost;
        }

        /**
         * Variables Semaforizacion
         */

        String red = "";
        String yellow = "";
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            red = ow.writeValueAsString(variablesService.obtenerValorVariable("SEMAFORIZACION_ROJA"));
            yellow = ow.writeValueAsString(variablesService.obtenerValorVariable("SEMAFORIZACION_AMARILLA"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //Ordenamiento de valores en concenso por nombre de usuario (codigo Proasecal)
        try {
            formSimulacion.setValoresConsensosList(formSimulacion.getValoresConsensosList().stream().sorted((o1, o2) -> {
                return Integer.parseInt(o2.getNombreUsuario()) < Integer.parseInt(o1.getNombreUsuario()) ? 1 : -1;
            }).collect(Collectors.toList()));
        } catch (Exception e) {
        }

        //Ordenamiento de valores atipicos por nombre de usuario (codigo Proasecal)
        try {
            formSimulacion.setValoresAtipicosList(formSimulacion.getValoresAtipicosList().stream().sorted((o1, o2) -> {
                return Integer.parseInt(o2.getNombreUsuario()) < Integer.parseInt(o1.getNombreUsuario()) ? 1 : -1;
            }).collect(Collectors.toList()));
        } catch (Exception e) {
        }

        modelAndView.addObject("red", red);
        modelAndView.addObject("yellow", yellow);
        modelAndView.addObject("formSimulacion", formSimulacion);
        modelAndView.addObject("consensoInicialList", consensoInicialList);
        modelAndView.addObject("muestraInfo", muestras);
        modelAndView.addObject("mensurando", mensurandos);
        modelAndView.addObject("metodosList", mensurandos.getMetodosList());       // modelAndView.addObject("escenarios", mensurandoService.obtenerMensurandosxPrograma(muestras.getIdPrograma()));
        return modelAndView;
    }
    /* Fin interfaz grafica inicial */


    @RequestMapping(value = "/descargarResultados")
    public ModelAndView descargarResultados(@RequestParam("muestra") String idMuestra,
                                            @RequestParam("mensurando") String idMensurando) {

        Muestras muestras = muestraService.find(Long.valueOf(idMuestra)).get();
        Mensurandos mensurandos = mensurandoService.getMensurandos(Long.valueOf(idMensurando));

        List<Resultados> resultadosList = resultadosService.resultadosPorMuestra(muestras);
        List<ResultadosDetallados> resultadosDetalladosList = resultadosDetalladosService.obtenerListaPorResultadosyMensurando(resultadosList, mensurandos);
        //Envio de lista
        return new ModelAndView(new ResultadosReportados(muestras, mensurandos), "resultadosDetallados", resultadosDetalladosList);

    }


    @RequestMapping(value = "/simulacion", method = RequestMethod.POST)
    public ModelAndView resultadosParticipantePost(@Valid @ModelAttribute("formSimulacion") FormSimulacion
                                                           formSimulacion,
                                                   BindingResult bindingResult,
                                                   RedirectAttributes redirectAttributes,
                                                   ModelAndView model, @RequestParam(value = "tipoSubmit", required = false) String tipoSubmit) {


        /** Consulta del ultimo escenario fijo */
        EscenariosFijos escenarioFijoAnterior = this.escenariosFijosService.ultimoEscenarioFijo(
                formSimulacion.getEscenariosFijos().getIdMuestras(),
                formSimulacion.getEscenariosFijos().getIdMensurandos(),
                formSimulacion.getEscenariosFijos().getMetodoId());
        /**  Fin Consulta del ultimo escenario fijo */

        /** Se valida si se esta recibiendo un valor en el campo VA */

        try {
            if (!(formSimulacion.getEscenariosFijos().getValorAsignado() != null) && escenarioFijoAnterior != null) {
                formSimulacion.getEscenariosFijos().setValorAsignado(escenarioFijoAnterior.getValorAsignado());
            }
        } catch (Exception e) {
            FormSimulacion formSimulacionCatch = new FormSimulacion();
            formSimulacionCatch.setEscenariosFijos(formSimulacion.getEscenariosFijos());
            formSimulacionCatch.getEscenariosFijos().setMetodoId(new Metodos());
            formSimulacionCatch.getEscenariosFijos().setValorAsignado(null);
            return simulacionGet(
                    formSimulacion.getEscenariosFijos().getIdMuestras().getMuestraId().toString(),
                    formSimulacion.getEscenariosFijos().getIdMensurandos().getMensurandosId().toString(),
                    formSimulacionCatch);
        }


        List<ResultadosDetallados> resultadosDetalladosList = null;
        if (formSimulacion.getEscenariosFijos().getMetodoId() != null) {
           resultadosDetalladosList= resultadosDetalladosService.findByMuestrasAndMensurandoAndMetodo(formSimulacion.getEscenariosFijos().getIdMuestras(),formSimulacion.getEscenariosFijos().getIdMensurandos(),
                    formSimulacion.getEscenariosFijos().getMetodoId());
        } else {
            resultadosDetalladosList = resultadosDetalladosService.findByMuestrasAndMensurando(formSimulacion.getEscenariosFijos().getIdMuestras(),
                    formSimulacion.getEscenariosFijos().getIdMensurandos());
        }

        FormSimulacion formSimulacionTemp = new FormSimulacion();


        /**
         * Se valida si es el primer calculo o > a 1 ves
         */
        if (formSimulacion.getValoresConsensosList() != null || formSimulacion.getValoresAtipicosList() != null) {
            //Guardamos en base de datos de una vez los aberrantes seleccionados por el usuario
            try {
                excluirAberrantes(formSimulacion.getValoresConsensosList(), formSimulacion.getValoresAtipicosList(), tipoSubmit);
                formSimulacionTemp = calculosSimulador(formSimulacion, resultadosDetalladosList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            formSimulacionTemp = calculosSimulador(formSimulacion, resultadosDetalladosList);


            formSimulacionTemp.setEscenariosFijoAnterior(escenarioFijoAnterior);


            return simulacionGet(
                    formSimulacion.getEscenariosFijos().getIdMuestras().getMuestraId().toString(),
                    formSimulacion.getEscenariosFijos().getIdMensurandos().getMensurandosId().toString(),
                    formSimulacionTemp);
        }


        /** Guardar escenario fijo */

        Optional<String> optionalTipoSubmit = Optional.ofNullable(tipoSubmit);

        if (optionalTipoSubmit.isPresent() && optionalTipoSubmit.get().equals("fijarEscenario")) {


            EscenariosFijos escenariosFijosSave = this.escenariosFijosService.fijarEscenario(
                    formSimulacion.getEscenariosFijos(),
                    formSimulacion.getValoresConsensosList(), formSimulacion.getValoresAtipicosList());


            this.escenariosFijosService.simulacionesHijas(
                    escenariosFijosSave.getIdEscenarioFijo(),
                    escenariosFijosSave.getIdMuestras().getMuestraId(),
                    escenariosFijosSave.getIdMensurandos().getMensurandosId(),
                    escenariosFijosSave.getMetodoId().getMetodoId());


            AuditoriaControlExterno ace = new AuditoriaControlExterno();
            Optional<Muestras> muestras2 = this.muestraService.find(formSimulacion.getEscenariosFijos().getIdMuestras().getMuestraId());
            ace.setPrograma(muestras2.get().getIdPrograma().getAbreviatura());
            ace.setIdMuestras(muestras2.get());
            ace.setAccion("Asignar valor");
            Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
            Usuarios usr = (Usuarios) auth.get().getPrincipal();
            Usuarios user = usuarioService.findByUserName(usr.getNombreUsuario());
            ace.setIdUsuario(user);
            ace.setValorAsignado(String.valueOf(formSimulacion.getEscenariosFijos().getValorAsignado()));
            ace.setIdMensurandos(formSimulacion.getEscenariosFijos().getIdMensurandos());
            ace.setFiltro(formSimulacion.getEscenariosFijos().getMetodoId());
            auditoriaControlExternoService.save(ace);
        }
        /** Fin guardar escenario fijo */


        formSimulacionTemp.setEscenariosFijoAnterior(escenarioFijoAnterior);


        return simulacionGet(
                formSimulacion.getEscenariosFijos().getIdMuestras().getMuestraId().toString(),
                formSimulacion.getEscenariosFijos().getIdMensurandos().getMensurandosId().toString(),
                formSimulacionTemp);
    }


    private ValoresConsenso simulacion(ResultadosDetallados resultadosDetallados, EscenariosFijos escenariosFijos,
                                       ConsensoInicial consensoInicial, List<ConsensoInicial> valoresAsignadosCandidatos, Double
                                               valorAceptable, String unidadMedida) throws Exception {
        ValoresConsenso valoresConsensoInd = new ValoresConsenso();
        valoresConsensoInd.setValorAceptable(valorAceptable);
        valoresConsensoInd.setUnidadMedida(unidadMedida);

        //valoresAsignadosCandidatos ;0-> Grubbs;1->Alg A;2->DS
        /*Laboratorio*/
        /*Sede*/
        /*Usuario*/
        /*Valor reportado*/
        valoresConsensoInd.setNombreUsuario(resultadosDetallados.getIdResultados().getIdInscripcionMuestras().getIdUsuarios().getUsuarioId().getNombreUsuario());
        valoresConsensoInd.setNombreSede(resultadosDetallados.getIdResultados().getIdInscripcionMuestras().getIdUsuarios().getIdSedes().getNombreSede());
        valoresConsensoInd.setNombreLaboratorio(resultadosDetallados.getIdResultados().getIdInscripcionMuestras().getIdUsuarios().getIdLaboratoriosSedes().getRazonSocial());
        /**
         * Seteamos los aberrantes
         *   Consultamos los mismos en tiempo real por cuestion de persistencia
         */
        for (Aberrantes aberrantes : resultadosDetalladosService.findAberrantes(resultadosDetallados.getIdResultadosDetallados())) {
            resultadosDetallados.setAberranteA(aberrantes.getAberranteA());
            resultadosDetallados.setAberranteDs(aberrantes.getAberranteDs());
            resultadosDetallados.setAberranteGrubbs(aberrantes.getAberranteGrubbs());
        }
        valoresConsensoInd.setResultadosDetallados(resultadosDetallados);
        //valoresConsenso.setResultados(resultadosDetallados.getIdResultados());

        /*Clia*/
        if (valoresConsensoInd.getUnidadMedida().equalsIgnoreCase("%")) {
            try {
                valoresConsensoInd.setDesviacionValorAsignadoClia(((resultadosDetallados.getValorReportado() - escenariosFijos.getValorAsignado()) / escenariosFijos.getValorAsignado()) * 100);
                valoresConsensoInd.setIndiceVarianzaClia((valoresConsensoInd.getDesviacionValorAsignadoClia() / valorAceptable) * 200);
            } catch (NullPointerException e) {
                valoresConsensoInd.setDesviacionValorAsignadoClia(null);
                valoresConsensoInd.setIndiceVarianzaClia(null);
            }
        } else {
            try {
                valoresConsensoInd.setDesviacionValorAsignadoClia(resultadosDetallados.getValorReportado() - escenariosFijos.getValorAsignado());
                valoresConsensoInd.setIndiceVarianzaClia((valoresConsensoInd.getDesviacionValorAsignadoClia() / valoresConsensoInd.getValorAceptable()) * 200);
            } catch (NullPointerException e) {
                valoresConsensoInd.setDesviacionValorAsignadoClia(null);
                valoresConsensoInd.setIndiceVarianzaClia(null);
            }
        }
        if (escenariosFijos.getValorAsignado() != null) {
            valoresConsensoInd.setIndiceDesviacionEstandarClia(((resultadosDetallados.getValorReportado() - consensoInicial.getMedia()) / consensoInicial.getDesviacionEstandar()));
        } else {
            valoresConsensoInd.setIndiceDesviacionEstandarClia(null);
        }

        /*GRB*/
        if (valoresConsensoInd.getUnidadMedida().equalsIgnoreCase("%")) {
            try {
                valoresConsensoInd.setDesviacionValorAsignadoGrb(((resultadosDetallados.getValorReportado() - valoresAsignadosCandidatos.get(0).getMedia()) / valoresAsignadosCandidatos.get(0).getMedia()) * 100);
                valoresConsensoInd.setIndiceVarianzaGrb((valoresConsensoInd.getDesviacionValorAsignadoGrb() / valorAceptable) * 200);
            } catch (NullPointerException e) {
                valoresConsensoInd.setDesviacionValorAsignadoGrb(null);
                valoresConsensoInd.setIndiceVarianzaGrb(null);
            }
        } else {
            try {
                valoresConsensoInd.setDesviacionValorAsignadoGrb(resultadosDetallados.getValorReportado() - valoresAsignadosCandidatos.get(0).getMedia());
                valoresConsensoInd.setIndiceVarianzaGrb((valoresConsensoInd.getDesviacionValorAsignadoGrb() / valorAceptable) * 200);
            } catch (NullPointerException e) {
                valoresConsensoInd.setDesviacionValorAsignadoGrb(null);
                valoresConsensoInd.setIndiceVarianzaGrb(null);
            }

        }

        /*AlgA*/
        if (valoresConsensoInd.getUnidadMedida().equalsIgnoreCase("%")) {
            try {
                valoresConsensoInd.setDesviacionValorAsignadoAlgA(((resultadosDetallados.getValorReportado() - valoresAsignadosCandidatos.get(1).getMedia()) / valoresAsignadosCandidatos.get(1).getMedia()) * 100);
                valoresConsensoInd.setIndiceVarianzaAlgA((valoresConsensoInd.getDesviacionValorAsignadoAlgA() / valorAceptable) * 200);
            } catch (NullPointerException e) {
                valoresConsensoInd.setDesviacionValorAsignadoAlgA(null);
                valoresConsensoInd.setIndiceVarianzaAlgA(null);
            }
        } else {
            try {
                valoresConsensoInd.setDesviacionValorAsignadoAlgA(resultadosDetallados.getValorReportado() - valoresAsignadosCandidatos.get(1).getMedia());
                valoresConsensoInd.setIndiceVarianzaAlgA((valoresConsensoInd.getDesviacionValorAsignadoAlgA() / valorAceptable) * 200);
            } catch (NullPointerException e) {
                valoresConsensoInd.setDesviacionValorAsignadoAlgA(null);
                valoresConsensoInd.setIndiceVarianzaAlgA(null);
            }

        }

        /*SD*/
        if (valoresConsensoInd.getUnidadMedida().equalsIgnoreCase("%")) {
            try {
                valoresConsensoInd.setDesviacionValorAsignadoSd(((resultadosDetallados.getValorReportado() - valoresAsignadosCandidatos.get(2).getMedia()) / valoresAsignadosCandidatos.get(2).getMedia()) * 100);
                valoresConsensoInd.setIndiceVarianzaSd((valoresConsensoInd.getDesviacionValorAsignadoSd() / valorAceptable) * 200);
            } catch (NullPointerException e) {
                valoresConsensoInd.setDesviacionValorAsignadoSd(null);
                valoresConsensoInd.setIndiceVarianzaSd(null);
            }
        } else {
            try {
                valoresConsensoInd.setDesviacionValorAsignadoSd(resultadosDetallados.getValorReportado() - valoresAsignadosCandidatos.get(2).getMedia());
                valoresConsensoInd.setIndiceVarianzaSd((valoresConsensoInd.getDesviacionValorAsignadoSd() / valorAceptable) * 200);
            } catch (NullPointerException e) {
                valoresConsensoInd.setDesviacionValorAsignadoSd(null);
                valoresConsensoInd.setIndiceVarianzaSd(null);
            }

        }
        /**Validamos por si valor es falso*/
        valoresConsensoInd.setIsAberrante(resultadosDetallados.getExcluido() != null ? resultadosDetallados.getExcluido() : false);


        return valoresConsensoInd;
    }

    // Pestañas

    // Historico
    @RequestMapping(value = "/historico", method = RequestMethod.GET)
    public String historico(@RequestParam("muestra") String muestras,
                            @RequestParam("mensurando") String mensurandos,
                            Model model) {

        Muestras muestra = muestraService.find(Long.valueOf(muestras)).get();
        Mensurandos mensurando = mensurandoService.getMensurandos(Long.valueOf(mensurandos));

        List<EscenariosFijos> escenariosFijos = escenariosFijosService.escenariosXMuestraYMensurando(muestra, mensurando);

        if (escenariosFijos.size() != 0) {
            model.addAttribute("historico", escenariosFijos);
        } else {
            model.addAttribute("noHistorico", "No hay escenarios fijos para este mensurando");
        }

        return "controlexterno/simulacion/historico :: historicoFrag";
    }


    // Consulta general de escenarios fijos
    @RequestMapping(value = "/consulta", method = RequestMethod.GET)
    public String consultaGeneralEscenario(@RequestParam("muestra") String muestras, Model model) {

        Muestras muestra = muestraService.find(Long.valueOf(muestras)).get();

        List<Mensurandos> mensurandosList = mensurandoService.obtenerMensurandosxPrograma(muestra.getIdPrograma());
        List<ConsultaEscenarios> consultaEscenariosList = new ArrayList<>();

        for (Mensurandos mensurando : mensurandosList) {
            EscenariosFijos escenarioFijo;
            ConsultaEscenarios consultaEscenarios;
            List<EscenariosFijos> escenariosFijosList = new ArrayList<>();
            List<Metodos> metodosList = metodoService.buscarXMensurando(mensurando);

            for (Metodos method : metodosList) {
                try {
                    List<EscenariosFijos> escenarios = escenariosFijosService.escenariosXMuestraMensurandoMetodo(muestra, mensurando, method);

                    if (escenarios.size() > 1) {
                        escenarioFijo = maxId(escenarios);
                    } else {
                        escenarioFijo = escenarios.get(0);
                    }

                    escenariosFijosList.add(escenarioFijo);

                } catch (Exception e) {
                }
            }

            if (escenariosFijosList.size() > 0) {
                consultaEscenarios = new ConsultaEscenarios(
                        mensurando,
                        escenariosFijosList
                );
                consultaEscenariosList.add(consultaEscenarios);
            }
        }

        if (consultaEscenariosList.size() != 0) {
            model.addAttribute("escenarios", consultaEscenariosList);
        } else {
            model.addAttribute("noEscenario", "No existen escenarios fijos");
        }

        return "controlexterno/simulacion/consultaGeneral :: consultaFrag";
    }


    private EscenariosFijos maxId(List<EscenariosFijos> escenariosFijosList) {
        EscenariosFijos escenariosFijoMaxId = escenariosFijosList.get(0);

        for (EscenariosFijos escenariosFijo : escenariosFijosList) {
            if (escenariosFijoMaxId.getIdEscenarioFijo() < escenariosFijo.getIdEscenarioFijo()) {
                escenariosFijoMaxId = escenariosFijo;
            }
        }
        return escenariosFijoMaxId;
    }

/*    private List<EscenariosMetodo> escenariosMetodo (List<EscenariosFijos> escenariosFijosList){
        List<EscenariosMetodo> escenariosMetodoList= new ArrayList<>();

        for (EscenariosFijos escenariosFijos: escenariosFijosList) {
            escenariosMetodoList.add(new EscenariosMetodo(escenariosFijos, esVersion(escenariosFijos)));
        }

        return escenariosMetodoList;
    }*/


    private boolean esVersion(EscenariosFijos escenariosFijos) {
        List<EscenariosResultados> resultados = new ArrayList<>();
        resultados.addAll(escenariosFijos.getResultadosDetalladosEscenariosList());
        List<Informes> informes = resultados.get(0).getIdResultadosDetallados().getIdResultados().getInformesList();

        for (Informes informe : informes) {
            if(informe.isEsVersion()){
                return true;
            }
        }
        return false;
    }


    // Escenario fijo
    @RequestMapping(value = "/escenarioFijo", method = RequestMethod.GET)
    public String escenarioFijo(@RequestParam("muestra") String muestra,
                                @RequestParam("mensurando") String mensurando,
                                @RequestParam("metodo") String metodo,
                                Model model) {

        Muestras muestras = muestraService.find(Long.valueOf(muestra)).get();
        Mensurandos mensurandos = mensurandoService.getMensurandos(Long.valueOf(mensurando));
        Metodos metodos = metodoService.find(Long.valueOf((metodo))).get();

        /** Consulta del ultimo escenario fijo */
        EscenariosFijos escenarioFijoAnterior = this.escenariosFijosService.ultimoEscenarioFijo(
                muestras,
                mensurandos,
                metodos);
        /**  Fin Consulta del ultimo escenario fijo */

        if (escenarioFijoAnterior != null) {

            /*Criterios de aceptabilidad*/
            List<CriteriosAceptabilidad> criteriosAceptabilidadList = criteriosAceptabilidadService.getAll();
            CriteriosAceptabilidad criteriosAceptabilidad = criteriosAceptabilidadList.stream().filter(x -> x.getIdMensurandos().getMensurandosId() == mensurandos.getMensurandosId()).findFirst().orElse(new CriteriosAceptabilidad());
            //DA
            /*Inicio validacion dato con respecto a tabla en Criterio de aceptabilidad*/
            Double valorAceptable;
            String unidadMedida;
            if (criteriosAceptabilidad.getValorAnotacion() != null) {
                if (escenarioFijoAnterior.getValorAsignado() < criteriosAceptabilidad.getValorAnotacion()) {
                    //Se mantiene que cuando esta condicion ocurra siempre este tendra un porcentaje(%)
                    valorAceptable = criteriosAceptabilidad.getDesviacionAceptable();
                    unidadMedida = criteriosAceptabilidad.getUnidadesMedidasId().getUnidad();
                } else {
                    valorAceptable = criteriosAceptabilidad.getValorAlternativo();
                    unidadMedida = unidadesMedidaService.get(4l).getUnidad();
                }
            } else {
                valorAceptable = criteriosAceptabilidad.getDesviacionAceptable();
                unidadMedida = criteriosAceptabilidad.getUnidadesMedidasId().getUnidad();
            }

            model.addAttribute("escenarioFijo", escenarioFijoAnterior);
            model.addAttribute("consenso", generarValoresEscenarioSimulacion(
                    escenarioResultadoService.escenariosResultadosXescenariosFijosYAberrante(escenarioFijoAnterior, false), valorAceptable, unidadMedida));
            model.addAttribute("atipico", generarValoresEscenarioSimulacion(
                    escenarioResultadoService.escenariosResultadosXescenariosFijosYAberrante(escenarioFijoAnterior, true), valorAceptable, unidadMedida));
        }
        return "controlexterno/simulacion/escenarioFijo:: escenarioFijoFrag";
    }

    List<ValoresEscenarioSimulacion> generarValoresEscenarioSimulacion
            (List<EscenariosResultados> escenariosResultadosList, Double valorAceptable, String unidadMedida) {
        List<ValoresEscenarioSimulacion> valoresEscenarioSimulacionList = new ArrayList<>();


        for (EscenariosResultados escenariosResultados : escenariosResultadosList) {

            ValoresEscenarioSimulacion valoresEscenarioSimulacion = new ValoresEscenarioSimulacion(
                    escenariosResultados.getIdResultadosDetallados().getIdResultados().getIdInscripcionMuestras().getIdUsuarios().getIdLaboratoriosSedes().getRazonSocial(),
                    escenariosResultados.getIdResultadosDetallados().getIdResultados().getIdInscripcionMuestras().getIdUsuarios().getIdSedes().getNombreSede(),
                    escenariosResultados.getIdResultadosDetallados().getIdResultados().getIdInscripcionMuestras().getIdUsuarios().getUsuarioId().getNombreUsuario(),
                    escenariosResultados.getIdResultadosDetallados().getValorReportado(),
                    valorAceptable + " "
                            + unidadMedida,
                    escenariosResultados.getIdEscenarioFijo().getLimiteInferior(),
                    escenariosResultados.getIdEscenarioFijo().getLimiteSuperior(),
                    escenariosResultados.getDesviacionValorAsignado(),
                    escenariosResultados.getIndiceVarianza(),
                    escenariosResultados.getIndiceDesviacionEstandar()
            );
            valoresEscenarioSimulacionList.add(valoresEscenarioSimulacion);
        }

        return valoresEscenarioSimulacionList;
    }

    //Eliminar Escenarios fijos
    @RequestMapping(value = "/eliminarEscenarios/{escenario}")
    public ResponseEntity eliminarEscenarios(@PathVariable long escenario) {

        try {
            EscenariosFijos escenarioFijo = escenariosFijosService.escenarioFijoXId(escenario).get();

            AuditoriaControlExterno ace = new AuditoriaControlExterno();
            Optional<Muestras> muestras2 = this.muestraService.find(escenarioFijo.getIdMuestras().getMuestraId());
            ace.setPrograma(muestras2.get().getIdPrograma().getAbreviatura());
            ace.setIdMuestras(muestras2.get());
            ace.setAccion("Eliminar valor asignado");
            Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
            Usuarios usr = (Usuarios) auth.get().getPrincipal();
            Usuarios user = usuarioService.findByUserName(usr.getNombreUsuario());
            ace.setIdUsuario(user);
            ace.setValorAsignado(String.valueOf(escenarioFijo.getValorAsignado()));
            ace.setIdMensurandos(escenarioFijo.getIdMensurandos());
            ace.setFiltro(escenarioFijo.getMetodoId());

            escenariosFijosService.eliminarEscenarios(escenarioFijo.getIdMuestras(), escenarioFijo.getIdMensurandos(), escenarioFijo.getMetodoId());

            auditoriaControlExternoService.save(ace);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    private void excluirAberrantes
            (List<ValoresConsenso> valoresConsensosList, List<ValoresConsenso> valoresAtipicosList, String tipoSubmit) throws
            Exception {
        List<ResultadosDetallados> resultadosDetalladosList = new ArrayList<>();

        if (tipoSubmit != null) {
            if (tipoSubmit.equalsIgnoreCase("excluir")) {
                if (valoresConsensosList != null && !valoresConsensosList.isEmpty()) {
                    for (ValoresConsenso valoresConsenso : valoresConsensosList) {
                        if (valoresConsenso.getIsAberrante()) {
                            valoresConsenso.setResultadosDetallados(resultadosDetalladosService.get(valoresConsenso.getResultadosDetallados().getIdResultadosDetallados()).get());
                            valoresConsenso.getResultadosDetallados().setExcluido(true);
                            resultadosDetalladosList.add(valoresConsenso.getResultadosDetallados());
                        }
                    }
                }
            } else if (tipoSubmit.equalsIgnoreCase("incluir")) {
                if (valoresAtipicosList != null && !valoresAtipicosList.isEmpty()) {
                    for (ValoresConsenso valoresConsenso : valoresAtipicosList) {
                        valoresConsenso.setResultadosDetallados(resultadosDetalladosService.get(valoresConsenso.getResultadosDetallados().getIdResultadosDetallados()).get());
                        if (valoresConsenso.getIsAberrante()) {
                            valoresConsenso.getResultadosDetallados().setExcluido(false);
                        } else {
                            valoresConsenso.getResultadosDetallados().setExcluido(true);
                        }

                        resultadosDetalladosList.add(valoresConsenso.getResultadosDetallados());
                    }
                }
            }
        }
        resultadosDetalladosService.saveAll(resultadosDetalladosList);
    }


    private FormSimulacion calculosSimulador(FormSimulacion
                                                     formSimulacion, List<ResultadosDetallados> resultadosDetalladosList) {
        /**Atributos que almacenan lo que sera retornado**/
        List<ValoresConsenso> valoresConsensosList = new ArrayList<>();
        List<ValoresConsenso> valoresAtipicosList = new ArrayList<>();

        /*Iniciamos valores para nuevo calculoSimulador*/
        resultadosDetalladosService.resetAberrantes(Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMuestras().getMuestraId()),
                Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMensurandos().getMensurandosId()), 0, 0, 0);
        /*Llenamos objeto con recursos disponible*/
        //ConsensoInicial consensoInicialFiltro = Util.parseStringToConsensoInicial(resultadosDetalladosService.getConsensoInicial(Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMuestras().getMuestraId()), Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMensurandos().getMensurandosId()), Math.toIntExact(formSimulacion.getEscenariosFijos().getMetodoId().getMetodoId()), 0, 0));
        ConsensoInicial consensoInicialFiltro = null;
        try {
            if (formSimulacion.getEscenariosFijos().getMetodoId().getMetodoId() != null) {
                //ss
            }
            consensoInicialFiltro = Util.parseStringToConsensoInicial(resultadosDetalladosService.getConsensoInicial(Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMuestras().getMuestraId()), Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMensurandos().getMensurandosId()), Math.toIntExact(formSimulacion.getEscenariosFijos().getMetodoId().getMetodoId()), 0, 0));
        } catch (Exception e) {
            consensoInicialFiltro = Util.parseStringToConsensoInicial(resultadosDetalladosService.getConsensoInicial(Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMuestras().getMuestraId()), Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMensurandos().getMensurandosId()), 0, 0, 0));
        }

        ConsensoInicial consensoInicialGeneral = Util.parseStringToConsensoInicial(resultadosDetalladosService.getConsensoInicial(Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMuestras().getMuestraId()), Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMensurandos().getMensurandosId()), 0, 0, 0));
        ConsensoInicial algoritmoAG = Util.parseStringToConsensoInicial(resultadosDetalladosService.getAlgoritmoA(Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMuestras().getMuestraId()), Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMensurandos().getMensurandosId()), 0, 0, 0));
        formSimulacion.getEscenariosFijos().setMediaFiltro(consensoInicialFiltro.getMedia());
        formSimulacion.getEscenariosFijos().setDesviacionEstandar(consensoInicialFiltro.getDesviacionEstandar());
        /*Limites se obtienen luego*/
        formSimulacion.getEscenariosFijos().setTotalValores(consensoInicialFiltro.getTotalElementos());
        formSimulacion.getEscenariosFijos().setCantidadAberrantes(consensoInicialFiltro.getTotalAberrantes());
    /*  formSimulacion.getEscenariosFijos().setMediaGeneral(consensoInicialGeneral.getMedia());
        formSimulacion.getEscenariosFijos().setDesviacionGeneral(consensoInicialGeneral.getDesviacionEstandar());*/
    //Correccion nueva mantis
        formSimulacion.getEscenariosFijos().setMediaGeneral(algoritmoAG.getMedia());
        formSimulacion.getEscenariosFijos().setDesviacionGeneral(algoritmoAG.getDesviacionEstandar());

        formSimulacion.getEscenariosFijos().setTotalGeneral(Optional.of(consensoInicialGeneral.getTotalElementos()).orElse(0)+Optional.of(consensoInicialGeneral.getTotalAberrantes()).orElse(0));
        Double desvGenEntreRaiztotal = formSimulacion.getEscenariosFijos().getDesviacionGeneral() / Math.sqrt(formSimulacion.getEscenariosFijos().getTotalGeneral());
        formSimulacion.getEscenariosFijos().setIncertidumbreGeneral(desvGenEntreRaiztotal * variablesService.obtenerValorVariable("N_PRODUCTO_INC_GENERAL").getValor());
        formSimulacion.getEscenariosFijos().setIncertidumbreSimulacion(consensoInicialFiltro.getDesviacionEstandar() / Math.sqrt(consensoInicialFiltro.getTotalElementos()));
        formSimulacion.getEscenariosFijos().setCoeficienteVariacion((consensoInicialFiltro.getDesviacionEstandar() / consensoInicialFiltro.getMedia()) * 100);

        /*ValoresAsignadosCandidatos*/
        List<ConsensoInicial> valoresAsignadosCandidatos = new ArrayList<>();

        /*Consenso inicial Filtro*/
        formSimulacion.setConsensoInicialFiltro(consensoInicialFiltro);

        /*Grubbs*/
        if (resultadosDetalladosList != null && resultadosDetalladosList.size() >= 3 && formSimulacion.getEscenariosFijos().getMetodoId() != null) {
            valoresAsignadosCandidatos.add(Util.parseStringToConsensoInicialGrubbs(resultadosDetalladosService.getIteracionGrubbs(Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMuestras().getMuestraId()), Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMensurandos().getMensurandosId()), Math.toIntExact(formSimulacion.getEscenariosFijos().getMetodoId().getMetodoId()), 0, 0, Util.grubbs(resultadosDetalladosList.size(), variablesService.obtenerValorVariable("N_SIGNIFICANCIA").getValor()))));
        } else if (resultadosDetalladosList != null && resultadosDetalladosList.size() >= 3 && formSimulacion.getEscenariosFijos().getMetodoId() == null) {
            valoresAsignadosCandidatos.add(Util.parseStringToConsensoInicialGrubbs(resultadosDetalladosService.getIteracionGrubbs(Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMuestras().getMuestraId()), Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMensurandos().getMensurandosId()), 0, 0, 0, Util.grubbs(resultadosDetalladosList.size(), variablesService.obtenerValorVariable("N_SIGNIFICANCIA").getValor()))));
        } else {
            valoresAsignadosCandidatos.add(new ConsensoInicial());
        }
        resultadosDetalladosService.resetAberrantes(Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMuestras().getMuestraId()), Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMensurandos().getMensurandosId()), 0, 0, 0);
        /*Alg A*/
        if (resultadosDetalladosList != null && resultadosDetalladosList.size() >= 5 && formSimulacion.getEscenariosFijos().getMetodoId() != null) {
            valoresAsignadosCandidatos.add(Util.parseStringToConsensoInicial(resultadosDetalladosService.getAlgoritmoA(Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMuestras().getMuestraId()), Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMensurandos().getMensurandosId()), Math.toIntExact(formSimulacion.getEscenariosFijos().getMetodoId().getMetodoId()), 0, 0)));
        } else if (resultadosDetalladosList != null && resultadosDetalladosList.size() >= 5 && formSimulacion.getEscenariosFijos().getMetodoId() == null) {
            valoresAsignadosCandidatos.add(Util.parseStringToConsensoInicial(resultadosDetalladosService.getAlgoritmoA(Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMuestras().getMuestraId()), Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMensurandos().getMensurandosId()), 0, 0, 0)));
        } else {
            valoresAsignadosCandidatos.add(new ConsensoInicial());
        }

        /*Desviacion standart*/
        if (resultadosDetalladosList != null && resultadosDetalladosList.size() >= 2 && formSimulacion.getEscenariosFijos().getMetodoId() != null) {
            valoresAsignadosCandidatos.add(Util.parseStringToConsensoInicial(resultadosDetalladosService.getAlgoritmoDS(Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMuestras().getMuestraId()), Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMensurandos().getMensurandosId()), Math.toIntExact(formSimulacion.getEscenariosFijos().getMetodoId().getMetodoId()), 0, 0)));
        } else if (resultadosDetalladosList != null && resultadosDetalladosList.size() >= 5 && formSimulacion.getEscenariosFijos().getMetodoId() == null) {
            valoresAsignadosCandidatos.add(Util.parseStringToConsensoInicial(resultadosDetalladosService.getAlgoritmoDS(Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMuestras().getMuestraId()), Math.toIntExact(formSimulacion.getEscenariosFijos().getIdMensurandos().getMensurandosId()), 0, 0, 0)));
        } else {
            valoresAsignadosCandidatos.add(new ConsensoInicial());
        }
        formSimulacion.setConsensoInicialList(valoresAsignadosCandidatos);
        /*Criterios de aceptabilidad*/
        List<CriteriosAceptabilidad> criteriosAceptabilidadList = criteriosAceptabilidadService.getAll();
        CriteriosAceptabilidad criteriosAceptabilidad = criteriosAceptabilidadList.stream().filter(x -> x.getIdMensurandos().getMensurandosId() == formSimulacion.getEscenariosFijos().getIdMensurandos().getMensurandosId()).findFirst().orElse(new CriteriosAceptabilidad());
        //DA
        /*Inicio validacion dato con respecto a tabla en Criterio de aceptabilidad*/
        Double valorAceptable;
        String unidadMedida;
        if (criteriosAceptabilidad.getValorAnotacion() != null) {
            if (formSimulacion.getEscenariosFijos().getValorAsignado() != null) {
                if (formSimulacion.getEscenariosFijos().getValorAsignado() < criteriosAceptabilidad.getValorAnotacion()) {
                    //Se mantiene que cuando esta condicion ocurra siempre este tendra un porcentaje(%)
                    valorAceptable = criteriosAceptabilidad.getDesviacionAceptable();
                    unidadMedida = criteriosAceptabilidad.getUnidadesMedidasId().getUnidad();
                } else {
                    valorAceptable = criteriosAceptabilidad.getValorAlternativo();
                    unidadMedida = unidadesMedidaService.get(4l).getUnidad();
                }
            } else {
                valorAceptable = criteriosAceptabilidad.getValorAlternativo();
                unidadMedida = unidadesMedidaService.get(4l).getUnidad();
            }

        } else {
            valorAceptable = criteriosAceptabilidad.getDesviacionAceptable();
            unidadMedida = criteriosAceptabilidad.getUnidadesMedidasId().getUnidad();
        }

        /*Limite Inferior*/
        try {
            if (unidadMedida.equalsIgnoreCase("%")) {
                formSimulacion.getEscenariosFijos().setLimiteInferior(formSimulacion.getEscenariosFijos().getValorAsignado() - ((valorAceptable / 100) * formSimulacion.getEscenariosFijos().getValorAsignado()));
            } else {
                formSimulacion.getEscenariosFijos().setLimiteInferior(formSimulacion.getEscenariosFijos().getValorAsignado() - valorAceptable);
            }
        } catch (NullPointerException e) {
            formSimulacion.getEscenariosFijos().setLimiteInferior(null);
        }
        /*Limite Superior*/
        try {
            if (unidadMedida.equalsIgnoreCase("%")) {
                formSimulacion.getEscenariosFijos().setLimiteSuperior(formSimulacion.getEscenariosFijos().getValorAsignado() + ((valorAceptable / 100) * formSimulacion.getEscenariosFijos().getValorAsignado()));
            } else {
                formSimulacion.getEscenariosFijos().setLimiteSuperior(formSimulacion.getEscenariosFijos().getValorAsignado() + valorAceptable);
            }
        } catch (NullPointerException e) {
            formSimulacion.getEscenariosFijos().setLimiteSuperior(null);
        }

        try {
            for (ResultadosDetallados resultadosDetallados : resultadosDetalladosList) {
                ValoresConsenso valoresConsenso;
                valoresConsenso = simulacion(resultadosDetallados, formSimulacion.getEscenariosFijos(), consensoInicialFiltro, valoresAsignadosCandidatos, valorAceptable, unidadMedida);
                if (valoresConsenso.getIsAberrante()) {
                    valoresAtipicosList.add(valoresConsenso);
                } else {
                    valoresConsensosList.add(valoresConsenso);
                }
            }
            formSimulacion.setValoresConsensosList(valoresConsensosList);
            formSimulacion.setValoresAtipicosList(valoresAtipicosList);
        } catch (Exception e) {
            FormSimulacion formSimulacionCatch = new FormSimulacion();
            formSimulacionCatch.setEscenariosFijos(formSimulacion.getEscenariosFijos());
            formSimulacionCatch.getEscenariosFijos().setMetodoId(new Metodos());
            formSimulacionCatch.getEscenariosFijos().setValorAsignado(null);
            return formSimulacionCatch;
        }

        return formSimulacion;
    }
}