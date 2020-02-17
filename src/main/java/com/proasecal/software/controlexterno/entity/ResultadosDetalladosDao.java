package com.proasecal.software.controlexterno.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ResultadosDetalladosDao {

    @Getter @Setter
   private List<ResultadosDetallados> resultadosDetalladosList;

    @Getter @Setter
    private Resultados resultados;
}
