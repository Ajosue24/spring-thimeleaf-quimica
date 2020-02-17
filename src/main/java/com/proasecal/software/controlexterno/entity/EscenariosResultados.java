package com.proasecal.software.controlexterno.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "ESCENARIOS_RESULTADOS")
@AllArgsConstructor
@NoArgsConstructor
public class EscenariosResultados {
    @GenericGenerator(name = "resultadosDetalladosEscenariosGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "resultados_detallados_escenarios_seq"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),})

    @Id
    @Column(name = "id_escenario_resultado", columnDefinition = "serial")
    @GeneratedValue(generator = "resultadosDetalladosEscenariosGenerator")
    @Getter
    @Setter
    private Long idEscenarioResultado;

    @ManyToOne
    @JoinColumn(name = "id_escenario_fijo")
    @Getter
    @Setter
    private EscenariosFijos idEscenarioFijo;

    @ManyToOne
    @JoinColumn(name = "id_resultados_detallados")
    @Getter
    @Setter
    private ResultadosDetallados idResultadosDetallados;

    @Column(name= "b_aberrante")
    @Getter
    @Setter
    private boolean aberrante =false;

    @Column(name = "n_desviacion_valor_asignado")
    @Getter
    @Setter
    private Double desviacionValorAsignado;

    @Column(name = "n_indice_varianza")
    @Getter
    @Setter
    private Double indiceVarianza;

    @Column(name = "n_indice_desviacion_estandar")
    @Getter
    @Setter
    private Double indiceDesviacionEstandar;

    public EscenariosResultados(Double indiceVarianza) {
        this.indiceVarianza = indiceVarianza;
    }
}