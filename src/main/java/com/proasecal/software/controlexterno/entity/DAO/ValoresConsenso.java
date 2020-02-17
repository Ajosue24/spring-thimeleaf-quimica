package com.proasecal.software.controlexterno.entity.DAO;

import com.proasecal.software.controlexterno.entity.CriteriosAceptabilidad;
import com.proasecal.software.controlexterno.entity.EscenariosFijos;
import com.proasecal.software.controlexterno.entity.Resultados;
import com.proasecal.software.controlexterno.entity.ResultadosDetallados;
import com.proasecal.software.web.entity.administrar.Laboratorios;
import com.proasecal.software.web.entity.administrar.Sedes;
import lombok.Getter;
import lombok.Setter;

public class ValoresConsenso {

    @Getter
    @Setter
    Boolean isAberrante;

    @Getter
    @Setter
    String nombreUsuario;

    @Getter
    @Setter
    String nombreLaboratorio;

    @Getter
    @Setter
    String nombreSede;

    @Getter
    @Setter
    private ResultadosDetallados resultadosDetallados;

    @Getter
    @Setter
    private Double valorAceptable;
    @Getter
    @Setter
    private String unidadMedida;

    @Getter
    @Setter
    private Double indiceVarianzaClia;

    @Getter
    @Setter
    private Double desviacionValorAsignadoClia;

    @Getter
    @Setter
    private Double indiceDesviacionEstandarClia;

    @Getter
    @Setter
    private Double desviacionValorAsignadoGrb;

    @Getter
    @Setter
    private Double indiceVarianzaGrb;

    @Getter
    @Setter
    private Double desviacionValorAsignadoAlgA;

    @Getter
    @Setter
    private Double indiceVarianzaAlgA;

    @Getter
    @Setter
    private Double desviacionValorAsignadoSd;

    @Getter
    @Setter
    private Double indiceVarianzaSd;
}
