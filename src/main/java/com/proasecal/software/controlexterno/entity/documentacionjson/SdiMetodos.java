package com.proasecal.software.controlexterno.entity.documentacionjson;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SdiMetodos {

    @Getter @Setter
    String mensurando;
    @Getter @Setter
    String titulo;
    @Getter @Setter
    Double valor;
    @Getter @Setter
    Double media_consenso;
    @Getter @Setter
    String desviacion_estandar;
    @Getter @Setter
    String sdi;
    @Getter @Setter
    Integer n_laboratorios;
    @Getter @Setter
    String incertidumbre;
    @Getter @Setter
    Integer cont;
    @Getter @Setter
    Integer numero_decimales;
    @Getter @Setter
    List<Graph> graph;


}
