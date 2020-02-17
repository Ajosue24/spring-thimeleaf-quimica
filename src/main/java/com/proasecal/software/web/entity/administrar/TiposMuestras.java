package com.proasecal.software.web.entity.administrar;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tipos_muestras")
public class TiposMuestras {

	@GenericGenerator(name = "tiposmuestrasGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@org.hibernate.annotations.Parameter(name = "sequence_name", value = "tiposmuestras_seq"),
			@org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
			@org.hibernate.annotations.Parameter(name = "increment_size", value = "1"), })

	@Id
	@Column(name = "id_tipos_muestras", columnDefinition = "serial")
	@GeneratedValue(generator = "tiposmuestrasGenerator")
	@Getter @Setter
	private Long tiposMuestrasId;
	
	@ManyToOne
	@JoinColumn(name = "id_programas")
	@NotNull(message = "Seleccione un programa")
	@Getter @Setter
	private Programas idPrograma;	
	
	
	@Column(name = "v_nombre", unique=true)
	@Getter @Setter
	@NotNull(message = "El nombre es requerido")	
	private String nombre;	
	

	@Column(name = "b_estado")
	@Getter @Setter
	private Boolean estado;

	@Column(name = "d_fecha_creacion")
	@CreationTimestamp
	@Getter @Setter
	private Date fechaCreacion;
}
