package com.proasecal.software.controlexterno.controller.resultadosParticipante;

import com.proasecal.software.controlexterno.entity.*;
import com.proasecal.software.controlexterno.service.AuditoriaResultadosDirectorService;
import com.proasecal.software.controlexterno.service.AuditoriaResultadosParticipanteService;
import com.proasecal.software.controlexterno.service.ResultadosDetalladosService;
import com.proasecal.software.controlexterno.service.ResultadosService;
import com.proasecal.software.web.entity.administrar.*;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import com.proasecal.software.web.entity.seguridad.AuditoriaControlExterno;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import com.proasecal.software.web.service.administrar.EquipoService;
import com.proasecal.software.web.service.administrar.MensurandoService;
import com.proasecal.software.web.service.administrar.MuestraService;
import com.proasecal.software.web.service.administrar.ReactivoService;
import com.proasecal.software.web.service.inscripcion.InscripcionMuestrasService;
import com.proasecal.software.web.service.seguridad.AuditoriaControlExternoService;
import com.proasecal.software.web.service.seguridad.UsuarioService;
import com.proasecal.software.web.service.seguridad.UsuariosLabSedesService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

@Controller
@RequestMapping(value = "/controlexterno")
public class ResultadosParticipanteController {

    @Autowired
    ResultadosService resultadosService;
    @Autowired
    ResultadosDetalladosService resultadosDetalladosService;
    @Autowired
    MensurandoService mensurandoService;
    @Autowired
    ReactivoService reactivoService;
    @Autowired
    EquipoService equipoService;
    @Autowired
    MuestraService muestraService;
    @Autowired
    AuditoriaResultadosParticipanteService auditoriaResultadosParticipanteService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    AuditoriaResultadosDirectorService auditoriaResultadosDirectorService;
    @Autowired
    InscripcionMuestrasService inscripcionMuestrasService;
    @Autowired
    UsuariosLabSedesService usuariosLabSedesService;
    @Autowired
    AuditoriaControlExternoService auditoriaControlExternoService;

    //Atributo que almacena la muestra para ser usada por la tabla auditoria
    private static long muestraId = 0l;
    private final Logger LOG = LoggerFactory.getLogger(ResultadosParticipanteController.class);


    @RequestMapping(value = "/resultadosParticipante/{inscripcionMuestraId}", method = RequestMethod.GET)
    public ModelAndView resultadosParticipanteGet(@PathVariable long inscripcionMuestraId) {
        ModelAndView modelAndView = new ModelAndView("controlexterno/resultados-participante/resultadosParticipante");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<InscripcionMuestras> inscripcionMuestrasp = this.inscripcionMuestrasService.find(inscripcionMuestraId);
        if (!inscripcionMuestrasp.isPresent()) {
            if (!authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_PARTICIPANTE"))) {
                return new ModelAndView("redirect:/controlexterno/listResultadosDirector");
            } else {
                return new ModelAndView("redirect:/controlexterno/index");
            }
        }
        //Primero registrar en tabla de auditoria
        Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
        Usuarios usr = (Usuarios) auth.get().getPrincipal();

        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_PARTICIPANTE"))) {
            Usuarios usr2 = this.usuarioService.findByCodProasecal(String.valueOf(usr.getCodProasecal()));
            UsuariosLabSedes usuvalid = this.usuariosLabSedesService.buscarXUsuario(usr2.getIdUsuario());

            Optional<InscripcionMuestras> inscripcionMuestraspp = this.inscripcionMuestrasService.findByUsuario(usuvalid, inscripcionMuestraId);
            if (!inscripcionMuestraspp.isPresent()) {
                return new ModelAndView("redirect:/controlexterno/index");
            }
        }

        InscripcionMuestras inscripcionMuestras = inscripcionMuestrasService.findById(inscripcionMuestraId);

        this.muestraId = inscripcionMuestras.getIdMuestras().getMuestraId();

