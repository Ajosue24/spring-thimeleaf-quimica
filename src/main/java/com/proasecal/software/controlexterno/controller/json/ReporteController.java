package com.proasecal.software.controlexterno.controller.json;

import com.proasecal.software.controlexterno.entity.*;
import com.proasecal.software.controlexterno.entity.DAO.Observacion;
import com.proasecal.software.controlexterno.entity.documentacionjson.*;
import com.proasecal.software.controlexterno.service.*;
import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import com.proasecal.software.web.entity.parametricas.Variables;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import com.proasecal.software.web.service.administrar.MensurandoService;
import com.proasecal.software.web.service.administrar.UnidadesMedidaService;
import com.proasecal.software.web.service.inscripcion.InscripcionMuestrasService;
import com.proasecal.software.web.service.parametricas.VariablesService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/controlexterno/reporte")
public class ReporteController {

    @Autowired
    ResultadosService resultadosService;
    @Autowired
    InscripcionMuestrasService inscripcionMuestrasService;
    @Autowired
    VariablesService variablesService;
    private final Logger LOG = LoggerFactory.getLogger(ReporteController.class);
    @Autowired
    ObservacionMuestraService observacionMuestraService;
    @Autowired
    MensurandoService mensurandoService;
    @Autowired
    CriteriosAceptabilidadService criteriosAceptabilidadService;
    @Autowired
    UnidadesMedidaService unidadesMedidaService;
    @Autowired
    ResultadosDetalladosService resultadosDetalladosService;

    @Autowired
    ObservacionResultadoService observacionResultadoService;

    @Autowired
    EscenariosFijosService escenariosFijosService;


