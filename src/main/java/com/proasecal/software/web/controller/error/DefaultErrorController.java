package com.proasecal.software.web.controller.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class DefaultErrorController implements ErrorController {

    @RequestMapping(value = "error", method = RequestMethod.GET)
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest, HttpServletResponse response) {

        ModelAndView errorPage = new ModelAndView("error/errorPage");
        String errorMsg = "";
        int nroError = getErrorCode(httpRequest);
        if(httpRequest.getAttribute("acceso")!=null){
            nroError = 401;
        }

        switch (nroError) {
            case 400: {
                errorMsg = "Solicitud incorrecta";
                break;
            }
            case 401: {
                errorMsg = "No autorizado";
                break;
            }
            case 404: {
                errorMsg = "No se encuentra la página o recurso solicitado";
                break;
            }
            case 405: {
                errorMsg = "Metodo no soportado";
                break;
            }
            case 500: {
                errorMsg = "Error Interno";
                break;
            }
        }

        errorPage.addObject("nroError", nroError);
        errorPage.addObject("errorMsg", errorMsg);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
                .getAttribute("javax.servlet.error.status_code");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}