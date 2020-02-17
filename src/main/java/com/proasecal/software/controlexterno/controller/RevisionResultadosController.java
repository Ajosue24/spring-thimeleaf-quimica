package com.proasecal.software.controlexterno.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.proasecal.software.controlexterno.controller.json.ReporteController;
import com.proasecal.software.controlexterno.entity.*;
import com.proasecal.software.controlexterno.entity.DAO.Observacion;
import com.proasecal.software.controlexterno.entity.documentacionjson.ObjetoJson;
import com.proasecal.software.controlexterno.service.*;
import com.proasecal.software.web.entity.administrar.Laboratorios;
import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.administrar.Sedes;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import com.proasecal.software.web.entity.seguridad.AuditoriaControlExterno;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import com.proasecal.software.web.service.administrar.LaboratorioService;
import com.proasecal.software.web.service.administrar.MuestraService;
import com.proasecal.software.web.service.administrar.SedesService;
import com.proasecal.software.web.service.inscripcion.InscripcionMuestrasService;
import com.proasecal.software.web.service.seguridad.AuditoriaControlExternoService;
import com.proasecal.software.web.service.seguridad.UsuarioService;
import com.proasecal.software.web.service.seguridad.UsuariosLabSedesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "controlexterno/revisionResultados")
public class RevisionResultadosController {

