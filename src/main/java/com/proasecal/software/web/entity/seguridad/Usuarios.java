package com.proasecal.software.web.entity.seguridad;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proasecal.software.controlexterno.entity.Informes;
import com.proasecal.software.controlexterno.entity.Resultados;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.PropertySource;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "USUARIOS_SISTEMA")
@PropertySource("classpath:static/properties/msg.properties")
public class Usuarios {


    @GenericGenerator(
            name = "usuarioGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "usuario_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            })

    @Id
    @Column(name = "id_usuarios_sistema", columnDefinition = "serial")
    @GeneratedValue(generator = "usuarioGenerator")
    @Getter @Setter
    private long idUsuario;

    @Column(name = "v_usuario",unique=true)
    @NotNull(message = "Ingresa un nombre usuario")
    @Getter @Setter
    private String nombreUsuario;

    @Column(name = "v_password")
    @NotNull(message = "Ingresa un password valido")
    @Size(min = 4,message = "Password mayor a 4  Caracteres")
    @Getter @Setter
    private String password;

    @Column(name = "v_nombre")
    @Getter @Setter
    private String nombres;

    @Column(name = "v_apellido")
    @Getter @Setter
    private String apellidos;

    @Column(name = "b_estado")
    @NotNull
    @Getter @Setter
    private Boolean estado = true;

    @Column(name="d_fecha_creacion")
    @CreationTimestamp
    @Getter @Setter
    private Date fechaCreacion = new Date();

    @Column(name="n_cod_proasecal")
    @Getter @Setter
    private Integer codProasecal;

    @Column(name="b_password_reset")
    @Getter @Setter
    private Boolean passwordReset;

    @Column(name = "v_correo")
    @Getter @Setter
    private String correo;

    @ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name="ROLES_X_USUARIOS", joinColumns=@JoinColumn(name="id_usuarios_sistema"), inverseJoinColumns=@JoinColumn(name="id_roles"))
    @Getter @Setter
    @JsonIgnore
    private Set<Roles> rolesList;

    @OneToMany(mappedBy = "usuarioId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @Getter
    @Setter
    @JsonIgnore
    private Set<UsuariosLabSedes> usuariosLaboratorioList;

    @OneToMany(mappedBy = "idUsuarios", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @Getter
    @Setter
    @JsonIgnore
    private Set<Resultados> resultadosList;
    
    @OneToMany(mappedBy = "idUsuarioModificacion", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @Getter
    @Setter
    @JsonIgnore
    private Set<Resultados> resultadosListModificacion;

    @OneToMany(mappedBy = "usuarioId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @Getter
    @Setter
    @JsonIgnore
    private Set<UsuariosLabSedes> usuariosLabSedesSet;

    @OneToMany(mappedBy = "usuarioDirectorId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    @Getter @Setter
    private Set<Informes> informeDirectorList;

    @OneToMany(mappedBy = "usuarioRevisorId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    @Getter @Setter
    private Set<Informes> informeRevisorList;

    public Usuarios(Usuarios usuario) {
        this.nombreUsuario = usuario.getNombreUsuario();
        this.password = usuario.getPassword();
        this.nombres = usuario.getNombres();
        this.apellidos = usuario.getApellidos();
        this.estado  = usuario.getEstado();
        this.fechaCreacion = usuario.getFechaCreacion();
        this.codProasecal = usuario.getCodProasecal();
        this.correo = usuario.getCorreo();
        this.rolesList = usuario.getRolesList();
    }

    public Usuarios(@NotNull(message = "Ingresa un nombre usuario") String nombreUsuario, @NotNull(message = "Ingresa un password valido") @Size(min = 4, message = "Password mayor a 4  Caracteres") String password, String nombres, String apellidos, @NotNull Boolean estado, Date fechaCreacion, Integer codProasecal, Boolean passwordReset, String correo, Set<Roles> rolesList) {
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.codProasecal = codProasecal;
        this.passwordReset = passwordReset;
        this.correo = correo;
        this.rolesList = rolesList;
    }

    public Usuarios() {
    }
}
