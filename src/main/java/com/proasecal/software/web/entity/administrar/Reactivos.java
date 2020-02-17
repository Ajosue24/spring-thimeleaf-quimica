package com.proasecal.software.web.entity.administrar;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.proasecal.software.controlexterno.entity.ResultadosDetallados;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "reactivos")
public class Reactivos {

	@GenericGenerator(name = "reactivoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@org.hibernate.annotations.Parameter(name = "sequence_name", value = "reactivo_seq"),
			@org.hibernate.annotations.Parameter(name = "initial_value", value = "52"),
			@org.hibernate.annotations.Parameter(name = "increment_size", value = "1"), })

	@Id
	@Column(name = "id_reactivo", columnDefinition = "serial")
	@GeneratedValue(generator = "reactivoGenerator")
	@Getter
	@Setter
	private Long reactivoId;

	@ManyToOne
	@JoinColumn(name = "id_programa")
	@NotNull(message = "Seleccione un programa")
	@Getter @Setter
	private Programas idPrograma;

	@Column(name = "v_nombre_reactivo", unique=true)
	@Getter @Setter
	@NotNull(message = "El nombre es requerido")	
	private String nombreReactivo;

	@Column(name = "n_grupo", columnDefinition="Varchar(255) default ''")
	@Getter @Setter
	private String grupo;

	@Column(name = "n_cod_proasecal")
	@Getter @Setter
	@NotNull(message = "El código proasecal es requerido")
	private Integer codProasecal;

	@Column(name = "b_estado")
	@Getter @Setter
	private Boolean estado=true;

	@Column(name = "d_fecha_creacion")
	@CreationTimestamp
	@Getter @Setter
	private Date fechaCreacion;

	@OneToMany(mappedBy = "reactivoId", fetch = FetchType.LAZY)
	@Getter @Setter
	private Set<ResultadosDetallados> resultadosDetalladosList;
	
}