package com.proasecal.software.controlexterno.controller;

import com.proasecal.software.controlexterno.entity.*;
import com.proasecal.software.controlexterno.service.*;
import com.proasecal.software.web.entity.administrar.Laboratorios;
import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.administrar.Sedes;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import com.proasecal.software.web.entity.parametricas.Pager;
import com.proasecal.software.web.entity.seguridad.AuditoriaControlExterno;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import com.proasecal.software.web.service.administrar.EquipoService;
import com.proasecal.software.web.service.administrar.LaboratorioService;
import com.proasecal.software.web.service.administrar.MensurandoService;
import com.proasecal.software.web.service.administrar.MuestraService;
import com.proasecal.software.web.service.administrar.ReactivoService;
import com.proasecal.software.web.service.administrar.SedesService;
import com.proasecal.software.web.service.inscripcion.InscripcionMuestrasService;
import com.proasecal.software.web.service.seguridad.AuditoriaControlExternoService;
import com.proasecal.software.web.service.seguridad.UsuarioService;
import com.proasecal.software.web.service.seguridad.UsuariosLabSedesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/controlexterno")
@SessionAttributes("test")
public class ControlExternoController {

    @Autowired
    DocRelacionadosService docRelacionadosService;
    @Autowired
    InscripcionMuestrasService inscripcionMuestrasService;
    @Autowired
    VideosService videosService;
    @Autowired
    UsuariosLabSedesService usuariosLabSedesService;
    @Autowired
    MuestraService muestraService;
    @Autowired
    LaboratorioService laboratorioService;
    @Autowired
    SedesService sedesService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ResultadosParticipanteService resultadosParticipanteService;
    @Autowired
    ResultadosService resultadosService;
    @Autowired
    MensurandoService mensurandoService;
    @Autowired
    ReactivoService reactivoService;
    @Autowired
    EquipoService equipoService;
    @Autowired
    ResultadosDetalladosService resultadosDetalladosService;
    @Autowired
    AuditoriaResultadosParticipanteService auditoriaResultadosParticipanteService;
    @Autowired
    AuditoriaResultadosDirectorService auditoriaResultadosDirectorService;
    @Autowired
    AuditoriaControlExternoService auditoriaControlExternoService;

    private static int currentPage = 1;
    private static int pagSize = 20;
    private static String sortColumn = "orden";
    private static String sortO = "DESC";
    private static String nombre = "";
    public static String nombreSede = "";


    @ModelAttribute("test")
    public void getNombreSedeTest() {

        try {
            Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
            Usuarios usr = (Usuarios) auth.get().getPrincipal();
            this.nombreSede = usuariosLabSedesService.buscarPorNombreUsuario(usr.getNombreUsuario()).getIdSedes().getNombreSede();
        } catch (Exception e) {
            this.nombreSede = "";
        }
    }

