package com.proasecal.software.controlexterno.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proasecal.software.web.entity.administrar.Muestras;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "PROCESO")
public class Proceso {
    @GenericGenerator(name = "id_procesoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_proceso_seq"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),})

    @Id
    @Column(name = "id_proceso", columnDefinition = "serial")
    @GeneratedValue(generator = "id_procesoGenerator")
    @Getter
    @Setter
    private Long idProceso;


    @Column(name = "n_tipo_proceso")
    @NotNull
    @Getter
    @Setter
    private int tipoProceso;

    @Column(name = "d_fecha_proceso")
    @Getter
    @Setter
    @UpdateTimestamp
    private Date fechaProceso;

    @ManyToOne
    @JoinColumn(name = "id_muestra")
    @Getter
    @Setter
    @JsonIgnore
    private Muestras muestraId;

    public Proceso() {
    }

    public Proceso(@NotNull int tipoProceso, Muestras muestraId) {
        this.tipoProceso = tipoProceso;
        this.muestraId = muestraId;
    }
}
