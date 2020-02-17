package com.proasecal.software.web.controller.seguridad;

import com.proasecal.software.web.entity.parametricas.Pager;
import com.proasecal.software.web.entity.seguridad.Roles;
import com.proasecal.software.web.service.seguridad.ModuloService;
import com.proasecal.software.web.service.seguridad.PermisoService;
import com.proasecal.software.web.service.seguridad.RolService;
import com.proasecal.software.web.util.Notify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping(value = "/roles")
@PropertySource("classpath:static/properties/msg.properties")
public class RolesController {
    @Autowired
    RolService rolService;
    @Autowired
    PermisoService permisoService;
    @Autowired
    ModuloService moduloService;

    @Autowired
    Environment env;

    private static int currentPage = 1;
    private static int pagSize = 5;
    private static String sortColumn = "idRoles";
    private static String sortO = "DESC";
    private static String nombreRol  ="";
    private static String descripcion="";
    private static Boolean estado    =true;
    private static final int[] PAGE_SIZES = {5, 10,20};
    private static final int BUTTONS_TO_SHOW = 3;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView main(@RequestParam("nombreRol") Optional<String> nombreRol,
                             @RequestParam("descripcion") Optional<String> descripcion,
                             @RequestParam("estado")Optional<Boolean> estado,
                             @RequestParam("nameColumn") Optional<String> nameColumn,
                             @RequestParam("sortBy") Optional<String> sortBy,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size,
                             @RequestParam("save") Optional<String> save,
                             @RequestParam("delete") Optional<Boolean> delete) {
        ModelAndView modelAndView = new ModelAndView("web/security/rolesAdminList");

        pagSize = size.isPresent() ? size.get() : 20;
        Page<Roles> listaRoles =
                rolService.listPaginated(
                        nombreRol.orElse(this.nombreRol),
                        descripcion.orElse(this.descripcion),
                        estado.orElse(true),
                        PageRequest.of(
                                page.orElse(currentPage) - 1, pagSize,new Sort(Sort.Direction.valueOf(sortO),sortColumn)
                        ));

        int evalPageSize = size.orElse(pagSize);
        Pager pager = new Pager(listaRoles.getTotalPages(), listaRoles.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("listaRoles",listaRoles);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("sort", sortBy.orElse(sortO));
        modelAndView.addObject("nombreRol", nombreRol.orElse("").equalsIgnoreCase("_") ? "" : nombreRol.orElse(""));
        modelAndView.addObject("descripcion", descripcion.orElse("").equalsIgnoreCase("_") ? "" : descripcion.orElse(""));
        modelAndView.addObject("estado", estado.orElse(true));

        if(save.isPresent()) {
        	if(save.get().equalsIgnoreCase("create")) {
        		modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Rol creado correctamente"));
        	}
        	else {
        		modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Rol modificado correctamente"));
        	}
        }
        return modelAndView;
    }

    @RequestMapping(value="/create",method= RequestMethod.GET )
    public ModelAndView usuarioCreate(){
        ModelAndView modelAndView = new ModelAndView("web/security/rolesAdminForm");
        modelAndView.addObject("rolesForm", new Roles());
        return modelAndView;
    }


    @RequestMapping(value = "updateRoles/{id}",method = RequestMethod.GET)
    public ModelAndView updateRoles(@PathVariable long id ){
        ModelAndView modelAndView = new ModelAndView("web/security/rolesAdminForm");
        Roles rol = rolService.obtenerRol(id);
        modelAndView.addObject("rolesForm", rol);
        return modelAndView;
    }

    @RequestMapping(value = "/guardarRol",method = RequestMethod.POST)
    public ModelAndView updateRoles(@Valid @ModelAttribute("rolesForm")Roles rolesForm, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                    ModelAndView model){
        rolesForm.setNombreRol(rolesForm.getNombreRol().toUpperCase().trim());
        if (rolService.validarSiExisteRol(rolesForm.getNombreRol())&&rolesForm.getIdRoles()==0){
            bindingResult.rejectValue("nombreRol", "error", "El nombre del rol debe ser unico.");
        }else if(rolService.validarSiExisteRolParaLab()&&rolesForm.getCodigoProasecal()){
            bindingResult.rejectValue("codigoProasecal", "error","Ya existe un rol para laboratorios");
        }
        if(bindingResult.hasErrors()){
            model.setViewName("web/security/rolesAdminForm");
            model.addObject("rolesForm", rolesForm);
            return model;
        }

		String ir="create";
		if(rolesForm.getIdRoles()!=0) {
			ir="update";			
		}
		
        rolService.guardarRol(rolesForm);
    	redirectAttributes.addAttribute("save",ir);
        return new ModelAndView("redirect:/roles/list");
    }
}
