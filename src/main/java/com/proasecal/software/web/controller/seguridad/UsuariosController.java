package com.proasecal.software.web.controller.seguridad;


import com.proasecal.software.web.entity.parametricas.Pager;
import com.proasecal.software.web.entity.seguridad.Roles;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import com.proasecal.software.web.service.seguridad.FiltrosSeccionesService;
import com.proasecal.software.web.service.seguridad.RolService;
import com.proasecal.software.web.service.seguridad.SeccionesService;
import com.proasecal.software.web.service.seguridad.UsuarioService;
import com.proasecal.software.web.util.Notify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/usuarios")
@PropertySource("classpath:static/properties/msg.properties")
public class UsuariosController {

	@Autowired
	Environment env;
	@Autowired
	RolService rolService;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	SeccionesService seccionesService;
	@Autowired
	FiltrosSeccionesService filtrosSeccionesService;

	private static int currentPage = 1;
	private static int pagSize = 5;
	private static String sortColumn = "idUsuario";
	private static String sortO = "DESC";
	private static String nombreUsuario  ="_";
	private static String nombreApellido ="_";
	private static String codProasecal="_";
	private static String estado    ="true";
	private static final int[] PAGE_SIZES = {5, 10,20};
	private static final int BUTTONS_TO_SHOW = 3;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam("nombreUsuario") Optional<String> nombreUsuario,
							 @RequestParam("nombreApellido") Optional<String> nombreApellido,
							 @RequestParam("codProasecal")Optional<String> codProasecal,
							 @RequestParam("estado")Optional<String> estado,
							 @RequestParam("nameColumn") Optional<String> nameColumn,
							 @RequestParam("sortBy") Optional<String> sortBy,
							 @RequestParam("page") Optional<Integer> page,
							 @RequestParam("size") Optional<Integer> size,
							 @RequestParam("save") Optional<String> save,
							 @RequestParam("delete") Optional<Boolean> delete) {

		sortColumn = nameColumn.isPresent() ? nameColumn.get() : "idUsuario";
		sortO = sortBy.isPresent() ? sortBy.get() : "DESC";
		currentPage = page.isPresent() ? page.get() : 1;
		pagSize = size.isPresent() ? size.get() : 20;
		this.nombreUsuario      =nombreUsuario.isPresent()?nombreUsuario.get():"";
		this.nombreApellido     =nombreApellido.isPresent()?nombreApellido.get():"";
		this.codProasecal    =codProasecal.isPresent()?codProasecal.get():"";
		this.estado         = estado.isPresent()?estado.get():"true";
		ModelAndView modelAndView = new ModelAndView("web/security/usuariosAdminList");
		Page<Usuarios> usuariosList = usuarioService.ListPaginated(
				nombreUsuario.orElse(this.nombreUsuario),
				nombreApellido.orElse(this.nombreApellido),
				nombreApellido.orElse(this.nombreApellido),
				codProasecal.orElse(this.codProasecal),
				estado.orElse(this.estado),
				PageRequest.of(
						page.orElse(currentPage) - 1, pagSize,new Sort(Sort.Direction.valueOf(sortO),sortColumn)
				));


		Pager pager = new Pager(usuariosList.getTotalPages(), usuariosList.getNumber(), BUTTONS_TO_SHOW);
		int evalPageSize = size.orElse(pagSize);
		int evalPage = (page.orElse(0) < 1) ? currentPage : page.get() - 1;

		modelAndView.addObject("nombreUsuario", nombreUsuario.orElse("").equalsIgnoreCase("_") ? "" : nombreUsuario.orElse(""));
		modelAndView.addObject("nombreApellido", nombreApellido.orElse("").equalsIgnoreCase("_") ? "" : nombreApellido.orElse(""));
		modelAndView.addObject("codProasecal", codProasecal.orElse("").equalsIgnoreCase("_") ? "" : codProasecal.orElse(""));
		modelAndView.addObject("estado", estado.orElse("true"));
		modelAndView.addObject("usuariosList",usuariosList);
		modelAndView.addObject("selectedPageSize", evalPageSize);
		modelAndView.addObject("pageSizes", PAGE_SIZES);
		modelAndView.addObject("pager", pager);
		modelAndView.addObject("sort", sortBy.orElse(sortO));

        if(save.isPresent()) {
        	if(save.get().equalsIgnoreCase("create")) {
        		modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Usuario creado correctamente"));
        	}
        	else {
        		modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Usuario modificado correctamente"));
        	}
        }
		return modelAndView;
	}


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView usuarioCreate() {
		ModelAndView modelAndView = new ModelAndView("web/security/usuariosAdminForm");
		List<Roles> listaRoles = rolService.obtenerListaRoles();
		modelAndView.addObject("usuariosForm", new Usuarios());
		modelAndView.addObject("listaRoles", listaRoles);
		modelAndView.addObject("create", "true");
		Roles roles = listaRoles.stream().filter(rolName -> rolName.getCodigoProasecal()).findAny().orElse(new Roles());
		modelAndView.addObject("idParticipante", roles.getIdRoles());
		return modelAndView;
	}


	// editar
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public ModelAndView editArticle(@PathVariable long id) {
		ModelAndView modelAndView = new ModelAndView("web/security/usuariosAdminForm");
		Usuarios usuariosForm = usuarioService.get(id);
		List<Roles> listaRoles = rolService.obtenerListaRoles();
		modelAndView.addObject("listaRoles", listaRoles);
		modelAndView.addObject("usuariosForm", usuariosForm);
		Roles roles = listaRoles.stream().filter(rolName -> rolName.getCodigoProasecal()).findAny().orElse(new Roles());
		modelAndView.addObject("idParticipante", roles.getIdRoles());
		return modelAndView;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("usuariosForm") Usuarios usuariosForm, BindingResult bindingResult, RedirectAttributes redirectAttributes,
							 @RequestParam(value = "password2", required = false) String password2, ModelAndView model) {
		ModelAndView modelAndView = usuarioCreate();

		Usuarios usuarioExistente = new Usuarios();
		if (usuariosForm.getIdUsuario() > 0) {
			usuarioExistente = usuarioService.get(usuariosForm.getIdUsuario());
		}

		Usuarios userNameEx = usuarioService.findByUserName(usuariosForm.getNombreUsuario());
		if (userNameEx != null && userNameEx.getIdUsuario() != usuariosForm.getIdUsuario()) {
			bindingResult.rejectValue("nombreUsuario", "error", "Esta combinación debe ser única.");
		} else if (usuariosForm.getCodProasecal() != null && usuariosForm.getCodProasecal() > 1) {
			if (usuarioService.validarCodProasecalExistente(usuariosForm.getCodProasecal())
					&& usuarioExistente.getIdUsuario() != usuariosForm.getIdUsuario()) {
				bindingResult.rejectValue("codProasecal", "error", "Codigo en uso.");
			}
			// Usuario nuevo
		}

		if (usuarioExistente.getIdUsuario() == 0 && usuarioExistente.getIdUsuario() == usuariosForm.getIdUsuario()) {
			if (usuariosForm.getPassword().length() < 4 || !password2.equalsIgnoreCase(usuariosForm.getPassword())) {
				bindingResult.rejectValue("password", "error", "Contraseña inválida");
			} else {
				usuariosForm.setPassword(new BCryptPasswordEncoder().encode(usuariosForm.getPassword()));
			}
			// Usuario ya existente
		} else if (usuarioExistente.getIdUsuario() != 0
				&& usuarioExistente.getIdUsuario() == usuariosForm.getIdUsuario()) {
			if (usuariosForm.getPassword().isEmpty()) {
				usuariosForm.setPassword(usuarioExistente.getPassword());
			} else if (usuariosForm.getPassword().length() < 4
					|| !password2.equalsIgnoreCase(usuariosForm.getPassword())) {
				bindingResult.rejectValue("password", "error", "Contraseña inválida");
			} else {
				usuariosForm.setPassword(new BCryptPasswordEncoder().encode(usuariosForm.getPassword()));
			}

		}

		if (bindingResult.hasErrors()) {
			model.setViewName("web/security/usuariosAdminForm");
			List<Roles> listaRoles = rolService.obtenerListaRoles();
			model.addObject("usuariosForm", usuariosForm);
			model.addObject("listaRoles", listaRoles);
			Roles roles = listaRoles.stream().filter(rolName -> rolName.getCodigoProasecal()).findAny()
					.orElse(new Roles());
			model.addObject("idParticipante", roles.getIdRoles());
			model.addObject("notify", Notify.ERROR("ERROR", "Ocurrio un error al procesar la solicitud"));
			return model;
		}
		
		String ir="create";
		if(usuariosForm.getIdUsuario()!=0) {
			ir="update";			
		}
			
		usuarioService.save(usuariosForm);

		redirectAttributes.addAttribute("save",ir);
		return new ModelAndView("redirect:/usuarios/list");
	}

}