    @Autowired
    MuestraService muestraService;
    @Autowired
    LaboratorioService laboratorioService;
    @Autowired
    SedesService sedesService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    InscripcionMuestrasService inscripcionMuestrasService;
    @Autowired
    ObservacionMuestraService observacionMuestraService;
    @Autowired
    TipoObservacionService tipoObservacionService;
    @Autowired
    InformesService informesService;
    @Autowired
    ProcesoService procesoService;
    @Autowired
    ObservacionResultadoService observacionResultadoService;
    @Autowired
    UsuariosLabSedesService usuariosLabSedesService;
    @Autowired
    ResultadosService resultadosService;
    @Autowired
    AuditoriaControlExternoService auditoriaControlExternoService;
    @Autowired
    ReporteController reporteController;
    @Autowired
    EscenariosFijosService escenariosFijosService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView revisionResultados(
            @PathVariable("id") String id,
            @RequestParam("laboratorio") Optional<String> laboratorioO,
            @RequestParam("sede") Optional<String> sedeO,
            @RequestParam("usuario") Optional<String> usuarioO,
            @RequestParam("resultados") Optional<String> resultadosO,
            @RequestParam("proceso") Optional<String> procesoO) {

        Muestras muestras;
        ModelAndView modelAndView;
        String laboratorio, sede, usuario, resultado, proceso;
        laboratorio = laboratorioO.isPresent() ? laboratorioO.get() : "";
        sede = sedeO.isPresent() ? sedeO.get() : "";
        usuario = usuarioO.isPresent() ? usuarioO.get() : "";
        resultado = resultadosO.isPresent() ? resultadosO.get() : "";
        proceso = procesoO.isPresent() ? procesoO.get() : "";
        Boolean boton = true, observacionReadOnly = true, validadBoton = true;

        try {
            muestras = muestraService.find(Long.valueOf(id)).get();
            modelAndView = new ModelAndView("controlexterno/revisionResultados/revisionResultados");

        } catch (Exception e) {
            return new ModelAndView("redirect:/controlexterno/listResultadosDirector");
        }

        /*Queries para el carge de los selects de los filtros con base a la muestra seleccionada*/
        List<Laboratorios> modelsLab = laboratorioService.obtLabxMuestra(muestras.getMuestraId().toString());
        List<Sedes> modelsSede = sedesService.obtSedexMuestra(muestras.getMuestraId().toString());
        List<Usuarios> modelsUsu = usuarioService.obtUsuxMuestra(muestras.getMuestraId().toString());


        /*Aplicacion de Query para el filtro*/
        List<InscripcionMuestras> modelsListUsuarios = new ArrayList<>();
        if (resultado == "" && proceso == "") {
            modelsListUsuarios = inscripcionMuestrasService.revisionResultados
                    (muestras.getMuestraId().toString(), laboratorio, sede, usuario);
        }

        if (resultado == "" && proceso != "") {
            modelsListUsuarios = inscripcionMuestrasService.revisionResultadosProceso
                    (muestras.getMuestraId().toString(), laboratorio, sede, usuario, proceso);
        }

        if (resultado != "" && proceso == "") {
            if (resultado.equalsIgnoreCase("no")) {
                modelsListUsuarios = resultadosNoInformados
                        (inscripcionMuestrasService.revisionResultados
                                (muestras.getMuestraId().toString(), laboratorio, sede, usuario));
            } else {
                modelsListUsuarios = inscripcionMuestrasService.revisionResultadosResultado
                        (muestras.getMuestraId().toString(), laboratorio, sede, usuario, resultado);
            }
        }

        if (resultado != "" && proceso != "") {
            if (resultado.equalsIgnoreCase("no")) {
                modelsListUsuarios = resultadosNoInformados
                        (inscripcionMuestrasService.revisionResultadosProceso
                                (muestras.getMuestraId().toString(), laboratorio, sede, usuario, proceso));
            } else {
                modelsListUsuarios = inscripcionMuestrasService.revisionResultadosFull
                        (muestras.getMuestraId().toString(), laboratorio, sede, usuario, resultado, proceso);
            }
        }


        /*Habilitar botón generar versiónes definitivas*/
        List<InscripcionMuestras> inscripcionMuestras = inscripcionMuestrasService.revisionResultadosEnFecha(muestras.getMuestraId().toString());
        for (InscripcionMuestras insc : inscripcionMuestras) {
            Resultados resultados = resultadosService.findLastByInscripcionMuestras(insc);

            boolean a = resultados == null; //false
            boolean b = !a && !resultados.getInformesList().isEmpty(); //true
            boolean c = b && resultados.getInformesList().get(resultados.getInformesList().size() - 1).isRevisionDirector(); //true
            boolean d = b && resultados.getInformesList().get(resultados.getInformesList().size() - 1).isRevisionRevisor(); //true
            boolean e = b && resultados.getInformesList().get(resultados.getInformesList().size() - 1).isEsVersion();

            if ((!c || !d) && validadBoton) {
                boton = false;
                validadBoton = false;
            }

            observacionReadOnly = observacionReadOnly && e;
        }


        /*Observaciones*/
        List<TipoObservacion> tipoObservacion = muestras.getIdPrograma().getTipoProgramaId().getTipoObservacionList().stream()
                .sorted(Comparator.comparing(TipoObservacion::getIdtipoObservacion)).collect(Collectors.toList());

        List<Observacion> observacionList = new ArrayList<>();

        for (TipoObservacion tipoObs : tipoObservacion) {
            Optional<ObservacionMuestra> observacionOptional = observacionMuestraService.encontrarTipoMuestra(muestras, tipoObs);

            Observacion obs = new Observacion(
                    tipoObs.getIdtipoObservacion(),
                    tipoObs.getNombre(),
                    observacionOptional.isPresent() ? observacionOptional.get().getObservacion() : ""
            );
            observacionList.add(obs);
        }


        /*Ordenamiento de lista por resultado y usuario*/
        List<InscripcionMuestras> stream = modelsListUsuarios.stream().sorted((o2, o1) -> {
            Resultados ro1 = resultadosService.findLastByInscripcionMuestras(o1);
            Resultados ro2 = resultadosService.findLastByInscripcionMuestras(o2);

            boolean a = ro1 != null;
            boolean b = ro2 != null;
            boolean c = a ? ro1.getResultadoFecha() : false;
            boolean d = b ? ro2.getResultadoFecha() : false;
            int r = (!a && !b) ? 0 : ((a == b && c == d) ? 0 : ((!b || (a && !c) || (a && d)) ? 1 : -1));
            return r;
        }).collect(Collectors.toList());


        /*Validar mensaje*/
        Boolean mensaje = false;
        if (muestras.getProcesoList().size() > 0) {
            for (EscenariosFijos e : muestras.getEscenariosFijosList()) {
                if (e.getFechaCreacion().after(muestras.getProcesoList().get(muestras.getProcesoList().size() - 1).getFechaProceso())) {
                    mensaje = true;
                    break;
                }
            }
        }


        /*Cargue del front*/
        modelAndView.addObject("muestraInfo", muestras);
        modelAndView.addObject("modelsLab", modelsLab);
        modelAndView.addObject("modelsSede", modelsSede);
        modelAndView.addObject("modelsUsu", modelsUsu);
        modelAndView.addObject("boton", boton);
        modelAndView.addObject("observaciones", observacionList);
        modelAndView.addObject("observacionReadOnly", observacionReadOnly);
        modelAndView.addObject("modelsListUsuarios", stream);
        modelAndView.addObject("mensaje", mensaje);

        /*Devolver los valores del filtro al front*/
        modelAndView.addObject("laboratorio", laboratorioO.orElse("").equalsIgnoreCase("_") ? "" : laboratorioO.orElse(""));
        modelAndView.addObject("sede", sedeO.orElse("").equalsIgnoreCase("_") ? "" : sedeO.orElse(""));
        modelAndView.addObject("usuario", usuarioO.orElse("").equalsIgnoreCase("_") ? "" : usuarioO.orElse(""));
        modelAndView.addObject("resultado", resultadosO.orElse("").equalsIgnoreCase("_") ? "" : resultadosO.orElse(""));
        modelAndView.addObject("proceso", procesoO.orElse("").equalsIgnoreCase("_") ? "" : procesoO.orElse(""));

        return modelAndView;
    }

