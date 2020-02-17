package com.proasecal.software.web.entity.parametricas;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proasecal.software.controlexterno.entity.TipoObservacion;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import java.util.Set;

@Entity
@Table(name="tipos_programas")
public class TipoProgramas {
	
	
	@GenericGenerator(name = "tiposprogramaGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@org.hibernate.annotations.Parameter(name = "sequence_name", value = "tiposprograma_seq"),
			@org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
			@org.hibernate.annotations.Parameter(name = "increment_size", value = "1"), })

	@Id
	@Column(name = "id_tipo_programa", columnDefinition = "serial")
	@GeneratedValue(generator = "tiposprogramaGenerator")
	@Getter
	@Setter
	private Long tipoProgramaId;
	
	@Column(name="v_nombre_tipo_programa")
	@Getter @Setter
	@JsonIgnore
	private String nombreTipoPrograma;

	@OneToMany(mappedBy = "tipoProgramasId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JsonIgnore
	@Getter @Setter
	private Set<TipoObservacion> tipoObservacionList;

	public TipoProgramas(Long tipoProgramaId, String nombreTipoPrograma) {
		super();
		this.tipoProgramaId = tipoProgramaId;
		this.nombreTipoPrograma = nombreTipoPrograma;
	}

	public TipoProgramas() {}

}
