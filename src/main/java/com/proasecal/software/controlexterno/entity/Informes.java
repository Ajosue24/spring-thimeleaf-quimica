package com.proasecal.software.controlexterno.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "INFORMES")
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
public class Informes {
    @GenericGenerator(name = "id_informesGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_informes_seq"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),})

    @Id
    @Column(name = "id_informes", columnDefinition = "serial")
    @GeneratedValue(generator = "id_informesGenerator")
    @Getter
    @Setter
    private Long idInformes;

    @ManyToOne
    @JoinColumn(name = "id_resultados")
    @NotNull (message = "El resultado es requerido")
    @Getter
    @Setter
    @JsonIgnore
    private Resultados resultadosId;

    @Column(name = "b_revision_director")
    @Getter
    @Setter
    private boolean revisionDirector;

    @Column(name= "d_fecha_revision_director")
    @Getter
    @Setter
    private Date fechaRevisionDirector;

    @ManyToOne
    @JoinColumn(name = "id_usuario_director")
    @Getter
    @Setter
    @JsonIgnore
    private Usuarios usuarioDirectorId;

    @Column(name = "b_revision_revisor")
    @Getter
    @Setter
    private boolean revisionRevisor;

    @Column(name = "d_fecha_revision_revisor")
    @Getter
    @Setter
    private Date fechaRevisionRevisor;

    @ManyToOne
    @JoinColumn(name = "id_usuario_revisor")
    @Getter
    @Setter
    @JsonIgnore
    private Usuarios usuarioRevisorId;

    @Column(name = "b_version")
    @Getter
    @Setter
    private boolean esVersion;

    @Column(name = "n_numero_version")
    @Getter
    @Setter
    private int numeroVersion;

    @Column(name = "v_informe_json", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    @Getter @Setter
    private String informeJson;


    @Column(name = "v_plantilla_informe")
    @Getter
    @Setter
    private String plantillaInforme;

    @Column(name = "n_estado_proceso")
    @Getter
    @Setter
    private int estadoProceso;

    public Informes() {
    }

    public Informes(@NotNull(message = "El resultado es requerido") Resultados resultadosId, boolean esVersion, int estadoProceso) {
        this.resultadosId = resultadosId;
        this.esVersion = esVersion;
        this.estadoProceso = estadoProceso;
    }
}