    @GetMapping(value = "/", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<ObjetoJson> inicioJson(InscripcionMuestras inscripcionMuestras,Boolean Accion) {
        /*inscripcionMuestras = inscripcionMuestrasService.find(1443l).get();*/
        Resultados resultados = resultadosService.findByIdInscripcionMuestras(inscripcionMuestras).orElse(new Resultados());
        List<Mensurandos> mensurandosList = mensurandoService.listHabilitado(inscripcionMuestras.getInscripProgramaId().getProgramaId().getProgramaId());
        List<CriteriosAceptabilidad> criteriosAceptabilidadsList = criteriosAceptabilidadService.getAll();
        UsuariosLabSedes usuariosLabSedes = inscripcionMuestras.getIdUsuarios();
        //REPORTES =  4 en base de datos
        List<Variables> variablesList = variablesService.getAllVarByProcesosId(4l);
        List<CuadroDesempeno> cuadroDesempenosList = new ArrayList<>();
        List<CuadroIncertidumbre> cuadroIncertidumbreList = new ArrayList<>();
        LOG.info("Inicio Portada");
        Portada portada = generarDatosPortada(inscripcionMuestras, resultados,Accion);
        portada = addComentariosGenerales(portada, inscripcionMuestras);
        portada = addComentariosParticipante(portada, observacionResultadoService.getObservacionResultadoByResultado(resultados).orElse(new ObservacionResultado()), resultados);
        //String portadaS = parsePortadaToString(portada);
        LOG.info("Fin Portada");

        /**
         * Obtenemos Escenario fijo padre
         */

        List <ResultadosDetallados> result =  resultados.getResultadosDetalladosList().stream().sorted(Comparator.comparing(o -> o.getMensurandosId().getOrden())).
                collect(Collectors.toList());
        for (ResultadosDetallados rd : result) {
            EscenariosFijos escenariosFijosP;
            EscenariosResultados escenariosResultados;
            //Obtengo el nombre del mensurando concatenado con su unidad
            String nombreMensurandoConUnidad = rd.getMensurandosId().getNombreMensurando() + "(" + rd.getMensurandosId().getIdUnidadesMedidas().getUnidad() + ")";
            if (rd.getResultadosDetalladosEscenariosList() != null && !rd.getResultadosDetalladosEscenariosList().isEmpty()) {
                //Obtengo el ultimo escenario fijo padre para el resultado
                List<EscenariosResultados> escenariosResultadosList = new ArrayList<>(rd.getResultadosDetalladosEscenariosList());
                //nos aseguramos de tener el escenarioResultado del padre asi a su vez el escenariofijoPadre
                escenariosFijosP = escenariosResultadosList.get(rd.getResultadosDetalladosEscenariosList().size() - 1).getIdEscenarioFijo().getEscenariosFijosId() != null ?
                        escenariosResultadosList.get(rd.getResultadosDetalladosEscenariosList().size() - 1).getIdEscenarioFijo().getEscenariosFijosId() : escenariosResultadosList.get(rd.getResultadosDetalladosEscenariosList().size() - 1).getIdEscenarioFijo();
                EscenariosFijos finalEscenariosFijosP = escenariosFijosP;
                escenariosResultados = escenariosResultadosList.stream().filter(x -> x.getIdEscenarioFijo().getIdEscenarioFijo() == finalEscenariosFijosP.getIdEscenarioFijo()).findFirst()
                        .orElse(new EscenariosResultados());
                escenariosResultadosList.get(rd.getResultadosDetalladosEscenariosList().size() - 1);
                LOG.info("obtener Cuadro desempeno para " + rd.getMensurandosId().getNombreMensurando());
                cuadroDesempenosList.add(generarCuadroDesempeno(nombreMensurandoConUnidad, escenariosResultados, variablesList));
                LOG.info("Fin Cuadro desempeno para " + rd.getMensurandosId().getNombreMensurando());
                LOG.info("INICIO Cuadro incertidumbre para " + rd.getMensurandosId().getNombreMensurando());
                cuadroIncertidumbreList.add(getCuadroIncertidumbre(nombreMensurandoConUnidad, escenariosFijosP));
                LOG.info("FIN Cuadro Incertidumbre para " + rd.getMensurandosId().getNombreMensurando());
            }
        }
        ObjetoJson objJson = new ObjetoJson();
        LOG.info("INICIO historico List");
        objJson = getHistoricoIv(resultados, mensurandosList, criteriosAceptabilidadsList, usuariosLabSedes, variablesList,inscripcionMuestras.getIdMuestras());
        LOG.info("Fin historico List");

        objJson.setPortada(portada);
        objJson.setCuadro_desempeño(cuadroDesempenosList);
        objJson.setCuadro_incertidumbre(cuadroIncertidumbreList);
        try {
            return new ResponseEntity<>(objJson, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(objJson, HttpStatus.BAD_REQUEST);
        }
    }


    public Portada generarDatosPortada(InscripcionMuestras inscripcionMuestras, Resultados resultados,Boolean Accion) {
        String nroVersion;
        String fechaLibRes;
        String fechaCierre;
        String fechaCreacion;
        Resultados resultados1 = resultadosService.findLastByInscripcionMuestras(inscripcionMuestras);
        try {
            //nroVersion = resultados.getInformesList().get(resultados.getInformesList().size() - 1) != null && resultados.getInformesList().get(resultados.getInformesList().size() - 1).getNumeroVersion() > 0 ? String.valueOf(resultados.getInformesList().get(resultados.getInformesList().size() - 1).getNumeroVersion()) : "1";
            if (Accion)
                nroVersion = String.valueOf(resultados.getInformesList().size()+1);
            else
                nroVersion = String.valueOf(resultados.getInformesList().size());

        } catch (Exception e) {
            nroVersion = "1";
        }
        try {
            fechaLibRes = inscripcionMuestras.getIdMuestras().getFechaLibResultado() != null ? new SimpleDateFormat("yyyy/MM/dd").format(inscripcionMuestras.getIdMuestras().getFechaLibResultado()) : "yyyy/MM/dd";
            fechaCierre = inscripcionMuestras.getIdMuestras().getFechaCierre() != null ? new SimpleDateFormat("yyyy/MM/dd").format(inscripcionMuestras.getIdMuestras().getFechaCierre()) : "yyyy/MM/dd";
            fechaCreacion = resultados1.getFechaCreacion() != null ? new SimpleDateFormat("yyyy/MM/dd").format(resultados1.getFechaCreacion()) : "yyyy/MM/dd";
        } catch (Exception e) {
            fechaLibRes = "yyyy/mm/dd";
            fechaCierre = "yyyy/mm/dd";
            fechaCreacion = "yyyy/mm/dd";
        }
        Portada portada = new Portada(inscripcionMuestras.getInscripProgramaId().getProgramaId().getNombrePrograma(),
                inscripcionMuestras.getIdUsuarios().getUsuarioId().getNombreUsuario(),
                inscripcionMuestras.getIdMuestras().getNumeroMuestra().toString(),
                inscripcionMuestras.getIdMuestras().getIdTipoMuestra().getNombre(),
                inscripcionMuestras.getIdMuestras().getNumeroMuestra() + "" + inscripcionMuestras.getIdUsuarios().getUsuarioId().getNombreUsuario() + "" + nroVersion,
                fechaLibRes,
                fechaCierre,
                fechaCreacion,
                nroVersion);


        return portada;
    }


    public CuadroDesempeno generarCuadroDesempeno(String nombreMensurandoConUnidad, EscenariosResultados escenariosResultados, List<Variables> variablesList) {
        LOG.info("Generando cuadro de desempeno");
        try {
            CuadroDesempeno cuadroDesempeno = new CuadroDesempeno();
            cuadroDesempeno.setMensurando(nombreMensurandoConUnidad);
            cuadroDesempeno.setIv(escenariosResultados.getIndiceVarianza()!=null?escenariosResultados.getIndiceVarianza():0);
            cuadroDesempeno.setDesempeño(Math.abs(cuadroDesempeno.getIv()!=null?cuadroDesempeno.getIv():0) <= variablesList.stream().filter(x -> x.getNombreVariable().equalsIgnoreCase("CUADRO_DESEMPENO")).findFirst().orElse(new Variables(200.00)).getValor() ? "SATISFACTORIO" : "NO SATISFACTORIO");
            return cuadroDesempeno;
        } catch (Exception e) {
            LOG.error("error al generar el cuadro desempeno\n" + e);
            return null;
        }
    }

    public CuadroIncertidumbre getCuadroIncertidumbre(String nombreMensurandoConUnidad, EscenariosFijos escenariosFijos) {

        LOG.info("Generando cuadro incertidumbre");
        try {
            CuadroIncertidumbre cuadroIncertidumbre = new CuadroIncertidumbre();
            cuadroIncertidumbre.setMensurando(nombreMensurandoConUnidad);
            cuadroIncertidumbre.setMedia_general(escenariosFijos.getMediaGeneral());
            cuadroIncertidumbre.setDesv_general(escenariosFijos.getDesviacionGeneral());
            cuadroIncertidumbre.setN_total(escenariosFijos.getTotalGeneral());
            cuadroIncertidumbre.setIncertidumbre_gen(escenariosFijos.getIncertidumbreGeneral());
            return cuadroIncertidumbre;
        } catch (Exception e) {
            LOG.error("Error al generar cuadro incertidumbre\n" + e);
            return null;
        }
    }


    public String parsePortadaToString(Portada portada) {
        JSONObject jsonString = new JSONObject()
                .put("programa", portada.getPrograma())
                .put("codigo_proasecal", portada.getCodigo_proasecal())
                .put("numero_muestra", portada.getNumero_muestra())
                .put("tipo_muestra", portada.getTipo_muestra())
                .put("numero_informe", portada.getNumero_informe())
                .put("fecha_emision", portada.getFecha_emision())
                .put("fecha_corte", portada.getFecha_corte())
                .put("fecha_ingreso", portada.getFecha_ingreso())
                .put("version", portada.getVersion())
                .put("observaciones_generales", portada.getObservaciones_generales())
                .put("comentarios_generales", portada.getComentarios_generales())
                .put("observaciones_participante", portada.getObservaciones_participante())
                .put("observaciones_para_participante", portada.getObservaciones_para_participante());

        if (!(portada.getObservaciones_generales() != null)) {
            jsonString.remove("observaciones_generales");
        }
        if (!(portada.getComentarios_generales() != null)) {
            jsonString.remove("comentarios_generales");
        }
        if (!(portada.getObservaciones_participante() != null)) {
            jsonString.remove("observaciones_participante");
        }
        if (!(portada.getObservaciones_para_participante() != null)) {
            jsonString.remove("observaciones_para_participante");
        }
        return jsonString.toString();
    }


    Portada addComentariosGenerales(Portada portada, InscripcionMuestras inscripcionMuestras) {
        //1 Observaciónes para todos los participantes
        //2 Comentarios generales


        /*Observaciones*/
        List<TipoObservacion> tipoObservacion = inscripcionMuestras.getIdMuestras().getIdPrograma().getTipoProgramaId().getTipoObservacionList().stream()
                .sorted(Comparator.comparing(TipoObservacion::getIdtipoObservacion)).collect(Collectors.toList());

        List<Observacion> observacionList = new ArrayList<>();
        if(tipoObservacion!=null&&!tipoObservacion.isEmpty()){

        for (TipoObservacion tipoObs : tipoObservacion) {
            Optional<ObservacionMuestra> observacionOptional = observacionMuestraService.encontrarTipoMuestra(inscripcionMuestras.getIdMuestras(), tipoObs);

            Observacion obs = new Observacion(
                    tipoObs.getIdtipoObservacion(),
                    tipoObs.getNombre(),
                    observacionOptional.isPresent() ? observacionOptional.get().getObservacion() : ""
            );
            observacionList.add(obs);
        }
        }
        portada.setObservaciones_generales(observacionList.stream().filter(x -> x.getIdTipo() == 1).findFirst().orElse(null).getObservacion());
        portada.setComentarios_generales(observacionList.stream().filter(x -> x.getIdTipo() == 2).findFirst().orElse(null).getObservacion());
        return portada;
    }


    Portada addComentariosParticipante(Portada portada, ObservacionResultado observacionResultado, Resultados resultados) {
        portada.setObservaciones_para_participante(observacionResultado.getObservacion());
        portada.setObservaciones_participante(resultados.getObservaciones());
        return portada;
    }

    public ObjetoJson getHistoricoIv(Resultados resultados, List<Mensurandos> mensurandosList, List<CriteriosAceptabilidad> criteriosAceptabilidadList, UsuariosLabSedes usuariosLabSedes,
                                     List<Variables> variablesList, Muestras muestras) {
        List<Resumen> resumenList = new ArrayList<>();
        List<HistoricoIV> historicoIVList = new ArrayList<>();
        int i = 1;
        List<ConsolidadoIV> consolidadoIVList = new ArrayList<>();
        List<SdiMetodos> sdiMetodosList = new ArrayList<>();
        List<SdiMetodos> sdiMetodosEquipo = new ArrayList<>();
        for (Mensurandos mensurandos : mensurandosList) {
            String mensurandoNombre = mensurandos.getNombreMensurando() + "(" + mensurandos.getIdUnidadesMedidas().getUnidad() + ")";
            List<EscenariosFijos> escenariosFijosList = escenariosFijosService.escenariosXMuestraYMensurando(muestras,mensurandos);
            /*Criterios de aceptabilidad*/
            CriteriosAceptabilidad criteriosAceptabilidad = criteriosAceptabilidadList.stream().filter(x -> x.getIdMensurandos().getMensurandosId() == mensurandos.getMensurandosId()).findFirst().orElse(new CriteriosAceptabilidad());
            HistoricoIV historicoIV = new HistoricoIV();
            historicoIV.setMensurando(mensurandos.getNombreMensurando() + "(" + mensurandos.getIdUnidadesMedidas().getUnidad() + ")");
            ResultadosDetallados resultadoDetallado = resultados.getResultadosDetalladosList().stream().filter(x -> x.getMensurandosId().getMensurandosId() == mensurandos.getMensurandosId()).findFirst().orElse(null);
            EscenariosFijos escenariosFijosP = null;
            EscenariosFijos escenariosFijosH = null;
            historicoIV.setCont(i);
            EscenariosResultados escenariosResultadosH = null;
            EscenariosResultados escenariosResultados = null;
            String criteriosAceptabilidadString = "";
            if (resultadoDetallado != null) {
                historicoIV.setTitulo(" Método: " + resultadoDetallado.getMetodoId().getCodProasecal() + " - " + resultadoDetallado.getMetodoId().getNombreMetodo() + ", " + "Reactivo: " + resultadoDetallado.getReactivoId().getCodProasecal()
                        + " - " + resultadoDetallado.getReactivoId().getNombreReactivo() + ", " + "Equipo: " + resultadoDetallado.getEquipoId().getCodProasecal() + " - " + resultadoDetallado.getEquipoId().getNombreEquipo());
                historicoIV.setValor(resultadoDetallado.getValorReportado());
                List<EscenariosResultados> escenariosResultadosList = new ArrayList<>(resultadoDetallado.getResultadosDetalladosEscenariosList());
                //nos aseguramos de tener el escenarioResultado del padre asi a su vez el escenariofijoPadre
               if(resultadoDetallado.getResultadosDetalladosEscenariosList()!=null&&!resultadoDetallado.getResultadosDetalladosEscenariosList().isEmpty()){
                   escenariosFijosP = escenariosResultadosList.get(resultadoDetallado.getResultadosDetalladosEscenariosList().size() - 1).getIdEscenarioFijo().getEscenariosFijosId() != null ?
                           escenariosResultadosList.get(resultadoDetallado.getResultadosDetalladosEscenariosList().size() - 1).getIdEscenarioFijo().getEscenariosFijosId() : escenariosResultadosList.get(resultadoDetallado.getResultadosDetalladosEscenariosList().size() - 1).getIdEscenarioFijo();
                EscenariosFijos finalEscenariosFijosP = escenariosFijosP;
                escenariosResultados = escenariosResultadosList.stream().filter(x -> x.getIdEscenarioFijo().getIdEscenarioFijo() == finalEscenariosFijosP.getIdEscenarioFijo()).findFirst()
                        .orElse(new EscenariosResultados());
                escenariosFijosH = escenariosFijosP.getEscenariosFijosList().stream().filter(x -> x.getEquipoId().getEquipoId() == resultadoDetallado.getEquipoId().getEquipoId()).findFirst().orElse(new EscenariosFijos());
                EscenariosFijos finalEscenariosFijosH = escenariosFijosH;
                escenariosResultadosH = escenariosResultadosList.stream().filter(x -> x.getIdEscenarioFijo().getIdEscenarioFijo() == finalEscenariosFijosH.getIdEscenarioFijo()).findFirst()
                        .orElse(new EscenariosResultados());
                criteriosAceptabilidadString = findCriteriosAceptabilidadxMensurando(criteriosAceptabilidad,escenariosFijosP.getValorAsignado());
               }
                if (escenariosResultados!=null&&escenariosResultados.getIdEscenarioResultado() != null) {
                    List<ResultadosDetallados> resultadosDetalladosTmp = new ArrayList<>(resultadosDetalladosService.findLast12xUsuarioAndMensurandoAndIdResultadosLessThanEqualsOrderByFechaInicial(usuariosLabSedes,mensurandos,resultados));
                            historicoIV.setValor_asignado(escenariosFijosP.getValorAsignado());
                    historicoIV.setDesviacion(escenariosResultados.getDesviacionValorAsignado());
                    historicoIV.setDesviacion_aceptable(criteriosAceptabilidadString);
                    historicoIV.setIndice_varianza(escenariosResultados.getIndiceVarianza());
                    historicoIV.setCont(i);
                    historicoIV.setNumero_decimales(mensurandos.getCantDecimales());
                    historicoIV.setGraph(getListGraph(variablesList, resultadosDetalladosTmp));
                    //Para no realizar for de nuevo y optimizar reutilizaremos el de este
                    //CorreccionError
                    LOG.info("inicio Consolidado para mensurando"+mensurandoNombre);
                    consolidadoIVList.add(getConsolidadoIvList(escenariosFijosList, mensurandos, i, usuariosLabSedes, mensurandoNombre));
                    LOG.info("Fin Consolidado para mensurando"+mensurandoNombre);
                    LOG.info("inicio Metodos "+mensurandoNombre);
                    sdiMetodosList.add(getSdiMetodo(escenariosFijosP, escenariosResultados, mensurandos, mensurandoNombre, i, resultadosDetalladosTmp, true));
                    LOG.info("Fin Metodos "+mensurandoNombre);
                    LOG.info("Inicio metodo equipo" + mensurandoNombre);
                    sdiMetodosEquipo.add(getSdiMetodo(escenariosFijosH, escenariosResultadosH, mensurandos, mensurandoNombre, i++, resultadosDetalladosTmp, false));
                    LOG.info("Fin metodo equipo" + mensurandoNombre);
                }else {
                    List<ResultadosDetallados> resultadosDetalladosTmp = new ArrayList<>(resultadosDetalladosService.findLast12xUsuarioAndMensurandoAndIdResultadosLessThanEqualsOrderByFechaInicial(usuariosLabSedes,mensurandos,resultados));
                    historicoIV.setGraph(getListGraph(variablesList, resultadosDetalladosTmp));
                    consolidadoIVList.add(getConsolidadoIvList(escenariosFijosList, mensurandos, i, usuariosLabSedes, mensurandoNombre));
                    sdiMetodosList.add(getSdiMetodo(escenariosFijosP, null, mensurandos, mensurandoNombre, i, resultadosDetalladosTmp, true));
                    sdiMetodosEquipo.add(getSdiMetodo(escenariosFijosH, null, mensurandos, mensurandoNombre, i++, resultadosDetalladosTmp, false));

                }
            } else {
                List<ResultadosDetallados> resultadosDetalladosTmp = new ArrayList<>(resultadosDetalladosService.findLast12xUsuarioAndMensurandoAndIdResultadosLessThanEqualsOrderByFechaInicial(usuariosLabSedes,mensurandos,resultados));
                historicoIV.setGraph(getListGraph(variablesList, resultadosDetalladosTmp));
                consolidadoIVList.add(getConsolidadoIvList(escenariosFijosList, mensurandos, i, usuariosLabSedes, mensurandoNombre));
                sdiMetodosList.add(getSdiMetodo(escenariosFijosP, null, mensurandos, mensurandoNombre, i, resultadosDetalladosTmp, true));
                sdiMetodosEquipo.add(getSdiMetodo(escenariosFijosH, null, mensurandos, mensurandoNombre, i++, resultadosDetalladosTmp, false));

            }
            historicoIVList.add(historicoIV);
            /**
             * Obj Resumen
             */
            if(escenariosResultados!=null&&escenariosFijosP!=null){
            Resumen resumen = new Resumen();
            resumen.setMensurando(mensurandoNombre);
            resumen.setValor(resultadoDetallado != null ? resultadoDetallado.getValorReportado() : 0.0);
            resumen.setValor_asignado(escenariosFijosP != null ? escenariosFijosP.getValorAsignado() : 0.0);
            resumen.setDesviacion(escenariosResultados != null ? escenariosResultados.getDesviacionValorAsignado()!=null?escenariosResultados.getDesviacionValorAsignado():0 : 0.0);
            resumen.setDesviacion_aceptable(criteriosAceptabilidadString);
            resumen.setIndice_varianza(escenariosResultados != null ? escenariosResultados.getIndiceVarianza()!=null?escenariosResultados.getIndiceVarianza():0 : 0.0);
            resumen.setTotal_laboratorios(escenariosFijosP != null ? escenariosFijosP.getTotalGeneral() : 0);
            resumen.setMedia_consenso_metodo(escenariosFijosP != null ? escenariosFijosP.getMediaFiltro() : 0.0);
            resumen.setDesviacion_estandar_metodo(escenariosFijosP.getDesviacionEstandar()== null? "N/A" : Double.toString(redondeoXdecimal(escenariosFijosP.getDesviacionEstandar(), mensurandos.getCantDecimales())));
            resumen.setSdi_metodo(escenariosResultados.getIndiceDesviacionEstandar()== null? "N/A" : Double.toString(redondeoXdecimal(escenariosResultados.getIndiceDesviacionEstandar(), mensurandos.getCantDecimales())));
            resumen.setN_laboratorios_metodo(escenariosFijosP.getTotalValores());
            resumen.setIncertidumbre_metodo(escenariosFijosP.getIncertidumbreSimulacion()== null? "N/A" : Double.toString(redondeoXdecimal(escenariosFijosP.getIncertidumbreSimulacion(), mensurandos.getCantDecimales())));
            resumen.setMedia_consenso_metodo_equipo(escenariosFijosH != null &&escenariosFijosH.getIdEscenarioFijo()!=null? escenariosFijosH.getMediaFiltro() : 0);
            resumen.setDesviacion_estandar_metodo_equipo(escenariosFijosH.getDesviacionEstandar()== null ? "N/A" : Double.toString(redondeoXdecimal(escenariosFijosH.getDesviacionEstandar(), mensurandos.getCantDecimales())));
            resumen.setSdi_metodo_equipo(escenariosResultadosH.getIndiceDesviacionEstandar()== null? "N/A" : Double.toString(redondeoXdecimal(escenariosResultadosH.getIndiceDesviacionEstandar(), mensurandos.getCantDecimales())));
            resumen.setN_laboratorios_metodo_equipo(escenariosFijosH.getTotalValores()!=null?escenariosFijosH.getTotalValores():0);
            resumenList.add(resumen);
            }
        }

        ObjetoJson objetoJson = new ObjetoJson();
        objetoJson.setConsolidado_iv(consolidadoIVList);
        objetoJson.setHistorico_iv(historicoIVList);
        objetoJson.setSdi_metodo(sdiMetodosList);
        objetoJson.setSdi_metodo_equipo(sdiMetodosEquipo);
        objetoJson.setResumen(resumenList);

        return objetoJson;
    }


    String findCriteriosAceptabilidadxMensurando(CriteriosAceptabilidad criteriosAceptabilidad, Double valorAsignado) {
        //DA
        /*Inicio validacion dato con respecto a tabla en Criterio de aceptabilidad*/
        Double valorAceptable;
        String unidadMedida;
        if (criteriosAceptabilidad.getValorAnotacion() != null) {
            if (valorAsignado < criteriosAceptabilidad.getValorAnotacion()) {
                //Se mantiene que cuando esta condicion ocurra siempre este tendra un porcentaje(%)
                valorAceptable = criteriosAceptabilidad.getDesviacionAceptable();
                unidadMedida = criteriosAceptabilidad.getUnidadesMedidasId().getUnidad();
            } else {
                valorAceptable = criteriosAceptabilidad.getValorAlternativo();
                unidadMedida = unidadesMedidaService.get(4l).getUnidad();
            }
            return "" + valorAceptable + unidadMedida;
        } else {
            valorAceptable = criteriosAceptabilidad.getDesviacionAceptable();
            unidadMedida = criteriosAceptabilidad.getUnidadesMedidasId().getUnidad();
            return "" + valorAceptable + unidadMedida;
        }
    }


    List<Graph> getListGraph(List<Variables> variablesList, List<ResultadosDetallados> resultadosDetalladosList) {

        EscenariosFijos escenariosFijosP = new EscenariosFijos();
        EscenariosResultados escenariosResultados = new EscenariosResultados();
        List<Graph> graphList = new ArrayList<>();
        for (ResultadosDetallados rd : resultadosDetalladosList) {
            Graph gr = new Graph();
            if (rd.getResultadosDetalladosEscenariosList() != null && !rd.getResultadosDetalladosEscenariosList().isEmpty()) {
                List<EscenariosResultados> escenariosResultadosList = new ArrayList<>(rd.getResultadosDetalladosEscenariosList());
                /*
                New
                 */
                escenariosFijosP = escenariosResultadosList.get(rd.getResultadosDetalladosEscenariosList().size() - 1).getIdEscenarioFijo().getEscenariosFijosId() != null ?
                        escenariosResultadosList.get(rd.getResultadosDetalladosEscenariosList().size() - 1).getIdEscenarioFijo().getEscenariosFijosId() : escenariosResultadosList.get(rd.getResultadosDetalladosEscenariosList().size() - 1).getIdEscenarioFijo();
                EscenariosFijos finalEscenariosFijosP = escenariosFijosP;
                escenariosResultados = escenariosResultadosList.stream().filter(x -> x.getIdEscenarioFijo().getIdEscenarioFijo() == finalEscenariosFijosP.getIdEscenarioFijo()).findFirst()
                        .orElse(new EscenariosResultados());
                /**
                 * Fin new
                 */
                Optional<Double> indiceVarianza = Optional.ofNullable(escenariosResultados.getIndiceVarianza());
                    if(indiceVarianza.isPresent()){
                if (indiceVarianza.get() > variablesList.stream().filter(x -> x.getNombreVariable().equalsIgnoreCase("IND_VAR_MAYOR")).findFirst().orElse(new Variables(350.00)).getValor()) {
                    indiceVarianza = Optional.of(Double.parseDouble(variablesList.stream().filter(x -> x.getNombreVariable().equalsIgnoreCase("IND_VAR_MAYOR")).findFirst().orElse(new Variables("380")).getColor()));
                } else if (indiceVarianza.get() < variablesList.stream().filter(x -> x.getNombreVariable().equalsIgnoreCase("IND_VAR_MENOR")).findFirst().orElse(new Variables(-349.00)).getValor()) {
                    indiceVarianza = Optional.of(Double.parseDouble(variablesList.stream().filter(x -> x.getNombreVariable().equalsIgnoreCase("IND_VAR_MENOR")).findFirst().orElse(new Variables("-349")).getColor()));
                } }

                gr.setInd_var((int) Math.round(indiceVarianza.orElse(0.0)));
                if (gr.getInd_var() != null) {
                    //Gestion de mayores
                    if (gr.getInd_var() <= variablesList.stream().filter(x -> x.getNombreVariable().equalsIgnoreCase("VERDE_MENOR_IGUAL")).findFirst().orElse(new Variables(200.00)).getValor() && gr.getInd_var() >= variablesList.stream().filter(x -> x.getNombreVariable().equalsIgnoreCase("VERDE_MAYOR_IGUAL")).findFirst().orElse(new Variables(-100.00)).getValor()) {
                        gr.setSerie("VERDE");
                    }
                    if (gr.getInd_var() > variablesList.stream().filter(x -> x.getNombreVariable().equalsIgnoreCase("AMARILLO_P_MAYOR")).findFirst().orElse(new Variables(100.00)).getValor() && gr.getInd_var() <= variablesList.stream().filter(x -> x.getNombreVariable().equalsIgnoreCase("AMARILLO_P_MENOR_IGUAL")).findFirst().orElse(new Variables(200.00)).getValor()) {
                        gr.setSerie("AMARILLO");
                    }
                    if (gr.getInd_var() < variablesList.stream().filter(x -> x.getNombreVariable().equalsIgnoreCase("AMARILLO_N_MENOR")).findFirst().orElse(new Variables(-100.00)).getValor() && gr.getInd_var() >= variablesList.stream().filter(x -> x.getNombreVariable().equalsIgnoreCase("AMARILLO_N_MAYOR_IGUAL")).findFirst().orElse(new Variables(-200.00)).getValor()) {
                        gr.setSerie("AMARILLO");
                    }
                    if (gr.getInd_var() > variablesList.stream().filter(x -> x.getNombreVariable().equalsIgnoreCase("ROJO_P_MAYOR")).findFirst().orElse(new Variables(200.00)).getValor() && gr.getInd_var() <= variablesList.stream().filter(x -> x.getNombreVariable().equalsIgnoreCase("ROJO_P_MENOR_IGUAL")).findFirst().orElse(new Variables(300.00)).getValor()) {
                        gr.setSerie("ROJO");
                    }
                    if (gr.getInd_var() < variablesList.stream().filter(x -> x.getNombreVariable().equalsIgnoreCase("ROJO_N_MENOR")).findFirst().orElse(new Variables(-200.00)).getValor() && gr.getInd_var() >= variablesList.stream().filter(x -> x.getNombreVariable().equalsIgnoreCase("ROJO_N_MAYOR_IGUAL")).findFirst().orElse(new Variables(-300.00)).getValor()) {
                        gr.setSerie("ROJO");
                    }
                    if (gr.getInd_var() > variablesList.stream().filter(x -> x.getNombreVariable().equalsIgnoreCase("HIGH_MAYOR")).findFirst().orElse(new Variables(300.00)).getValor()) {
                        gr.setSerie("HIGH");
                    }
                    if (gr.getInd_var() < variablesList.stream().filter(x -> x.getNombreVariable().equalsIgnoreCase("HIGH_MENOR")).findFirst().orElse(new Variables(-300.00)).getValor()) {
                        gr.setSerie("HIGH");
                    }
                    gr.setNumber(Integer.toString(rd.getIdResultados().getIdInscripcionMuestras().getIdMuestras().getNumeroMuestra()));
                }
                graphList.add(gr);
            }
        }
        return graphList;
    }



    ConsolidadoIV getConsolidadoIvList(List<EscenariosFijos> escenariosFijosPadre, Mensurandos mensurandos, Integer cont, UsuariosLabSedes usuariosLabSedes, String mensurandosNombre) {
        ConsolidadoIV consolidadoIV = new ConsolidadoIV();
        consolidadoIV.setEvaluationName(mensurandosNombre);
        consolidadoIV.setCont(cont);
        Set<EscenariosResultados> escenariosResultadosList = new HashSet<>();
        Double indiceVarianzaUsuario = 0.0;
        if(escenariosFijosPadre!=null&&!escenariosFijosPadre.isEmpty()){
        for(EscenariosFijos escenariosFijos:escenariosFijosPadre){
            if(escenariosFijos!=null){
                escenariosResultadosList.addAll(escenariosFijos.getResultadosDetalladosEscenariosList().stream().filter(x -> x.getIdResultadosDetallados().getMensurandosId().getMensurandosId() == mensurandos.getMensurandosId()).collect(Collectors.toList()));
                    if(indiceVarianzaUsuario.equals(0.0)){
                        indiceVarianzaUsuario = escenariosResultadosList.stream().filter(x -> x.getIdResultadosDetallados().getIdResultados().getIdInscripcionMuestras().getIdUsuarios().getIdUsuarioLabSedes() ==
                                usuariosLabSedes.getIdUsuarioLabSedes()).findAny().orElse(new EscenariosResultados(0.00)).getIndiceVarianza();
                    }
                }
        }
        }


        //100% = porcentaje

        AtomicReference<Integer> allCount = new AtomicReference<>(0);
        List<Histograma> histogramas = new ArrayList<Histograma>() {
            {
                add(new Histograma("< -200 - -200", Integer.MIN_VALUE, -200));
                add(new Histograma("-200 - -150", -200, -150));
                add(new Histograma("-150 - -100", -150, -100));
                add(new Histograma("-100 - -50", -100, -50));
                add(new Histograma("-50 – 0", -50, 0));
                add(new Histograma("0 - 50", 0, 50));
                add(new Histograma("50 - 100", 50, 100));
                add(new Histograma("100 - 150", 100, 150));
                add(new Histograma("150 - 200", 150, 200));
                add(new Histograma("200 - > 200", 200, Integer.MAX_VALUE));
            }
        };

        if (escenariosResultadosList != null && !escenariosResultadosList.isEmpty()) {
            for (EscenariosResultados er : escenariosResultadosList) {
                Double iv = er.getIndiceVarianza();
                Double finalIndiceVarianzaUsuario = indiceVarianzaUsuario;
                histogramas.forEach(histograma -> {
                    if (histograma.apply(iv)) {
                        histograma.plus();
                        allCount.getAndSet(allCount.get() + 1);
                    }
                    if (histograma.apply(finalIndiceVarianzaUsuario)) {
                        histograma.select();
                    }
                });
            }
            histogramas.forEach(histograma -> histograma.calculateValue(allCount.get()));
        }
        consolidadoIV.setHistograma(histogramas);

        return consolidadoIV;
    }


    SdiMetodos getSdiMetodo(EscenariosFijos escenariosFijosP, EscenariosResultados escenarioResultado, Mensurandos mensurandos, String mensurandoNombre, Integer count,
                            List<ResultadosDetallados> resultadosDetalladosList, Boolean padre) {
        SdiMetodos sdiMetodos = new SdiMetodos();
        sdiMetodos.setMensurando(mensurandoNombre);
        if (escenarioResultado != null&&escenarioResultado.getIdEscenarioResultado()!=null) {
                sdiMetodos.setTitulo("Método :" + escenarioResultado.getIdResultadosDetallados().getMetodoId().getCodProasecal() + " - " +
                        escenarioResultado.getIdResultadosDetallados().getMetodoId().getNombreMetodo() + ", Reactivo: " +
                        escenarioResultado.getIdResultadosDetallados().getReactivoId().getCodProasecal() + " - " +
                        escenarioResultado.getIdResultadosDetallados().getReactivoId().getNombreReactivo() + ", Equipo: " +
                        escenarioResultado.getIdResultadosDetallados().getEquipoId().getCodProasecal() + " - " +
                        escenarioResultado.getIdResultadosDetallados().getEquipoId().getNombreEquipo());


            sdiMetodos.setValor(escenarioResultado.getIdResultadosDetallados().getValorReportado());
            sdiMetodos.setMedia_consenso(escenariosFijosP.getMediaFiltro());
            sdiMetodos.setDesviacion_estandar(!(escenariosFijosP.getDesviacionEstandar() != null)? "N/A" : Double.toString(redondeoXdecimal(escenariosFijosP.getDesviacionEstandar(), mensurandos.getCantDecimales())));
            sdiMetodos.setSdi(!(escenarioResultado.getIndiceDesviacionEstandar() != null)? "N/A" : Double.toString(redondeoXdecimal(escenarioResultado.getIndiceDesviacionEstandar(), mensurandos.getCantDecimales())));
            sdiMetodos.setN_laboratorios(escenariosFijosP.getTotalValores());
            if(padre){
                sdiMetodos.setIncertidumbre(!(escenariosFijosP.getIncertidumbreSimulacion() != null)? "N/A" : Double.toString(redondeoXdecimal(escenariosFijosP.getIncertidumbreSimulacion(), mensurandos.getCantDecimales())));
            }else{
                Double v =Math.sqrt(escenariosFijosP.getTotalValores()!=null?escenariosFijosP.getTotalValores():0);
                Double ds = escenariosFijosP.getDesviacionEstandar()!=null?escenariosFijosP.getDesviacionEstandar():0;
                sdiMetodos.setIncertidumbre(!(escenariosFijosP.getIncertidumbreSimulacion() != null)? "N/A" : Double.toString(redondeoXdecimal((ds/v), mensurandos.getCantDecimales())));
            }
            if(sdiMetodos.getN_laboratorios()<3){
                sdiMetodos.setIncertidumbre("N/A");
            }
            if(sdiMetodos.getDesviacion_estandar().equalsIgnoreCase("N/A")){
                sdiMetodos.setSdi("N/A");
            }



            sdiMetodos.setCont(count);
            sdiMetodos.setNumero_decimales(mensurandos.getCantDecimales());
        }
        sdiMetodos.setGraph(graphMetodo(resultadosDetalladosList, padre));

        return sdiMetodos;

    }


    private Double redondeoXdecimal(Double valorIngresado, Integer cantDecimales) {
        if(valorIngresado!=null&&!valorIngresado.isNaN()&&!valorIngresado.isInfinite()){
        BigDecimal numberBigDecimal = new BigDecimal(valorIngresado, MathContext.DECIMAL64);
        numberBigDecimal = numberBigDecimal.setScale(cantDecimales, RoundingMode.DOWN);
            return numberBigDecimal.doubleValue();
        }
        return 0.0;
    }


    List<Graph> graphMetodo(List<ResultadosDetallados> resultadosDetalladosList, Boolean padre) {
        EscenariosFijos escenariosFijosP = new EscenariosFijos();
        EscenariosFijos escenariosFijosH = new EscenariosFijos();
        EscenariosResultados escenariosResultados = new EscenariosResultados();
        EscenariosResultados escenariosResultadosH = new EscenariosResultados();

        List<Graph> graphList = new ArrayList<>();
        Double sdi = 0.0;
        for (ResultadosDetallados resultadoDetallado : resultadosDetalladosList) {
            List<EscenariosResultados> escenariosResultadosList = new ArrayList<>(resultadoDetallado.getResultadosDetalladosEscenariosList());
            if (resultadoDetallado.getResultadosDetalladosEscenariosList() != null && !resultadoDetallado.getResultadosDetalladosEscenariosList().isEmpty()) {


            //nos aseguramos de tener el escenarioResultado del padre asi a su vez el escenariofijoPadre
            escenariosFijosP = escenariosResultadosList.get(resultadoDetallado.getResultadosDetalladosEscenariosList().size() - 1).getIdEscenarioFijo().getEscenariosFijosId() != null ?
                    escenariosResultadosList.get(resultadoDetallado.getResultadosDetalladosEscenariosList().size() - 1).getIdEscenarioFijo().getEscenariosFijosId() : escenariosResultadosList.get(resultadoDetallado.getResultadosDetalladosEscenariosList().size() - 1).getIdEscenarioFijo();
            EscenariosFijos finalEscenariosFijosP = escenariosFijosP;
            escenariosResultados = escenariosResultadosList.stream().filter(x -> x.getIdEscenarioFijo().getIdEscenarioFijo() == finalEscenariosFijosP.getIdEscenarioFijo()).findFirst()
                    .orElse(new EscenariosResultados());

            escenariosFijosH = escenariosFijosP.getEscenariosFijosList().stream().filter(x -> x.getEquipoId().getEquipoId() == resultadoDetallado.getEquipoId().getEquipoId()).findFirst().orElse(new EscenariosFijos());
            EscenariosFijos finalEscenariosFijosH = escenariosFijosH;
            escenariosResultadosH = escenariosResultadosList.stream().filter(x -> x.getIdEscenarioFijo().getIdEscenarioFijo() == finalEscenariosFijosH.getIdEscenarioFijo()).findFirst()
                    .orElse(new EscenariosResultados());
            sdi = padre ? escenariosResultados.getIndiceDesviacionEstandar() : escenariosResultadosH.getIndiceDesviacionEstandar();
            }

            Graph gr = new Graph();
            gr.setSerie("");
            if (sdi != null && !(sdi == 0)) {
                //Gestion de mayores
                if (sdi <= 1) {
                    gr.setSerie("VERDE");
                }
                if (sdi > 1 && sdi <= 2) {
                    gr.setSerie("AMARILLO");
                }
                if (sdi < -1 && sdi >= -2) {
                    gr.setSerie("AMARILLO");
                }
                if (sdi > 2 && sdi <= 3) {
                    gr.setSerie("ROJO");
                }
                if (sdi < -2 && sdi >= -3) {
                    gr.setSerie("ROJO");
                }
                if (sdi > 3) {
                    gr.setSerie("HIGH");
                    sdi = (3.5);
                }
                if (sdi < -3) {
                    gr.setSerie("HIGH");
                    sdi = (-3.1);
                }

            } else {
                try{
                    if (sdi==null) {
                        gr.setSerie("NODATA");
                        sdi = (-99999.00);
                    }else if(sdi==0){
                        gr.setSerie("NODATA");
                        sdi =(-99999.00);
                    }
                }catch (Exception e){
                    gr.setSerie("NODATA");
                    sdi=(-99999.00);
                }

            }
            gr.setSdi_graph(sdi);
            if(gr.getSdi_graph()!=null){
                gr.setNumber(Integer.toString(resultadoDetallado.getIdResultados().getIdInscripcionMuestras().getIdMuestras().getNumeroMuestra()));
            }else {
                gr.setNumber("");
            }
            if(resultadoDetallado.getResultadosDetalladosEscenariosList()!=null&&!resultadoDetallado.getResultadosDetalladosEscenariosList().isEmpty()){
                graphList.add(gr);
            }

        }


        return graphList;
    }
}

