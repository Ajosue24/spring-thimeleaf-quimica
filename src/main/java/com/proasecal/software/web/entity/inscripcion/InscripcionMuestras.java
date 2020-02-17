package com.proasecal.software.web.entity.inscripcion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proasecal.software.controlexterno.entity.Resultados;
import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "inscripcion_muestras")
public class InscripcionMuestras {

    @GenericGenerator(
            name = "inscripcionMuestrasGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_inscripcion_muestras_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            }
    )

    @Id
    @Getter
    @Setter
    @Column(name = "id_inscripcion_muestras", columnDefinition = "serial")
    @GeneratedValue(generator = "inscripcionMuestrasGenerator")
    private long idInscripcionMuestras;

    @ManyToOne
    @JoinColumn(name = "id_inscripcionprogramas")
    @NotNull
    @Getter
    @Setter
    @JsonIgnore
    private InscripcionProgramas inscripProgramaId;

    @ManyToOne
    @JoinColumn(name = "id_muestras")
    @NotNull
    @Getter
    @Setter
    @JsonIgnore
    private Muestras idMuestras;

    @ManyToOne
    @JoinColumn(name = "id_usuarios_lab_sedes")
    @NotNull
    @Getter
    @Setter
    @JsonIgnore
    private UsuariosLabSedes idUsuarios;

    @Column(name = "d_fecha_creacion", updatable = false)
    @CreationTimestamp
    @Getter
    @Setter
    private Date fechaCreacion;

    @OneToMany(mappedBy = "idInscripcionMuestras", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    @Getter @Setter
    private List<Resultados> resultadosList;

    public InscripcionMuestras() {
    }

    public InscripcionMuestras(@NotNull InscripcionProgramas inscripProgramaId, @NotNull Muestras idMuestras, @NotNull UsuariosLabSedes idUsuarios) {
        this.inscripProgramaId = inscripProgramaId;
        this.idMuestras = idMuestras;
        this.idUsuarios = idUsuarios;
    }

    public InscripcionMuestras(@NotNull InscripcionProgramas inscripProgramaId, @NotNull Muestras idMuestras, @NotNull UsuariosLabSedes idUsuarios, Date fechaCreacion) {
        this.inscripProgramaId = inscripProgramaId;
        this.idMuestras = idMuestras;
        this.idUsuarios = idUsuarios;
        this.fechaCreacion = fechaCreacion;
    }

}