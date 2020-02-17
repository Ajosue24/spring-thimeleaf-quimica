package com.proasecal.software.web.entity.administrar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proasecal.software.web.entity.parametricas.Ciudad;
import com.proasecal.software.web.entity.parametricas.Departamentos;
import com.proasecal.software.web.entity.parametricas.Pais;
import com.proasecal.software.web.entity.parametricas.TipoDocumentoPais;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "LABORATORIOS")
public class Laboratorios {

    @GenericGenerator(
            name = "laboratoriosGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_laboratorios_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            }
    )

    @Id
    @Column(name = "id_laboratorios", columnDefinition = "serial")
    @GeneratedValue(generator = "laboratoriosGenerator")
    @Getter @Setter
    private long idLaboratoriosSedes;

    @ManyToOne
    @JoinColumn(name = "id_pais")
    @NotNull
    @JsonIgnore
    @Getter @Setter
    private Pais idPais;

    @ManyToOne
    @JoinColumn(name = "id_departamentos")
    @NotNull
    @JsonIgnore
    @Getter @Setter
    private Departamentos idDepartamentos;

    @ManyToOne
    @JoinColumn(name = "id_ciudad")
    @NotNull
    @JsonIgnore
    @Getter @Setter
    private Ciudad idCiudad;

    @ManyToOne
    @JoinColumn(name = "id_clientes")
    @JsonIgnore
    @Getter @Setter
    private Clientes clienteId;

    @ManyToOne
    @JoinColumn(name = "id_tipo_documento_pais")
    @NotNull
    @JsonIgnore
    @Getter @Setter
    private TipoDocumentoPais idTipoPais;

    @Column(name = "v_direccion")
    @Getter @Setter
    private String direccion;

    @Column(name = "v_correo")
    @Getter @Setter
    private String correo;

    @Column(name = "v_correo_alternativo")
    @Getter @Setter
    private String correoAlternativo;

    @Column(name = "v_telefono")
    @Getter @Setter
    private String telefono;

    @Column(name = "v_telefono_alternativo")
    @Getter @Setter
    private String telefonoAlternativo;

    @Column(name = "d_fecha_creacion", updatable = false)
    @CreationTimestamp
    @Getter @Setter
    private Date fechaCreacion;

    @Column(name = "v_usuario_director")
    @Getter @Setter
    private String usuarioDirector;

    @Column(name = "v_usuario_calidad")
    @Getter @Setter
    private String usuarioCalidad;

    @Column(name = "v_numero_identificacion")
    @Getter @Setter
    private String numeroIdentificacion;

    @Column(name = "v_razon_social")
    @Getter @Setter
    private String razonSocial;

    @Column(name = "v_nombre_comercial")
    @Getter @Setter
    private String nombreComercial;

    @Column(name = "b_estado")
    @Getter @Setter
    private Boolean estadoLabSedes;

    @OneToMany(mappedBy = "idLaboratoriosSedes", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    @JsonIgnore
    @Getter
    @Setter
    private Set<Sedes> laboratSedesList;

    @Column(name = "b_imprimir_resultados")
    @Getter @Setter
    private Boolean imprimirResultados = false;

    @OneToMany(mappedBy = "idLaboratoriosSedes", cascade=CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    @Getter @Setter
    private Set<UsuariosLabSedes> labUsuarioslist;

    public Laboratorios() {
    }

    public Laboratorios(@NotNull Pais idPais, @NotNull Departamentos idDepartamentos, @NotNull Ciudad idCiudad, Clientes clienteId, @NotNull TipoDocumentoPais idTipoPais, String direccion, String correo, String correoAlternativo, String telefono, String telefonoAlternativo/*, Boolean imprimirResultados*/, Date fechaCreacion, String usuarioDirector, String usuarioCalidad, String numeroIdentificacion, String razonSocial, String nombreComercial, Boolean estadoLabSedes) {
        this.idPais = idPais;
        this.idDepartamentos = idDepartamentos;
        this.idCiudad = idCiudad;
        this.clienteId = clienteId;
        this.idTipoPais = idTipoPais;
        this.direccion = direccion;
        this.correo = correo;
        this.correoAlternativo = correoAlternativo;
        this.telefono = telefono;
        this.telefonoAlternativo = telefonoAlternativo;
        this.fechaCreacion = fechaCreacion;
        this.usuarioDirector = usuarioDirector;
        this.usuarioCalidad = usuarioCalidad;
        this.numeroIdentificacion = numeroIdentificacion;
        this.razonSocial = razonSocial;
        this.nombreComercial = nombreComercial;
        this.estadoLabSedes = estadoLabSedes;
    }

}