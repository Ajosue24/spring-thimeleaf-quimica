package com.proasecal.software.controlexterno.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "OBSERVACION_RESULTADO")
public class ObservacionResultado {


    @GenericGenerator(name = "observacionResultadoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "observacion_resultado_seq"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),})

    @Id
    @Column(name = "id_observacion_resultado", columnDefinition = "serial")
    @GeneratedValue(generator = "observacionResultadoGenerator")
    @Getter
    @Setter
    private Long idObservacionResultado;

    @ManyToOne
    @JoinColumn(name = "id_resultados")
    @NotNull
    @Getter
    @Setter
    @JsonIgnore
    private Resultados resultadosId;

    @Column(name = "v_observacion" , columnDefinition = "TEXT")
    @Getter
    @Setter
    private String observacion;

    @Column(name = "d_fecha_creacion")
    @Getter
    @Setter
    @CreationTimestamp
    private Date fechaCreacion;

    @Column(name = "d_fecha_modificacion")
    @Getter
    @Setter
    @UpdateTimestamp
    private Date fechaModificacion;
}
