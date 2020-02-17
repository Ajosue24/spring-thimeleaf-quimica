package com.proasecal.software.web.repository.inscripcion;

import com.proasecal.software.web.entity.administrar.Metodos;
import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import com.proasecal.software.web.entity.inscripcion.InscripcionProgramas;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface InscripcionMuestrasRepository extends CrudRepository<InscripcionMuestras, Long> {

    InscripcionMuestras findByInscripProgramaId(InscripcionProgramas inscripcionProgramas);

    InscripcionMuestras findByInscripProgramaIdAndFechaCreacionBetween(InscripcionProgramas inscripcionProgramas,Date fechaInicial, Date fechaFinal);

    Optional<InscripcionMuestras> findByInscripProgramaIdAndIdMuestrasAndIdUsuarios
            (InscripcionProgramas inscripcionProgramas, Muestras muestras, UsuariosLabSedes usuarios);

    List<InscripcionMuestras> findByIdUsuariosOrderByIdMuestras_FechaInicialAsc(UsuariosLabSedes usuario);

    @Query("select u from InscripcionMuestras u where u.idUsuarios = :usuario "
            + "AND CONCAT(u.idMuestras.fechaInicial,'') LIKE CONCAT('%',:desde,'%') "
            + "AND CONCAT(u.idMuestras.fechaFinal,'') LIKE CONCAT('%',:hasta,'%')"
            + "AND CONCAT(u.idMuestras.numeroMuestra,'') LIKE CONCAT('%',:numero,'%') ")
    Page<InscripcionMuestras> findByUsuarioAndFechaIncialDescConFiltro(@Param("usuario") UsuariosLabSedes usuario
            , @Param("desde") String desde, @Param("hasta") String hasta, @Param("numero") String numero, Pageable pageable);


    boolean existsByInscripProgramaIdAndIdMuestrasAndIdUsuarios
            (InscripcionProgramas inscripcionProgramas, Muestras muestras, UsuariosLabSedes usuarios);

    InscripcionMuestras findByIdUsuariosAndIdMuestras(UsuariosLabSedes usuarios, Muestras muestras);

    Optional<InscripcionMuestras> findByIdUsuariosAndIdInscripcionMuestras(UsuariosLabSedes usuario, Long inscripcionMuestraId);

    @Query("select  im from InscripcionMuestras im "
            + "inner join UsuariosLabSedes uls on im.idUsuarios=uls.idUsuarioLabSedes "
            + "inner join Usuarios u on uls.usuarioId=u.idUsuario "
            + "inner join Laboratorios l on uls.idLaboratoriosSedes=l.idLaboratoriosSedes "
            + "inner join Sedes s on uls.idSedes=s.idSedes "

            + " where CONCAT(im.idMuestras,'')  LIKE CONCAT('%',:muestra,'%') "
            + "and CONCAT(l.idLaboratoriosSedes,'')  LIKE CONCAT('%',:laboratorio,'%') "
            + "and CONCAT(s.idSedes,'')  LIKE CONCAT('%',:sede,'%') "
            + "and CONCAT(u.idUsuario,'')  LIKE CONCAT('%',:usuario,'%') order by  u.codProasecal asc"
    )
    List<InscripcionMuestras> obtListUsuarios(@Param("muestra") String muestra, @Param("laboratorio") String laboratorio, @Param("sede") String sede, @Param("usuario") String usuario);


    @Query("select  im from InscripcionMuestras im "
            + "inner join UsuariosLabSedes uls on im.idUsuarios=uls.idUsuarioLabSedes "
            + "inner join Usuarios u on uls.usuarioId=u.idUsuario "
            + "inner join Laboratorios l on uls.idLaboratoriosSedes=l.idLaboratoriosSedes "
            + "inner join Sedes s on uls.idSedes=s.idSedes "
            + "inner join Resultados r on im.idInscripcionMuestras=r.idInscripcionMuestras "

            + " where CONCAT(im.idMuestras,'')  LIKE CONCAT('%',:muestra,'%') "
            + "and CONCAT(l.idLaboratoriosSedes,'')  LIKE CONCAT('%',:laboratorio,'%') "
            + "and CONCAT(s.idSedes,'')  LIKE CONCAT('%',:sede,'%') "
            + "and CONCAT(u.idUsuario,'')  LIKE CONCAT('%',:usuario,'%') order by  u.codProasecal asc"
    )
    List<InscripcionMuestras> obtListUsuarios2(@Param("muestra") String muestra, @Param("laboratorio") String laboratorio, @Param("sede") String sede, @Param("usuario") String usuario);


    @Query("select  im from InscripcionMuestras im "
            + "inner join UsuariosLabSedes uls on im.idUsuarios=uls.idUsuarioLabSedes "
            + "inner join Usuarios u on uls.usuarioId=u.idUsuario "
            + "inner join Laboratorios l on uls.idLaboratoriosSedes=l.idLaboratoriosSedes "
            + "inner join Sedes s on uls.idSedes=s.idSedes "
            + "left join Resultados r on im.idInscripcionMuestras=r.idInscripcionMuestras "
            + " where CONCAT(im.idMuestras,'')  LIKE CONCAT('%',:muestra,'%') "
            + "and CONCAT(l.idLaboratoriosSedes,'')  LIKE CONCAT('%',:laboratorio,'%') "
            + "and CONCAT(s.idSedes,'')  LIKE CONCAT('%',:sede,'%') "
            + "and CONCAT(u.idUsuario,'')  LIKE CONCAT('%',:usuario,'%') "
            + " and r.idResultados = null order by u.codProasecal asc"
    )
    List<InscripcionMuestras> obtListUsuarios3(@Param("muestra") String muestra, @Param("laboratorio") String laboratorio, @Param("sede") String sede, @Param("usuario") String usuario);


    @Query("select distinct count(*) from InscripcionMuestras im "
            + "inner join UsuariosLabSedes uls on im.idUsuarios=uls.idUsuarioLabSedes "
            + "inner join Usuarios u on uls.usuarioId=u.idUsuario "
            + "inner join Laboratorios l on uls.idLaboratoriosSedes=l.idLaboratoriosSedes "
            + "inner join Sedes s on uls.idSedes=s.idSedes "
            + "inner join Resultados r on im.idInscripcionMuestras=r.idInscripcionMuestras "

            + " where CONCAT(im.idMuestras,'')  LIKE CONCAT('%',:muestra,'%') "
            + "and CONCAT(l.idLaboratoriosSedes,'')  LIKE CONCAT('%',:laboratorio,'%') "
            + "and CONCAT(s.idSedes,'')  LIKE CONCAT('%',:sede,'%') "
            + "and CONCAT(u.idUsuario,'')  LIKE CONCAT('%',:usuario,'%') "
    )
    Integer obtListUsuariosInscritos(@Param("muestra") String muestra, @Param("laboratorio") String laboratorio, @Param("sede") String sede, @Param("usuario") String usuario);

    @Query("select distinct count(*) from InscripcionMuestras im "
            + "inner join UsuariosLabSedes uls on im.idUsuarios=uls.idUsuarioLabSedes "
            + "inner join Usuarios u on uls.usuarioId=u.idUsuario "
            + "inner join Laboratorios l on uls.idLaboratoriosSedes=l.idLaboratoriosSedes "
            + "inner join Sedes s on uls.idSedes=s.idSedes "
            + "left join Resultados r on im.idInscripcionMuestras=r.idInscripcionMuestras "

            + " where CONCAT(im.idMuestras,'')  LIKE CONCAT('%',:muestra,'%') "
            + "and CONCAT(l.idLaboratoriosSedes,'')  LIKE CONCAT('%',:laboratorio,'%') "
            + "and CONCAT(s.idSedes,'')  LIKE CONCAT('%',:sede,'%') "
            + "and CONCAT(u.idUsuario,'')  LIKE CONCAT('%',:usuario,'%') "
            + "and r.idResultados = null "
    )
    Integer obtListUsuariosNoInscritos(@Param("muestra") String muestra, @Param("laboratorio") String laboratorio, @Param("sede") String sede, @Param("usuario") String usuario);


    @Query("select im from InscripcionMuestras im "
            + "inner join UsuariosLabSedes uls on im.idUsuarios=uls.idUsuarioLabSedes "
            + "inner join Usuarios u on uls.usuarioId=u.idUsuario "
            + "inner join Laboratorios l on uls.idLaboratoriosSedes=l.idLaboratoriosSedes "
            + "inner join Sedes s on uls.idSedes=s.idSedes "
            + "left join Resultados r on im.idInscripcionMuestras = r.idInscripcionMuestras "

            + "where CONCAT(im.idMuestras,'')  LIKE CONCAT('%',:muestra,'%') "
            + "and CONCAT(l.idLaboratoriosSedes,'')  LIKE CONCAT('%',:laboratorio,'%') "
            + "and CONCAT(s.idSedes,'')  LIKE CONCAT('%',:sede,'%') "
            + "and CONCAT(u.idUsuario,'')  LIKE CONCAT('%',:usuario,'%')"
            + "order by u.codProasecal")
    List<InscripcionMuestras> revisionResultados(
            @Param("muestra") String muestra,
            @Param("laboratorio") String laboratorio,
            @Param("sede") String sede,
            @Param("usuario") String usuario
    );


    @Query("select im from InscripcionMuestras im "
            + "inner join UsuariosLabSedes uls on im.idUsuarios=uls.idUsuarioLabSedes "
            + "inner join Usuarios u on uls.usuarioId=u.idUsuario "
            + "inner join Laboratorios l on uls.idLaboratoriosSedes=l.idLaboratoriosSedes "
            + "inner join Sedes s on uls.idSedes=s.idSedes "
            + "left join Resultados r on im.idInscripcionMuestras = r.idInscripcionMuestras "

            + "where CONCAT(im.idMuestras,'')  LIKE CONCAT('%',:muestra,'%') "
            + "and CONCAT(l.idLaboratoriosSedes,'')  LIKE CONCAT('%',:laboratorio,'%') "
            + "and CONCAT(s.idSedes,'')  LIKE CONCAT('%',:sede,'%') "
            + "and CONCAT(u.idUsuario,'')  LIKE CONCAT('%',:usuario,'%')"
            + "and CONCAT(r.resultadoFecha,'') LIKE CONCAT('%',:resultado,'%')"
            + "order by u.codProasecal")
    List<InscripcionMuestras> revisionResultadosResultado(
            @Param("muestra") String muestra,
            @Param("laboratorio") String laboratorio,
            @Param("sede") String sede,
            @Param("usuario") String usuario,
            @Param("resultado") String resultado
    );


    @Query("select im from InscripcionMuestras im "
            + "inner join UsuariosLabSedes uls on im.idUsuarios=uls.idUsuarioLabSedes "
            + "inner join Usuarios u on uls.usuarioId=u.idUsuario "
            + "inner join Laboratorios l on uls.idLaboratoriosSedes=l.idLaboratoriosSedes "
            + "inner join Sedes s on uls.idSedes=s.idSedes "
            + "left join Resultados r on im.idInscripcionMuestras = r.idInscripcionMuestras "
            + "left join Informes inf on r.idResultados = inf.resultadosId "

            + "where CONCAT(im.idMuestras,'')  LIKE CONCAT('%',:muestra,'%') "
            + "and CONCAT(l.idLaboratoriosSedes,'')  LIKE CONCAT('%',:laboratorio,'%') "
            + "and CONCAT(s.idSedes,'')  LIKE CONCAT('%',:sede,'%') "
            + "and CONCAT(u.idUsuario,'')  LIKE CONCAT('%',:usuario,'%')"
            + "and CONCAT(inf.estadoProceso,'') LIKE CONCAT('%',:proceso,'%') "
            + "order by u.codProasecal")
    List<InscripcionMuestras> revisionResultadosProceso(
            @Param("muestra") String muestra,
            @Param("laboratorio") String laboratorio,
            @Param("sede") String sede,
            @Param("usuario") String usuario,
            @Param("proceso") String proceso
    );


    @Query("select im from InscripcionMuestras im "
            + "inner join UsuariosLabSedes uls on im.idUsuarios=uls.idUsuarioLabSedes "
            + "inner join Usuarios u on uls.usuarioId=u.idUsuario "
            + "inner join Laboratorios l on uls.idLaboratoriosSedes=l.idLaboratoriosSedes "
            + "inner join Sedes s on uls.idSedes=s.idSedes "
            + "left join Resultados r on im.idInscripcionMuestras = r.idInscripcionMuestras "
            + "left join Informes inf on r.idResultados = inf.resultadosId "

            + "where CONCAT(im.idMuestras,'')  LIKE CONCAT('%',:muestra,'%') "
            + "and CONCAT(l.idLaboratoriosSedes,'')  LIKE CONCAT('%',:laboratorio,'%') "
            + "and CONCAT(s.idSedes,'')  LIKE CONCAT('%',:sede,'%') "
            + "and CONCAT(u.idUsuario,'')  LIKE CONCAT('%',:usuario,'%')"
            + "and CONCAT(r.resultadoFecha,'') LIKE CONCAT('%',:resultado,'%')"
            + "and CONCAT(inf.estadoProceso,'') LIKE CONCAT('%',:proceso,'%') "
            + "order by u.codProasecal")
    List<InscripcionMuestras> revisionResultadosFull(
            @Param("muestra") String muestra,
            @Param("laboratorio") String laboratorio,
            @Param("sede") String sede,
            @Param("usuario") String usuario,
            @Param("resultado") String resultado,
            @Param("proceso") String proceso
    );


    @Query("select im from InscripcionMuestras im "
            + "inner join Resultados r on im.idInscripcionMuestras = r.idInscripcionMuestras "
            + "where CONCAT(im.idMuestras,'')  LIKE CONCAT('%',:muestra,'%') "
            + "and r.resultadoFecha = false")
    List<InscripcionMuestras> revisionResultadosEnFecha(
            @Param("muestra") String muestra
    );

    List<InscripcionMuestras> findAllByIdMuestras(Muestras muestras);
}