package com.proasecal.software.web.entity.inscripcion;

import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.entity.administrar.Sedes;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "inscripcion_programas")
public class InscripcionProgramas {

	@GenericGenerator(name = "inscripProgramasGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@org.hibernate.annotations.Parameter(name = "sequence_name", value = "inscripProgramas_seq"),
			@org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
			@org.hibernate.annotations.Parameter(name = "increment_size", value = "1"), })
	@Id
	@Column(name = "id_inscripcion_programas", columnDefinition = "serial")
	@GeneratedValue(generator = "inscripProgramasGenerator")
	@Getter @Setter
	private Long inscripProgramaId;

	@ManyToOne
	@JoinColumn(name = "id_laboratorios_sedes ")
	@NotNull(message = "La sede es requerida")
	@Getter @Setter
	private Sedes idSedes;

	@ManyToOne
	@JoinColumn(name = "id_usuarios_lab_sedes")
	@NotNull(message = "El usuario es requerido")
	@Getter @Setter
	private UsuariosLabSedes idUsuarioLabSedes;

	@ManyToOne
	@JoinColumn(name = "id_programas")
	@NotNull(message = "Seleccione un programa")
	@Getter @Setter
	private Programas programaId;

	@Column(name = "d_fecha_creacion")
	@CreationTimestamp
	@Getter @Setter
	private Date fechaCreacion;

	@Column(name = "n_cod_proasecal")
	@Getter @Setter
	private Long codProasecal;

	@OneToMany(mappedBy = "inscripProgramaId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@OrderColumn(name="d_fecha_fin")
	@JsonIgnore
	@Getter @Setter
	private Set<PeriodosVigencia> periodosVigenciaList;

	@OneToMany(mappedBy = "inscripProgramaId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JsonIgnore
	@Getter @Setter
	private Set<InscripcionMuestras> inscripcionMuestrasList;

	@OneToMany(mappedBy = "idPrograma", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JsonIgnore
	@Getter @Setter
	private Set<Muestras> muestrasList;
}
