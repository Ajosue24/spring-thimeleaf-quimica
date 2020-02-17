package com.proasecal.software.controlexterno.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proasecal.software.web.entity.administrar.Equipos;
import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.administrar.Metodos;
import com.proasecal.software.web.entity.administrar.Muestras;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "ESCENARIOS_FIJOS")
    @NamedStoredProcedureQueries({
            @NamedStoredProcedureQuery(
                    name = "calculoSimulacionesHijas",
                    procedureName = "calculosimulacioneshijas",
                    parameters = {
                            @StoredProcedureParameter(
                                    name = "idescenariofijo",
                                    type = Integer.class,
                                    mode = ParameterMode.IN),
                            @StoredProcedureParameter(
                                    name = "muestra",
                                    type = Integer.class,
                                    mode = ParameterMode.IN),
                            @StoredProcedureParameter(
                                    name = "mensurando",
                                    type = Integer.class,
                                    mode = ParameterMode.IN),
                            @StoredProcedureParameter(
                                    name = "metodo",
                                    type = Integer.class,
                                    mode = ParameterMode.IN),
                            @StoredProcedureParameter(
                                    name = "resultado",
                                    type = String.class,
                                    mode = ParameterMode.OUT)}
            ),
            @NamedStoredProcedureQuery(
                    name = "actualizarVersionProcedure",
                    procedureName = "actualizar_version",
                    parameters = {
                            @StoredProcedureParameter(
                                    name = "numeroVersion",
                                    type = Integer.class,
                                    mode = ParameterMode.IN),
                            @StoredProcedureParameter(
                                    name = "id_resultado",
                                    type = Integer.class,
                                    mode = ParameterMode.IN),
                            @StoredProcedureParameter(
                                    name = "resultado",
                                    type = String.class,
                                    mode = ParameterMode.OUT)}
            ),
            @NamedStoredProcedureQuery(
                    name = "calculoSimulacionesHijasAberrates",
                    procedureName = "calculoSimulacionesHijasAberrantes",
                    parameters = {
                            @StoredProcedureParameter(
                                    name = "idescenariofijo",
                                    type = Integer.class,
                                    mode = ParameterMode.IN),
                            @StoredProcedureParameter(
                                    name = "muestra",
                                    type = Integer.class,
                                    mode = ParameterMode.IN),
                            @StoredProcedureParameter(
                                    name = "mensurando",
                                    type = Integer.class,
                                    mode = ParameterMode.IN),
                            @StoredProcedureParameter(
                                    name = "metodo",
                                    type = Integer.class,
                                    mode = ParameterMode.IN),
                            @StoredProcedureParameter(
                                    name = "resultado",
                                    type = String.class,
                                    mode = ParameterMode.OUT)}
            )
    })
public class EscenariosFijos {
    @GenericGenerator(name = "escenarioFijoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "escenarios_fijos_seq"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),})

    @Id
    @Column(name = "id_escenario_fijo", columnDefinition = "serial")
    @GeneratedValue(generator = "escenarioFijoGenerator")
    @Getter
    @Setter
    private Long idEscenarioFijo;

    @ManyToOne
    @JoinColumn(name = "id_muestras")
    @NotNull (message = "La muestra es requerido")
    @Getter @Setter
    private Muestras idMuestras;

    @ManyToOne
    @JoinColumn(name = "id_mensurandos")
    @NotNull(message = "El mensurando es requerido")
    @Getter
    @Setter
    private Mensurandos idMensurandos;

    @Column(name = "d_fecha_creacion", updatable = false)
    @CreationTimestamp
    @Getter
    @Setter
    private Date fechaCreacion;

    @Column(name = "d_fecha_modificacion")
    @CreationTimestamp
    @Getter
    @Setter
    private Date fechaModificacion;

    @ManyToOne
    @JoinColumn(name = "id_metodos")
    @Getter
    @Setter
    private Metodos metodoId;

    @ManyToOne
    @JoinColumn(name = "id_equipo")
    @Getter
    @Setter
    private Equipos equipoId;

    @Column(name = "n_valor_asignado")
    @Getter
    @Setter
    @NotNull(message = "El valor asignado es requerido")
    private Double valorAsignado;

    @Column(name = "n_media")
    @Getter
    @Setter
    @NotNull(message = "El promedio es requerido")
    private Double mediaFiltro;

    @Column(name = "n_desviacion_estandar")
    @Getter
    @Setter
    @NotNull(message = "La desviacion estandar es requerida")
    private Double desviacionEstandar;

    @Column(name = "n_limite_inferior")
    @Getter
    @Setter
    @NotNull(message = "El limite inferior es requerido")
    private Double limiteInferior;

    @Column(name = "n_limite_superior")
    @Getter
    @Setter
    @NotNull(message = "El limite superior es requerido")
    private Double limiteSuperior;

    @Column(name = "n_total_valores")
    @Getter
    @Setter
    @NotNull(message = "La cantidad de valor es requerida")
    private Integer totalValores;

    @Column(name = "n_cantidad_aberrantes")
    @Getter
    @Setter
    @NotNull(message = "La cantidad de aberrantes es requerida")
    private Integer cantidadAberrantes;

    @Column(name = "n_media_general")
    @Getter
    @Setter
    @NotNull(message = "El promedio del algoritmo es requerido")
    private Double mediaGeneral;

    @Column(name = "n_desviacion_general")
    @Getter
    @Setter
    @NotNull(message = "La desviacion estandar del algoritmo es requerida")
    private Double desviacionGeneral;

    @Column(name = "n_total_general")
    @Getter
    @Setter
    @NotNull(message = "La cantidad de elementos reportados es requerida")
    private Integer totalGeneral;

    @Column(name = "n_incertidumbre_general")
    @Getter
    @Setter
    @NotNull(message = "El valor de incertidimbre general requerido")
    private Double incertidumbreGeneral;

    @Column(name = "n_incertidumbre")
    @Getter
    @Setter
    @NotNull(message = "El valor de incertidumbre es requerido")
    private Double incertidumbreSimulacion;

    @Column(name = "b_es_version")
    @Getter
    @Setter
    @NotNull(message = "El valor de version es requerido")
    private Boolean esVersion=false;

    @Column(name = "n_coeficiente_variacion")
    @Getter
    @Setter
    private Double coeficienteVariacion;

    @Column(name = "n_numero_version")
    @Getter
    @Setter
    private Integer numeroVersion;

    @ManyToOne
    @JoinColumn(name="id_escenario_fijo_padre", nullable = true)
    @Getter
    @Setter
    private EscenariosFijos escenariosFijosId;

    @OneToMany(mappedBy = "escenariosFijosId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @Getter @Setter
    @OrderBy
    private Set<EscenariosFijos> escenariosFijosList;

    @OneToMany(mappedBy = "idEscenarioFijo", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @Getter @Setter
    @JsonIgnore
    @OrderBy
    private Set<EscenariosResultados> resultadosDetalladosEscenariosList;

    public EscenariosFijos() {
    }

}
