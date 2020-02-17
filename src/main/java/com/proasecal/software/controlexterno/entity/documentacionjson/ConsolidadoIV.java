package com.proasecal.software.controlexterno.entity.documentacionjson;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ConsolidadoIV {

    @Getter @Setter
    String evaluationName;
    @Getter
    @Setter
    Integer cont;
    @Getter
    @Setter
    List<Histograma> histograma;

}
