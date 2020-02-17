package com.proasecal.software.web.controller.administrar;

import com.proasecal.software.web.entity.administrar.Metodos;
import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.parametricas.Pager;
import com.proasecal.software.web.service.administrar.MetodoService;
import com.proasecal.software.web.service.seguridad.FiltrosSeccionesService;
import com.proasecal.software.web.service.seguridad.SeccionesService;
import com.proasecal.software.web.util.Notify;
import com.proasecal.software.web.service.administrar.MensurandoService;
import com.proasecal.software.web.service.administrar.ProgramasService;
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

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value="/metodos")
public class MetodosController {

    @Autowired
	MetodoService metodoService;
	@Autowired
	MensurandoService mensurandoService;
	@Autowired
	ProgramasService programaService;		
	@Autowired
    SeccionesService seccionesService;
	@Autowired
    FiltrosSeccionesService filtrosSeccionesService;

	@PersistenceContext
	private EntityManager em;	
	
	// atributos para la paginacion
    private static int currentPage = 1;
    private static int pagSize = 20;
    private static String sortColumn = "metodoId";
    private static String sortO = "ASC";
    private static String programaFront  ="_";
    private static String mensurandoFront ="_";
    private static String nombreFront="_";
    private static String grupoFront   ="_";
    private static String codProsecalFront = "_";
    private static Boolean estadoFront    =true;
    private static final int[] PAGE_SIZES = {5, 10, 20, 50};
    private static final int BUTTONS_TO_SHOW = 3;

	
    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam("programaFront") Optional<String> programaFront,
                             @RequestParam("mensurandoFront") Optional<String> mensurandoFront,
                             @RequestParam("nombreFront")Optional<String> nombreFront,
                             @RequestParam("grupoFront")Optional<String> grupoFront,
                             @RequestParam("codProsecalFront")Optional<String> codProsecalFront,
                             @RequestParam("estadoFront")Optional<Boolean> estadoFront,
                             @RequestParam("nameColumn") Optional<String> nameColumn,
                             @RequestParam("sortBy") Optional<String> sortBy,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size,
                             @RequestParam("save") Optional<String> save,
                             @RequestParam("delete") Optional<Boolean> delete) {
      
        sortColumn = nameColumn.isPresent() ? nameColumn.get() : "idMensurandos.idPrograma.nombrePrograma";
        sortO = sortBy.isPresent() ? sortBy.get() : "ASC";
        currentPage = page.isPresent() ? page.get() : 1;
        pagSize = size.isPresent() ? size.get() : 20;
        this.programaFront      =programaFront.isPresent()?programaFront.get():"";
        this.mensurandoFront     =mensurandoFront.isPresent()?mensurandoFront.get():"";
        this.nombreFront    =nombreFront.isPresent()?nombreFront.get():"";
        this.grupoFront       =grupoFront.isPresent()?grupoFront.get():"";
        this.codProsecalFront= codProsecalFront.isPresent()?codProsecalFront.get():"";
        this.estadoFront         = estadoFront.isPresent()?estadoFront.get():true;

    

        int evalPageSize = size.orElse(pagSize);
        int evalPage = (page.orElse(0) < 1) ? currentPage : page.get() - 1;
        Page<Metodos> metodos =
                metodoService.ListPaginated(
                		programaFront.orElse(this.programaFront),
                		mensurandoFront.orElse(this.mensurandoFront),
                		nombreFront.orElse(this.nombreFront),
                		grupoFront.orElse(this.grupoFront),
                		codProsecalFront.orElse(this.codProsecalFront),
                		estadoFront.orElse(true),
                        PageRequest.of(
                        page.orElse(currentPage) - 1, pagSize,new Sort(Sort.Direction.valueOf(sortO),sortColumn, "metodoId")
                        ));
   
        Pager pager = new Pager(metodos.getTotalPages(), metodos.getNumber(), BUTTONS_TO_SHOW);

        ModelAndView modelAndView = new ModelAndView("web/administrar/metodos/list");
        
        modelAndView.addObject("filtrosList", filtrosSeccionesService.getListFiltros(seccionesService.findByName("metodos")));
        modelAndView.addObject("metodos", metodos);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("sort", sortBy.orElse(sortO));
        modelAndView.addObject("nameColumn", nameColumn.orElse(sortColumn));           
        modelAndView.addObject("programaFront", programaFront.orElse("").equalsIgnoreCase("_") ? "" : programaFront.orElse(""));
        modelAndView.addObject("mensurandoFront", mensurandoFront.orElse("").equalsIgnoreCase("_") ? "" : mensurandoFront.orElse(""));
        modelAndView.addObject("nombreFront", nombreFront.orElse("").equalsIgnoreCase("_") ? "" : nombreFront.orElse(""));
        modelAndView.addObject("grupoFront", grupoFront.orElse("").equalsIgnoreCase("_") ? "" : grupoFront.orElse(""));
        modelAndView.addObject("codProsecalFront", codProsecalFront.orElse("").equalsIgnoreCase("_") ? "" : codProsecalFront.orElse(""));
        modelAndView.addObject("estadoFront", estadoFront.orElse(true));
		modelAndView.addObject("programasList",programaService.list());
		modelAndView.addObject("mensurandoList",mensurandoService.list());
        

        if(save.isPresent()) {
        	if(save.get().equalsIgnoreCase("create")) {
        		modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Método creado correctamente"));
        	}
        	else {
        		modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Método modificado correctamente"));
        	}
        }
        //Si Borra un metodo
        if (delete.isPresent()){
            if(delete.get()){
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Método eliminado correctamente"));
            }else{
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo eliminar el Método"));
            }
        }        
        return modelAndView;
    }    
	
    @RequestMapping(value="/create")
    public ModelAndView form() {
        ModelAndView modelAndView = new ModelAndView("web/administrar/metodos/form");
        modelAndView.addObject("metodosForm",new Metodos());
        modelAndView.addObject("programas",programaService.list());              
        return modelAndView;
    } 
    
	
	@GetMapping(value = "/edit/{id}")
	public ModelAndView edit(@PathVariable long id) {
		ModelAndView modelAndView = new ModelAndView("web/administrar/metodos/form");
		Optional<Metodos> metodos = this.metodoService.find(id);
		
		if( !metodos.isPresent() ) {
			modelAndView.addObject("metodosForm", new Metodos());
			modelAndView.addObject("notify", Notify.ERROR("ERROR", "El metodo a consultar no existe"));
			return modelAndView;
		}
		
        List<Mensurandos> models = mensurandoService.obtenerMensurandosxPrograma(metodos.get().getIdMensurandos().getIdPrograma());
        modelAndView.addObject("models3", models);
        modelAndView.addObject("programas",programaService.list());       
        modelAndView.addObject("metodosForm", metodos.get());
		return modelAndView;
	}   
	
	@PostMapping(value = "/save")
	public ModelAndView save(@Valid @ModelAttribute("metodosForm") Metodos metodosForm, BindingResult bindingResult, RedirectAttributes redirectAttributes,
			ModelAndView model) {
		ModelAndView modelAndView = new ModelAndView("web/administrar/metodos/form");
		if(this.metodoService.exist(metodosForm)) {
			bindingResult.rejectValue("codProasecal", "error","La combinación Código Proasecal y Mensurando debe ser única.");
		}			

		if(bindingResult.hasErrors()){
            model.setViewName("web/administrar/metodos/form");
            model.addObject("metodosForm", metodosForm);     
			model.addObject("programas",programaService.list());     			
		    List<Mensurandos> models = mensurandoService.obtenerMensurandosxPrograma(metodosForm.getIdMensurandos().getIdPrograma());				
	        model.addObject("models3", models);
            return model;
        }	
		
		String ir="create";
		if(metodosForm.getMetodoId()!=null&&metodosForm.getMetodoId()!=0) {
			ir="update";			
		}
			
		try {
			this.metodoService.save(metodosForm);
		} catch (Exception e) {
	        modelAndView.addObject("programas",programaService.list());  
		    List<Mensurandos> models = mensurandoService.obtenerMensurandosxPrograma(metodosForm.getIdMensurandos().getIdPrograma());				
		    modelAndView.addObject("models3", models);	        
        	if(ir.equalsIgnoreCase("create")) {
        		modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo crear el Método"));
        	}
        	else {
        		modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo modificar el Método"));
        	}			
			
			return modelAndView;
		}

		redirectAttributes.addAttribute("save",ir);
		return new ModelAndView("redirect:/metodos/list");
	}   
	
    @RequestMapping(value = "/delete")
    public ModelAndView form(@RequestParam("metodoId") String metodoId, ModelAndView model, RedirectAttributes
            redirectAttributes){
        
    	try {
    		metodoService.borrarXId(Long.parseLong(metodoId));
            //Si Borra un metodo
            redirectAttributes.addAttribute("delete",true );
            return new ModelAndView("redirect:/metodos/list");    		
		} catch (Exception e) {
            redirectAttributes.addAttribute("delete",false);
            return new ModelAndView("redirect:/metodos/list");
		}
    }
    
    @RequestMapping("/obtMensurandos")
    public String obtenerMensurandosxPrograma(@RequestParam("idPrograma") String idPrograma, Model model) {
        Programas programa = new Programas();
        programa.setProgramaId(Long.valueOf(idPrograma).longValue());
        List<Mensurandos> models = mensurandoService.obtenerMensurandosxPrograma(programa);
        model.addAttribute("models3", models);
        return "web/administrar/metodos/form :: models3";
    }	
}