package com.proasecal.software.web.controller.inscripcion;

import com.proasecal.software.web.entity.DAO.MuestrasInscritas;
import com.proasecal.software.web.entity.administrar.*;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import com.proasecal.software.web.entity.inscripcion.InscripcionProgramas;
import com.proasecal.software.web.entity.inscripcion.PeriodosVigencia;
import com.proasecal.software.web.entity.parametricas.Pager;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import com.proasecal.software.web.repository.administrar.MuestraRepository;
import com.proasecal.software.web.service.administrar.ClienteService;
import com.proasecal.software.web.service.administrar.LaboratorioService;
import com.proasecal.software.web.service.administrar.MuestraService;
import com.proasecal.software.web.service.administrar.ProgramasService;
import com.proasecal.software.web.service.administrar.SedesService;
import com.proasecal.software.web.service.inscripcion.InscripcionMuestrasService;
import com.proasecal.software.web.service.inscripcion.InscripcionProgramasService;
import com.proasecal.software.web.service.inscripcion.PeriodosVigenciaService;
import com.proasecal.software.web.service.seguridad.FiltrosSeccionesService;
import com.proasecal.software.web.service.seguridad.SeccionesService;
import com.proasecal.software.web.service.seguridad.UsuariosLabSedesService;
import com.proasecal.software.web.util.Notify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/periodosvigencia")
public class PeriodosVigenciaController {

    @Autowired
    PeriodosVigenciaService periodosVigenciaService;
    @Autowired
    SeccionesService seccionesService;
    @Autowired
    FiltrosSeccionesService filtrosSeccionesService;

    @Autowired
    LaboratorioService laboratorioService;
    @Autowired
    ClienteService clienteService;
    @Autowired
    SedesService sedesService;
    @PersistenceContext
    private EntityManager em;

    @Autowired
    UsuariosLabSedesService usuariosLabSedesService;
    @Autowired
    MuestraService muestraService;
    @Autowired
    InscripcionProgramasService inscripcionProgramasService;
    @Autowired
    MuestraRepository muestraRepository;
    @Autowired
    InscripcionMuestrasService inscripcionMuestrasService;
    @Autowired
    ProgramasService programaService;    

