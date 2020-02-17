package com.proasecal.software.controlexterno.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tipo_documentos")
public class TipoDocumentos {
	
	@GenericGenerator(name = "tipodocumentoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@org.hibernate.annotations.Parameter(name = "sequence_name", value = "tipodocumento_seq"),
			@org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
			@org.hibernate.annotations.Parameter(name = "increment_size", value = "1"), })
	
	@Id
	@Column(name = "id_tipos_documentos", columnDefinition = "serial")
	@GeneratedValue(generator = "tipodocumentoGenerator")
	@Getter
	@Setter
	private Long tipoDocumentoId;	
	
	@Column(name = "v_nombre")
	@Getter @Setter
	@NotNull(message = "El nombre es requerido")	
	private String nombre;
	
	@Column(name = "v_descripcion")
	@Getter @Setter
	@NotNull(message = "La descripcion es requerido")	
	private String descripcion;
}
