package com.proasecal.software.web.entity.seguridad;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "FILTROS_SECCIONES")
public class FiltrosSecciones {


    @GenericGenerator(
            name = "filtrosGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_filtros_secciones_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            }
    )

    @Id
    @Column(name = "id_filtros_secciones", columnDefinition = "serial")
    @GeneratedValue(generator = "filtrosGenerator")
    @Setter
    @Getter
    Long idFiltros;

    @Setter
    @Getter
    @Column(name = "v_nombre")
    String nombre;

    @Setter
    @Getter
    @Column(name = "v_obj_nombre")
    String bdNombre;

    @ManyToOne
    @JoinColumn(name = "id_secciones")
    @Setter @Getter
    private Secciones idSecciones;
}
