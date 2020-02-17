package com.proasecal.software.web.controller.administrar;


import com.proasecal.software.web.entity.administrar.Reactivos;
import com.proasecal.software.web.entity.parametricas.Pager;
import com.proasecal.software.web.service.administrar.ProgramasService;
import com.proasecal.software.web.service.administrar.ReactivoService;
import com.proasecal.software.web.service.seguridad.FiltrosSeccionesService;
import com.proasecal.software.web.service.seguridad.SeccionesService;
import com.proasecal.software.web.util.Notify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping(value = "/reactivos")
public class ReactivosController {
	private final Logger LOG = LoggerFactory.getLogger(ReactivosController.class);

	@Autowired
	private ReactivoService reactivoService;
	@Autowired
	private FiltrosSeccionesService filtrosSeccionesService;
	@Autowired
	private SeccionesService seccionesService;
	@Autowired
	private ProgramasService programasService;

	private static int currentPage = 1;
	private static int pagSize = 5;
	private static String sortColumn = "idPrograma.nombrePrograma";
	private static String sortO = "ASC";
	private static final int[] PAGE_SIZES = { 5, 10,20 ,50};
	private static final int BUTTONS_TO_SHOW = 3;
    private static String codProasecal="_";
	private static String programas ="_";
    private static String nombreReactivo= "_";
    private static String grupo="_";
    private static String estado = "true";

	@GetMapping(value = "/list")
	public ModelAndView index(
            @RequestParam("programas") Optional<String> programas,
            @RequestParam("nombreReactivo") Optional<String>         nombreReactivo,
            @RequestParam("grupo") Optional<String>          grupo,
            @RequestParam("codProasecal") Optional<String>   codProasecal,
	        @RequestParam("estado") Optional<String> estado,
			@RequestParam("nameColumn") Optional<String> nameColumn,
            @RequestParam("sortBy") Optional<String> sortBy,
			@RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("save") Optional<String> save,            
            @RequestParam("delete") Optional<Boolean> delete) {
		LOG.info("redireccionara a la vista");
		ModelAndView modelAndView = new ModelAndView("web/administrar/reactivos/list");
		LOG.info("ingreso a la vista");
		sortColumn = nameColumn.isPresent() ? nameColumn.get() : "idPrograma.nombrePrograma";		
		sortO = sortBy.isPresent() ? sortBy.get() : "ASC";
		currentPage = page.isPresent() ? page.get() : 1;	
		pagSize = size.isPresent() ? size.get() : 20;
        this.codProasecal      =codProasecal.isPresent()?codProasecal.get():"";
        this.programas      =programas.isPresent()?programas.get():"";
        this.grupo    =grupo.isPresent()?grupo.get():"";
        this.nombreReactivo       =nombreReactivo.isPresent()?nombreReactivo.get():"";
        this.estado         = estado.isPresent()? estado.get():"";
		int evalPageSize = size.orElse(pagSize);
		int evalPage = (page.orElse(0) < 1) ? currentPage : page.get() - 1;


		Page<Reactivos> reactivosList = this.reactivoService.ListPaginated(codProasecal.orElse(this.codProasecal),
                grupo.orElse(this.grupo),programas.orElse(this.programas),nombreReactivo.orElse(this.nombreReactivo),estado.orElse(this.estado),
                PageRequest.of(
                        page.orElse(currentPage) - 1, pagSize,new Sort(Sort.Direction.valueOf(sortO),sortColumn, "reactivoId")
                ));

		//
		Pager pager = new Pager(reactivosList.getTotalPages(), reactivosList.getNumber(), BUTTONS_TO_SHOW);
		
		 modelAndView.addObject("filtrosList",filtrosSeccionesService.getListFiltros(seccionesService.findByName("Reactivos")));
         modelAndView.addObject("reactivos",reactivosList);
         modelAndView.addObject("selectedPageSize", evalPageSize);
         modelAndView.addObject("pageSizes", PAGE_SIZES);
         modelAndView.addObject("pager", pager);
         modelAndView.addObject("sort",sortBy.orElse(sortO));
        modelAndView.addObject("nameColumn", nameColumn.orElse(sortColumn));            
        modelAndView.addObject("codProasecal", codProasecal.orElse("").equalsIgnoreCase("_") ? "" : codProasecal.orElse(""));
        modelAndView.addObject("nombreReactivo", nombreReactivo.orElse("").equalsIgnoreCase("_") ? "" : nombreReactivo.orElse(""));
        modelAndView.addObject("grupo", grupo.orElse("").equalsIgnoreCase("_") ? "" : grupo.orElse(""));
        modelAndView.addObject("programas", programas.orElse("").equalsIgnoreCase("_") ? "" : programas.orElse(""));
        modelAndView.addObject("estado", this.estado);
		modelAndView.addObject("programasList",programasService.list());
        if(save.isPresent()) {
        	if(save.get().equalsIgnoreCase("create")) {
        		modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Reactivo creado correctamente"));
        	}
        	else {
        		modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Reactivo modificado correctamente"));
        	}
        }        
        //Si Borra un cliente
        if (delete.isPresent()){
            if(delete.get()){
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Reactivo eliminado correctamente"));
            }else{
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo eliminar el Reactivo"));
            }
        }
        LOG.info("Va a retornar");
         return modelAndView;
	}

