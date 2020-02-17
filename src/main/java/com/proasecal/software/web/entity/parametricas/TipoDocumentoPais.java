package com.proasecal.software.web.entity.parametricas;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
    @Table(name = "TIPO_DOCUMENTO_PAIS")
    public class TipoDocumentoPais {

        @GenericGenerator(
                name = "idTipoDocumentoPaisGenerator",
                strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
                parameters = {
                        @org.hibernate.annotations.Parameter(name = "sequence_name", value = "id_tipo_documento_pais_seq"),
                        @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                        @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
                }
        )

        @Id
        @Column(name = "id_tipo_documento_pais",columnDefinition = "serial")
        @GeneratedValue(generator = "idTipoDocumentoPaisGenerator")
        @Getter @Setter
       private long idTipoPais;

    @ManyToOne
    @JoinColumn(name = "id_pais")
    @NotNull
    @JsonIgnore
    @Getter @Setter
    private Pais idPais;

    @Column(name="v_nombre")
    @Getter @Setter
    String nombreId;

    @Column(name="v_descripcion")
    @Getter @Setter
    String descripcion;
}