    private static int currentPage = 1;
    private static int pagSize = 20;
    private static String sortColumn = "periodosvigenciaId";
    private static String sortO = "ASC";
    private static String laboratorio = "_";
    private static String sede = "_";
    private static String usuario = "_";
    private static String programa = "_";
    private static String modalidad = "_";
    private static String muestra = "_";
    private static String enmora = "";
    private static String patrocinada = "";
    private static String clientePatrocinador = "_";
    private static String fechaInicio = "_";
    private static String fechaFin = "_";
    private static final int[] PAGE_SIZES = {5, 10, 20, 50};
    private static final int BUTTONS_TO_SHOW = 3;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam("idLaboratoriosSedes") Optional<String> laboratorio,
                             @RequestParam("sede") Optional<String> sede,
                             @RequestParam("usuario") Optional<String> usuario,
                             @RequestParam("programa") Optional<String> programa,
                             @RequestParam("modalidad") Optional<String> modalidad,
                             @RequestParam("muestra") Optional<String> muestra,
                             @RequestParam("enmora") Optional<String> enmora,
                             @RequestParam("muestraPatrocinada") Optional<String> patrocinada,
                             @RequestParam("clientePatrocinador") Optional<String> clientePatrocinador,
                             @RequestParam("fechaInicio") Optional<String> fechaInicio,
                             @RequestParam("fechaFin") Optional<String> fechaFin,
                             @RequestParam("nameColumn") Optional<String> nameColumn,
                             @RequestParam("sortBy") Optional<String> sortBy,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size,
                             @RequestParam("save") Optional<String> save,
                             @RequestParam("delete") Optional<String> delete,
                             @RequestParam("inscripcion") Optional<String> inscripcion) {

        sortColumn = nameColumn.isPresent() ? nameColumn.get() : "inscripProgramaId.codProasecal";
        sortO = sortBy.isPresent() ? sortBy.get() : "ASC";
        currentPage = page.isPresent() ? page.get() : 1;
        pagSize = size.isPresent() ? size.get() : 20;
        this.laboratorio = laboratorio.isPresent() ? laboratorio.get() : "";
        this.sede = sede.isPresent() ? sede.get() : "";
        this.usuario = usuario.isPresent() ? usuario.get() : "";
        this.programa = programa.isPresent() ? programa.get() : "";
        this.modalidad = modalidad.isPresent() ? modalidad.get() : "";
        this.muestra = muestra.isPresent() ? muestra.get() : "";
        this.enmora = enmora.isPresent() ? enmora.get() : "";
        this.patrocinada = patrocinada.isPresent() ? patrocinada.get() : "";
        this.clientePatrocinador = clientePatrocinador.isPresent() ? clientePatrocinador.get() : "";
        this.fechaInicio = fechaInicio.isPresent() ? fechaInicio.get() : "";
        this.fechaFin = fechaFin.isPresent() ? fechaFin.get() : "";


        int evalPageSize = size.orElse(pagSize);
        Page<PeriodosVigencia> periodosVigencia =
                periodosVigenciaService.ListPaginated(
                        laboratorio.orElse(this.laboratorio),
                        sede.orElse(this.sede),
                        usuario.orElse(this.usuario),
                        programa.orElse(this.programa),
                        modalidad.orElse(this.modalidad),
                        muestra.orElse(this.muestra),
                        enmora.orElse(this.enmora),
                        patrocinada.orElse(this.patrocinada),
                        clientePatrocinador.orElse(this.clientePatrocinador),
                        fechaInicio.orElse(this.fechaInicio),
                        fechaFin.orElse(this.fechaFin),
                        PageRequest.of(
                                page.orElse(currentPage) - 1, pagSize, new Sort(Sort.Direction.valueOf(sortO), sortColumn, "inscripProgramaId.codProasecal")
                        ));

        Pager pager = new Pager(periodosVigencia.getTotalPages(), periodosVigencia.getNumber(), BUTTONS_TO_SHOW);

        ModelAndView modelAndView = new ModelAndView("web/inscripcion/periodosvigencia/list");
        List<Set<Integer>> muestrasListString = new ArrayList<>();
        Integer i = 0;
        for (PeriodosVigencia perv : periodosVigencia.getContent()) {
            i.toString();
            if (perv.getInscripProgramaId().getInscripcionMuestrasList() != null) {
                    try {
                        List<MuestrasInscritas> miL = periodosVigenciaXMuestras(perv.getFechaInicio(), perv.getFechaFin(), perv.getPeriodosvigenciaId());                        /*perv.getInscripProgramaId().getInscripcionMuestrasList().stream().forEach(x->miL.removeIf(n->!(x.getIdMuestras().getNumeroMuestra().equalsIgnoreCase(n.getNombreMuestra()))));*/
                        //Inicio de logica
                        if(perv.getInscripProgramaId().getInscripcionMuestrasList().size()>=1) {
                            Set<Integer> t2= new HashSet<>();
                            for (MuestrasInscritas mi : miL) {
                                    for (InscripcionMuestras tmp : perv.getInscripProgramaId().getInscripcionMuestrasList()) {
                                        if (mi.getNombreMuestra()==(tmp.getIdMuestras().getNumeroMuestra())) {
                                           t2.add(mi.getNombreMuestra());
                                        }
                                    }

                                }
                            muestrasListString.add(t2);
                            }else{
                            Set<Integer> t2 = new HashSet<>();
                            muestrasListString.add(t2);
                        }

                        // FIn de la logica

                    } catch (Exception e) {
                        modelAndView.addObject("notify", Notify.ERROR("ERROR", "Al obtener las muestras"));
                    }
            }
            i++;
        }

        modelAndView.addObject("muestrasListString", muestrasListString);
        modelAndView.addObject("filtrosList", filtrosSeccionesService.getListFiltros(seccionesService.findByName("periodosVigencia")));
        modelAndView.addObject("periodosVigencia", periodosVigencia);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("sort", sortBy.orElse(sortO));
        modelAndView.addObject("nameColumn", nameColumn.orElse(sortColumn));             
        modelAndView.addObject("idLaboratoriosSedes", laboratorio.orElse("").equalsIgnoreCase("_") ? "" : laboratorio.orElse(""));

        //no se consideran variables vacias
        if (laboratorio.isPresent() && !(laboratorio.orElse("").equalsIgnoreCase("_")) && (!laboratorio.get().isEmpty())) {
            Laboratorios lab = new Laboratorios();
            lab.setIdLaboratoriosSedes(Long.valueOf(laboratorio.get()).longValue());
            List<Sedes> listSedes = sedesService.listSedesxIdlab(lab);
            if (sede.isPresent() && !(sede.orElse("").equalsIgnoreCase("_")) && !(sede.get().isEmpty())) {
                for (Sedes s : listSedes) {
                    if (s.getIdSedes() == Long.parseLong(sede.get())) {
                        modelAndView.addObject("models4", s.getSedesUsuariosList());
                        break;
                    }
                }
            }
            modelAndView.addObject("models3", listSedes);
        }
        if (usuario.isPresent() && !(usuario.orElse("").equalsIgnoreCase("_")) && !(usuario.get().isEmpty())) {
            UsuariosLabSedes usuariosLabSedes = new UsuariosLabSedes();
            usuariosLabSedes.setIdUsuarioLabSedes(Long.valueOf(usuario.get()).longValue());

            modelAndView.addObject("models5", inscripcionProgramasService.obtenerPorUsuario(usuariosLabSedes));
        }

        modelAndView.addObject("selectMuestras", muestraService.list());
        modelAndView.addObject("sede", sede.orElse("").equalsIgnoreCase("_") ? "" : sede.orElse(""));
        modelAndView.addObject("usuario", usuario.orElse("").equalsIgnoreCase("_") ? "" : usuario.orElse(""));
        modelAndView.addObject("programa", programa.orElse("").equalsIgnoreCase("_") ? "" : programa.orElse(""));
        modelAndView.addObject("modalidad", modalidad.orElse("").equalsIgnoreCase("_") ? "" : modalidad.orElse(""));
        modelAndView.addObject("muestra", muestra.orElse("").equalsIgnoreCase("_") ? "" : muestra.orElse(""));
        modelAndView.addObject("enmora", enmora.orElse("false"));
        modelAndView.addObject("muestraPatrocinada", patrocinada.orElse("false"));
        modelAndView.addObject("clientePatrocinador", clientePatrocinador.orElse("").equalsIgnoreCase("_") ? "" : clientePatrocinador.orElse(""));
        modelAndView.addObject("fechaInicio", fechaInicio.orElse("").equalsIgnoreCase("_") ? "" : fechaInicio.orElse(""));
        modelAndView.addObject("fechaFin", fechaFin.orElse("").equalsIgnoreCase("_") ? "" : fechaFin.orElse(""));
        if (save.isPresent()) {
            if (save.get().equalsIgnoreCase("create")) {
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Periodo de Vigencia creado correctamente"));
            } else {
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Periodo de Vigencia modificado correctamente"));
            }
        }
        //Si Borra un metodo
        if (delete.isPresent()) {
            if (delete.get().equalsIgnoreCase("si")) {
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Periodo de Vigencia eliminado correctamente"));
            } else if (delete.get().equalsIgnoreCase("no")) {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo eliminar el Periodo de Vigencia"));
            } else {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "El Periodo de vigencia a eliminar no existe"));
            }
        }

        if (inscripcion.isPresent()) {
            if (inscripcion.get().equals("success")) {
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Muestras inscritas correctamente."));
            }
            if (inscripcion.get().equals("error")) {
                modelAndView.addObject("notify", Notify.ERROR("!OK", "No se pudo realizar la inscripción de Muestras."));
            }
        }

        List<Muestras> muestrasList = new ArrayList<>();
        for (PeriodosVigencia per : periodosVigencia) {
            muestrasList.addAll(per.getInscripProgramaId().getMuestrasList());
        }
        modelAndView.addObject("muestrasList", muestrasList);
        List<Laboratorios> laboratoriosList = laboratorioService.list();
        modelAndView.addObject("laboratoriosList", laboratoriosList);
        List<Sedes> sedesList = sedesService.list();
        modelAndView.addObject("sedesList", sedesList);
        List<UsuariosLabSedes> usuarioLabSedesList = usuariosLabSedesService.list();
        modelAndView.addObject("usuarioLabSedesList", usuarioLabSedesList);
        List<Programas> listProgramas = programaService.list();
        modelAndView.addObject("listProgramas",listProgramas );
        List<Clientes> clientesList = clienteService.getAllClientes();
        modelAndView.addObject("clientesList", clientesList);
        List<Muestras> listMuestras = (List<Muestras>) muestraRepository.findAll();
        modelAndView.addObject("listMuestras", listMuestras);
        return modelAndView;
    }

    @RequestMapping(value = "/create")
    public ModelAndView form(@RequestParam("idLaboratoriosSedes") Optional<String> laboratorio,
                             @RequestParam("sede") Optional<String> sede,
                             @RequestParam("usuario") Optional<String> usuario,
                             @RequestParam("programa") Optional<String> programa,
                             @RequestParam("modalidad") Optional<String> modalidad,
                             @RequestParam("muestra") Optional<String> muestra,
                             @RequestParam("enmora") Optional<String> enmora,
                             @RequestParam("muestraPatrocinada") Optional<String> patrocinada,
                             @RequestParam("clientePatrocinador") Optional<String> clientePatrocinador,
                             @RequestParam("fechaInicio") Optional<String> fechaInicio,
                             @RequestParam("fechaFin") Optional<String> fechaFin) {
        ModelAndView modelAndView = new ModelAndView("web/inscripcion/periodosvigencia/form");

        PeriodosVigencia periodosVigencia = new PeriodosVigencia();
        InscripcionProgramas inscripcionProgramas = new InscripcionProgramas();
        periodosVigencia.setInscripProgramaId(inscripcionProgramas);
        Sedes sedes = new Sedes();
        Laboratorios laboratorios = new Laboratorios();
        UsuariosLabSedes usuariosLabSedes = new UsuariosLabSedes();
        inscripcionProgramas.setIdSedes(sedes);


        if (laboratorio.isPresent() && !(laboratorio.orElse("").equalsIgnoreCase("_")) && (!laboratorio.get().isEmpty())) {
            laboratorios.setIdLaboratoriosSedes(Long.parseLong(laboratorio.get()));
            periodosVigencia.getInscripProgramaId().getIdSedes().setIdLaboratoriosSedes(laboratorios);
            List<Sedes> models = sedesService.listSedesxIdlab(periodosVigencia.getInscripProgramaId().getIdSedes().getIdLaboratoriosSedes());
            modelAndView.addObject("models3", models);
        }
        if (sede.isPresent() && !(sede.orElse("").equalsIgnoreCase("_")) && (!sede.get().isEmpty())) {
            sedes.setIdSedes(Long.parseLong(sede.get()));
            periodosVigencia.getInscripProgramaId().setIdSedes(sedes);
            List<UsuariosLabSedes> models2 = usuariosLabSedesService.obtenerUsuariosPorSedes(periodosVigencia.getInscripProgramaId().getIdSedes().getIdSedes());
            modelAndView.addObject("models4", models2);
        }

        if (usuario.isPresent() && !(usuario.orElse("").equalsIgnoreCase("_")) && (!usuario.get().isEmpty())) {
            usuariosLabSedes.setIdUsuarioLabSedes(Long.parseLong(usuario.get()));
            periodosVigencia.getInscripProgramaId().setIdUsuarioLabSedes(usuariosLabSedes);
            modelAndView.addObject("models5", inscripcionProgramasService.obtenerPorUsuario(usuariosLabSedes));            
        }
        if (programa.isPresent() && !(programa.orElse("").equalsIgnoreCase("_")) && (!programa.get().isEmpty())) {
            inscripcionProgramas.setInscripProgramaId(Long.parseLong(programa.get()));
            periodosVigencia.setInscripProgramaId(inscripcionProgramas);
            //modelAndView.addObject("models5", inscripcionProgramasService.obtenerPorUsuario(usuariosLabSedes));
        }

        if (modalidad.isPresent() && !(modalidad.orElse("").equalsIgnoreCase("_")) && (!modalidad.get().isEmpty())) {
            periodosVigencia.setModalidad(modalidad.get());
        }
        if (enmora.isPresent() && !(enmora.orElse("").equalsIgnoreCase("_")) && (!enmora.get().isEmpty())) {
            periodosVigencia.setEnMora(Boolean.valueOf(enmora.get()));
        }
        if (patrocinada.isPresent() && !(patrocinada.orElse("").equalsIgnoreCase("_")) && (!patrocinada.get().isEmpty())) {
            periodosVigencia.setMuestraPatrocinada(Boolean.valueOf(patrocinada.get()));
        }
        if (clientePatrocinador.isPresent() && !(clientePatrocinador.orElse("").equalsIgnoreCase("_")) && (!clientePatrocinador.get().isEmpty())) {
            periodosVigencia.setClientePatrocinador(clientePatrocinador.get());
        }
        if (fechaInicio.isPresent() && !(fechaInicio.orElse("").equalsIgnoreCase("_")) && (!fechaInicio.get().isEmpty())) {
            try {
                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(fechaInicio.get());
                periodosVigencia.setFechaInicio(date);
            } catch (ParseException e) {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "Error al obtener datos del filtro"));
                e.printStackTrace();
            }

        }
        if (fechaFin.isPresent() && !(fechaFin.orElse("").equalsIgnoreCase("_")) && (!fechaFin.get().isEmpty())) {
            try {
                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(fechaFin.get());
                periodosVigencia.setFechaFin(date);
            } catch (ParseException e) {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "Error al obtener datos del filtro"));
                e.printStackTrace();
            }
        }

        modelAndView.addObject("periodosprogramaForm", periodosVigencia);
        List<Laboratorios> laboratoriosList = laboratorioService.list();
        modelAndView.addObject("laboratoriosList", laboratoriosList);
        List<Clientes> clientesList = clienteService.getAllClientes();
        modelAndView.addObject("clientesList", clientesList);
        return modelAndView;
    }

    @GetMapping(value = "/edit/{id}")
    public ModelAndView edit(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("web/inscripcion/periodosvigencia/form");
        Optional<PeriodosVigencia> periodosVigencia = this.periodosVigenciaService.find(id);

        if (!periodosVigencia.isPresent()) {
            modelAndView.addObject("periodosprogramaForm", new PeriodosVigencia());
            modelAndView.addObject("notify", Notify.ERROR("ERROR", "El Periodo de vigencia a consultar no existe"));
            return modelAndView;
        }
        List<Laboratorios> laboratoriosList = laboratorioService.list();
        modelAndView.addObject("laboratoriosList", laboratoriosList);
        List<Clientes> clientesList = clienteService.getAllClientes();
        modelAndView.addObject("clientesList", clientesList);
        List<Sedes> models = sedesService.listSedesxIdlab(periodosVigencia.get().getInscripProgramaId().getIdSedes().getIdLaboratoriosSedes());
        modelAndView.addObject("models3", models);          

        List<UsuariosLabSedes> models2 = usuariosLabSedesService.obtenerUsuariosPorSedes(periodosVigencia.get().getInscripProgramaId().getIdUsuarioLabSedes().getIdSedes().getIdSedes());
        modelAndView.addObject("models4", models2);   
        List<InscripcionProgramas> models3 = inscripcionProgramasService.obtenerPorUsuario(periodosVigencia.get().getInscripProgramaId().getIdUsuarioLabSedes());
        modelAndView.addObject("models5", models3);

        UsuariosLabSedes usuariosLabSedes = new UsuariosLabSedes();
        Programas programas = new Programas();
        usuariosLabSedes.setIdUsuarioLabSedes(periodosVigencia.get().getInscripProgramaId().getIdUsuarioLabSedes().getIdUsuarioLabSedes());
        InscripcionProgramas inp = inscripcionProgramasService.findById(periodosVigencia.get().getInscripProgramaId().getInscripProgramaId()).get();
        programas.setProgramaId(inp.getProgramaId().getProgramaId());
        InscripcionProgramas inscripcionProgramas = this.inscripcionProgramasService.optenerUsuarioXProgramasIns(usuariosLabSedes, programas);	        
        modelAndView.addObject("listPerVigR", periodosVigenciaService.periodosVigenciasInscritos2(inscripcionProgramas));
        
        modelAndView.addObject("periodosprogramaForm", periodosVigencia.get());
        return modelAndView;
    }


    @PostMapping(value = "/save")
    public ModelAndView save(@Valid @ModelAttribute("periodosprogramaForm") PeriodosVigencia periodosprogramaForm, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                             ModelAndView model) {
        ModelAndView modelAndView = new ModelAndView("web/inscripcion/periodosvigencia/form");
        Boolean validacion=true;
        if (periodosprogramaForm.getPeriodosvigenciaId() != null && periodosprogramaForm.getPeriodosvigenciaId() != 0) {
           PeriodosVigencia periodosVigencia = this.periodosVigenciaService.find(periodosprogramaForm.getPeriodosvigenciaId()).get();

          if(periodosVigencia.getFechaInicio().compareTo(periodosprogramaForm.getFechaInicio())==0&&periodosVigencia.getFechaFin().compareTo(periodosprogramaForm.getFechaFin())==0&&periodosVigencia.getInscripProgramaId().getIdUsuarioLabSedes().getUsuarioId().equals(periodosprogramaForm.getInscripProgramaId().getIdUsuarioLabSedes().getUsuarioId())) {
               validacion=false;
          }
          else {
              List<PeriodosVigencia> periodosVigenciaList = periodosVigenciaService.periodosVigenciasInscritos(periodosprogramaForm.getInscripProgramaId(),periodosprogramaForm.getInscripProgramaId().getIdUsuarioLabSedes(),periodosprogramaForm.getPeriodosvigenciaId());

  	        Boolean ifAfter = periodosVigenciaList.stream().anyMatch(x->x.getFechaFin().after(periodosprogramaForm.getFechaInicio()));
  	        if(ifAfter){
  	            bindingResult.rejectValue("fechaInicio", "error", "La fecha inicial debe ser mayor a la ultima fecha registrada");
  	        }
  	        Boolean ifCompare = periodosVigenciaList.stream().anyMatch(x->x.getFechaFin().compareTo(periodosprogramaForm.getFechaInicio())==0);
  	        if(ifCompare){
  	            bindingResult.rejectValue("fechaInicio", "error", "La fecha inicial debe ser mayor a la ultima fecha registrada");
  	        }
  	        if (this.periodosVigenciaService.validDate(periodosprogramaForm)) {
  	            bindingResult.rejectValue("fechaFin", "error", "La Fecha fin no puede ser inferior a la Fecha inicio.");
  	        }
          }
          validacion=false;
        }
        

        if(validacion){
     	
            List<PeriodosVigencia> periodosVigenciaList = periodosVigenciaService.periodosVigenciasInscritos(periodosprogramaForm.getInscripProgramaId(),periodosprogramaForm.getInscripProgramaId().getIdUsuarioLabSedes());

	        Boolean ifAfter = periodosVigenciaList.stream().anyMatch(x->x.getFechaFin().after(periodosprogramaForm.getFechaInicio()));
	        if(ifAfter){
	            bindingResult.rejectValue("fechaInicio", "error", "La fecha inicial debe ser mayor a la ultima fecha registrada");
	        }
	        Boolean ifCompare = periodosVigenciaList.stream().anyMatch(x->x.getFechaFin().compareTo(periodosprogramaForm.getFechaInicio())==0);
	        if(ifCompare){
	            bindingResult.rejectValue("fechaInicio", "error", "La fecha inicial debe ser mayor a la ultima fecha registrada");
	        }
	        if (this.periodosVigenciaService.validDate(periodosprogramaForm)) {
	            bindingResult.rejectValue("fechaFin", "error", "La Fecha fin no puede ser inferior a la Fecha inicio.");
	        }
        }
        
        
        //Validaciones de RN

        if (bindingResult.hasErrors()) {
            model.setViewName("web/inscripcion/periodosvigencia/form");
            model.addObject("periodosprogramaForm", periodosprogramaForm);
            List<Laboratorios> laboratoriosList = laboratorioService.list();
            model.addObject("laboratoriosList", laboratoriosList);
	        List<Sedes> models = sedesService.listSedesxIdlab(periodosprogramaForm.getInscripProgramaId().getIdSedes().getIdLaboratoriosSedes());
	        model.addObject("models3", models);     
	        List<UsuariosLabSedes> models2 = usuariosLabSedesService.obtenerUsuariosPorSedes(periodosprogramaForm.getInscripProgramaId().getIdUsuarioLabSedes().getIdSedes().getIdSedes());
	        model.addObject("models4", models2);
	        List<InscripcionProgramas> models3 = new ArrayList<InscripcionProgramas>();
	        models3 = inscripcionProgramasService.obtenerPorUsuario(periodosprogramaForm.getInscripProgramaId().getIdUsuarioLabSedes());
	        model.addObject("models5", models3);
	        UsuariosLabSedes usuariosLabSedes = new UsuariosLabSedes();
	        Programas programas = new Programas();
	        usuariosLabSedes.setIdUsuarioLabSedes(periodosprogramaForm.getInscripProgramaId().getIdUsuarioLabSedes().getIdUsuarioLabSedes());//periodosprogramaForm.getInscripProgramaId().getIdUsuarioLabSedes().getIdSedes().getIdSedes()
	        InscripcionProgramas inp = inscripcionProgramasService.findById(periodosprogramaForm.getInscripProgramaId().getInscripProgramaId()).get();//periodosprogramaForm.getInscripProgramaId().getInscripProgramaId()
	        programas.setProgramaId(inp.getProgramaId().getProgramaId());
	        InscripcionProgramas inscripcionProgramas = this.inscripcionProgramasService.optenerUsuarioXProgramasIns(usuariosLabSedes, programas);	        
	        model.addObject("listPerVigR", periodosVigenciaService.periodosVigenciasInscritos2(inscripcionProgramas));	 
            List<Clientes> clientesList = clienteService.getAllClientes();
            model.addObject("clientesList", clientesList);
            return model;
        }

        String ir = "create";
        if (periodosprogramaForm.getPeriodosvigenciaId() != null && periodosprogramaForm.getPeriodosvigenciaId() != 0) {
            ir = "update";
        }

        try {
            this.periodosVigenciaService.save(periodosprogramaForm);
        } catch (Exception e) {
            modelAndView.addObject("laboratoriosList", laboratorioService.list());
            List<Laboratorios> laboratoriosList = laboratorioService.list();
            modelAndView.addObject("laboratoriosList", laboratoriosList);
	        List<Sedes> models = sedesService.listSedesxIdlab(periodosprogramaForm.getInscripProgramaId().getIdSedes().getIdLaboratoriosSedes());
	        modelAndView.addObject("models3", models);     
	        List<UsuariosLabSedes> models2 = usuariosLabSedesService.obtenerUsuariosPorSedes(periodosprogramaForm.getInscripProgramaId().getIdUsuarioLabSedes().getIdSedes().getIdSedes());
	        modelAndView.addObject("models4", models2);
	        List<InscripcionProgramas> models3 = new ArrayList<InscripcionProgramas>();
	        models3 = inscripcionProgramasService.obtenerPorUsuario(periodosprogramaForm.getInscripProgramaId().getIdUsuarioLabSedes());
	        modelAndView.addObject("models5", models3);
            List<Clientes> clientesList = clienteService.getAllClientes();
            modelAndView.addObject("clientesList", clientesList);
            if (ir.equalsIgnoreCase("create")) {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo crear el Periodo de Vigencia"));
            } else {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo modificar el Periodo de Vigencia"));
            }

            return modelAndView;
        }

        redirectAttributes.addAttribute("save", ir);
        return new ModelAndView("redirect:/periodosvigencia/list");
    }


    @RequestMapping(value = "/delete")
    public ModelAndView form(@RequestParam("periodosvigenciaId") String periodosvigenciaId, ModelAndView model, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("web/inscripcion/periodosvigencia/list");

        Optional<PeriodosVigencia> periodosVigencia = this.periodosVigenciaService.find(Long.parseLong(periodosvigenciaId));
        if (!periodosVigencia.isPresent()) {
            //Si no se encuentra el periodo
            redirectAttributes.addAttribute("delete", "excepcion");
            return new ModelAndView("redirect:/periodosvigencia/list");
        } else {
            if (periodosVigenciaService.borrarXId(periodosVigencia.get())) {
                //Si Borra un metodo
                redirectAttributes.addAttribute("delete", "si");
                return new ModelAndView("redirect:/periodosvigencia/list");
            } else {
                redirectAttributes.addAttribute("delete", "no");
                return new ModelAndView("redirect:/periodosvigencia/list");
            }
        }
    }


    @RequestMapping("/obtSedes")
    public String obtenerSedesxLaboratorio(@RequestParam("idLaboratorio") String idLaboratorio, Model model) {
        Laboratorios laboratorio = new Laboratorios();
        laboratorio.setIdLaboratoriosSedes(Long.valueOf(idLaboratorio).longValue());
        List<Sedes> models = sedesService.listSedesxIdlab(laboratorio);
        model.addAttribute("models3", models);
        return "web/inscripcion/periodosvigencia/form :: models3";
    }

    @RequestMapping("/obtUsuarios")
    public String obtenerUsuariosxSede(@RequestParam("idSede") String idSede, Model model) {
        List<UsuariosLabSedes> models2 = usuariosLabSedesService.obtenerUsuariosPorSedes(Long.valueOf(idSede).longValue());
        model.addAttribute("models4", models2);
        return "web/inscripcion/periodosvigencia/form :: models4";
    }

    @RequestMapping("/obtInsProgramas")
    public String obtenerInsProgramasxUsuario(@RequestParam("idusuario") String idusuario, Model model) {
        try{
            if (idusuario != null && !idusuario.isEmpty()&&!idusuario.equalsIgnoreCase("null")) {
                UsuariosLabSedes usuariosLabSedes = new UsuariosLabSedes();
                usuariosLabSedes.setIdUsuarioLabSedes(Long.valueOf(idusuario).longValue());
                List<InscripcionProgramas> models3 = new ArrayList<InscripcionProgramas>();
                models3 = inscripcionProgramasService.obtenerPorUsuario(usuariosLabSedes);

                model.addAttribute("models5", models3);
            }
        }catch (NumberFormatException e){
            return "web/inscripcion/periodosvigencia/form :: models5";
        }
        return "web/inscripcion/periodosvigencia/form :: models5";
    }

    @RequestMapping("/perVigUserXPrograma")
    public String perVigUserXPrograma(@RequestParam("idusuario") String idusuario, @RequestParam("inscripProgramaId") String inscripProgramaId, Model model) {
        try{
            if (idusuario != null && !idusuario.isEmpty() && inscripProgramaId != null && !inscripProgramaId.isEmpty()&&!inscripProgramaId.equalsIgnoreCase("null")) {
                UsuariosLabSedes usuariosLabSedes = new UsuariosLabSedes();
                Programas programas = new Programas();
                usuariosLabSedes.setIdUsuarioLabSedes(Long.parseLong(idusuario));
                InscripcionProgramas inp = inscripcionProgramasService.findById(Long.parseLong(inscripProgramaId)).get();
                programas.setProgramaId(inp.getProgramaId().getProgramaId());
                InscripcionProgramas inscripcionProgramas = this.inscripcionProgramasService.optenerUsuarioXProgramasIns(usuariosLabSedes, programas);
                List <PeriodosVigencia> listaTemp = new ArrayList<>();
                listaTemp.add(new PeriodosVigencia());
                listaTemp.addAll(periodosVigenciaService.periodosVigenciasInscritos2(inscripcionProgramas));

                model.addAttribute("listPerVigR", listaTemp);
            }
        }catch (NumberFormatException e){
            return "web/inscripcion/periodosvigencia/form :: perVRegistrado";
        }

        return "web/inscripcion/periodosvigencia/form :: perVRegistrado";
    }

    public List<MuestrasInscritas> periodosVigenciaXMuestras(Date fechaInicial,
                                                             Date fechaFinal, Long idPrograma) throws Exception {

        List<MuestrasInscritas> muestrasInscritasList = new ArrayList<>();

        List<Muestras> muestrasLists = muestraService.findByFechaInicialBetween(fechaInicial, fechaFinal);
        Optional<PeriodosVigencia> periodosVigencia = periodosVigenciaService.find(idPrograma);

        for (int i = 0; i < muestrasLists.size(); i++) {
            MuestrasInscritas muestrasInscritas = new MuestrasInscritas();
            muestrasInscritas.setIdMuestra(muestrasLists.get(i).getMuestraId());
            muestrasInscritas.setNombreMuestra(muestrasLists.get(i).getNumeroMuestra());

            if (inscripcionMuestrasService.findMuestraInscrita(muestrasLists.get(i), periodosVigencia.get().getInscripProgramaId(),
                    periodosVigencia.get().getInscripProgramaId().getIdUsuarioLabSedes()).isPresent()) {
                muestrasInscritas.setEstado(true);
            } else {
                muestrasInscritas.setEstado(false);
            }
            muestrasInscritasList.add(i, muestrasInscritas);
        }

        return muestrasInscritasList;
    }
}
