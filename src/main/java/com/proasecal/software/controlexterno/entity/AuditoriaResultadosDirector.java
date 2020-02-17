package com.proasecal.software.controlexterno.entity;

import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "AUDIT_RES_DIRECTOR")
public class AuditoriaResultadosDirector {


    @GenericGenerator(name = "id_auditResDirectorGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_audit_res_director_seq"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"), })

    @Id
    @Column(name = "id_audit_res_director", columnDefinition = "serial")
    @GeneratedValue(generator = "id_auditResDirectorGenerator")
    @Getter
    @Setter
    private Long idAuditResDirector;

    @ManyToOne
    @JoinColumn(name = "id_muestras")
    @NotNull
    @Getter @Setter
    private Muestras idMuestras;

    //usuario que realiza la accion
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    @NotNull
    @Getter @Setter
    private Usuarios usuarioId;

    @Column(name="n_tipo") // almacena si fue eliminado o modificado = U modificado D eliminado
    @Getter @Setter
    private char tipoModificacionEliminacion = 'U';

    @Column(name="d_fecha_creacion_resultado")
    @Getter @Setter
    private Date fechaCreacionResultado;

    //usuario Creo el resultado
    @ManyToOne
    @JoinColumn(name = "v_usuario_sede_creacion")
    @NotNull
    @Getter @Setter
    private Usuarios usuarioUSedeCreacion;    
    
    @Column(name="d_fecha_modificacion",updatable = false)
    @Getter @Setter
    @CreationTimestamp
    private Date fechaModificacion;

    @Column(name="v_aprobado")
    @Getter @Setter
    private String aprovadoPor; //Usuario que creo la auditoria

    @Column(name="v_justificacion")
    @Getter @Setter
    private String justificacion;
}
