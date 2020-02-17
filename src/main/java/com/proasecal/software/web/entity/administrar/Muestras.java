package com.proasecal.software.web.entity.administrar;

import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proasecal.software.controlexterno.entity.EscenariosFijos;
import com.proasecal.software.controlexterno.entity.ObservacionMuestra;
import com.proasecal.software.controlexterno.entity.Proceso;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "muestras")
public class Muestras {
	
	@GenericGenerator(name = "muestrasGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@org.hibernate.annotations.Parameter(name = "sequence_name", value = "muestras_seq"),
			@org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
			@org.hibernate.annotations.Parameter(name = "increment_size", value = "1"), })

	@Id
	@Column(name = "id_muestras", columnDefinition = "serial")
	@GeneratedValue(generator = "muestrasGenerator")
	@Getter @Setter
	private Long muestraId;	
	
	@ManyToOne
	@JoinColumn(name = "id_programas")
	@NotNull(message = "Seleccione un programa")
	@Getter @Setter
	private Programas idPrograma;	

	@ManyToOne
	@JoinColumn(name = "id_tipos_muestras")
	@NotNull(message = "Seleccione un tipo de muestra")
	@Getter @Setter
	private TiposMuestras idTipoMuestra;
	
	@Column(name = "n_numero_muestra")
	@Getter @Setter
	@NotNull(message = "El numero de muestra es requerido")
	private Integer numeroMuestra;
	
	@Column(name = "v_numero1")
	@Getter @Setter	
	private Integer numero1;	
	
	@Column(name = "v_numero2")
	@Getter @Setter	
	private Integer numero2;	
	
	@Column(name = "v_numero3")
	@Getter @Setter	
	private Integer numero3;	
	
	@Column(name = "v_observacion_muestra", columnDefinition = "TEXT")
	@Getter @Setter	
	private String observacionMuestra;		
	
	@Column(name = "v_observacion_usuarios")
	@Getter @Setter	
	private String observacionUsuario;	
	
	@Column(name = "b_mostrar_muestra")
	@Getter @Setter	
	private Boolean mostrarMuestra;	
	
	@Column(name = "d_fecha_inicial")
	@Getter @Setter	
	@NotNull(message = "La fecha inicial es requerida")	
	@DateTimeFormat(pattern = "yyyy-MM-dd")	
	private Date fechaInicial;		
	
	@Column(name = "d_fecha_final")
	@Getter @Setter	
	@NotNull(message = "La fecha final es requerida")	
	@DateTimeFormat(pattern = "yyyy-MM-dd")		
	private Date fechaFinal;	
	
	@Column(name = "d_fecha_bloqueo")
	@Getter @Setter			
	@DateTimeFormat(pattern = "yyyy-MM-dd")		
	private Date fechaBloqueo;	
	
	@Column(name = "d_fecha_cierre")
	@Getter @Setter			
	@DateTimeFormat(pattern = "yyyy-MM-dd")		
	private Date fechaCierre;	

	@Column(name = "d_fecha_lib_resultados")
	@Getter @Setter	
	@DateTimeFormat(pattern = "yyyy-MM-dd")		
	private Date fechaLibResultado;	
	
	@Column(name = "d_fecha_creacion")
	@CreationTimestamp
	@Getter @Setter
	@DateTimeFormat(pattern = "yyyy-MM-dd")		
	private Date fechaCreacion;

	@OneToMany(mappedBy = "idMuestras", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@Getter
	@Setter
	@JsonIgnore
	private Set<InscripcionMuestras> inscripcionMuestrasList;

	@OneToMany(mappedBy = "idMuestras", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@Getter
	@Setter
	@JsonIgnore
	private Set<EscenariosFijos> escenariosFijosList;

	@OneToMany(mappedBy = "muestraId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JsonIgnore
	@Getter @Setter
	@OrderBy
	private List<Proceso> procesoList;

	@OneToMany(mappedBy = "muestraId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JsonIgnore
	@Getter @Setter
	private Set<ObservacionMuestra> observacionMuestraList;
}
