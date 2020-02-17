package com.proasecal.software.web.entity.administrar;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import com.proasecal.software.web.entity.parametricas.TipoProgramas;

@Entity
@Table(name = "programas")
public class Programas {

	@GenericGenerator(name = "programaGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@org.hibernate.annotations.Parameter(name = "sequence_name", value = "programa_seq"),
			@org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
			@org.hibernate.annotations.Parameter(name = "increment_size", value = "1"), })

	@Id
	@Column(name = "id_programa", columnDefinition = "serial")
	@GeneratedValue(generator = "programaGenerator")
	@Getter @Setter
	private Long programaId;
	
	@ManyToOne
	@JoinColumn(name = "id_tipos_programas")
	@NotNull
	@Getter
	@Setter
	private TipoProgramas tipoProgramaId;
	
	@Column(name="v_nombre_programa")
	@Getter @Setter
	private String nombrePrograma;
	
	@Column(name="v_abreviatura")
	@Getter @Setter
	private String abreviatura;
	
	@Column(name="n_orden")
	@Getter @Setter
	private Integer orden;
	
	@Column(name ="d_fecha_creacion")
	@CreationTimestamp
	@Getter @Setter
	private Date fechaCreacion;
	
	@Column(name="b_estado")
	@Getter @Setter
	private Boolean estado;

	public Programas(){}

	public Programas(Long programaId){
		this.programaId=programaId;
	}
}