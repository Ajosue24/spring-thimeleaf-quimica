package com.proasecal.software.web.controller.administrar;


import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.entity.parametricas.Pager;
import com.proasecal.software.web.service.administrar.ProgramasService;
import com.proasecal.software.web.service.parametricas.TipoProgramasService;
import com.proasecal.software.web.service.seguridad.FiltrosSeccionesService;
import com.proasecal.software.web.service.seguridad.SeccionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping(value = "/programas")
public class ProgramasController {

	@Autowired
    ProgramasService programasService;
	@Autowired
    TipoProgramasService tipoProgramasService;
	@Autowired
    SeccionesService seccionesService;
	@Autowired
    FiltrosSeccionesService filtrosSeccionesService;

    private static int currentPage = 1;
    private static int pagSize = 5;
    private static String sortColumn = "programaId";
    private static String sortO = "DESC";
    private static String nameFind = "_";
    private static String columnFind = "_";
    private static final int[] PAGE_SIZES = { 5, 10};
    private static final int BUTTONS_TO_SHOW = 3;

    @RequestMapping(value="/list",method= RequestMethod.GET)
	public ModelAndView list(@RequestParam("find") Optional<String> find,
                             @RequestParam("nameColumn") Optional<String> nameColumn,
                             @RequestParam("findColumn") Optional<String> findColumn,
                             @RequestParam("sortBy") Optional<String> sortBy,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size){
            find.ifPresent(i -> nameFind = i);
            nameColumn.ifPresent(j -> sortColumn = j);
            sortBy.ifPresent(k -> sortO = k);
            page.ifPresent(l -> currentPage = l);
            size.ifPresent(m -> pagSize = m);
            findColumn.ifPresent(n->columnFind=n);

            int evalPageSize = size.orElse(pagSize);
            int evalPage = (page.orElse(0) < 1) ? currentPage : page.get() - 1;
        Page<Programas> programasList = programasService.ListPaginated(
                find.orElse(nameFind),
                findColumn.orElse(columnFind),
                sortBy.orElse(sortO),
                nameColumn.orElse(sortColumn),
                PageRequest.of(
                        page.orElse(currentPage)-1, pagSize,
                                Sort.Direction.valueOf(sortO), sortColumn, "programaId"));

        Pager pager = new Pager(programasList.getTotalPages(),programasList.getNumber(),BUTTONS_TO_SHOW);


            ModelAndView modelAndView = new ModelAndView("web/administrar/programas/list");

            modelAndView.addObject("filtrosList",filtrosSeccionesService.getListFiltros(seccionesService.findByName("programas")));
            modelAndView.addObject("listaProgramas",programasList);
            modelAndView.addObject("selectedPageSize", evalPageSize);
            modelAndView.addObject("pageSizes", PAGE_SIZES);
            modelAndView.addObject("pager", pager);
            modelAndView.addObject("find", find.orElse("").equalsIgnoreCase("_")?"":find.orElse(""));
            modelAndView.addObject("pager", pager);
            modelAndView.addObject("filtroSelected",findColumn.orElse(columnFind));
            modelAndView.addObject("sort",sortBy.orElse(sortO));

            return  modelAndView;
	}

    @RequestMapping(value="/form",method= RequestMethod.GET)
    public ModelAndView form() {
        ModelAndView modelAndView = new ModelAndView("web/administrar/programas/form");
        modelAndView.addObject("programasForm",new Programas());
        modelAndView.addObject("tiposProgramas",tipoProgramasService.list());
        return modelAndView;
    }

    @RequestMapping(value="/save",method= RequestMethod.POST)
    public ModelAndView save(@Valid @ModelAttribute("programasForm") Programas programasForm, BindingResult bindingResult, ModelAndView model) {
        if(bindingResult.hasErrors()){
            //bindingResult.rejectValue("error", "error","Error en el formulario");
            model.setViewName("web/administrar/programas/form");
            model.addObject("programasForm", programasForm);
            model.addObject("tiposProgramas",tipoProgramasService.list());
            return model;
        }
        programasService.save(programasForm);
        return form();
    }

    @RequestMapping(value="/edit/{programaId}",method= RequestMethod.GET )
    public ModelAndView editarCliente(@PathVariable long programaId){
        Programas programas = programasService.getProgramas(programaId);
        ModelAndView modelAndView = form().addObject("programasForm",programas);
        return modelAndView;
    }
}
