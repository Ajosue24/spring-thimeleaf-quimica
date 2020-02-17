package com.proasecal.software.web.entity.seguridad;

import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.administrar.Metodos;
import com.proasecal.software.web.entity.administrar.Muestras;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auditoria_control_externo")
public class AuditoriaControlExterno {

    @GenericGenerator(
            name = "auditoriaControlExternoGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_auditoria_control_externo"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            })
    @Id
    @Column(name = "id_auditoria_control_externo", columnDefinition = "serial")
    @GeneratedValue(generator = "auditoriaControlExternoGenerator")
    @Getter @Setter
    Long idAuditoriaControlExterno;


    @Getter @Setter
    @Column(name = "d_fecha")
    @CreationTimestamp
    private Date fecha;

    @Column(name="v_programa")
    @Getter @Setter
    private String programa;

    @ManyToOne
    @JoinColumn(name = "id_muestras")
    @NotNull
    @Getter @Setter
    private Muestras idMuestras;

    @Setter @Getter
    @Column(name = "v_accion")
    private String accion;

    @Column(name="v_justificacion")
    @Getter @Setter
    private String justificacion= "";

    @Column(name="v_responsable")
    @Getter @Setter
    private String responsable= "";

    @ManyToOne(optional = false)
    @Getter @Setter
    @JoinColumn(name = "id_usuario")
    private Usuarios idUsuario;

    @Column(name = "v_usuario_resultado")
    @Getter @Setter
    private String usuarioResultado="";

    @Column(name = "n_valor_asignado")
    @Getter @Setter
    private String valorAsignado= "";

    //mensurandos
    @ManyToOne
    @JoinColumn(name = "id_mensurandos")
    @Getter @Setter
    private Mensurandos idMensurandos;

    //metodos
    @ManyToOne
    @JoinColumn(name = "id_metodos")
    @Getter @Setter
    private Metodos filtro;

    @Column(name = "n_numero_version")
    @Getter @Setter
    private Integer numeroVersion;

    public Usuarios getUsuarioLogged(){
        Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
        Usuarios usuarios = (Usuarios) auth.get().getPrincipal();
        return usuarios;
    }
}
