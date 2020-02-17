package com.proasecal.software.controlexterno.entity.documentacionjson;

import lombok.Getter;
import lombok.Setter;

public class CuadroIncertidumbre {

    @Getter
    @Setter
    private String mensurando;
    @Getter
    @Setter
    private Double media_general;
    @Getter
    @Setter
    private Double desv_general;
    @Getter
    @Setter
    private Integer n_total;
    @Getter
    @Setter
    private Double incertidumbre_gen;
}
