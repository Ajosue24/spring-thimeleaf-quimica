package com.proasecal.software.controlexterno.entity.DAO;

import lombok.Getter;
import lombok.Setter;

public class ConsensoInicial {
@Getter @Setter
    private Double media ;
     @Getter @Setter
    private Double desviacionEstandar;
     @Getter @Setter
    private Double valorMinimo;
     @Getter @Setter
    private Double valorMaximo;
     @Getter @Setter
    private Integer totalElementos;
     @Getter @Setter
    private Integer totalAberrantes;
    @Getter @Setter
    private Boolean esAberranteMin;
    @Getter @Setter
    private Double aberranteMin;
    @Getter @Setter
    private Boolean esAberranteMax;
    @Getter @Setter
    private Double aberranteMax;

    public ConsensoInicial() {
    }

}
