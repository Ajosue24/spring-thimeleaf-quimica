package com.proasecal.software.web.entity.parametricas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.proasecal.software.web.entity.administrar.Clientes;
import com.proasecal.software.web.entity.administrar.Laboratorios;
import com.proasecal.software.web.entity.administrar.Sedes;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "PAIS")
public class Pais {

    @GenericGenerator(
            name = "paisGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_pais_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            }
    )

    @Id
    @Column(name = "id_pais",columnDefinition = "serial")
    @GeneratedValue(generator = "paisGenerator")
    @Getter @Setter
    private long idPais;

    @Column(name = "v_nombre")
    @NotNull
    @Getter @Setter
    private String nombrePais;

    @Column(name = "v_inicial")
    @Getter @Setter
    private String inicialPais;

    @Column(name = "b_estado")
    @Getter @Setter
    private Boolean estado;

    @OneToMany(mappedBy = "idPais", cascade = CascadeType.PERSIST)
    @JsonIgnore
    @Getter @Setter
    private Set<Departamentos> departamentoList;

    @OneToMany(mappedBy = "idPais", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    @Getter @Setter
    private Set<Clientes> clientesList;

    @OneToMany(mappedBy = "idPais", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonIgnore
    @Getter @Setter
    private Set<TipoDocumentoPais> tipoDocumentoPaisList;

    @OneToMany(mappedBy = "idPais", cascade=CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("LaboratoriosSedesList")
    @Getter @Setter
    private Set<Laboratorios> LaboratoriosSedesList;

    @OneToMany(mappedBy = "idPais", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    @Getter @Setter
    private Set<Sedes> sedesList;

}
