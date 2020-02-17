package com.proasecal.software.web.entity.seguridad;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proasecal.software.web.entity.administrar.Laboratorios;
import com.proasecal.software.web.entity.administrar.Sedes;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import com.proasecal.software.web.entity.inscripcion.InscripcionProgramas;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.PropertySource;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "USUARIOS_LAB_SEDES")
@PropertySource("classpath:static/properties/msg.properties")
@NamedEntityGraph(
        name = "nombre", attributeNodes = {
        @NamedAttributeNode("idUsuarioLabSedes"),
})
public class UsuariosLabSedes {

    @GenericGenerator(
            name = "usuariosLabSedesGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_usuarios_lab_sedes_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            })

    @Id
    @Column(name = "id_usuarios_lab_sedes", columnDefinition = "serial")
    @GeneratedValue(generator = "usuariosLabSedesGenerator")
    @Getter
    @Setter
    private long idUsuarioLabSedes;

    @ManyToOne
    @JoinColumn(name = "id_usuarios_sistema")
    @JsonBackReference
    @JsonIgnore
    @Getter @Setter
    private Usuarios usuarioId;

    @ManyToOne
    @JoinColumn(name = "id_laboratorio")
    @JsonBackReference
    @JsonIgnore
    @Getter @Setter
    private Laboratorios idLaboratoriosSedes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_sedes")
    @JsonBackReference
    @JsonIgnore
    @Getter @Setter
    private Sedes idSedes;

    @OneToMany(mappedBy = "idUsuarios", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @Getter
    @Setter
    @JsonIgnore
    private Set<InscripcionMuestras> inscripcionMuestrasList;

    @OneToMany(mappedBy = "idUsuarioLabSedes", fetch = FetchType.LAZY)
    @Getter
    @Setter
    @JsonIgnore
    private Set<InscripcionProgramas> inscripcionProgramas;

    public UsuariosLabSedes() {
    }

    public UsuariosLabSedes(Usuarios usuarioId, Laboratorios idLaboratoriosSedes, Sedes idSedes) {
        this.usuarioId = usuarioId;
        this.idLaboratoriosSedes = idLaboratoriosSedes;
        this.idSedes = idSedes;
    }
}