        List<Mensurandos> mensurandosList = mensurandoService.listHabilitado(inscripcionMuestras.getInscripProgramaId().getProgramaId().getProgramaId());
        List<Reactivos> reactivosList = reactivoService.listHabilitado(inscripcionMuestras.getInscripProgramaId().getProgramaId().getProgramaId());
        List<Equipos> equiposList = equipoService.listHabilitado(inscripcionMuestras.getInscripProgramaId().getProgramaId().getProgramaId());


        Resultados resultados = resultadosService.findByIdInscripcionMuestras(inscripcionMuestras).orElse(new Resultados());


        //Validacion de ultima muestra registrada por este usuario cuando la misma esta nueva
        if (!(resultados.getIdResultados() != null && resultados.getIdResultados() > 0)) {
            Resultados rt = new Resultados();
            try {
                if (!authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_PARTICIPANTE"))) {
                    rt = resultadosService.findLastResultByUser(usuarioService.findByUserName(inscripcionMuestras.getIdUsuarios().getUsuarioId().getNombreUsuario()));
                } else {
                    rt = resultadosService.findLastResultByUser(usuarioService.findByUserName(usr.getNombreUsuario()));
                }
            } catch (Exception e) {
            }

            if (rt != null) {
                List<ResultadosDetallados> rdTemp = new ArrayList<>();
                for (ResultadosDetallados rd : rt.getResultadosDetalladosList()) {
                    ResultadosDetallados objOrg = new ResultadosDetallados();
                    objOrg.setMensurandosId(rd.getMensurandosId());
                    objOrg.setMetodoId(rd.getMetodoId());
                    objOrg.setReactivoId(rd.getReactivoId());
                    objOrg.setEquipoId(rd.getEquipoId());
                    //objOrg.setValorReportado(rd.getValorReportado());
                    rdTemp.add(objOrg);
                }
                resultados.setResultadosDetalladosList(rdTemp != null && !rdTemp.isEmpty() ? rdTemp : new ArrayList<>());
            }

        }

        if (!authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_PARTICIPANTE"))) {

            String autorizadoPor = usr.getNombres().concat(' ' + usr.getApellidos());
            modelAndView.addObject("autorizadoPor", autorizadoPor);
        }
        ResultadosDetalladosDao resultadosDetalladosDao = new ResultadosDetalladosDao();
        resultadosDetalladosDao.setResultadosDetalladosList(new ArrayList<>());
        resultadosDetalladosDao.setResultados(resultados);
        resultadosDetalladosDao.getResultados().setIdInscripcionMuestras(inscripcionMuestras);
        for (Mensurandos mensurandos : mensurandosList) {
            ResultadosDetallados resultadosDetallados = new ResultadosDetallados();
            if (resultados.getResultadosDetalladosList() != null && !(resultados.getResultadosDetalladosList().isEmpty())) {
                resultadosDetallados = resultados.getResultadosDetalladosList()
                        .stream().parallel()
                        .filter(x -> x.getMensurandosId().getMensurandosId() == mensurandos.getMensurandosId()).findAny().orElse(resultadosDetallados);
            }
            resultadosDetallados.setMensurandosId(mensurandos);
            resultadosDetalladosDao.getResultadosDetalladosList().add(resultadosDetallados);

        }

        Muestras muestras = muestraService.find(this.muestraId).orElse(new Muestras());
        //Atributo que almacena si usuario posee resultados registrados


        //Validamos si usuario puede editar los campos del formulario
        Boolean desactivarFormulario = false; //Formulario habilitado hasta que se demuestre lo contrario
        boolean rol = true;

        if (authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ANONYMOUS"))) {

        } else if (authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_PARTICIPANTE"))) {
            if (rol && muestras.getFechaCierre() != null && new Date().after(muestras.getFechaCierre())) {
                if (resultados.getIdResultados() != null)
                    desactivarFormulario = true;
                	/*else if(muestras.getFechaLibResultado()!=null&&muestras.getFechaLibResultado().after(new Date()))		
                			desactivarFormulario = true; 	*/
            }
            if (resultados.getFechaModificacion() != null) {
                desactivarFormulario = true;
            }
            if (rol && muestras.getFechaLibResultado() != null && new Date().after(muestras.getFechaLibResultado())) {
                desactivarFormulario = true;
            }
        } else {

            if (rol && muestras.getFechaLibResultado() != null && new Date().after(muestras.getFechaLibResultado())) {
                desactivarFormulario = true;
            }
        }


