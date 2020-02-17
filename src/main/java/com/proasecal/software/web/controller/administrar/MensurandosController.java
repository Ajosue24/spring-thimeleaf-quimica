package com.proasecal.software.web.controller.administrar;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value="/mensurandos")
public class MensurandosController {


    @GetMapping
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("/web/administrar/mensurandos/list");
        return modelAndView;
    }
    
    
    @RequestMapping(value="/create",method= RequestMethod.GET )
    public ModelAndView mesurandoCreate(){
        ModelAndView modelAndView = new ModelAndView("web/administrar/mensurandos/form");
        return modelAndView;
    }
}