    @GetMapping("/")
    public String controlexterno() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return "controlexterno/login";
        } else if (authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_PARTICIPANTE"))) {
            return "redirect:/controlexterno/index";
        }
        return "redirect:/controlexterno/listResultadosDirector";
    }


    @PostMapping("/login")
    public ModelAndView postLogin() {
        return new ModelAndView("redirect:/index");
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "logout", required = false) String logout, @RequestParam(value = "error", required = false) String error, ModelMap modelMap) {
        ModelAndView model = new ModelAndView();
        if (logout != null) {
            model.addObject("msg", "logout correcto");
            return "controlexterno/login";
        }
        if (error != null) {
            modelMap.put("msg", "contraseña invalida");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return "controlexterno/login";
        } else if (authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_PARTICIPANTE"))) {
            return "redirect:/controlexterno/index";
        } else {
            return "redirect:/controlexterno/listResultadosDirector";
        }
    }

    private static int currentPageIndex = 1;
    private static int pagSizeIndex = 12;
    private static String sortColumnIndex = "idMuestras.fechaInicial";
    private static String desde;
    private static String hasta;
    private static String numero;
    private static final int BUTTONS_TO_SHOW = 3;

    @RequestMapping(value = "/index")
    public ModelAndView controlExternoIndex(@RequestParam("desde") Optional<String> desde,
                                            @RequestParam("hasta") Optional<String> hasta,
                                            @RequestParam("numero") Optional<String> numero,
                                            @RequestParam("sortColumnIndex") Optional<String> sortColumnIndex,
                                            @RequestParam("currentPageIndex") Optional<Integer> currentPageIndex,
                                            @RequestParam("pagSizeIndex") Optional<Integer> pagSizeIndex,
                                            @RequestParam("sortOIndex") Optional<String> sortOIndex,
                                            @RequestParam("page") Optional<Integer> page,
                                            @RequestParam("save") Optional<Boolean> save) {
        getNombreSedeTest();
        ModelAndView modelAndView = new ModelAndView("controlexterno/index");
        ControlExternoController.sortColumnIndex = sortColumnIndex.isPresent() ? sortColumnIndex.get() : "idMuestras.fechaInicial";
        sortO = sortOIndex.isPresent() ? sortOIndex.get() : "DESC";
        ControlExternoController.currentPageIndex = currentPageIndex.isPresent() ? currentPageIndex.get() : 1;
        ControlExternoController.pagSizeIndex = pagSizeIndex.isPresent() ? pagSizeIndex.get() : 12;
        ControlExternoController.desde = desde.isPresent() ? desde.get() : "";
        ControlExternoController.hasta = hasta.isPresent() ? hasta.get() : "";
        ControlExternoController.numero = numero.isPresent() ? numero.get() : "";

        int evalPageSize = pagSizeIndex.orElse(ControlExternoController.pagSizeIndex);


        Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
        Usuarios usr = (Usuarios) auth.get().getPrincipal();
        UsuariosLabSedes usuariosLabSedes;
        try {
            usuariosLabSedes = usuariosLabSedesService.buscarPorNombreUsuario(usr.getNombreUsuario());
        } catch (Exception e) {
            usuariosLabSedes = new UsuariosLabSedes();
        }

        Page<InscripcionMuestras> muestrasInscritasList = inscripcionMuestrasService.
                findByUsuarioAndFechaIncialDescConFiltro(usuariosLabSedes,
                        desde.orElse(this.desde),
                        hasta.orElse(this.hasta),
                        numero.orElse(this.numero), PageRequest.of(
                                page.orElse(currentPage) - 1, this.pagSizeIndex, new Sort(Sort.Direction.valueOf(sortO), this.sortColumnIndex)
                        ));
        Pager pager = new Pager(muestrasInscritasList.getTotalPages(), muestrasInscritasList.getNumber(), BUTTONS_TO_SHOW);

        if (save.isPresent()) {
            if (save.get()) {
                modelAndView.addObject("save", save);
            }
        }
        List<Boolean> mostrarBotonCrear = new ArrayList<>();
        for (InscripcionMuestras im : muestrasInscritasList) {
            im.getIdMuestras().getFechaInicial();
            LocalDate mesInicioInscripcionR = im.getIdMuestras().getFechaInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            mostrarBotonCrear.add(LocalDate.now().isAfter(mesInicioInscripcionR.withDayOfMonth(14).minusMonths(1)));
        }
        modelAndView.addObject("mostrarBotonCrear", mostrarBotonCrear);
        modelAndView.addObject("muestrasInscritasList", muestrasInscritasList);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("sort", sortOIndex.orElse(sortO));
        modelAndView.addObject("desde", desde.orElse("").equalsIgnoreCase("_") ? "" : desde.orElse(""));
        modelAndView.addObject("hasta", hasta.orElse("").equalsIgnoreCase("_") ? "" : hasta.orElse(""));
        modelAndView.addObject("numero", numero.orElse("").equalsIgnoreCase("_") ? "" : numero.orElse(""));

        return modelAndView;
    }

    @RequestMapping(value = "/instructivos", method = RequestMethod.GET)
    public ModelAndView instructivos(@RequestParam("nombre") Optional<String> nombre, @RequestParam("page") Optional<Integer> page) {
        ControlExternoController.nombre = nombre.isPresent() ? nombre.get() : "";

        Page<DocRelacionados> docRelacionados = docRelacionadosService.ListPaginated(ControlExternoController.nombre, 2l, PageRequest.of(
                page.orElse(currentPage) - 1, pagSize, new Sort(Sort.Direction.valueOf(sortO), sortColumn)));

        ModelAndView modelAndView = new ModelAndView("controlexterno/instructivos");
        modelAndView.addObject("instructivos", docRelacionados);
        modelAndView.addObject("nombre", nombre.orElse("").equalsIgnoreCase("_") ? "" : nombre.orElse(""));
        return modelAndView;
    }


    @RequestMapping(value = "/documentosRelacionados", method = RequestMethod.GET)
    public ModelAndView documentosRealacionados(@RequestParam("nombre") Optional<String> nombre, @RequestParam("page") Optional<Integer> page) {
        ControlExternoController.nombre = nombre.isPresent() ? nombre.get() : "";

        Page<DocRelacionados> docRelacionados = docRelacionadosService.ListPaginated(ControlExternoController.nombre, 1l, PageRequest.of(
                page.orElse(currentPage) - 1, pagSize, new Sort(Sort.Direction.valueOf(sortO), sortColumn)));

        ModelAndView modelAndView = new ModelAndView("controlexterno/documentosRelacionados");
        modelAndView.addObject("documentos", docRelacionados);
        modelAndView.addObject("nombre", nombre.orElse("").equalsIgnoreCase("_") ? "" : nombre.orElse(""));
        return modelAndView;
    }


    @RequestMapping(value = "/videosRelacionados", method = RequestMethod.GET)
    public ModelAndView videosRelacionados(@RequestParam("nombre") Optional<String> nombre, @RequestParam("page") Optional<Integer> page) {
        ControlExternoController.nombre = nombre.isPresent() ? nombre.get() : "";

        Page<Videos> videos = videosService.ListPaginated(ControlExternoController.nombre, PageRequest.of(
                page.orElse(currentPage) - 1, pagSize, new Sort(Sort.Direction.valueOf(sortO), sortColumn)));

        ModelAndView modelAndView = new ModelAndView("controlexterno/videosRelacionados");
        modelAndView.addObject("videos", videos);
        modelAndView.addObject("nombre", nombre.orElse("").equalsIgnoreCase("_") ? "" : nombre.orElse(""));
        return modelAndView;
    }

    @RequestMapping(value = "/videosRelacionados/{id}", method = RequestMethod.GET)
    public ModelAndView videosRelacionados(@PathVariable("id") String id) {
        Optional<Videos> optionalVideos = videosService.findByid(Long.valueOf(id));

        if (optionalVideos.isPresent()) {
            Videos video = optionalVideos.get();
            ModelAndView modelAndView = new ModelAndView("controlexterno/reproductorVideo");
            modelAndView.addObject("titulo", video.getNombre());
            modelAndView.addObject("url", video.getDireccionArchivo());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("controlexterno/index");
            return modelAndView;
        }
    }

    private static int currentPageIndex2 = 1;
    private static int pagSizeIndex2 = 12;
    private static String sortColumnIndex2 = "idMuestras.fechaInicial";
    private static String ano;
    private static String muestra;
    private static String laboratorio;
    private static String sede;
    private static String usuario;
    private static String estado;

    @RequestMapping(value = "/listResultadosDirector")
    public ModelAndView controlExternoIndex2(@RequestParam("ano") Optional<Integer> ano,
                                             @RequestParam("muestra") Optional<String> muestra,
                                             @RequestParam("laboratorio") Optional<String> laboratorio,
                                             @RequestParam("sede") Optional<String> sede,
                                             @RequestParam("usuario") Optional<String> usuario,
                                             @RequestParam("estado") Optional<String> estado,
                                             @RequestParam("sortColumnIndex2") Optional<String> sortColumnIndex2,
                                             @RequestParam("currentPageIndex2") Optional<Integer> currentPageIndex2,
                                             @RequestParam("pagSizeIndex2") Optional<Integer> pagSizeIndex2,
                                             @RequestParam("sortOIndex2") Optional<String> sortOIndex2,
                                             @RequestParam("page") Optional<Integer> page,
                                             @RequestParam("save") Optional<Boolean> save,
                                             @RequestParam("delete") Optional<Boolean> delete) {

        ModelAndView modelAndView = new ModelAndView("controlexterno/gestionResultadosDirector/listResultadosDirector");
        if (muestra.isPresent()) {
            if (muestra != null) {
                Optional<Muestras> muestrasobt = this.muestraService.find(Long.valueOf(muestra.get()));
                if (muestrasobt.isPresent()) {
                    String pruebString = String.valueOf(muestrasobt.get().getFechaInicial()).substring(0, 4);
                    ano = Optional.of(Integer.valueOf(pruebString));
                }
            }
        }
        if (!ano.isPresent()) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            ano = Optional.of(year);
        }

        List<Muestras> models3 = muestraService.obtMuestrasxAno(ano.get());
        modelAndView.addObject("models3", models3);

        if (muestra.isPresent()) {

            // modelAndView.addObject("muestrasForm", new Muestras());
            List<Laboratorios> modelsLab = laboratorioService.obtLabxMuestra(muestra.get());
            modelAndView.addObject("modelsLab", modelsLab);
            List<Sedes> modelsSede = sedesService.obtSedexMuestra(muestra.get());
            modelAndView.addObject("modelsSede", modelsSede);
            List<Usuarios> modelsUsu = usuarioService.obtUsuxMuestra(muestra.get());
            modelAndView.addObject("modelsUsu", modelsUsu);

            //List<Resultados> modelsListUsuarios	= resultadosService.obtListUsuarios(muestra.get(),laboratorio.get(),sede.get(),usuario.get(),estado.get());
            List<InscripcionMuestras> modelsListUsuarios = inscripcionMuestrasService.obtListUsuarios(muestra.get(), laboratorio.get(), sede.get(), usuario.get(), estado.get());
            modelAndView.addObject("modelsListUsuarios", modelsListUsuarios);
            List<Boolean> mostrarBotonCrear = new ArrayList<>();
            for (InscripcionMuestras im : modelsListUsuarios) {
                im.getIdMuestras().getFechaInicial();
                LocalDate mesInicioInscripcionR = im.getIdMuestras().getFechaInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                mostrarBotonCrear.add(LocalDate.now().isAfter(mesInicioInscripcionR.withDayOfMonth(14).minusMonths(1)));
            }
            modelAndView.addObject("mostrarBotonCrear", mostrarBotonCrear);
            Integer Participantes = inscripcionMuestrasService.obtListUsuariosInscritos(muestra.get(), laboratorio.get(), sede.get(), usuario.get());
            Integer NoParticipantes = inscripcionMuestrasService.obtListUsuariosNoInscritos(muestra.get(), laboratorio.get(), sede.get(), usuario.get());
            modelAndView.addObject("NoParticipantes", NoParticipantes);
            Participantes = Participantes + NoParticipantes;
            modelAndView.addObject("Participantes", Participantes);
            Muestras muestras = muestraService.find(Long.parseLong(muestra.get())).orElse(new Muestras());
            modelAndView.addObject("muestras", muestras);
        }

        if (save.isPresent()) {
            if (save.get()) {
                modelAndView.addObject("save", save);
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuarios usr = (Usuarios) authentication.getPrincipal();
        if (!authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_PARTICIPANTE"))) {
            String autorizadoPor = usr.getNombres().concat(' ' + usr.getApellidos());
            modelAndView.addObject("autorizadoPor", autorizadoPor);
        }
        //Si Borra un metodo
        if (delete.isPresent()) {
            if (delete.get()) {
                modelAndView.addObject("delete", delete);
            } else {
                modelAndView.addObject("delete2", delete);
            }
        }
        modelAndView.addObject("ano", ano.get());
        modelAndView.addObject("muestra", muestra.orElse("").equalsIgnoreCase("_") ? "" : muestra.orElse(""));
        modelAndView.addObject("laboratorio", laboratorio.orElse("").equalsIgnoreCase("_") ? "" : laboratorio.orElse(""));
        modelAndView.addObject("sede", sede.orElse("").equalsIgnoreCase("_") ? "" : sede.orElse(""));
        modelAndView.addObject("usuario", usuario.orElse("").equalsIgnoreCase("_") ? "" : usuario.orElse(""));
        modelAndView.addObject("estado", estado.orElse("").equalsIgnoreCase("_") ? "" : estado.orElse(""));
        return modelAndView;
    }

    @RequestMapping("/obtMuestrasxAno")
    public String obtMuestrasxAno(@RequestParam("ano") Integer ano, Model model) {
        List<Muestras> models = muestraService.obtMuestrasxAno(ano);
        model.addAttribute("models3", models);
        return "controlexterno/gestionResultadosDirector/listResultadosDirector :: models3";
    }


    @RequestMapping("/obtLabxMuestra")
    public String obtLabxMuestra(@RequestParam("muestra") String muestra, Model model) {
        List<Laboratorios> modelsLab = new ArrayList<>();
        modelsLab.add(new Laboratorios());
        modelsLab.addAll(laboratorioService.obtLabxMuestra(muestra));
        model.addAttribute("modelsLab", modelsLab);
        return "controlexterno/index2 :: modelsLab";
    }


    @RequestMapping("/obtSedexMuestra")
    public String obtSedexMuestra(@RequestParam("muestra") String muestra, Model model) {
        List<Sedes> modelsSede = new ArrayList<>();
        modelsSede.add(new Sedes());
        modelsSede.addAll(sedesService.obtSedexMuestra(muestra));
        model.addAttribute("modelsSede", modelsSede);
        return "controlexterno/gestionResultadosDirector/listResultadosDirector :: modelsSede";
    }

    @RequestMapping("/obtUsuxMuestra")
    public String obtUsuxMuestra(@RequestParam("muestra") String muestra, Model model) {
        List<Usuarios> modelsUsu = new ArrayList<>();
        modelsUsu.add(new Usuarios());
        modelsUsu.addAll(usuarioService.obtUsuxMuestra(muestra));
        model.addAttribute("modelsUsu", modelsUsu);
        return "controlexterno/gestionResultadosDirector/listResultadosDirector :: modelsUsu";
    }


    private static int currentPageAudi = 1;
    private static int pagSizeAudi = 12;
    private static String sortColumnAudi = "idMuestras.fechaInicial";
    private static String muestraAudi;

    @RequestMapping(value = "/auditoria")
    public ModelAndView controlExternoauditoria(@RequestParam("muestraAudi") Optional<String> muestraAudi,
                                                @RequestParam("sortColumnAudi") Optional<String> sortColumnAudi,
                                                @RequestParam("currentPageAudi") Optional<Integer> currentPageAudi,
                                                @RequestParam("pagSizeAudi") Optional<Integer> pagSizeAudi,
                                                @RequestParam("page") Optional<Integer> page) {

        ModelAndView modelAndView = new ModelAndView("controlexterno/gestionResultadosDirector/auditoria");
        Optional<Muestras> muestras = this.muestraService.find(Long.parseLong(muestraAudi.get()));
        List<AuditoriaResultadosDirector> modelsListAuditoria = auditoriaResultadosDirectorService.findByMuestras(muestras.get());
        modelAndView.addObject("modelsListAuditoria", modelsListAuditoria);

        modelAndView.addObject("muestras", muestras.get());

        return modelAndView;
    }


    @RequestMapping(value = "/deleteResultadoDirector", method = RequestMethod.GET)
    public ModelAndView deleteResultadoDirector2(@RequestParam("idResultadoT") String idResultadoT,
                                                 @RequestParam("justificacionT") String JustificacionT,
                                                 @RequestParam("autorizadoPorT") String AutorizadoPorT,
                                                 ModelAndView model, RedirectAttributes
                                                         redirectAttributes) {

        Optional<Resultados> resultado = this.resultadosService.find(Long.parseLong(idResultadoT));

        if (!resultado.isPresent()) {
            redirectAttributes.addAttribute("delete", false);
            return new ModelAndView("redirect:/controlexterno/listResultadosDirector");
        }

        //Inicio tabla auditoria Director
        AuditoriaResultadosDirector auditoriaDirector = new AuditoriaResultadosDirector();
        auditoriaDirector.setIdMuestras(resultado.get().getIdInscripcionMuestras().getIdMuestras());
        //Primero registrar en tabla de auditoria
        Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
        Usuarios usr = (Usuarios) auth.get().getPrincipal();
        Usuarios user = usuarioService.findByUserName(usr.getNombreUsuario());
        auditoriaDirector.setUsuarioId(resultado.get().getIdInscripcionMuestras().getIdUsuarios().getUsuarioId());
        auditoriaDirector.setTipoModificacionEliminacion('D');
        auditoriaDirector.setFechaCreacionResultado(resultado.get().getFechaCreacion());
        auditoriaDirector.setUsuarioUSedeCreacion(resultado.get().getIdUsuarios());
        auditoriaDirector.setAprovadoPor(AutorizadoPorT);
        auditoriaDirector.setJustificacion(JustificacionT);
        //Auditoria control externo
        AuditoriaControlExterno ace = new AuditoriaControlExterno();
        ace.setPrograma(resultado.get().getIdInscripcionMuestras().getIdMuestras().getIdPrograma().getAbreviatura());
        ace.setIdMuestras(resultado.get().getIdInscripcionMuestras().getIdMuestras());
        ace.setAccion("Eliminación resultado");
        ace.setJustificacion(JustificacionT);
        ace.setResponsable(AutorizadoPorT);
        ace.setIdUsuario(user);
        ace.setUsuarioResultado(resultado.get().getIdInscripcionMuestras().getIdUsuarios().getUsuarioId().getNombreUsuario());
        auditoriaControlExternoService.save(ace);

        try {
            if (resultadosDetalladosService.eliminarDetalle(resultado.get())) {
                if (resultadosService.borrarXId(Long.parseLong(idResultadoT))) {
                    auditoriaResultadosDirectorService.save(auditoriaDirector);
                    redirectAttributes.addAttribute("delete", true);
                    return new ModelAndView("redirect:/controlexterno/listResultadosDirector");

                } else {
                    redirectAttributes.addAttribute("delete", false);
                    return new ModelAndView("redirect:/controlexterno/listResultadosDirector");
                }

            } else {
                redirectAttributes.addAttribute("delete", false);
                return new ModelAndView("redirect:/controlexterno/listResultadosDirector");
            }
        } catch (Exception e) {
            redirectAttributes.addAttribute("delete", false);
            return new ModelAndView("redirect:/controlexterno/listResultadosDirector");
        }
    }
}