        resultadosDetalladosDao.getResultados().setJustificacion("");
        modelAndView.addObject("disabled", desactivarFormulario);
        //fin validacion
        modelAndView.addObject("inscripcionMuestras", inscripcionMuestras);
        modelAndView.addObject("muestras", muestras);
        modelAndView.addObject("mensurandosList", mensurandosList);
        modelAndView.addObject("reactivosList", reactivosList);
        modelAndView.addObject("equiposList", equiposList);
        modelAndView.addObject("resultadosParticipanteForm", resultadosDetalladosDao);
        return modelAndView;
    }


    @RequestMapping(value = "/resultadosParticipante", method = RequestMethod.POST)
    public ModelAndView resultadosParticipantePost(@Valid @ModelAttribute("resultadosParticipanteForm") ResultadosDetalladosDao resultadosDetallados,
                                                   @RequestParam("autorizadoPor") Optional<String> autorizadoPor,
                                                   BindingResult bindingResult,
                                                   RedirectAttributes redirectAttributes,
                                                   ModelAndView model) {

        Optional<InscripcionMuestras> inscripcionMuestrasval = this.inscripcionMuestrasService.find(resultadosDetallados.getResultados().getIdInscripcionMuestras().getIdInscripcionMuestras());

        if (resultadosDetallados.getResultados().getIdResultados() == null && inscripcionMuestrasval.get().getResultadosList().size() > 0) {
            return new ModelAndView("redirect:/controlexterno/listResultadosDirector");
        } else {
            model.setViewName("controlexterno/resultados-participante/resultadosParticipante");
            try {
                //Primero registrar en tabla de auditoria
                Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
                Usuarios usr = (Usuarios) auth.get().getPrincipal();
                Usuarios user = usuarioService.findByUserName(usr.getNombreUsuario());
                Muestras ms = new Muestras();
                ms.setMuestraId(this.muestraId);
                AuditoriaResultadosParticipante audResPart = new AuditoriaResultadosParticipante();
                audResPart.setIdMuestras(ms);
                audResPart.setUsuarioId(user);
                auditoriaResultadosParticipanteService.save(audResPart);
                //Fin de registro tabla auditoria
                Boolean mensaje0=null;
                int i = 0;
                Boolean valorAjustado = false;
                for (ResultadosDetallados rd : resultadosDetallados.getResultadosDetalladosList()) {
                    if (rd.getValorReportado() != null && rd.getValorReportado() > 0) {
                        if (!(rd.getMetodoId() != null)) {
                            bindingResult.rejectValue("resultadosDetalladosList[" + i + "].metodoId", "error", "Este campo es obligatorio, si usted ingreso un valor de mensurando.");
                        }
                        if (!(rd.getReactivoId() != null)) {
                            bindingResult.rejectValue("resultadosDetalladosList[" + i + "].reactivoId", "error", "Este campo es obligatorio, si usted ingreso un valor de mensurando.");
                        }
                        if (!(rd.getEquipoId() != null)) {
                            bindingResult.rejectValue("resultadosDetalladosList[" + i + "].equipoId", "error", "Este campo es obligatorio, si usted ingreso un valor de mensurando.");
                        }
                        //Atributo que redondea
                        BigDecimal numberBigDecimal = new BigDecimal(rd.getValorReportado(), MathContext.DECIMAL64);
                        Mensurandos mensurandos = mensurandoService.getMensurandos(rd.getMensurandosId().getMensurandosId());
                        numberBigDecimal = numberBigDecimal.setScale(mensurandos.getCantDecimales(), RoundingMode.HALF_UP);

                        if (!valorAjustado) {
                            if (!(numberBigDecimal.doubleValue() == rd.getValorReportado())) {
                                valorAjustado = true;
                            }
                        }
                        rd.setValorReportado(numberBigDecimal.doubleValue());

                    } else {
                        Boolean valorInformado = false;
                        if (rd.getMetodoId() != null) {
                            valorInformado = true;
                        } else if (rd.getReactivoId() != null) {
                            valorInformado = true;
                        } else if (rd.getEquipoId() != null) {
                            valorInformado = true;
                        }
                        if (valorInformado) {
                            bindingResult.rejectValue("resultadosDetalladosList[" + i + "].valorReportado", "error", "Error");
                        } else if (rd.getValorReportado() != null && rd.getValorReportado() == 0) {
                            bindingResult.rejectValue("resultadosDetalladosList[" + i + "].valorReportado", "error", "Error");
                        }
                        if (rd.getValorReportado() != null && rd.getValorReportado() ==0) {
                            mensaje0=true;
                        }

                    }

                    i++;
                }
                if (bindingResult.hasErrors()) {
                    Muestras muestras = muestraService.find(this.muestraId).orElse(new Muestras());
                    InscripcionMuestras inscripcionMuestras = inscripcionMuestrasService.findById(resultadosDetallados.getResultados().getIdInscripcionMuestras().getIdInscripcionMuestras());
                    List<Mensurandos> mensurandosList = mensurandoService.listHabilitado(inscripcionMuestras.getInscripProgramaId().getProgramaId().getProgramaId());
                    List<Reactivos> reactivosList = reactivoService.listHabilitado(inscripcionMuestras.getInscripProgramaId().getProgramaId().getProgramaId());
                    List<Equipos> equiposList = equipoService.listHabilitado(inscripcionMuestras.getInscripProgramaId().getProgramaId().getProgramaId());
                    ResultadosDetallados resultadosDetalladosObj = new ResultadosDetallados();
                    ResultadosDetalladosDao resultadosDetalladosDao = new ResultadosDetalladosDao();
                    resultadosDetalladosDao.setResultadosDetalladosList(new ArrayList<>());
                    int j = 0;
                    for (Mensurandos mensurandos : mensurandosList) {
                        resultadosDetalladosObj.setMensurandosId(mensurandos);
                        resultadosDetalladosDao.getResultadosDetalladosList().add(resultadosDetalladosObj);
                        j++;
                    }

                    //Atributo que almacena si usuario posee resultados registrados
                    model.addObject("disabled", false);
                    model.addObject("mensaje0", mensaje0);
                    model.addObject("inscripcionMuestras", inscripcionMuestras);
                    model.addObject("muestras", muestras);
                    model.addObject("mensurandosList", mensurandosList);
                    model.addObject("reactivosList", reactivosList);
                    model.addObject("equiposList", equiposList);
                    model.addObject(resultadosDetallados);
                    model.addObject("errorFormulario", true);
                    model.addObject("autorizadoPor", autorizadoPor.get());
                    return model;
                }
                //flujo correcto
                List<ResultadosDetallados> listaEliminar = new ArrayList<>();
                resultadosDetallados.getResultados().setIdUsuarios(user);
                //se eliminan atributos al guardar para no cargar datos irrelevantes a BD
                for (ResultadosDetallados rd : resultadosDetallados.getResultadosDetalladosList()) {
                    if (rd.getValorReportado() == null || !(rd.getValorReportado() > 0)) {
                        listaEliminar.add(rd);
                    } else {
                        rd.setIdResultados(resultadosDetallados.getResultados());
                    }
                }
                resultadosDetallados.getResultadosDetalladosList().removeAll(listaEliminar);
                if (!resultadosDetallados.getResultadosDetalladosList().isEmpty()) {
                    //validamos que no se este editando

                    Resultados resul = new Resultados();
                    //Guardamos usuario editor
                    if (resultadosDetallados.getResultados().getIdResultados() != null && resultadosDetallados.getResultados().getIdResultados() > 0) {

                        Resultados rtm = resultadosService.findByIdInscripcionMuestras(resultadosDetallados.getResultados().getIdInscripcionMuestras()).orElse(resultadosDetallados.getResultados());
                        resul = rtm;
                        resul.setObservaciones(resultadosDetallados.getResultados().getObservaciones());
                        resul.setFechaModificacion(new Date());
                        resul.setIdUsuarioModificacion(user);
                        resultadosDetallados.getResultados().setIdUsuarioModificacion(user);
                        resultadosDetallados.getResultados().setFechaModificacion(new Date());
                        //Inicio tabla auditoria Director
                        AuditoriaResultadosDirector auditoriaDirector = new AuditoriaResultadosDirector();
                        auditoriaDirector.setIdMuestras(ms);
                        auditoriaDirector.setUsuarioId(rtm.getIdInscripcionMuestras().getIdUsuarios().getUsuarioId());
                        auditoriaDirector.setTipoModificacionEliminacion('U');
                        auditoriaDirector.setFechaCreacionResultado(rtm.getFechaCreacion());
                        auditoriaDirector.setUsuarioUSedeCreacion(rtm.getIdUsuarios());//rtm.getIdUsuarios().getNombreUsuario()
                        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                        if (authentication.getAuthorities().stream()
                                .anyMatch(r -> r.getAuthority().equals("ROLE_PARTICIPANTE"))) {
                            auditoriaDirector.setAprovadoPor(user.getNombreUsuario());
                        } else {
                            auditoriaDirector.setAprovadoPor(autorizadoPor.get());
                        }

                        auditoriaDirector.setJustificacion(resultadosDetallados.getResultados().getJustificacion());
                        auditoriaResultadosDirectorService.save(auditoriaDirector);
                        //AuditoriaControlExterno
                        AuditoriaControlExterno ace = new AuditoriaControlExterno();
                        Optional<Muestras> muestras = this.muestraService.find(ms.getMuestraId());
                        ace.setPrograma(muestras.get().getIdPrograma().getAbreviatura());
                        ace.setIdMuestras(muestras.get());
                        ace.setAccion("Modificación resultado");
                        ace.setJustificacion(resultadosDetallados.getResultados().getJustificacion());
                        ace.setResponsable(auditoriaDirector.getAprovadoPor());
                        ace.setIdUsuario(user);
                        ace.setUsuarioResultado(resul.getIdInscripcionMuestras().getIdUsuarios().getUsuarioId().getNombreUsuario());
                        auditoriaControlExternoService.save(ace);

                    } else {
                        resul = resultadosDetallados.getResultados();
                        ms = muestraService.find(ms.getMuestraId()).get();
                        try {
                            if (ms.getFechaCierre() != null && new Date().after(ms.getFechaCierre())) {
                                resultadosDetallados.getResultados().setResultadoFecha(true);
                            } else {
                                resultadosDetallados.getResultados().setResultadoFecha(false);
                            }
                        } catch (Exception e) {
                            resultadosDetallados.getResultados().setResultadoFecha(false);
                        }

                        //Auditoria control externo
                        AuditoriaControlExterno ace = new AuditoriaControlExterno();
                        Optional<Muestras> muestras2 = this.muestraService.find(ms.getMuestraId());
                        ace.setPrograma(muestras2.get().getIdPrograma().getAbreviatura());
                        ace.setIdMuestras(muestras2.get());
                        ace.setAccion("Ingreso resultado");
                        ace.setIdUsuario(user);
                        Optional<InscripcionMuestras> inscripcionMuestras2 = this.inscripcionMuestrasService.find(resul.getIdInscripcionMuestras().getIdInscripcionMuestras());
                        ace.setUsuarioResultado(inscripcionMuestras2.get().getIdUsuarios().getUsuarioId().getNombreUsuario());
                        auditoriaControlExternoService.save(ace);

                    }
          /*Aqui va la validacion de los fuera de fecha

            if(resultados.getIdResultados()>0 &&ms.getFechaFinal() ){

            }*/

                    /**
                     * Solucion a error que no se eliminan resultados delete if exist
                     */
                    try {
                        listaEliminar.removeIf(t -> !(t.getIdResultadosDetallados() != null));
                        resultadosDetalladosService.deleteIfExist(listaEliminar);
                    } catch (Exception e) {
                        LOG.error("error al eliminar los resultados ya existentes");
                    }


                    resultadosService.save(resul);
                    resultadosDetalladosService.saveAll(resultadosDetallados.getResultadosDetalladosList());
                }
                //TODO: Metodo de guardar validar que no se encuentre fuera de la fecha de cierre antes de realizar el guardado
                //Si esta editando no puede guardar fuera de fecha
                Optional<Muestras> muestraenviar = this.muestraService.find(Long.valueOf(ms.getMuestraId()));

                redirectAttributes.addAttribute("save", true);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication.getAuthorities().stream()
                        .anyMatch(r -> r.getAuthority().equals("ROLE_ANONYMOUS"))) {
                    redirectAttributes.addAttribute("ano", Integer.valueOf(String.valueOf(muestraenviar.get().getFechaInicial()).substring(0, 4)));
                    redirectAttributes.addAttribute("muestra", ms.getMuestraId());
                    redirectAttributes.addAttribute("laboratorio", "");
                    redirectAttributes.addAttribute("sede", "");
                    redirectAttributes.addAttribute("usuario", "");
                    redirectAttributes.addAttribute("estado", "Todos");
                    return new ModelAndView("redirect:/controlexterno/listResultadosDirector");
                } else if (authentication.getAuthorities().stream()
                        .anyMatch(r -> r.getAuthority().equals("ROLE_PARTICIPANTE"))) {
                    return new ModelAndView("redirect:/controlexterno/index");
                }
                redirectAttributes.addAttribute("ano", Integer.valueOf(String.valueOf(muestraenviar.get().getFechaInicial()).substring(0, 4)));
                redirectAttributes.addAttribute("muestra", ms.getMuestraId());
                redirectAttributes.addAttribute("laboratorio", "");
                redirectAttributes.addAttribute("sede", "");
                redirectAttributes.addAttribute("usuario", "");
                redirectAttributes.addAttribute("estado", "Todos");
                return new ModelAndView("redirect:/controlexterno/listResultadosDirector");

            } catch (Exception e) {
                e.printStackTrace();
                Muestras muestras = muestraService.find(this.muestraId).orElse(new Muestras());
                //TODO esto deberia de colocarse el usuario logueado
                InscripcionMuestras inscripcionMuestras = inscripcionMuestrasService.findById(resultadosDetallados.getResultados().getIdInscripcionMuestras().getIdInscripcionMuestras());


                List<Mensurandos> mensurandosList = mensurandoService.listHabilitado(inscripcionMuestras.getInscripProgramaId().getProgramaId().getProgramaId());
                List<Reactivos> reactivosList = reactivoService.listHabilitado(inscripcionMuestras.getInscripProgramaId().getProgramaId().getProgramaId());
                List<Equipos> equiposList = equipoService.listHabilitado(inscripcionMuestras.getInscripProgramaId().getProgramaId().getProgramaId());
                ResultadosDetallados resultadosDetalladosObj = new ResultadosDetallados();
                ResultadosDetalladosDao resultadosDetalladosDao = new ResultadosDetalladosDao();
                resultadosDetalladosDao.setResultadosDetalladosList(new ArrayList<>());
                resultadosDetalladosDao.setResultadosDetalladosList(new ArrayList<>());
                int j = 0;
                for (Mensurandos mensurandos : mensurandosList) {
                    resultadosDetalladosObj.setMensurandosId(mensurandos);
                    resultadosDetalladosDao.getResultadosDetalladosList().add(resultadosDetalladosObj);
                    j++;
                }

                //Atributo que almacena si usuario posee resultados registrados
                model.addObject("disabled", false);
                model.addObject("inscripcionMuestras", inscripcionMuestras);
                model.addObject("muestras", muestras);
                model.addObject("mensurandosList", mensurandosList);
                model.addObject("reactivosList", reactivosList);
                model.addObject("equiposList", equiposList);
                model.addObject("error", true);
                model.addObject(resultadosDetallados);
                model.addObject("autorizadoPor", autorizadoPor.orElse(""));
                return model;
            }
        }
    }
}

