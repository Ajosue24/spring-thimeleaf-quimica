package com.proasecal.software.controlexterno.repository;

import com.proasecal.software.controlexterno.entity.DAO.Aberrantes;
import com.proasecal.software.controlexterno.entity.Resultados;
import com.proasecal.software.controlexterno.entity.ResultadosDetallados;
import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.administrar.Metodos;
import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ResultadosDetalladosRepository extends CrudRepository<ResultadosDetallados,Long> {

    List<ResultadosDetallados> findByIdResultados_IdInscripcionMuestras_IdMuestrasAndMensurandosIdAndIdResultados_ResultadoFecha
            (Muestras muestras, Mensurandos mensurandos,Boolean isInfecha);

    List<ResultadosDetallados> findByIdResultados_IdInscripcionMuestras_IdMuestrasAndMensurandosIdAndMetodoIdAndIdResultados_ResultadoFecha
            (Muestras muestras, Mensurandos mensurandos,Metodos metodos,Boolean isInFecha);



    List<ResultadosDetallados> findByIdResultados_ResultadoFechaAndIdResultados_IdInscripcionMuestras_IdMuestrasAndMensurandosId
            (Boolean estado, Muestras muestras, Mensurandos mensurandos);
    
    
    void  deleteByIdResultados(Resultados resultado);

    @Procedure(name = "resetAberrantesProcedure")
    String resetAberrantes(@Param("id_muestras") Integer idMuestras,@Param("id_mensurandos") Integer idMensurandos,
                           @Param("id_metodos") Integer idMetodos, @Param("id_equipo") Integer idEquipo,@Param("id_reactivo") Integer idReactivo);

    @Procedure(name = "consensoInicialProcedure")
    String consensoInicial(@Param("id_muestras") Integer idMuestras,@Param("id_mensurandos") Integer idMensurandos,
                           @Param("id_metodos") Integer idMetodos, @Param("id_equipo") Integer idEquipo,@Param("id_reactivo") Integer idReactivo);

    @Procedure(name = "consensoInicialInicialProcedure")
    String consensoInicialInicial(@Param("id_muestras") Integer idMuestras,@Param("id_mensurandos") Integer idMensurandos,
                           @Param("id_metodos") Integer idMetodos, @Param("id_equipo") Integer idEquipo,@Param("id_reactivo") Integer idReactivo);

    @Procedure(name = "iteracionGrubbsProcedure")
    String iteracionGrubbs(@Param("id_muestras") Integer idMuestras,@Param("id_mensurandos") Integer idMensurandos,
                           @Param("id_metodos") Integer idMetodos, @Param("id_equipo") Integer idEquipo,@Param("id_reactivo") Integer idReactivo,@Param("grubbs") Double grubbs);

    @Procedure(name = "algoritmoAProcedure")
    String algoritmoA(@Param("id_muestras") Integer idMuestras,@Param("id_mensurandos") Integer idMensurandos,
                           @Param("id_metodos") Integer idMetodos, @Param("id_equipo") Integer idEquipo,@Param("id_reactivo") Integer idReactivo);

    @Procedure(name = "algoritmoDSProcedure")
    String algoritmoDS(@Param("id_muestras") Integer idMuestras,@Param("id_mensurandos") Integer idMensurandos,
                      @Param("id_metodos") Integer idMetodos, @Param("id_equipo") Integer idEquipo,@Param("id_reactivo") Integer idReactivo);


    List<ResultadosDetallados> findByIdResultadosInAndMensurandosId(List<Resultados> resultados,Mensurandos mensurandos);

    List<ResultadosDetallados> findByIdResultadosInAndMensurandosIdAndMetodoIdAndIdResultados_ResultadoFecha(List<Resultados> resultados, Mensurandos mensurandos, Metodos metodos,Boolean isInfecha);

    List<Aberrantes> findByIdResultadosDetallados(Long id);

    List<ResultadosDetallados> findTop12ByIdResultados_IdInscripcionMuestras_IdUsuariosAndMensurandosIdAndIdResultados_ResultadoFechaAndIdResultados_FechaCreacionBefore(UsuariosLabSedes usuariosLabSedes, Mensurandos mensurandos,Boolean isFueraFecha, Date fechaCreacion);

    List<ResultadosDetallados> findTop12ByIdResultados_IdInscripcionMuestras_IdUsuariosAndMensurandosIdAndIdResultados_ResultadoFecha(UsuariosLabSedes usuariosLabSedes, Mensurandos mensurandos,Boolean isFueraFecha);

    List<ResultadosDetallados> findTop12ByIdResultados_IdInscripcionMuestras_IdUsuariosAndMensurandosIdAndIdResultados_ResultadoFechaAndIdResultadosIsLessThanEqualOrderByIdResultados_IdInscripcionMuestras_IdMuestras_FechaInicialAsc(UsuariosLabSedes usuariosLabSedes,
    Mensurandos mensurando,Boolean isFueraFecha,Resultados resultados);

    Integer findAllByIdResultados_IdInscripcionMuestras_IdMuestrasAndMensurandosIdAndIdResultados_ResultadoFecha(Muestras muestras, Mensurandos mensurandos,Boolean isInfecha);

    Long countAllByIdResultados_ResultadoFechaAndIdResultados_IdInscripcionMuestras_IdMuestrasAndMensurandosId(Boolean estado, Muestras muestras, Mensurandos mensurandos);

}
