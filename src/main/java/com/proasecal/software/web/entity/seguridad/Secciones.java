package com.proasecal.software.web.entity.seguridad;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "SECCIONES")
public class Secciones {

        @GenericGenerator(
                name = "seccionesGenerator",
                strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
                parameters = {
                        @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_secciones_seq"),
                        @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                        @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
                }
        )

        @Id
        @Column(name = "id_secciones",columnDefinition = "serial")
        @GeneratedValue(generator = "seccionesGenerator")
        @Setter @Getter
        private Long idSecciones;

        @Column(name = "v_nombre")
        @NotNull
        @Setter @Getter
        String nombre;


    @OneToMany(mappedBy = "idSecciones", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Setter @Getter
    private List<Modulos> modulosSeccionesList;
}
