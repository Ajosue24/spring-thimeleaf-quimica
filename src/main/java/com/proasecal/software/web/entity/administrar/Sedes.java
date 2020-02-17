package com.proasecal.software.web.entity.administrar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proasecal.software.web.entity.parametricas.Ciudad;
import com.proasecal.software.web.entity.parametricas.Departamentos;
import com.proasecal.software.web.entity.parametricas.Pais;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "SEDES")
public class Sedes {

    @GenericGenerator(
            name = "sedesGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_sedes_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            }
    )

    @Id
    @Column(name = "id_sedes", columnDefinition = "serial")
    @GeneratedValue(generator = "sedesGenerator")
    @Getter @Setter
    private long idSedes;

    @Column(name = "v_nombre_sede")
    @Getter @Setter
    private String nombreSede;

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

    @Column(name = "v_usuario_director")
    @Getter @Setter
    private String usuarioDirector;

    @Column(name = "v_usuario_calidad")
    @Getter @Setter
    private String usuarioCalidad;

    @Column(name = "b_estado")
    @Getter @Setter
    private Boolean estado=true;

    @Column(name = "b_imprimir_resultados")
    @Getter @Setter
    private Boolean imprimirResultados = false;

    @ManyToOne
    @JoinColumn(name = "id_pais")
    @NotNull
    @JsonIgnore
    @Getter @Setter
    private Pais idPais;

    @ManyToOne
    @JoinColumn(name = "id_ciudad")
    @NotNull
    @Getter @Setter
    @JsonIgnore
    private Ciudad idCiudad;

    @ManyToOne
    @JoinColumn(name = "id_departamentos")
    @NotNull
    @JsonIgnore
    @Getter @Setter
    private Departamentos idDepartamentos;

    @ManyToOne
    @JoinColumn(name = "id_laboratorios")
    @NotNull
    @JsonIgnore
    @Getter @Setter
    private Laboratorios idLaboratoriosSedes;

    @Column(name = "d_fecha_creacion", updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Getter @Setter
    @CreationTimestamp
    private Date fechaCreacion;

    @OneToMany(mappedBy = "idSedes", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    @Getter
    @Setter
    private List<UsuariosLabSedes> sedesUsuariosList;

}
