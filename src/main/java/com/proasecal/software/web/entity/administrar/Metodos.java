package com.proasecal.software.web.entity.administrar;

import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.proasecal.software.controlexterno.entity.EscenariosFijos;
import com.proasecal.software.controlexterno.entity.ResultadosDetallados;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name = "metodos")
public class Metodos {
	
   
	@GenericGenerator(name = "metodoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@org.hibernate.annotations.Parameter(name = "sequence_name", value = "metodo_seq"),
			@org.hibernate.annotations.Parameter(name = "initial_value", value = "221"),
			@org.hibernate.annotations.Parameter(name = "increment_size", value = "1"), })

	@Id
	@Column(name = "id_metodos", columnDefinition = "serial")
	@GeneratedValue(generator = "metodoGenerator")
	@Getter
	@Setter
	private Long metodoId;	
	
	@ManyToOne
	@JoinColumn(name = "id_mensurandos")
	@NotNull(message = "Seleccione un mensurando")
	@Getter @Setter
	private Mensurandos idMensurandos;

	@Column(name = "v_nombre_metodo")
	@Getter @Setter
	@NotNull(message = "El nombre es requerido")	
	private String nombreMetodo;	
	
	@Column(name = "n_grupo")
	@Getter @Setter
	private String grupo;
	
	@Column(name = "n_cod_proasecal")
	@Getter @Setter
	@NotNull(message = "El código proasecal es requerido")
	private String codProasecal;

	@Column(name = "b_estado")
	@Getter @Setter
	private Boolean estado=true;

	@Column(name = "d_fecha_creacion")
	@CreationTimestamp
	@Getter @Setter
	private Date fechaCreacion;

	@OneToMany(mappedBy = "metodoId", fetch = FetchType.LAZY)
	@Getter @Setter
	private Set<ResultadosDetallados> resultadosDetalladosList;

	@OneToMany(mappedBy = "metodoId", fetch = FetchType.LAZY)
	@Getter @Setter
	private Set<EscenariosFijos> escenariosFijosList;

}
