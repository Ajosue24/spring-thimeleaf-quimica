package com.proasecal.software.web.entity.administrar;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "equipos")
public class Equipos {
	
	@GenericGenerator(name = "equipoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@org.hibernate.annotations.Parameter(name = "sequence_name", value = "equipo_seq"),
			@org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
			@org.hibernate.annotations.Parameter(name = "increment_size", value = "1"), })

	@Id
	@Column(name = "id_equipo", columnDefinition = "serial")
	@GeneratedValue(generator = "equipoGenerator")
	@Getter
	@Setter
	private Long equipoId;
	
	@ManyToOne
	@JoinColumn(name = "id_programa")
	@NotNull(message = "Seleccione un programa")
	@Getter
	@Setter
	private Programas idPrograma;
		
	@Column(name = "v_nombre_equipo")
	@NotNull(message="El nombre no puede estar vacio")
	@Getter
	@Setter
	private String nombreEquipo;
	
	@Column(name = "n_grupo")
	@Getter
	@Setter
	private String grupo;
	
	@Column(name = "n_cod_proasecal")
	@NotNull(message="El código no puede estar vacio")
	@Getter
	@Setter
	private String codProasecal;
	
	@Column(name = "b_estado")
	@Getter
	@Setter
	private Boolean estado=true;
		
	@Column(name = "d_fecha_creacion", updatable = false)
	@CreationTimestamp
	@Getter
	@Setter
	private Date  fechaCreacion;
}
