package com.proasecal.software.web.entity.seguridad;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name="ROLES")
public class Roles {
    @GenericGenerator(
            name = "rolesGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "roles_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            })

    @Id
    @Column(name = "id_roles", columnDefinition = "serial")
    @GeneratedValue(generator = "rolesGenerator")
    @Setter
    @Getter
    private long idRoles;


    @Column(name="v_nombre_rol",unique=true)
    @NotNull
    @Setter
    @Getter
    private String nombreRol;

    @Column(name="v_descripcion")
    @Setter
    @Getter
    private String descripcion;

    @Column(name="d_fecha_creacion")
    @CreationTimestamp
    @Setter
    @Getter
    private Date fechaCreacion = new Date();

    @Column(name = "b_estado")
    @Setter
    @Getter
    private Boolean estado = true;

    @Column(name = "b_cod_proasecal")
    @Setter
    @Getter
    private Boolean codigoProasecal = false;

    @ManyToMany(mappedBy = "rolesList")
    @Setter
    @Getter
    private Set<Usuarios> usuariosList = new HashSet<>();

    @ManyToMany(mappedBy = "listRoles", fetch=FetchType.EAGER)
    @Setter
    @Getter
    private Set<Permisos> permisosList = new HashSet<>();

    @ManyToMany(mappedBy = "rolesList",fetch=FetchType.EAGER)
    @Setter
    @Getter
    private Set<Modulos> modulosList = new HashSet<>();

}
