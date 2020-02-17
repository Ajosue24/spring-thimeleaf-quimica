package com.proasecal.software.web.repository.inscripcion;

import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.entity.administrar.Sedes;
import com.proasecal.software.web.entity.inscripcion.InscripcionProgramas;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface InscripcionProgramasRepository extends CrudRepository<InscripcionProgramas,Long> {
    List<InscripcionProgramas> findByIdSedes(Sedes idLaboratorio);
    List<InscripcionProgramas> findByProgramaId(Programas programaId);
    List<InscripcionProgramas> findByIdUsuarioLabSedes(UsuariosLabSedes idUsuario);
    Optional<InscripcionProgramas> findByIdSedesAndIdUsuarioLabSedesAndProgramaId(Sedes idLaboratorio, UsuariosLabSedes idUsuario, Programas programaId);
    InscripcionProgramas findInscripcionProgramasByInscripProgramaIdEquals(Long inscripProgramId);

    @Query("SELECT u FROM InscripcionProgramas u join InscripcionMuestras a on u.inscripProgramaId = a.inscripProgramaId "
            +"where CAST(a.idMuestras.numeroMuestra  as text) LIKE CONCAT('%',lower(:muestra),'%')"
            +"AND CONCAT (lower(u.idSedes.idLaboratoriosSedes.razonSocial),'') LIKE CONCAT('%',lower(:laboratorio),'%')"
            +"AND CONCAT(lower(u.idSedes.nombreSede),'') LIKE CONCAT('%',lower(:sede),'%') "
            +"AND CONCAT(lower(u.idUsuarioLabSedes.usuarioId.nombreUsuario),'') LIKE CONCAT('%',lower(:usuario),'%') "
            +"AND CONCAT(lower(u.programaId.nombrePrograma),'') LIKE CONCAT ('%',lower(:programaFront),'%')")
    Page<InscripcionProgramas> filtro(@Param("muestra") String muestra, @Param("laboratorio") String laboratorio,
                                      @Param("sede") String sede, @Param("usuario") String usuario,
                                      @Param("programaFront") String programaFront, Pageable pageable);
    @Query("SELECT u FROM InscripcionProgramas u "
            +"where CONCAT (lower(u.idSedes.idLaboratoriosSedes.razonSocial),'') LIKE CONCAT('%',lower(:laboratorio),'%')"
            +"AND CONCAT(lower(u.idSedes.nombreSede),'') LIKE CONCAT('%',lower(:sede),'%') "
            +"AND CONCAT(lower(u.idUsuarioLabSedes.usuarioId.nombreUsuario),'') LIKE CONCAT('%',lower(:usuario),'%') "
            +"AND CONCAT(lower(u.programaId.nombrePrograma),'') LIKE CONCAT ('%',lower(:programaFront),'%')")
    Page<InscripcionProgramas> filtro1( @Param("laboratorio") String laboratorio,
                                       @Param("sede") String sede, @Param("usuario") String usuario,
                                       @Param("programaFront") String programaFront, Pageable pageable);

    InscripcionProgramas findByIdUsuarioLabSedesAndProgramaId(UsuariosLabSedes idUsuarioLabSedes,Programas programaId);

    Boolean existsByIdUsuarioLabSedes_IdUsuarioLabSedes(Long idUsuariosLab);

    List<InscripcionProgramas> findByProgramaIdAndIdUsuarioLabSedes(Programas programa,UsuariosLabSedes usuariosLabSedes);

    @EntityGraph(value = "nombre", type = EntityGraph.EntityGraphType.LOAD)
    Optional<InscripcionProgramas> findById(Long id);

}
