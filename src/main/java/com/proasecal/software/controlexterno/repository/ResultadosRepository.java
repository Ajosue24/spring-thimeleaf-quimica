package com.proasecal.software.controlexterno.repository;

import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import org.springframework.data.repository.CrudRepository;

import com.proasecal.software.controlexterno.entity.Resultados;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ResultadosRepository extends CrudRepository<Resultados, Long> {

    List<Resultados> findByIdInscripcionMuestras_IdMuestras(Muestras muestras);

    List<Resultados> findByIdInscripcionMuestras_IdMuestrasAndResultadoFechaIsTrue(Muestras muestras);


    @Query("select distinct r from Resultados r "
            + "inner join InscripcionMuestras im on r.idInscripcionMuestras=im.idInscripcionMuestras "

            + " where CONCAT(im.idMuestras,'')  LIKE CONCAT('%',:muestraAudi,'%') "
            + "and r.estado<>null"
    )
    List<Resultados> obtListResultados(@Param("muestraAudi") String muestraAudi);


    List<Resultados> findByIdUsuariosAndIdInscripcionMuestras(Usuarios usuarios, InscripcionMuestras inscripcionMuestras);


    Optional<Resultados> findByIdInscripcionMuestras(InscripcionMuestras inscripcionMuestras);

    @Query("select idResultados from Resultados  where idInscripcionMuestras = :IdInsMuestra ")
    Long obtIdResultado(@Param("IdInsMuestra") InscripcionMuestras inscripcionMuestras);


    Resultados findFirstByIdInscripcionMuestras_IdUsuarios_UsuarioIdOrderByIdResultadosDesc(Usuarios usuarios);


    @Query("select distinct r from Resultados r "
            + "inner join ResultadosDetallados rd on r.idResultados = rd.idResultados "
            + "inner join InscripcionMuestras ins on r.idInscripcionMuestras = ins.idInscripcionMuestras "

            + "where CONCAT(ins.idMuestras,'') LIKE CONCAT('%',:muestra,'%') "
            + "and CONCAT(rd.mensurandosId,'') LIKE CONCAT('%',:mensurando,'%') "
            + "and CONCAT(rd.metodoId,'') LIKE CONCAT('%',:metodo,'%') "
    )
    List<Resultados> resultadosMuestraMensurandoMetodo(
            @Param("muestra")String muestra,
            @Param("mensurando")String mensurando,
            @Param("metodo")String metodo
    );

    List<Resultados> findAllByIdInscripcionMuestras(InscripcionMuestras inscripcionMuestras);

    Resultados findFirstByIdInscripcionMuestrasOrderByIdInscripcionMuestrasDesc(InscripcionMuestras inscripcionMuestras);

    Long countAllByIdInscripcionMuestras_IdMuestras(Muestras muestras);

}
