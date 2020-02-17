package com.proasecal.software.controlexterno.entity.documentacionjson;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class HistoricoIV {

    @Getter
    @Setter
    private String mensurando;
    @Getter
    @Setter
    private String titulo;
    @Getter
    @Setter
    private Double valor;
    @Getter
    @Setter
    private Double valor_asignado;
    @Getter
    @Setter
    private Double desviacion;
    @Getter
    @Setter
    private String desviacion_aceptable;
    @Getter
    @Setter
    private Double indice_varianza;
    @Getter
    @Setter
    private Integer cont;
    @Getter
    @Setter
    private Integer numero_decimales;
    @Getter
    @Setter
    private List<Graph> graph;
}
