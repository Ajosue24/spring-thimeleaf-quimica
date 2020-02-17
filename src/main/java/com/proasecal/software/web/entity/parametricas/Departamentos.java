package com.proasecal.software.web.entity.parametricas;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "DEPARTAMENTOS")
public class Departamentos {

    @GenericGenerator(
            name = "departamentoGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_departamentos_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            }
    )

    @Id
    @Column(name = "id_departamentos",columnDefinition = "serial")
    @GeneratedValue(generator = "departamentoGenerator")
    @Setter @Getter
    private long idDepartamentos;

    @Column(name = "v_nombre")
    @NotNull
    @Setter @Getter
    private String nombreDepartamento;

    @ManyToOne
    @JoinColumn(name = "id_pais")
    @JsonBackReference
    @Setter @Getter
    private Pais idPais;
}