package com.proasecal.software.web.entity.administrar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proasecal.software.web.entity.parametricas.Ciudad;
import com.proasecal.software.web.entity.parametricas.Departamentos;
import com.proasecal.software.web.entity.parametricas.Pais;
import com.proasecal.software.web.entity.parametricas.TipoDocumentoPais;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CLIENTES")
public class Clientes {
    @GenericGenerator(
            name = "clienteGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "cliente_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            }
    )

    @Id
    @Column(name = "id_clientes", columnDefinition = "serial")
    @GeneratedValue(generator = "clienteGenerator")
    @Getter @Setter
    private long clienteId;

    @ManyToOne
    @JoinColumn(name = "id_pais")
    @NotNull
    @Getter @Setter
    @JsonIgnore
    private Pais idPais;

    @ManyToOne
    @JoinColumn(name = "id_tipo_documento_pais")
    @NotNull
    @Getter @Setter
    @JsonIgnore
    private TipoDocumentoPais idTipoPais;

    @Column(name = "V_USUARIO_DIRECTOR")
    @Getter @Setter
    private String usuarioDirector;

    @Column(name = "V_USUARIO_CALIDAD")
    @Getter @Setter
    private String usuarioCalidad;

    @Column(name = "V_ID_CLIENTE",unique=true)
    @NotNull
    @Getter @Setter
    private String numeroIdentificacionCliente;

    @ManyToOne
    @JoinColumn(name = "ID_DEPARTAMENTOS")
    @Getter @Setter
    @JsonIgnore
    private Departamentos idDepartamentos;

    @ManyToOne
    @JoinColumn(name = "ID_CIUDAD")
    @NotNull
    @Getter @Setter
    @JsonIgnore
    private Ciudad idCiudad;

    @Column(name = "V_DIRECCION_CLIENTE")
    @Getter @Setter
    private String direccionCliente;
    @Column(name = "V_CORREO_CLIENTE")
    @Getter @Setter
    private String correoCliente;
    @Column(name = "V_CORREO_ALTERNATIVO")
    @Getter @Setter
    private String correoAlternativo;

    @Column(name = "V_TELEFONO_CLIENTE")
    @Getter @Setter
    private String telefonoCliente;
    @Column(name = "V_TELEFONO_ALTERNATIVO")
    @Getter @Setter
    private String telefonoAlternativo;

    @Column(name = "V_RAZON_SOCIAL")
    @NotNull
    @Getter @Setter
    private String razonSocial;
    @Column(name = "V_NOMBRE_COMERCIAL")
    @Getter @Setter
    private String nombreComercial;

    @Column(name = "B_ESTADO")
    @Getter @Setter
    private Boolean estado = true;

    @Column(name = "D_FECHA_CREACION")
    @CreationTimestamp
    @Getter @Setter
    private Date fechaCreacion;

    @JsonIgnore
    @OneToMany(mappedBy = "clienteId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Getter @Setter
    private List<Laboratorios> LaboratoriosSedesList;
}