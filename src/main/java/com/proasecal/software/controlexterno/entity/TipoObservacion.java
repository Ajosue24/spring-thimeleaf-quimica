package com.proasecal.software.controlexterno.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proasecal.software.web.entity.parametricas.TipoProgramas;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name="TIPO_OBSERVACION")
public class TipoObservacion {
    @GenericGenerator(name = "id_tipoObservacionGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_tipoObservacion_seq"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),})

    @Id
    @Column(name = "id_tipo_observacion", columnDefinition = "serial")
    @GeneratedValue(generator = "id_tipoObservacionGenerator")
    @Getter
    @Setter
    private Long idtipoObservacion;

    @ManyToOne
    @JoinColumn(name = "id_tipo_programa")
    @NotNull(message = "El tipo del programa es requerido")
    @Getter
    @Setter
    @JsonIgnore
    private TipoProgramas tipoProgramasId;

    @Column(name = "v_nombre")
    @Getter
    @Setter
    @NotNull(message = "El nombre de la observación es requerido")
    private String nombre;

    @Column(name = "b_activo")
    @Getter
    @Setter
    private boolean activo = true;

    @OneToMany(mappedBy = "tipoObservacionId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    @Getter @Setter
    private Set<ObservacionMuestra> observacionMuestraList;
}
