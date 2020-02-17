package com.proasecal.software.web.entity.seguridad;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Set;

@Entity
@Table(name = "PERMISOS")
public class Permisos {

    public Permisos(Long idPermisos){
        this.idPermisos = idPermisos;
    }


    public Permisos(String descripcion,
                    String nombrePermiso,
                    String url,
                    Modulos idModulos,
                    Permisos permisos,
                    Set<Roles> listRoles){
        this.idPermisos = idPermisos;
        this.descripcion = descripcion;
        this.nombrePermiso = nombrePermiso;
        this.url = url;
        this.idModulos = idModulos;
        this.listRoles = listRoles;
        this.permisos = permisos;
    }

    public Permisos() {
    }

    @GenericGenerator(
            name = "permisosGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "permisos_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            })

    @Id
    @Column(name = "id_permisos", columnDefinition = "serial")
    @GeneratedValue(generator = "permisosGenerator")
    @Getter @Setter
    private long idPermisos;

    @Column(name="v_nombre")
    @NotNull
    @Getter @Setter
    private String nombrePermiso;

    @Column(name="v_descripcion")
    @Getter @Setter
    private String descripcion;

    @Column(name="v_url")
    @NotNull
    @Getter @Setter
    private String url;

    @ManyToOne
    @JoinColumn(name = "id_modulos")
    @Null
    @Getter @Setter
    private Modulos idModulos;

    @ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name="ROLES_PERMISOS", joinColumns=@JoinColumn(name="id_permisos"), inverseJoinColumns=@JoinColumn(name="id_roles"))
    @Getter @Setter
    private Set<Roles> listRoles;

    @ManyToOne
    @JoinColumn(name = "id_permisos_dependiente")
    @Getter @Setter
    private Permisos permisos;

    @OneToMany(mappedBy = "idPermisos", fetch = FetchType.EAGER)
    @Getter @Setter
    private Set<Permisos> permisosList;
}
