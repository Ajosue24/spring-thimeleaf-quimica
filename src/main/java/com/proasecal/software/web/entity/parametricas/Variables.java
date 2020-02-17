package com.proasecal.software.web.entity.parametricas;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "VARIABLES")
public class Variables {

    @GenericGenerator(
            name = "variablesGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_variable_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            }
    )
    @Id
    @Column(name = "id_variable",columnDefinition = "serial")
    @GeneratedValue(generator = "procesosGenerator")
    @Getter @Setter
    long idVariable;

    @Column(name = "v_nombre_variable")
    @NotNull
    @Getter @Setter
    private String nombreVariable;

    @Column(name = "n_valor")
    @NotNull
    @Getter @Setter
    private Double valor;

    @ManyToOne
    @JoinColumn(name = "proceso_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonBackReference
    @Getter @Setter
    private Procesos idProceso;

    @Column(name = "b_estado")
    @NotNull
    @Getter @Setter
    private Boolean estado;

    @Column(name = "v_color")
    @Getter @Setter
    private String color;

    public Variables(@NotNull Double valor) {
        this.valor = valor;
    }

    public Variables(@NotNull String nombreVariable, @NotNull Double valor, Procesos idProceso, @NotNull Boolean estado, String color) {
        this.nombreVariable = nombreVariable;
        this.valor = valor;
        this.idProceso = idProceso;
        this.estado = estado;
        this.color = color;
    }

    public Variables(){

    }

    public Variables(String color) {
        this.color = color;
    }
}
