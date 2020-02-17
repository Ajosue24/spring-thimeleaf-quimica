package com.proasecal.software.web.entity.parametricas;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "PROCESOS")
public class Procesos {
    @GenericGenerator(
            name = "procesosGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_proceso_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            }
    )
    @Id
    @Column(name = "id_proceso",columnDefinition = "serial")
    @GeneratedValue(generator = "procesosGenerator")
    @Getter @Setter
    long idProceso;

    @Column(name = "v_nombre_proceso")
    @NotNull
    @Getter @Setter
    private String nombreProceso;

    @Column(name = "b_estado")
    @NotNull
    @Getter @Setter
    private Boolean estado;


    @OneToMany(mappedBy = "idProceso", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Variables> variablesList;
}
