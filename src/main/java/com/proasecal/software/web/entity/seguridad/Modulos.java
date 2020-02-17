package com.proasecal.software.web.entity.seguridad;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "MODULOS")
public class Modulos {

    public Modulos(Long idModulos){
        this.idModulos = idModulos;
    }

    public Modulos(@NotNull String nombreModulo, String descripcion, Set<Roles> rolesList) {
        this.nombreModulo = nombreModulo;
        this.descripcion = descripcion;
        this.rolesList = rolesList;
    }

    public Modulos() {
    }

    @GenericGenerator(
            name = "modulosGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "modulos_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            })

    @Id
    @Column(name = "id_modulos", columnDefinition = "serial")
    @GeneratedValue(generator = "modulosGenerator")
    @Getter @Setter
    private long idModulos;

    @Column(name = "v_nombre")
    @NotNull
    @Getter @Setter
    private String nombreModulo;

    @Column(name = "v_descripcion")
    @Getter @Setter
    private String descripcion;


    @OneToMany(mappedBy = "idModulos", fetch = FetchType.LAZY)
    @Getter @Setter
    private Set<Permisos> permisosList;


    @ManyToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name="MODULOS_X_ROLES", joinColumns=@JoinColumn(name="id_modulos"), inverseJoinColumns=@JoinColumn(name="id_roles"))
    @Getter @Setter
    private Set<Roles> rolesList;

    @ManyToOne
    @JoinColumn(name = "id_secciones")
    @NotNull
    @JsonBackReference
    @Setter @Getter
    private Secciones idSecciones;
}
