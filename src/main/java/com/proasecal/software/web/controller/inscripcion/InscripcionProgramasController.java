package com.proasecal.software.web.controller.inscripcion;

import com.proasecal.software.web.entity.administrar.Laboratorios;
import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.entity.administrar.Sedes;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import com.proasecal.software.web.entity.inscripcion.InscripcionProgramas;
import com.proasecal.software.web.entity.inscripcion.PeriodosVigencia;
import com.proasecal.software.web.entity.parametricas.Pager;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import com.proasecal.software.web.repository.administrar.MuestraRepository;
import com.proasecal.software.web.repository.administrar.SedesRepository;
import com.proasecal.software.web.repository.seguridad.UsuariosLabSedesRespository;
import com.proasecal.software.web.service.administrar.LaboratorioService;
import com.proasecal.software.web.service.administrar.MuestraService;
import com.proasecal.software.web.service.administrar.ProgramasService;
import com.proasecal.software.web.service.administrar.SedesService;
import com.proasecal.software.web.service.inscripcion.InscripcionProgramasService;
import com.proasecal.software.web.service.seguridad.UsuarioService;
import com.proasecal.software.web.service.seguridad.UsuariosLabSedesService;
import com.proasecal.software.web.util.Notify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping(value="/inscripcionprogramas")
public class InscripcionProgramasController {

    @Autowired
    UsuarioService usuarioService;
    @Autowired
    UsuariosLabSedesRespository usuariosLabSedesRespository;
    @Autowired
    UsuariosLabSedesService usuariosLabSedesService;
    @Autowired
    SedesService sedesService;
    @Autowired
    SedesRepository sedesRepository;
    @Autowired
    MuestraService muestraService;
    @Autowired
    MuestraRepository muestraRepository;
    @Autowired
    InscripcionProgramasService inscripcionProgramaService ;
    @Autowired
    LaboratorioService laboratorioService;
    @Autowired
    ProgramasService programaService;


    private static int currentPage = 1;
    private static int pagSize = 20;
    private static String sortColumn = "inscripProgramaId";
    private static String sortO = "ASC";
    private static String laboratorio  ="_";
    private static String sede="_";
    private static String usuario   ="_";
    private static String programaFront = "_";
    private static String muestra = "_";
    private static final int[] PAGE_SIZES = {5, 10,20};
    private static final int BUTTONS_TO_SHOW = 3;


