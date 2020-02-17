package com.proasecal.software.controlexterno.repository;

import com.proasecal.software.controlexterno.entity.EscenariosFijos;
import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.administrar.Metodos;
import com.proasecal.software.web.entity.administrar.Muestras;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EscenariosFijosRepository extends CrudRepository<EscenariosFijos, Long> {
//Buscalpadre
    List<EscenariosFijos> findByIdMuestrasAndIdMensurandosAndEscenariosFijosIdIsNull(Muestras muestra, Mensurandos mensurando);

    List<EscenariosFijos> findByIdMuestrasAndIdMensurandosAndMetodoIdAndEscenariosFijosIdIsNull(Muestras muestra, Mensurandos mensurando, Metodos metodos);

    List<EscenariosFijos> findByIdMuestrasAndIdMensurandosAndMetodoId(Muestras muestras, Mensurandos mensurandos, Metodos metodos);

    void deleteAllByIdMuestrasAndIdMensurandosAndMetodoId(Muestras muestras, Mensurandos mensurandos, Metodos metodos);
    
    EscenariosFijos findTop1ByIdMuestrasAndIdMensurandosAndMetodoIdAndEscenariosFijosIdIsNullOrderByFechaCreacionDesc(Muestras muestra, Mensurandos mensurando, Metodos metodo);

    @Procedure(name = "calculoSimulacionesHijas")
    String calculoSimulacionesHijas(@Param("idescenariofijo") Integer idEscenarioFijo, @Param("muestra") Integer idMuestra,
                           @Param("mensurando") Integer idMensurando, @Param("metodo") Integer idMetodo);
    @Procedure(name = "calculoSimulacionesHijasAberrantes")
    String calculoSimulacionesHijasAberrantes(@Param("idescenariofijo") Integer idEscenarioFijo, @Param("muestra") Integer idMuestra,
                                    @Param("mensurando") Integer idMensurando, @Param("metodo") Integer idMetodo);

    @Procedure(name = "actualizarVersionProcedure")
    String actualizarVersionProcedure(@Param("numeroVersion") Integer numeroVersion, @Param("id_resultado") Integer id_resultado);

    Long countAllByIdMuestrasAndIdMensurandosAndEscenariosFijosIdIsNull(Muestras muestra, Mensurandos mensurando);
}