	@GetMapping(value = "/create")
	public ModelAndView form() {
		ModelAndView modelAndView = new ModelAndView("web/administrar/reactivos/form");
		Reactivos reactivoForm = new Reactivos();
		modelAndView.addObject("programasList",programasService.list());
		modelAndView.addObject("reactivoForm", reactivoForm);
		return modelAndView;
	}

	@PostMapping(value = "/save")
	public ModelAndView save(@Valid @ModelAttribute("reactivoForm") Reactivos reactivoForm, BindingResult bindingResult, RedirectAttributes redirectAttributes,
							 ModelAndView model) {
		ModelAndView modelAndView = new ModelAndView("web/administrar/reactivos/form");
		
		if(this.reactivoService.exist2(reactivoForm)) {
			bindingResult.rejectValue("codProasecal", "error", "La combinación código Proasecal y Programa debe ser única.");
		}
		if (bindingResult.hasErrors()) {
			model.setViewName("web/administrar/reactivos/form");
			model.addObject("reactivoForm", reactivoForm);
			model.addObject("programasList",programasService.list());			
			return model;
		}
		String ir="create";
		if(reactivoForm.getReactivoId()!=null&&reactivoForm.getReactivoId()!=0) {
			ir="update";			
		}		
		
		try {
			this.reactivoService.save(reactivoForm);
		} catch (Exception e) {
			modelAndView.addObject("notify", Notify.ERROR("ERROR", "Ocurrio un error al procesar la solicitud."));
        	if(ir.equalsIgnoreCase("create")) {
        		modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo crear el Reactivo"));
        	}
        	else {
        		modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo modificar el Reactivo"));
        	}				
			return modelAndView;
		}

		redirectAttributes.addAttribute("save",ir);
		return new ModelAndView("redirect:/reactivos/list");
	}

	@GetMapping(value = "/edit/{id}")
	public ModelAndView edit(@PathVariable long id) {
		ModelAndView modelAndView = new ModelAndView("web/administrar/reactivos/form");
		Optional<Reactivos> reactivo = this.reactivoService.find(id);

		if (!reactivo.isPresent()) {
			modelAndView.addObject("reactivoForm", new Reactivos());
			modelAndView.addObject("notify", Notify.ERROR("ERROR", "El reactivo a consultar no existe"));
			return modelAndView;
		}
		modelAndView.addObject("programasList",programasService.list());
		modelAndView.addObject("reactivoForm", reactivo.get());
		return modelAndView;
	}


    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam("reactivoID") String reactivoID, ModelAndView model, RedirectAttributes
            redirectAttributes){
//        LOG.info("Se prepara para borrar"+reactivoID);
    	try {      
    		reactivoService.deleteById(Long.parseLong(reactivoID));
            //Si Borra un cliente
            redirectAttributes.addAttribute("delete",true );
            return new ModelAndView("redirect:/reactivos/list");
		} catch (Exception e) {
            LOG.info("Error al Borrar");
            redirectAttributes.addAttribute("delete",false);
            return new ModelAndView("redirect:/reactivos/list");
		}
    }
}
