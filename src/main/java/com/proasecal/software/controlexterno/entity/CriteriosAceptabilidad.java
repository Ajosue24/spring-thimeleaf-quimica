package com.proasecal.software.controlexterno.entity;

import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.administrar.UnidadesMedidas;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CRITERIOS_ACEPTABILIDAD")
public class CriteriosAceptabilidad {

    @GenericGenerator(name = "criteriosAceptabilidadGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "criterios_aceptabilidad_seq"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),})

    @Id
    @Column(name = "id_criterios_aceptabilidad", columnDefinition = "serial")
    @GeneratedValue(generator = "criteriosAceptabilidadGenerator")
    @Getter
    @Setter
    private Long idCriteriosAceptabilidad;

    @ManyToOne
    @JoinColumn(name = "id_mensurando")
    @NotNull
    @Getter @Setter
    private Mensurandos idMensurandos;

    @ManyToOne
    @JoinColumn(name = "id_unidad_desviacion_aceptable")
    @NotNull
    @Getter @Setter
    private UnidadesMedidas unidadesMedidasId;

    @Column(name = "n_desviacion_aceptable")
    @Getter
    @Setter
    private Double desviacionAceptable;

    @ManyToOne
    @JoinColumn(name = "id_unidades_medidas_valor_alternativo")
    @NotNull
    @Getter @Setter
    private UnidadesMedidas unidadesMedidasValorAlternativo;

    @Column(name = "n_valor_anotacion")
    @Getter
    @Setter
    private Double valorAnotacion;

    @Column(name = "n_valor_alternativo")
    @Getter
    @Setter
    private Double valorAlternativo;
}
