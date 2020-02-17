package com.proasecal.software.web.entity.administrar;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.proasecal.software.controlexterno.entity.CriteriosAceptabilidad;
import com.proasecal.software.controlexterno.entity.EscenariosFijos;
import com.proasecal.software.controlexterno.entity.ResultadosDetallados;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "mensurandos")
public class Mensurandos {
	@GenericGenerator(name = "mensurandosGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@org.hibernate.annotations.Parameter(name = "sequence_name", value = "mensurandos_seq"),
			@org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
			@org.hibernate.annotations.Parameter(name = "increment_size", value = "1"), })

	@Id
	@Column(name = "id_mensurandos", columnDefinition = "serial")
	@GeneratedValue(generator = "mensurandosGenerator")
	@Getter @Setter
	private Long mensurandosId;
	
	
	@ManyToOne
	@JoinColumn(name = "id_programas")
	@NotNull(message = "Seleccione un programa")
	@Getter @Setter
	private Programas idPrograma;	
	
	@ManyToOne
	@JoinColumn(name = "id_unidades_medida")
	@NotNull(message = "Seleccione una unidad de medida")
	@Getter @Setter
	private UnidadesMedidas idUnidadesMedidas;		
	
	@Column(name="v_nombre_mensurando")
	@Getter @Setter
	@NotNull(message = "El nombre es requerido")		
	private String nombreMensurando;
		
	@Column(name="v_abreviatura")
	@Getter @Setter
	private String abreviatura;
	
	@Column(name="n_orden")
	@Getter @Setter
	@NotNull(message = "La numero de orden es requerido")		
	private Integer orden;
	
	@Column(name ="d_fecha_creacion")
	@CreationTimestamp
	@Getter @Setter
	private Date fechaCreacion;
	
	@Column(name="b_estado")
	@Getter @Setter
	private Boolean estado;

	@Column(name="n_cant_decimales")
	@Getter @Setter
	private Integer cantDecimales;

	@OneToMany(mappedBy = "mensurandosId", fetch = FetchType.LAZY)
	@Getter @Setter
	private List<ResultadosDetallados> resultadosDetalladosList;

	@OneToMany(mappedBy = "idMensurandos", fetch = FetchType.LAZY)
	@Getter @Setter
	private List<Metodos> metodosList;

	@OneToMany(mappedBy = "idMensurandos", fetch = FetchType.LAZY)
	@Getter @Setter
	private List<EscenariosFijos> escenariosFijosList;

    @OneToMany(mappedBy = "idMensurandos", fetch = FetchType.LAZY)
    @Getter @Setter
    private List<CriteriosAceptabilidad> criteriosAceptabilidadList;

}
