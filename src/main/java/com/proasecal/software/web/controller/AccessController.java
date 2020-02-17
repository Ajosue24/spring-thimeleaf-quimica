package com.proasecal.software.web.controller;


import com.proasecal.software.controlexterno.service.ResultadosDetalladosService;
import com.proasecal.software.controlexterno.service.ResultadosService;
import com.proasecal.software.web.service.administrar.*;
import com.proasecal.software.web.service.seguridad.RolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class AccessController {

    private final Logger LOG = LoggerFactory.getLogger(AccessController.class);

    @Autowired
    ResultadosService resultadosService;
    @Autowired
    ResultadosDetalladosService resultadosDetalladosService;
    @Autowired
    RolService rolService;
    @Autowired
    ProgramasService programasService;


    @GetMapping("web/index")
    public ModelAndView index() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return new ModelAndView("web/login");
        }else if(authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_PARTICIPANTE"))){
            return new ModelAndView("redirect:/controlexterno/");
        }else {
            return  new ModelAndView("web/index");
        }
    }

    @PostMapping("web/index")
    public ModelAndView postIndex() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return new ModelAndView("web/login");
        }else if(authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_PARTICIPANTE"))){
            return new ModelAndView("redirect:/controlexterno/");
        }else {
            return  new ModelAndView("web/index");
        }
    }

    @PostMapping("/web/login")
    public ModelAndView postLogin() {
        return new ModelAndView("redirect:/index");
    }

    @GetMapping("/web/login")
    public String login(@RequestParam(value = "logout", required = false) String logout, @RequestParam(value = "error", required = false) String error, ModelMap modelMap) {
        ModelAndView model = new ModelAndView();
        if (logout != null) {
            model.addObject("msg", "logout correcto");
            return "web/login";
        }
        if(error!=null){
            modelMap.put("msg","contrase;a invalida");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return "web/login";
        }else if(authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_PARTICIPANTE"))){
            return "redirect:/controlexterno/";
        }else {
            return  "redirect:/";
        }

    }

    @GetMapping("/pagExtra")
    public String pagExtra() {
        return "pagExtra";
    }


    @GetMapping("/")
    public String home() {
        // usuarioService.deleteById(24l);

 /*       //Inicio Obtener nombre cliente
        Class<?> c = Clientes.class;
        Table table = c.getAnnotation(Table.class);
        String tableName = table.name();
        */
        //resultadosDetalladosService.resetAberrantes(88,1,1,0,0);
        //resultadosDetalladosService.getConsensoInicial(88, 0, 0, 0, 0);
        //resultadosDetalladosService.getIteracionGrubbs(88,1,1,0,0,3.48);
        //resultadosDetalladosService.getAlgoritmoA(93,3,0,0,0);
        //resultadosDetalladosService.getAlgoritmoDS(93,3,0,0,0);


        //Fin Obtener nombre cliente


        //Test

        //Fin Test

        //gestionProgramasService.getListProgramasTable("nombrePrograma","Programa1");
        // gestionProgramasService.getList();
        //gestionProgramasService.test();
        //throw new RuntimeException("error");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return "web/login";
        }else if(authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_PARTICIPANTE"))){
            return "redirect:/controlexterno/";
        }
        return "redirect:web/index";
    }
}
