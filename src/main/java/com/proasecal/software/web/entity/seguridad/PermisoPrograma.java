package com.proasecal.software.web.entity.seguridad;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.PropertySource;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proasecal.software.web.entity.administrar.Programas;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Permiso_Programa")
public class PermisoPrograma {
    @GenericGenerator(
            name = "permisoProgramaGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "permiso_programa_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            })
    
    @Id
    @Column(name = "id_permiso_programa", columnDefinition = "serial")
    @GeneratedValue(generator = "permisoProgramaGenerator")
    @Getter @Setter
    private long idPermisoPrograma;    
    
    @ManyToOne
    @JoinColumn(name = "id_usuarios_sistema")
    @JsonBackReference
    @JsonIgnore
    @Getter @Setter
    private Usuarios usuarioId;    

	@ManyToOne
	@JoinColumn(name = "id_programa")
	@NotNull(message = "Seleccione un programa")
	@Getter
	@Setter
	private Programas idPrograma;
	

	
	@Column(name = "es_director", columnDefinition="BOOLEAN DEFAULT false")
	@Getter @Setter
	private Boolean esDirector=false;	
	
	@Column(name = "es_revisor", columnDefinition="BOOLEAN DEFAULT false")
	@Getter @Setter
	private Boolean esRevisor=false;	
}
