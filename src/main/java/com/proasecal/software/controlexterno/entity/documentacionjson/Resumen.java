package com.proasecal.software.controlexterno.entity.documentacionjson;

import lombok.Getter;
import lombok.Setter;

public class Resumen {

    @Getter @Setter
    private String mensurando;
    @Getter @Setter
    private Double valor;
    @Getter @Setter
    private Double valor_asignado;
    @Getter @Setter
    private Double desviacion;
    @Getter @Setter
    private String desviacion_aceptable;
    @Getter @Setter
    private Double indice_varianza;
    @Getter @Setter
    private Integer total_laboratorios;
    @Getter @Setter
    private Double media_consenso_metodo;
    @Getter @Setter
    private String desviacion_estandar_metodo;
    @Getter @Setter
    private String sdi_metodo;
    @Getter @Setter
    private Integer n_laboratorios_metodo;
    @Getter @Setter
    private String incertidumbre_metodo;
    @Getter @Setter
    private Double media_consenso_metodo_equipo;
    @Getter @Setter
    private String desviacion_estandar_metodo_equipo;
    @Getter @Setter
    private String sdi_metodo_equipo;
    @Getter @Setter
    private Integer n_laboratorios_metodo_equipo;
}
