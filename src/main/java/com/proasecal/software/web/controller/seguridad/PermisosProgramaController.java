package com.proasecal.software.web.controller.seguridad;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.proasecal.software.web.entity.administrar.Equipos;
import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.entity.seguridad.PermisoPrograma;
import com.proasecal.software.web.entity.seguridad.Permisos;
import com.proasecal.software.web.entity.seguridad.Roles;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import com.proasecal.software.web.service.administrar.ProgramasService;
import com.proasecal.software.web.service.seguridad.PermisosProgramasService;
import com.proasecal.software.web.service.seguridad.RolService;
import com.proasecal.software.web.service.seguridad.UsuarioService;
import com.proasecal.software.web.util.Notify;


@Controller
@RequestMapping(value = "/pemisosProgramas")
public class PermisosProgramaController {
	@Autowired
	UsuarioService usuarioService;
    @Autowired
    RolService rolService;
	@Autowired
	ProgramasService programaService;    
	@Autowired
	PermisosProgramasService permisosProgramasService;
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView main() {
        ModelAndView modelAndView = new ModelAndView("web/security/permisosProgramas");
        List<Roles> listaRoles = rolService.listUsuariosRolAsesor(); 
        Set<Usuarios> listaUsuarios = new HashSet<>();
        for(Roles rol:listaRoles) {
    	 for(Usuarios user:rol.getUsuariosList()) {
    		 listaUsuarios.add(user);
    	 }
      }
        modelAndView.addObject("listaUsuarios", listaUsuarios);
        return modelAndView;
    }
    
    
    @RequestMapping("/obtListPermisosProgramas")
    public String obtListPermisosProgramas(@RequestParam("usuario") String usuario2, Model model) {
    	List<Programas> listaprogramas = programaService.list(); 
        Usuarios usuario = new Usuarios();
        usuario.setIdUsuario(Long.valueOf(usuario2));
        List<PermisoPrograma> modelsPP = permisosProgramasService.obtListPermisosProgramas(usuario);
        List<Boolean> permisoProgramaDirector = new ArrayList<>();
        List<Boolean> permisoProgramaRevisor = new ArrayList<>();
        
        for(Programas programa:listaprogramas) {
            if (modelsPP.stream()
                    .anyMatch(t -> t.getIdPrograma().getProgramaId()==programa.getProgramaId()&&t.getEsDirector()==true)) {
            	permisoProgramaDirector.add(true);
            } else {
            	permisoProgramaDirector.add(false);
            }                        
            if (modelsPP.stream()
                    .anyMatch(t -> t.getIdPrograma().getProgramaId()==programa.getProgramaId()&&t.getEsRevisor()==true)) {
            	permisoProgramaRevisor.add(true);
            } else {
            	permisoProgramaRevisor.add(false);
            }
        }
        model.addAttribute("listaPrograma", listaprogramas);
        model.addAttribute("permisoProgramaDirector",permisoProgramaDirector);
        model.addAttribute("permisoProgramaRevisor",permisoProgramaRevisor);
        return "web/security/permisosProgramas :: modelsPrograma";
    }	
    @RequestMapping(value = "/actulizarPermisos", method = RequestMethod.GET)
    public String enableUrl(@RequestParam("usuario") String usuario,
    		@RequestParam("programa") String programa,
    		@RequestParam("estado") Boolean estado,
    		@RequestParam("campo") String campo,
    		Model model) {
	        Usuarios usu = new Usuarios();
	        usu.setIdUsuario(Long.valueOf(usuario));
	        Programas progra =new Programas();
	        progra.setProgramaId(Long.valueOf(programa));
    		PermisoPrograma permisoPrograma = this.permisosProgramasService.buscarPermisoPrograma(progra,usu).orElse(new PermisoPrograma());
    		
    		permisoPrograma.setIdPrograma(progra);
    		permisoPrograma.setUsuarioId(usu);
    		if(campo.equalsIgnoreCase("revisor")) {
    			permisoPrograma.setEsRevisor(estado);
    		}
    		else {
    			permisoPrograma.setEsDirector(estado);
    		}
    		try {
    			this.permisosProgramasService.save(permisoPrograma);
    		} catch (Exception e) {

    		}

        return "web/security/permisosProgramas :: content";        
    }
}
