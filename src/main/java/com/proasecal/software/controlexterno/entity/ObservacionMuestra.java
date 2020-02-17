package com.proasecal.software.controlexterno.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proasecal.software.web.entity.administrar.Muestras;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "OBSERVACION_MUESTRA")
public class ObservacionMuestra {
    @GenericGenerator(name = "id_observacionMuestraGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_observacionMuestra_seq"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),})

    @Id
    @Column(name = "id_observacion_muestra", columnDefinition = "serial")
    @GeneratedValue(generator = "id_observacionMuestraGenerator")
    @Getter
    @Setter
    private Long idObservacionMuestra;

    @ManyToOne
    @JoinColumn(name = "id_muestra")
    @NotNull(message = "La muestra es requerida")
    @Getter
    @Setter
    @JsonIgnore
    private Muestras muestraId;

    @ManyToOne
    @JoinColumn(name = "id_tipo_observacion")
    @NotNull(message = "El tipo de observacion es requerido")
    @Getter
    @Setter
    @JsonIgnore
    private TipoObservacion tipoObservacionId;

    @Column(name = "v_observacion" , columnDefinition = "TEXT")
    @Getter
    @Setter
    private String observacion;

    @Column(name = "d_fecha_creacion", updatable = false)
    @Getter
    @Setter
    @CreationTimestamp
    private Date fechaCreacion;

    @Column(name = "d_fecha_modificacion")
    @Getter
    @Setter
    @UpdateTimestamp
    private Date fechaModificacion;

    public ObservacionMuestra() {
    }

    public ObservacionMuestra(@NotNull(message = "La muestra es requerida") Muestras muestraId, @NotNull(message = "El tipo de observacion es requerido") TipoObservacion tipoObservacionId, String observacion) {
        this.muestraId = muestraId;
        this.tipoObservacionId = tipoObservacionId;
        this.observacion = observacion;
    }
}