    @RequestMapping(value="/list")
    public ModelAndView list(@RequestParam("laboratorio") Optional<String> laboratorio,
                             @RequestParam("programaFront") Optional<String> programaFront,
                             @RequestParam("sede") Optional<String> sede,
                             @RequestParam("muestra")Optional<String> muestra,
                             @RequestParam("usuario")Optional<String> usuario,
                             @RequestParam("nameColumn") Optional<String> nameColumn,
                             @RequestParam("sortBy") Optional<String> sortBy,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size,
                             @RequestParam("delete") Optional<Boolean> delete,
                             @RequestParam("save") Optional<String> save,
                             @RequestParam("inscripcion") Optional<String> inscripcion) {

        ModelAndView modelAndView = new ModelAndView("web/inscripcion/inscripcionprogramas/list");
        sortColumn = nameColumn.isPresent() ? nameColumn.get() : "idUsuarioLabSedes.usuarioId.codProasecal";
        sortO = sortBy.isPresent() ? sortBy.get() : "ASC";
        currentPage = page.isPresent() ? page.get() : 1;
        pagSize = size.isPresent() ? size.get() : 20;
        int evalPageSize = size.orElse(pagSize);
        this.laboratorio  = laboratorio.isPresent()?laboratorio.get():"_";
        this.sede = sede.isPresent()?sede.get():"_";
        this.programaFront = programaFront.isPresent() ? programaFront.get() : "";
        this.usuario   = usuario.isPresent()?usuario.get():"_";
        this.muestra   = muestra.isPresent()?muestra.get():"_";

        Page<InscripcionProgramas> inscripProgramasLista ;
        if (this.muestra.equals("_")||this.muestra.equals("")){
             inscripProgramasLista = inscripcionProgramaService.filtro1(
                    validarLaboratorioString(laboratorio)[1],
                    sede.orElse(this.sede),
                    usuario.orElse(this.usuario),
                    programaFront.orElse(this.programaFront),
                    PageRequest.of(
                            page.orElse(currentPage) - 1, pagSize, new Sort(Sort.Direction.valueOf(sortO),sortColumn, "idUsuarioLabSedes.usuarioId.codProasecal")
                    ));
        }else{
             inscripProgramasLista = inscripcionProgramaService.filtro(
                    muestra.orElse(this.muestra),
                    validarLaboratorioString(laboratorio)[1],
                    sede.orElse(this.sede),
                    usuario.orElse(this.usuario),
                    programaFront.orElse(this.programaFront),
                    PageRequest.of(
                            page.orElse(currentPage) - 1, pagSize, new Sort(Sort.Direction.valueOf(sortO),sortColumn, "idUsuarioLabSedes.usuarioId.codProasecal")
                    ));
        }

        Pager pager = new Pager(inscripProgramasLista.getTotalPages(), inscripProgramasLista.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("laboratorio", laboratorio.orElse("").equalsIgnoreCase("_") ? "" : laboratorio.orElse(""));
        modelAndView.addObject("sede", sede.orElse("").equalsIgnoreCase("_") ? "" : sede.orElse(""));
        modelAndView.addObject("programaFront", programaFront.orElse("").equalsIgnoreCase("_") ? "" : programaFront.orElse(""));
        modelAndView.addObject("usuario", usuario.orElse("").equalsIgnoreCase("_") ? "" : usuario.orElse(""));
        modelAndView.addObject("muestra", muestra.orElse("").equalsIgnoreCase("_") ? "" : muestra.orElse(""));
        modelAndView.addObject("inscripcionProgramasList",inscripProgramasLista);


        if(inscripProgramasLista.getTotalElements() <= 0){
            modelAndView.addObject("notify", Notify.INFO("!Alert", "No se encuentran resultados de Programas inscritos"));
        }
        if(save.isPresent()){
            if(save.get().equalsIgnoreCase("exito")){
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Programa inscrito correctamente"));
            }else{
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo inscribir el nuevo Programa"));
            }
        }
        if (delete.isPresent()){
            if (delete.get().booleanValue() == true){
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Programa inscrito, eliminado correctamente"));
            }
            else{
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo eliminar el Programa inscrito"));
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

        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("sort", sortBy.orElse(sortO));
        modelAndView.addObject("nameColumn", nameColumn.orElse(sortColumn));             
        //aqui lista vigencia
        List<InscripcionProgramas> inscripRta = inscripProgramasLista.getContent();

        List<PeriodosVigencia> perVigenList = new ArrayList<PeriodosVigencia>();
        for (InscripcionProgramas inscPaso : inscripRta){
            Set<PeriodosVigencia> vigenPaso = inscPaso.getPeriodosVigenciaList();
            if (vigenPaso.size() > 0){
                List<PeriodosVigencia> perVigenPaso =  new ArrayList<PeriodosVigencia>();
                perVigenPaso.addAll(vigenPaso);
                Collections.sort(perVigenPaso, PeriodosVigencia.datePerComparator);
                perVigenList.add(perVigenPaso.get(perVigenPaso.size() -1));
            }else {
                PeriodosVigencia perVigenPaso = new PeriodosVigencia();
                perVigenList.add(perVigenPaso);
            }
        }
        modelAndView.addObject("listPerVigen",perVigenList );

        List<Laboratorios> listLaboratorios = laboratorioService.list();
        modelAndView.addObject("listLaboratorios",listLaboratorios );
        List<Programas> listProgramas = programaService.list();
        modelAndView.addObject("listProgramas",listProgramas );

        List<Sedes> listSedes = (List<Sedes>) sedesRepository.findAll();
        modelAndView.addObject("listSedes",listSedes );
        List<Muestras> listMuestras = (List<Muestras>) muestraRepository.findAll();
        modelAndView.addObject("listMuestras",listMuestras );
        List<UsuariosLabSedes> usuarioslabsedes = (List<UsuariosLabSedes>)  usuariosLabSedesRespository.findAll() ;
        List<Usuarios> listUsuarios = new ArrayList<>();
        for (int i =0 ; i < usuarioslabsedes.size(); i++) {
            Usuarios usuario1 = usuarioslabsedes.get(i).getUsuarioId();
            if (usuario1.getCodProasecal() != null) {
                listUsuarios.add(usuarioslabsedes.get(i).getUsuarioId());
            }
        }
        modelAndView.addObject("listUsuarios",listUsuarios );
        return modelAndView;
    }


    private String[] validarLaboratorioString(Optional<String> laboratorio){
        //logica para separar el id de lab con razon social
        String[] parts = new String[2];
        if(laboratorio.orElse(this.laboratorio).contains(" l ")){
            parts = laboratorio.orElse(this.laboratorio).split(" l ");
            if(parts.length!=2){
                String[] parts2 = new String[2];
                parts2[0]="";
                parts2[1]="";
                return  parts2;
            }
            return parts;
        }else{
            parts[0] = laboratorio.orElse(this.laboratorio);
            parts[1] = laboratorio.orElse(this.laboratorio);
            return parts;
        }
    }

    @RequestMapping(value="/create")
    public ModelAndView form(@RequestParam("save") Optional<String> save) {
        ModelAndView modelAndView = new ModelAndView("web/inscripcion/inscripcionprogramas/form");
        modelAndView.addObject("inscripcionprogramaForm",new InscripcionProgramas());
        List<Laboratorios> listLaboratorios = laboratorioService.list();
        modelAndView.addObject("listLaboratorios",listLaboratorios );
        List<Programas> listProgramas = programaService.list();
        modelAndView.addObject("listProgramas",listProgramas );
        if(save.isPresent()){
            modelAndView.addObject("notify", Notify.ERROR("ERROR",  "La combinación  Usuario y Programa debe ser única."));
        }
        return modelAndView;
    }

    @RequestMapping(value="/edit/{id}")
    public ModelAndView edit() {
        ModelAndView modelAndView = new ModelAndView("web/inscripcion/inscripcionprogramas/form");
        return modelAndView;
    }

    @RequestMapping(value="/save")
    public ModelAndView save(@Valid @ModelAttribute("inscripcionprogramaForm") InscripcionProgramas inscripcionprogramaForm, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                             ModelAndView model) {
        ModelAndView modelAndView = new ModelAndView("web/inscripcion/inscripcionprogramas/form");
        if(this.inscripcionProgramaService.valExist(inscripcionprogramaForm)){
            bindingResult.rejectValue("idUsuarioLabSedes","Error", "La combinación  Usuario y Programa debe ser única.");
        }

        String ir="create";
        if(bindingResult.hasErrors()){
            	
            model.setViewName("web/inscripcion/inscripcionprogramas/form");
            List<Laboratorios> listLaboratorios = laboratorioService.list();
            model.addObject("listLaboratorios",listLaboratorios );    
		    List<Sedes> models = sedesService.listSedesxIdlab(inscripcionprogramaForm.getIdSedes().getIdLaboratoriosSedes());				
	        model.addObject("models2", models);
	        List<UsuariosLabSedes> paso = usuariosLabSedesService.obtenerUsuariosPorSedes(inscripcionprogramaForm.getIdSedes().getIdSedes());
	        model.addObject("models3", paso);
            List<Programas> listProgramas = programaService.list();
            model.addObject("listProgramas",listProgramas );            
            model.addObject("inscripcionprogramaForm", inscripcionprogramaForm);
            return model;
        }

        if(inscripcionprogramaForm.getInscripProgramaId() !=null && inscripcionprogramaForm.getInscripProgramaId() !=0){
            ir = "update";
        }
        try {
            Set<PeriodosVigencia> pervigen = new HashSet();
            Set<InscripcionMuestras> insmuestr  = new HashSet();
            inscripcionprogramaForm.setPeriodosVigenciaList(pervigen);
            inscripcionprogramaForm.setInscripcionMuestrasList(insmuestr);
            this.inscripcionProgramaService.save(inscripcionprogramaForm);
            ir = "exito";
        }
        catch (Exception e ){
            modelAndView.addObject("programas",programaService.list());
            if(ir.equalsIgnoreCase("create")){
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo inscribir el Programa"));
            }else {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo inscribir el Programa"));
            }
            return modelAndView;
        }
        redirectAttributes.addAttribute("save",ir);
        return new ModelAndView("redirect:/inscripcionprogramas/list");
    }

    @RequestMapping(value="/delete")
    public ModelAndView delete(@RequestParam("inscripProgramaId") String inscripProgramaId, ModelAndView model, RedirectAttributes redirectAttributes){

        if ( inscripcionProgramaService.borrarXId(Long.valueOf(inscripProgramaId))){
            redirectAttributes.addAttribute("delete", true);
            return new ModelAndView("redirect:/inscripcionprogramas/list");
        }
        else{
            redirectAttributes.addAttribute("delete", false);
            return new ModelAndView("redirect:/inscripcionprogramas/list");
        }
    }

    @RequestMapping("/obtSedes")
    public String obtenerSedes(@RequestParam("laboratorioId")String laboratorio, Model model ){
        Laboratorios laboratorios = new Laboratorios();
        laboratorios.setIdLaboratoriosSedes(Long.valueOf(laboratorio));
        List<Sedes> models = sedesService.listSedesxIdlab(laboratorios);
        model.addAttribute("models2", models);
        return "web/inscripcion/inscripcionprogramas/form :: models2";
    }

    @RequestMapping("/obtUsuarios")
    public String obtenerUsuarios(@RequestParam("sedeId")String sede, Model model ){
        Sedes sedes = new Sedes();
        sedes.setIdSedes(Long.valueOf(sede));
        List<UsuariosLabSedes> paso = usuariosLabSedesService.obtenerUsuariosPorSedes(Long.valueOf(sede));
        List<UsuariosLabSedes> models = new ArrayList<UsuariosLabSedes>();
        for (int i =0 ; i < paso.size(); i++) {
            Usuarios usuario = paso.get(i).getUsuarioId();
            if (usuario.getCodProasecal() != null) {
                models.add(paso.get(i));
            }
        }
        model.addAttribute("models3", models);
        return "web/inscripcion/inscripcionprogramas/form :: models3";
    }
}