package com.proasecal.software.web.controller.inscripcion;

import com.proasecal.software.controlexterno.entity.Resultados;
import com.proasecal.software.controlexterno.service.ResultadosService;
import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import com.proasecal.software.web.entity.inscripcion.PeriodosVigencia;
import com.proasecal.software.web.service.administrar.MuestraService;
import com.proasecal.software.web.service.inscripcion.InscripcionMuestrasService;
import com.proasecal.software.web.service.inscripcion.PeriodosVigenciaService;
import com.proasecal.software.web.util.Notify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping(value = "/inscripcionMuestras")
public class InscripcionMuestrasController {

    @Autowired
    InscripcionMuestrasService inscripcionMuestrasService;
    @Autowired
    PeriodosVigenciaService periodosVigenciaService;
    @Autowired
    MuestraService muestraService;
	@Autowired	
	ResultadosService resultadosService;    


    @RequestMapping(value = "/save")
    public ResponseEntity<String> save(@RequestParam("periodo") String idPeriodoVigencia, @RequestParam("muestra") String idMuestra, Model model) {

        Optional<Muestras> muestra = muestraService.find(Long.valueOf(idMuestra));
        Optional<PeriodosVigencia> periodosVigencia = periodosVigenciaService.find(Long.valueOf(idPeriodoVigencia));

        InscripcionMuestras inscripcionMuestras = new InscripcionMuestras(
                periodosVigencia.get().getInscripProgramaId(),
                muestra.get(),
                periodosVigencia.get().getInscripProgramaId().getIdUsuarioLabSedes()
        );

        try {
            Optional<InscripcionMuestras> inscripcionMuestrasval=inscripcionMuestrasService.findMuestraInscrita( muestra.get(), periodosVigencia.get().getInscripProgramaId(), periodosVigencia.get().getInscripProgramaId().getIdUsuarioLabSedes());
            if (!inscripcionMuestrasval.isPresent()) {
                inscripcionMuestrasService.save(inscripcionMuestras);
                model.addAttribute("notify", Notify.ERROR("OK", "Muestra inscrita"));
            }
        } catch (Exception e) {
            model.addAttribute("notify", Notify.ERROR("ERROR", "No se pudo inscribir la muestra"));
            return new ResponseEntity<String>("No se pudo realizar la inscripción de Muestras.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("Muestras inscritas correctamente.", HttpStatus.OK);
    }


    @RequestMapping(value = "/delete")
    public ResponseEntity<String> delete(@RequestParam("periodo") String idPeriodoVigencia, @RequestParam("muestra") String idMuestra, Model model) {

        Optional<Muestras> muestra = muestraService.find(Long.valueOf(idMuestra));
        Optional<PeriodosVigencia> periodosVigencia = periodosVigenciaService.find(Long.valueOf(idPeriodoVigencia));
        Optional<InscripcionMuestras> inscripcionMuestras=inscripcionMuestrasService.findMuestraInscrita( muestra.get(), periodosVigencia.get().getInscripProgramaId(), periodosVigencia.get().getInscripProgramaId().getIdUsuarioLabSedes());

        try {
            Optional<Resultados> resultado= resultadosService.findByIdInscripcionMuestras(inscripcionMuestras.get());
            if (resultado.isPresent()) {
            	 return new ResponseEntity<String>("Error al desinscribir la muestra", HttpStatus.INTERNAL_SERVER_ERROR);
            }            
            inscripcionMuestrasService.delete(inscripcionMuestras.get());
        } catch (Exception e) {
            return new ResponseEntity<String>("Error al desinscribir la muestrar" , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("Muestra desinscrita correctamente", HttpStatus.OK);
    }
}
