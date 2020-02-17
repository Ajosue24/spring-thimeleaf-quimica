package com.proasecal.software.web.entity.administrar;

import javax.persistence.*;

import com.proasecal.software.controlexterno.entity.CriteriosAceptabilidad;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "unidades_medidas")
public class UnidadesMedidas {
	
	@GenericGenerator(name = "unidadesmedidasGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@org.hibernate.annotations.Parameter(name = "sequence_name", value = "unidadesmedidas_seq"),
			@org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
			@org.hibernate.annotations.Parameter(name = "increment_size", value = "1"), })

	@Id
	@Column(name = "id_unidades_medidas", columnDefinition = "serial")
	@GeneratedValue(generator = "unidadesmedidasGenerator")
	@Getter @Setter
	private Long unidadesMedidasId;
	
	
	@Column(name="v_magnitud")
	@Getter @Setter
	private String magnitud;
	
	@Column(name="v_unidad")
	@Getter @Setter
	private String unidad;	
	
	@Column(name="v_abreviatura")
	@Getter @Setter
	private String abreviatura;

	@OneToMany(mappedBy = "unidadesMedidasId", fetch = FetchType.LAZY)
	@Getter @Setter
	private List<CriteriosAceptabilidad> criteriosAceptabilidadList;

	@OneToMany(mappedBy = "unidadesMedidasValorAlternativo", fetch = FetchType.LAZY)
	@Getter @Setter
	private List<CriteriosAceptabilidad> criteriosAceptabilidadValorAlternativo;
}
