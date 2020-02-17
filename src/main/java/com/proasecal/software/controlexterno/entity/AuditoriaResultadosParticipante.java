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
@Table(name = "AUDIT_RES_PARTICIPANTE")
public class AuditoriaResultadosParticipante {

    @GenericGenerator(name = "id_auditResParticipanteGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_audit_res_participante_seq"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"), })

    @Id
    @Column(name = "id_audit_res_participante", columnDefinition = "serial")
    @GeneratedValue(generator = "id_auditResParticipanteGenerator")
    @Getter
    @Setter
    private Long idAuditResParticipante;

    @Column(name="d_valor_reportado",updatable = false)
    @Getter @Setter
    @CreationTimestamp
    private Date fechaGeneracionReporte;

    //usuario que realiza la accion
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    @NotNull
    @Getter @Setter
    private Usuarios usuarioId;


    @ManyToOne
    @JoinColumn(name = "id_muestras")
    @NotNull
    @Getter @Setter
    private Muestras idMuestras;
}
