package com.proasecal.software.controlexterno.controller;

import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.service.administrar.MensurandoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Optional;

@RestController
@RequestMapping(value = "/controlexternoRest")
public class ControlExternoRest {

    @Autowired
    MensurandoService mensurandoService;

    @GetMapping(value = "/decimalesMensurando", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<Double> getDecimalesMensurando(@RequestParam("mensurandoId") Optional<Long> mensurandoId,
                                                         @RequestParam("valorIngresado") Optional<Double> valorIngresado) {
        Mensurandos mensurandos = mensurandoService.getMensurandos(mensurandoId.get());
        BigDecimal numberBigDecimal = new BigDecimal(valorIngresado.get(), MathContext.DECIMAL64);
        numberBigDecimal = numberBigDecimal.setScale(mensurandos.getCantDecimales(), RoundingMode.HALF_UP);

        try {
            return new ResponseEntity<>(numberBigDecimal.doubleValue(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(numberBigDecimal.doubleValue(), HttpStatus.BAD_REQUEST);
        }
    }
}
