package com.proasecal.software.web.entity.seguridad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auditoria_web")
public class AuditoriaWeb {

    @GenericGenerator(
            name = "auditoriaWebGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_auditoria_web"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            })

    @Id
    @Column(name = "id_auditoria_web", columnDefinition = "serial")
    @GeneratedValue(generator = "auditoriaWebGenerator")
    @Getter
    @Setter
    Long idAuditoriaWeb;


    @Getter @Setter
    @CreationTimestamp
    @Column(name = "d_fecha")
    private Date fecha;

    @ManyToOne(optional = false)
    @CreatedBy
    @Getter @Setter
    @JoinColumn(name = "id_usuario")
    private Usuarios idUsuario;

    @Getter @Setter
    @Column(name = "v_nombre_tabla")
    private String tableName;

    @Getter @Setter
    @Column(name = "id_tabla")
    private Long idElementoTabla;

    @Setter @Getter
    @Column(name = "v_accion")
    private String accion;


    public AuditoriaWeb(String tableName, Long idElementoTabla,String accion) {
        this.idUsuario = getUsuarioLogged();
        this.tableName = tableName;
        this.idElementoTabla = idElementoTabla;
        this.accion = accion;
    }

    public Usuarios getUsuarioLogged(){
        Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
        Usuarios usuarios = (Usuarios) auth.get().getPrincipal();

        return usuarios;
    }

}
