package com.proasecal.software.web.entity.inscripcion;

import java.util.Comparator;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "periodosvigencia")
public class PeriodosVigencia {
    @GenericGenerator(name = "periodosvigenciaGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "periodosvigencia_seq"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),})


    @Id
    @Column(name = "id_periodosvigencia", columnDefinition = "serial")
    @GeneratedValue(generator = "periodosvigenciaGenerator")
    @Getter
    @Setter
    private Long periodosvigenciaId;


    @ManyToOne
    @JoinColumn(name = "id_inscripcionprogramas")
    @NotNull(message = "Seleccione un programa inscrito")
    @Getter
    @Setter
    @JsonBackReference
    private InscripcionProgramas inscripProgramaId;

    @Column(name = "v_periodos_vigencia_registrados")
    @Getter
    @Setter
    private String perVigenciaRegistrados;

    @Column(name = "v_modalidad")
    @Getter
    @Setter
    @NotNull(message = "La modalidad es requerida")
    private String modalidad;

    @Column(name = "d_fecha_inicio")
    @Getter
    @Setter
    @NotNull(message = "La fecha inicio es requerida")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaInicio;

    @Column(name = "d_fecha_fin")
    @Getter
    @Setter
    @NotNull(message = "La fecha fin es requerida")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaFin;

    @Column(name = "b_en_mora")
    @Getter
    @Setter
    @NotNull(message = "El campo en mora es requerido")
    private Boolean enMora = false;

    @Column(name = "b_muestra_patrocinada")
    @Getter
    @Setter
    @NotNull(message = "El campo muestra patrocinada es requerido")
    private Boolean muestraPatrocinada = false;

    @Column(name = "v_cliente_patrocinador")
    @Getter
    @Setter
    private String clientePatrocinador;

    @Column(name = "d_fecha_creacion", updatable = false)
    @CreationTimestamp
    @Getter
    @Setter
    private Date fechaCreacion;


    public static Comparator<PeriodosVigencia> datePerComparator = new Comparator<PeriodosVigencia>() {

        public int compare(PeriodosVigencia s1, PeriodosVigencia s2) {
            Date perVigen1 = s1.getFechaFin();
            Date perVigen2 = s2.getFechaFin();
            //ascending order
            return perVigen1.compareTo(perVigen2);
        }
    };
}
