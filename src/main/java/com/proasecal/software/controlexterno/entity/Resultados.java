package com.proasecal.software.controlexterno.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import com.proasecal.software.web.entity.seguridad.Usuarios;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "RESULTADOS")
public class Resultados {
    @GenericGenerator(name = "id_resultadosGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_resultados_seq"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"), })

    @Id
    @Column(name = "id_resultados", columnDefinition = "serial")
    @GeneratedValue(generator = "id_resultadosGenerator")
    @Getter
    @Setter
    private Long idResultados;

    @Column(name="n_valor_isi")
    @Getter @Setter
    private Integer valorIsi;

    @Column(name="n_valor_inr")
    @Getter @Setter
    private Integer valorInr;

    @Column(name="n_valor_media_p")
    @Getter @Setter
    private Integer valorMediaP;

    @Column(name="n_valor_control_n")
    @Getter @Setter
    private Integer valorControlN;

    @Column(name="b_reporte")
    @Getter @Setter
    private Boolean reporte;

    @Column(name="b_resultado_fecha", columnDefinition="BOOLEAN DEFAULT false")
    @Getter @Setter
    private Boolean resultadoFecha;

    @Column(name="v_observaciones")
    @Getter @Setter
    private String observaciones;

    @Column(name="v_justificacion")
    @Getter @Setter
    private String justificacion;

    @Column(name = "d_fecha_creacion",updatable = false)
    @CreationTimestamp
    @Getter @Setter
    private Date fechaCreacion;

    @Column(name = "d_fecha_modificacion")
    @Getter @Setter
    private Date fechaModificacion;

    @Column(name = "v_aprobado_por",updatable = false)
    @Getter @Setter
    private String aprobadoPor;
    
    @ManyToOne
    @JoinColumn(name = "id_inscripcion_muestras")
    @NotNull
    @Getter @Setter
    private InscripcionMuestras idInscripcionMuestras;

    @OneToMany(mappedBy = "idResultados", fetch = FetchType.LAZY)
    @Getter @Setter
    @OrderBy
    private List<ResultadosDetallados> resultadosDetalladosList;

    @ManyToOne
    @JoinColumn(name = "id_usuarios_creacion",updatable = false)
    @NotNull
    @Getter
    @Setter
    @JsonIgnore
    private Usuarios idUsuarios;
    
    @ManyToOne
    @JoinColumn(name = "id_usuarios_modificacion")
    @Getter
    @Setter
    @JsonIgnore
    private Usuarios idUsuarioModificacion;

    @Column(name = "b_estado") 
    @Getter @Setter
    private Boolean estado = true ;

    @OneToMany(mappedBy = "resultadosId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    @Getter @Setter
    @OrderBy
    private List<Informes> informesList;

    @OneToMany(mappedBy = "resultadosId", fetch = FetchType.LAZY)
    @JsonIgnore
    @Getter @Setter
    private List<ObservacionResultado> observacionResultadoList;
}
