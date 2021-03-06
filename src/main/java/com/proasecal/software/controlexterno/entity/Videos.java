package com.proasecal.software.controlexterno.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import com.proasecal.software.web.entity.administrar.Programas;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "videos")
public class Videos {
    @GenericGenerator(name = "videosGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "videos_seq"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),})

    @Id
    @Column(name = "id_videos", columnDefinition = "serial")
    @GeneratedValue(generator = "videosGenerator")
    @Getter
    @Setter
    private Long videosId;

    @ManyToOne
    @JoinColumn(name = "id_programa")
    @NotNull(message = "Seleccione un programa")
    @Getter
    @Setter
    private Programas idPrograma;

    @Column(name = "v_nombre")
    @Getter
    @Setter
    @NotNull(message = "El nombre es requerido")
    private String nombre;

    @Column(name = "v_direccion_archivo")
    @Getter
    @Setter
    @NotNull(message = "La direccion del archivo es requerida")
    private String direccionArchivo;

    @Column(name = "n_orden")
    @Getter
    @Setter
    @NotNull(message = "El numero de orden es requerido")
    private Integer orden;

    @Column(name = "b_estado")
    @Getter
    @Setter
    private Boolean estado = true;

    @Column(name = "d_fecha_creacion")
    @CreationTimestamp
    @Getter
    @Setter
    private Date fechaCreacion;

    @Column(name = "v_descripcion")
    @Getter
    @Setter
    private String descripcion;
}