package com.proasecal.software.controlexterno.entity;

import com.proasecal.software.web.entity.administrar.Equipos;
import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.administrar.Metodos;
import com.proasecal.software.web.entity.administrar.Reactivos;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
    @Table(name = "RESULTADOS_DETALLADOS")
        @NamedStoredProcedureQueries({
                @NamedStoredProcedureQuery(
                        name = "resetAberrantesProcedure",
                        procedureName = "reset_aberrantes",
                        parameters = {
                                @StoredProcedureParameter(
                                        name = "id_muestras",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_mensurandos",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_metodos",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_equipo",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_reactivo",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "resultado",
                                        type = String.class,
                                        mode = ParameterMode.OUT)}
                ),
                @NamedStoredProcedureQuery(
                        name = "consensoInicialProcedure",
                        procedureName = "consenso_inicial",
                        parameters = {
                                @StoredProcedureParameter(
                                        name = "id_muestras",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_mensurandos",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_metodos",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_equipo",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_reactivo",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "resultado",
                                        type = String.class,
                                        mode = ParameterMode.OUT)}
                ),
                @NamedStoredProcedureQuery(
                        name = "consensoInicialInicialProcedure",
                        procedureName = "consenso_inicial_inicial",
                        parameters = {
                                @StoredProcedureParameter(
                                        name = "id_muestras",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_mensurandos",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_metodos",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_equipo",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_reactivo",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "resultado",
                                        type = String.class,
                                        mode = ParameterMode.OUT)}
                ),
                @NamedStoredProcedureQuery(
                        name = "iteracionGrubbsProcedure",
                        procedureName = "iteracion_grubbs",
                        parameters = {
                                @StoredProcedureParameter(
                                        name = "id_muestras",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_mensurandos",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_metodos",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_equipo",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_reactivo",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "grubbs",
                                        type = Double.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "resultado",
                                        type = String.class,
                                        mode = ParameterMode.OUT)}
                ),
                @NamedStoredProcedureQuery(
                        name = "algoritmoAProcedure",
                        procedureName = "algoritmoA",
                        parameters = {
                                @StoredProcedureParameter(
                                        name = "id_muestras",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_mensurandos",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_metodos",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_equipo",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_reactivo",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "resultado",
                                        type = String.class,
                                        mode = ParameterMode.OUT)}
                ),
                @NamedStoredProcedureQuery(
                        name = "algoritmoDSProcedure",
                        procedureName = "algoritmoDS",
                        parameters = {
                                @StoredProcedureParameter(
                                        name = "id_muestras",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_mensurandos",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_metodos",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_equipo",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "id_reactivo",
                                        type = Integer.class,
                                        mode = ParameterMode.IN),
                                @StoredProcedureParameter(
                                        name = "resultado",
                                        type = String.class,
                                        mode = ParameterMode.OUT)}
                )
        })
    public class ResultadosDetallados {
        @GenericGenerator(name = "id_resultados_detalladosGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
                @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_resultados_detallados_seq"),
                @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"), })

        @Id
        @Column(name = "id_resultados_detallados", columnDefinition = "serial")
        @GeneratedValue(generator = "id_resultados_detalladosGenerator")
        @Getter
        @Setter
        private Long idResultadosDetallados;

    @ManyToOne
    @JoinColumn(name = "id_resultados")
    @NotNull
    @Getter @Setter
    private Resultados idResultados;

    @ManyToOne
    @JoinColumn(name = "id_mensurandos")
    @NotNull
    @Getter @Setter
    private Mensurandos mensurandosId;

    @ManyToOne
    @JoinColumn(name = "id_metodos")
    @NotNull
    @Getter @Setter
    private Metodos metodoId;

    @ManyToOne
    @JoinColumn(name = "id_reactivo")
    @NotNull
    @Getter @Setter
    private Reactivos reactivoId;

    @ManyToOne
    @JoinColumn(name = "id_equipos")
    @NotNull
    @Getter @Setter
    private Equipos equipoId;

    @Column(name="n_valor_reportado")
    @Getter @Setter
    private Double valorReportado;

    @Column(name = "d_fecha_creacion",updatable = false)
    @CreationTimestamp
    @Getter @Setter
    private Date fechaCreacion;

    @Column(name = "b_aberrante_grubbs", columnDefinition="BOOLEAN DEFAULT false")
    @Getter @Setter
    private Boolean aberranteGrubbs=false;

    @Column(name = "b_aberrante_a", columnDefinition="BOOLEAN DEFAULT false")
    @Getter @Setter
    private Boolean aberranteA=false;

    @Column(name = "b_aberrante_ds", columnDefinition="BOOLEAN DEFAULT false")
    @Getter @Setter
    private Boolean aberranteDs=false;

    @Column(name = "b_excluido", columnDefinition="BOOLEAN DEFAULT false")
    @Getter @Setter
    private Boolean excluido=false;

    @OneToMany(mappedBy = "idResultadosDetallados", fetch = FetchType.LAZY)
    @Getter @Setter
    @OrderBy
    private Set<EscenariosResultados> resultadosDetalladosEscenariosList;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResultadosDetallados)) return false;
        ResultadosDetallados that = (ResultadosDetallados) o;
        return getIdResultadosDetallados() == that.getIdResultadosDetallados() &&
                Objects.equals(getIdResultados(), that.getIdResultados()) &&
                Objects.equals(getMensurandosId(), that.getMensurandosId()) &&
                Objects.equals(getMetodoId(), that.getMetodoId()) &&
                Objects.equals(getReactivoId(), that.getReactivoId()) &&
                Objects.equals(getEquipoId(), that.getEquipoId()) &&
                Objects.equals(getValorReportado(), that.getValorReportado()) &&
                Objects.equals(getFechaCreacion(), that.getFechaCreacion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdResultadosDetallados(), getIdResultados(), getMensurandosId(), getMetodoId(), getReactivoId(), getEquipoId(), getValorReportado(), getFechaCreacion());
    }
}