    /*Filtrar resultados no informados*/
    private List<InscripcionMuestras> resultadosNoInformados(List<InscripcionMuestras> List) {
        List<InscripcionMuestras> noInformados = new ArrayList();
        for (InscripcionMuestras insMuestra : List) {
            if (insMuestra.getResultadosList().isEmpty()) {
                noInformados.add(insMuestra);
            }
        }

        return noInformados;
    }


    /*Guardado de observaciones generales */
    @RequestMapping(value = "/observacion", method = RequestMethod.POST)
    public ResponseEntity guardarObservacion(String idTipo, String idMuestra, String observacion) {

        Muestras muestras = muestraService.find(Long.valueOf(idMuestra)).get();
        TipoObservacion tipoObservacion = tipoObservacionService.encontrarId(Long.valueOf(idTipo)).get();
        Optional<ObservacionMuestra> observacionOptional = observacionMuestraService.encontrarTipoMuestra(muestras, tipoObservacion);
        ObservacionMuestra observacionMuestra;

        try {
            AuditoriaControlExterno ace = new AuditoriaControlExterno();
            if (observacionOptional.isPresent()) {
                observacionMuestra = observacionOptional.get();
                observacionMuestra.setObservacion(observacion);
                ace.setAccion("Modificar observación general");
            } else {
                observacionMuestra = new ObservacionMuestra(muestras, tipoObservacion, observacion);
                ace.setAccion("Crear observación general");
            }

            observacionMuestraService.save(observacionMuestra);

            /*Generar informe*/
            List<InscripcionMuestras> inscripcionMuestras = inscripcionMuestrasService.revisionResultadosEnFecha(muestras.getMuestraId().toString());
            List<Informes> informesList = new ArrayList<>();
            for (InscripcionMuestras insc : inscripcionMuestras) {
                try {
                    Informes info = insc.getResultadosList().get(0).getInformesList().get(insc.getResultadosList().get(0).getInformesList().size() - 1);
                    info.setInformeJson(generarInformeJson(insc, false));
                    informesList.add(info);
                } catch (Exception e) {
                    System.out.println("Falla en el informe");
                }
            }
            informesService.saveAll(informesList);
            /*fin */
            //AuditoriaControlExterno
            ace.setPrograma(muestras.getIdPrograma().getAbreviatura());
            ace.setIdMuestras(muestras);
            Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
            Usuarios usr = (Usuarios) auth.get().getPrincipal();
            Usuarios user = usuarioService.findByUserName(usr.getNombreUsuario());
            ace.setIdUsuario(user);
            auditoriaControlExternoService.save(ace);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    /*Generación de informes en fecha*/
    @RequestMapping(value = "/generarInformes/{id}")
    public ResponseEntity generarInformes(
            @PathVariable("id") String id) {

        try {
            Muestras muestras = muestraService.find(Long.valueOf(id)).get();

            List<EscenariosFijos> escenariosFijosList = new ArrayList<>(muestras.getEscenariosFijosList());
            Optional<Proceso> proceso = procesoService.muestraVersionGenerada(muestras);


            if (proceso.isPresent()) {
                List<Resultados> resultadosList = new ArrayList<>();
                for (EscenariosFijos escenariosFijos : escenariosFijosList) {
                    if (escenariosFijos.getFechaCreacion().after(proceso.get().getFechaProceso())) {
                        resultadosList.addAll(resultadosService.resultadosMuestraMensurandoMetodo(
                                escenariosFijos.getIdMuestras().getMuestraId().toString(),
                                escenariosFijos.getIdMensurandos().getMensurandosId().toString(),
                                escenariosFijos.getMetodoId().getMetodoId().toString()));
                    }
                }


                Set<Resultados> set = new HashSet<>();
                set.addAll(resultadosList);
                resultadosList = new ArrayList<>();
                resultadosList.addAll(set);

                List<Informes> informesList = new ArrayList<>();
                for (Resultados resultados : resultadosList) {
                    Informes info = new Informes(resultados, false, 1);
                    info.setInformeJson(generarInformeJson(resultados.getIdInscripcionMuestras(), true));
                    informesList.add(info);

                }
                informesService.saveAll(informesList);


            } else {

                List<InscripcionMuestras> inscripcionMuestras = inscripcionMuestrasService.revisionResultadosEnFecha(id);
                List<Informes> informesList = new ArrayList<>();

                for (InscripcionMuestras insMuestra : inscripcionMuestras) {
                    if (insMuestra.getResultadosList().get(0).getInformesList().size() == 0) {
                        Informes info = new Informes(insMuestra.getResultadosList().get(0), false, 1);
                        info.setInformeJson(generarInformeJson(insMuestra, true));
                        informesList.add(info);
                    } else {
                        Informes info = insMuestra.getResultadosList().get(0).getInformesList().get(insMuestra.getResultadosList().get(0).getInformesList().size() - 1);
                        info.setInformeJson(generarInformeJson(insMuestra, false));
                        informesList.add(info);
                    }
                }

                informesService.saveAll(informesList);
            }

            procesoService.save(new Proceso(1, muestras));

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    /*Generación de informes fuera de fecha*/
    @RequestMapping(value = "/generarInformeFueraFecha/{id}")
    public ResponseEntity generarInformesFueraFecha(
            @PathVariable("id") String id) {

        try {
            InscripcionMuestras inscripcionMuestras = inscripcionMuestrasService.find(Long.valueOf(id)).get();

            Informes info = new Informes(inscripcionMuestras.getResultadosList().get(0), false, 1);
            info.setInformeJson(generarInformeJson(inscripcionMuestras, true));
            informesService.save(info);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    /*Cargue de observacion individual*/
    @RequestMapping(value = "/obtenerObservacionIndividual/{id}")
    public ResponseEntity<String> obtenerObservacionIndividual(
            @PathVariable("id") String id) {

        String observacionIndividual = "";

        try {
            InscripcionMuestras inscripcionMuestras = inscripcionMuestrasService.find(Long.valueOf(id)).get();

            if (!inscripcionMuestras.getResultadosList().get(0).getObservacionResultadoList().isEmpty()) {
                observacionIndividual = inscripcionMuestras.getResultadosList().get(0).getObservacionResultadoList().get(0).getObservacion();
            }

            return new ResponseEntity<>(observacionIndividual, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(observacionIndividual, HttpStatus.BAD_REQUEST);
        }
    }


    /*Guardado de observacion individual*/
    @RequestMapping(value = "/guardarObservacionIndividual/{id}", method = RequestMethod.POST)
    public ResponseEntity guardarObservacionIndividual(
            @PathVariable("id") String id, String observacion) {

        ObservacionResultado observacionResultado = new ObservacionResultado();
        AuditoriaControlExterno ace = new AuditoriaControlExterno();
        try {
            InscripcionMuestras inscripcionMuestras = inscripcionMuestrasService.find(Long.valueOf(id)).get();

            if (!inscripcionMuestras.getResultadosList().get(0).getObservacionResultadoList().isEmpty()) {
                observacionResultado = inscripcionMuestras.getResultadosList().get(0).getObservacionResultadoList().get(0);
                ace.setAccion("Modificar observación individual");

            } else {
                observacionResultado.setResultadosId(inscripcionMuestras.getResultadosList().get(0));
                ace.setAccion("Crear observación individual");
            }

            observacionResultado.setObservacion(observacion);

            observacionResultadoService.guardarObservacion(observacionResultado);

            /*Registrar informe individual*/
            Informes info = inscripcionMuestras.getResultadosList().get(0).getInformesList().get(inscripcionMuestras.getResultadosList().get(0).getInformesList().size() - 1);
            info.setInformeJson(generarInformeJson(inscripcionMuestras, false));
            informesService.save(info);
            /*fin */
            //AuditoriaControlExterno
            ace.setPrograma(inscripcionMuestras.getIdMuestras().getIdPrograma().getAbreviatura());
            ace.setIdMuestras(inscripcionMuestras.getIdMuestras());
            Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
            Usuarios usr = (Usuarios) auth.get().getPrincipal();
            Usuarios user = usuarioService.findByUserName(usr.getNombreUsuario());
            ace.setIdUsuario(user);
            ace.setUsuarioResultado(inscripcionMuestras.getIdUsuarios().getUsuarioId().getNombreUsuario());
            auditoriaControlExternoService.save(ace);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    /*Generar versiónes definitivas en fecha*/
    @RequestMapping(value = "/versionFecha/{id}")
    public ResponseEntity generarVersionFecha(
            @PathVariable("id") String id) {

        try {
            Muestras muestras = muestraService.find(Long.valueOf(id)).get();
            AuditoriaControlExterno ace = new AuditoriaControlExterno();
            List<InscripcionMuestras> inscripcionMuestras = inscripcionMuestrasService.revisionResultadosEnFecha(id);

            for (InscripcionMuestras insMuestra : inscripcionMuestras) {

                // Informes informesave = informesService.informeXId(Long.valueOf(informe));

                Informes informesave = insMuestra.getResultadosList().get(0).getInformesList().get(insMuestra.getResultadosList().get(0).getInformesList().size() - 1);

                if (informesave.isEsVersion() == false) {
                    informesave.setEsVersion(true);
                    informesave.setEstadoProceso(2);
                    try {
                        informesave.setNumeroVersion(insMuestra.getResultadosList().get(0).getInformesList().get(insMuestra.getResultadosList().get(0).getInformesList().size() - 2).getNumeroVersion() + 1);
                    } catch (Exception e) {
                        informesave.setNumeroVersion(insMuestra.getResultadosList().get(0).getInformesList().get(insMuestra.getResultadosList().get(0).getInformesList().size() - 1).getNumeroVersion() + 1);
                    }
                    informesService.save(informesave);
                    ace.setNumeroVersion(informesave.getNumeroVersion());
                    procesoService.save(new Proceso(2, muestras));
                }
                escenariosFijosService.actualizarVersion(informesave.getNumeroVersion(), informesave.getResultadosId().getIdResultados().intValue());
            }

            //AuditoriaControlExterno
            ace.setPrograma(inscripcionMuestras.get(0).getIdMuestras().getIdPrograma().getAbreviatura());
            ace.setIdMuestras(inscripcionMuestras.get(0).getIdMuestras());
            Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
            Usuarios usr = (Usuarios) auth.get().getPrincipal();
            Usuarios user = usuarioService.findByUserName(usr.getNombreUsuario());
            ace.setIdUsuario(user);
            ace.setAccion("Generar versión");
            auditoriaControlExternoService.save(ace);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    /*Generación de informe Json*/
    private String generarInformeJson(InscripcionMuestras inscripcionMuestras, Boolean Accion) {

        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            return ow.writeValueAsString(reporteController.inicioJson(inscripcionMuestras, Accion).getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    @RequestMapping(value = "/actualizarEstadoRevision", method = RequestMethod.GET)
    public String enableUrl(@RequestParam("informe") String informe,
                            @RequestParam("estado") Boolean estado,
                            @RequestParam("campo") String campo) {
        estado = true;
        Informes informesave = informesService.informeXId(Long.valueOf(informe));
        Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
        Usuarios usr = (Usuarios) auth.get().getPrincipal();
        Usuarios user = usuarioService.findByUserName(usr.getNombreUsuario());
        AuditoriaControlExterno ace = new AuditoriaControlExterno();
        if (campo.equalsIgnoreCase("director")) {
            informesave.setRevisionDirector(estado);
            informesave.setFechaRevisionDirector(new Date());
            informesave.setUsuarioDirectorId(user);
            ace.setAccion("Revision por parte de director");
        } else {
            informesave.setRevisionRevisor(estado);
            informesave.setFechaRevisionRevisor(new Date());
            informesave.setUsuarioRevisorId(user);
            if (informesave.getResultadosId().getResultadoFecha()) {
                informesave.setEstadoProceso(2);
                informesave.setEsVersion(true);
                try {
                    informesave.setNumeroVersion(informesave.getResultadosId().getInformesList().get(informesave.getResultadosId().getInformesList().size() - 2).getNumeroVersion() + 1);
                } catch (Exception e) {
                    informesave.setNumeroVersion(1);
                }

            }
            ace.setAccion("Revision por parte de revisor");
        }
        try {
            this.informesService.save(informesave);
            escenariosFijosService.actualizarVersion(informesave.getNumeroVersion(), informesave.getResultadosId().getIdResultados().intValue());
            //AuditoriaControlExterno
            ace.setPrograma(informesave.getResultadosId().getIdInscripcionMuestras().getIdMuestras().getIdPrograma().getAbreviatura());
            ace.setIdMuestras(informesave.getResultadosId().getIdInscripcionMuestras().getIdMuestras());
            ace.setIdUsuario(user);
            ace.setUsuarioResultado(informesave.getResultadosId().getIdInscripcionMuestras().getIdUsuarios().getUsuarioId().getNombreUsuario());
            auditoriaControlExternoService.save(ace);
            if (!campo.equalsIgnoreCase("director")) {
                //AuditoriaControlExterno versiion
                AuditoriaControlExterno ace2 = new AuditoriaControlExterno();
                ace2.setPrograma(informesave.getResultadosId().getIdInscripcionMuestras().getIdMuestras().getIdPrograma().getAbreviatura());
                ace2.setIdMuestras(informesave.getResultadosId().getIdInscripcionMuestras().getIdMuestras());
                ace2.setIdUsuario(user);
                ace2.setAccion("Generar versión fuera de fecha");
                ace2.setNumeroVersion(informesave.getNumeroVersion());
                ace2.setUsuarioResultado(informesave.getResultadosId().getIdInscripcionMuestras().getIdUsuarios().getUsuarioId().getNombreUsuario());
                auditoriaControlExternoService.save(ace2);
            }

        } catch (Exception e) {

        }
        return "controlexterno/revisionResultados/revisionResultados :: content";
    }
}