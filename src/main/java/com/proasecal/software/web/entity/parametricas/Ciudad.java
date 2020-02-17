package com.proasecal.software.web.entity.parametricas;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.proasecal.software.web.entity.administrar.Clientes;
import com.proasecal.software.web.entity.administrar.Laboratorios;
import com.proasecal.software.web.entity.administrar.Sedes;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "CIUDAD")
public class Ciudad {

    @GenericGenerator(
            name = "ciudadGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ciudad_id_ciudad_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            }
    )

    @Id
    @Column(name = "id_ciudad",columnDefinition = "serial")
    @GeneratedValue(generator = "ciudadGenerator")
    @Getter @Setter
    long idCiudad;

    @Column(name = "v_nombre")
    @NotNull
    @Getter @Setter
    private String nombreCiudad;

    @ManyToOne
    @JoinColumn(name = "id_departamentos")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonBackReference
    @Getter @Setter
    private Departamentos idDepartamentos;


    @OneToMany(mappedBy = "idCiudad", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Getter @Setter
    private Set<Clientes> clientesList;

    @OneToMany(mappedBy = "idCiudad", cascade=CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Getter @Setter
    private Set<Laboratorios> LaboratoriosSedesList;


    @OneToMany(mappedBy = "idCiudad", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Getter @Setter
    private Set<Sedes> ciudSedesList;
}
