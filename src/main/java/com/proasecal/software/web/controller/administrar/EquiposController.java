package com.proasecal.software.web.controller.administrar;

import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.proasecal.software.web.entity.administrar.Equipos;
import com.proasecal.software.web.entity.parametricas.Pager;
import com.proasecal.software.web.service.administrar.EquipoService;
import com.proasecal.software.web.service.administrar.ProgramasService;
import com.proasecal.software.web.service.seguridad.FiltrosSeccionesService;
import com.proasecal.software.web.service.seguridad.SeccionesService;
import com.proasecal.software.web.util.Notify;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping(value = "equipos")
public class EquiposController {

	@Autowired
	EquipoService equipoService;	
	@Autowired
    FiltrosSeccionesService filtrosSeccionesService;
	@Autowired
    SeccionesService seccionesService;
	@Autowired
    ProgramasService programasService;	
	
	// atributos para la paginacion
    private static int currentPage = 1;
    private static int pagSize = 5;
    private static String sortColumn = "idPrograma.nombrePrograma";
    private static String sortO = "ASC";
    private static String programaFront  ="_";
    private static String nombreFront="_";
    private static String grupoFront   ="_";
    private static String codProsecalFront = "_";
    private static Boolean estadoFront    =true;
    private static final int[] PAGE_SIZES = {5, 10, 20, 50};
    private static final int BUTTONS_TO_SHOW = 3;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam("programaFront") Optional<String> programaFront,
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
      
        sortColumn = nameColumn.isPresent() ? nameColumn.get() : "idPrograma.nombrePrograma";
        sortO = sortBy.isPresent() ? sortBy.get() : "ASC";
        currentPage = page.isPresent() ? page.get() : 1;
        pagSize = size.isPresent() ? size.get() : 20;
        this.programaFront      =programaFront.isPresent()?programaFront.get():"";
        this.nombreFront    =nombreFront.isPresent()?nombreFront.get():"";
        this.grupoFront       =grupoFront.isPresent()?grupoFront.get():"";
        this.codProsecalFront= codProsecalFront.isPresent()?codProsecalFront.get():"";
        this.estadoFront         = estadoFront.isPresent()?estadoFront.get():true;

        

        int evalPageSize = size.orElse(pagSize);
        Page<Equipos> equipos =
        		equipoService.ListPaginated(
                		programaFront.orElse(this.programaFront),
                		nombreFront.orElse(this.nombreFront),
                		grupoFront.orElse(this.grupoFront),
                		codProsecalFront.orElse(this.codProsecalFront),
                		estadoFront.orElse(true),
                        PageRequest.of(
                        page.orElse(currentPage) - 1, pagSize,new Sort(Sort.Direction.valueOf(sortO), sortColumn, "equipoId")
                        ));
   
        Pager pager = new Pager(equipos.getTotalPages(), equipos.getNumber(), BUTTONS_TO_SHOW);

        ModelAndView modelAndView = new ModelAndView("web/administrar/equipos/list");
          
        modelAndView.addObject("filtrosList", filtrosSeccionesService.getListFiltros(seccionesService.findByName("equipos")));
        modelAndView.addObject("equipos", equipos);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("sort", sortBy.orElse(sortO));
        modelAndView.addObject("nameColumn", nameColumn.orElse(sortColumn));          
        modelAndView.addObject("programaFront", programaFront.orElse("").equalsIgnoreCase("_") ? "" : programaFront.orElse(""));
        modelAndView.addObject("nombreFront", nombreFront.orElse("").equalsIgnoreCase("_") ? "" : nombreFront.orElse(""));
        modelAndView.addObject("grupoFront", grupoFront.orElse("").equalsIgnoreCase("_") ? "" : grupoFront.orElse(""));
        modelAndView.addObject("codProsecalFront", codProsecalFront.orElse("").equalsIgnoreCase("_") ? "" : codProsecalFront.orElse(""));
        modelAndView.addObject("estadoFront", estadoFront.orElse(true));
		modelAndView.addObject("programasList",programasService.list());        

		if(save.isPresent()) {
        	if(save.get().equalsIgnoreCase("create")) {
        		modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Equipo creado correctamente"));
        	}
        	else {
        		modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Equipo modificado correctamente"));
        	}
        }
        //Si Borra un equipo
        if (delete.isPresent()){
            if(delete.get()){
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Equipo eliminado correctamente"));
            }else{
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo eliminar el Equipo"));
            }
        }        
        return modelAndView;
    }      

    @RequestMapping(value="/create")
    public ModelAndView form() {
        ModelAndView modelAndView = new ModelAndView("web/administrar/equipos/form");
        modelAndView.addObject("equiposForm",new Equipos());   
		modelAndView.addObject("programasList",programasService.list());  
        return modelAndView;
    }    
    
	@GetMapping(value = "/edit/{id}")
	public ModelAndView edit(@PathVariable long id) {
		ModelAndView modelAndView = new ModelAndView("web/administrar/equipos/form");
		Optional<Equipos> equipos = this.equipoService.find(id);
		
		if( !equipos.isPresent() ) {
			modelAndView.addObject("equiposForm", new Equipos());
			modelAndView.addObject("notify", Notify.ERROR("ERROR", "El equipo a consultar no existe"));
			modelAndView.addObject("programasList",programasService.list());  
			return modelAndView;
		}		
        modelAndView.addObject("equiposForm", equipos.get());
		modelAndView.addObject("programasList",programasService.list());  
		return modelAndView;
	}       
    
	@PostMapping(value = "/save")
	public ModelAndView save(@Valid @ModelAttribute("equiposForm") Equipos equiposForm, BindingResult bindingResult, RedirectAttributes redirectAttributes,
			ModelAndView model) {
		ModelAndView modelAndView = new ModelAndView("web/administrar/equipos/form");
		
		if(this.equipoService.exist(equiposForm)) {
			bindingResult.rejectValue("codProasecal", "error", "La combinación Código Proasecal y Programa debe ser única.");
		}	

		if(bindingResult.hasErrors()){
            model.setViewName("web/administrar/equipos/form");
            model.addObject("equiposForm", equiposForm);   
    		model.addObject("programasList",programasService.list());  
            return model;
        }	
		
		String ir="create";
		if(equiposForm.getEquipoId()!=null&&equiposForm.getEquipoId()!=0) {
			ir="update";			
		}
			
		try {
			this.equipoService.save(equiposForm);
		} catch (Exception e) {
        	if(ir.equalsIgnoreCase("create")) {
        		modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo crear el Equipo"));
        	}
        	else {
        		modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo modificar el Equipo"));
        	}		
    		modelAndView.addObject("programasList",programasService.list());  
			return modelAndView;
		}
		redirectAttributes.addAttribute("save",ir);
		return new ModelAndView("redirect:/equipos/list");
	} 
	
    @RequestMapping(value = "/delete")
    public ModelAndView form(@RequestParam("equipoId") String equipoId, ModelAndView model, RedirectAttributes
            redirectAttributes){
        
    	try {
    		equipoService.borrarXId(Long.parseLong(equipoId));
            //Si Borra un metodo
            redirectAttributes.addAttribute("delete",true );
            return new ModelAndView("redirect:/equipos/list");
		} catch (Exception e) {
	          redirectAttributes.addAttribute("delete",false);
	          return new ModelAndView("redirect:/equipos/list");
		}
    }	
}