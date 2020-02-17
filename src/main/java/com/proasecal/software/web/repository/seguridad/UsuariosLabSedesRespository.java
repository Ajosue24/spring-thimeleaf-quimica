package com.proasecal.software.web.repository.seguridad;

import com.proasecal.software.web.entity.administrar.Laboratorios;
import com.proasecal.software.web.entity.inscripcion.InscripcionProgramas;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuariosLabSedesRespository extends CrudRepository<UsuariosLabSedes, Long> {

    List<UsuariosLabSedes> findByIdSedes_IdSedes(Long idSedes);

    Optional<UsuariosLabSedes> findByIdLaboratoriosSedes(Laboratorios laboratorio);

    Optional<UsuariosLabSedes> findById(Long id);

    Optional<UsuariosLabSedes> findByUsuarioId(Usuarios usuarios);

    @Query("SELECT u FROM UsuariosLabSedes u WHERE CONCAT(u.usuarioId.nombreUsuario,'') LIKE CONCAT('%',:usuario,'%')")
    List<UsuariosLabSedes> filtrarxUsuario(@Param("usuario") String usuario);

    UsuariosLabSedes findByUsuarioId_IdUsuario(Long idUsuario);

    UsuariosLabSedes findByUsuarioId_NombreUsuario(String nombreUsuario);

    UsuariosLabSedes findByUsuarioId_CodProasecal(Integer codProasecal);

    @Query("SELECT p FROM UsuariosLabSedes p JOIN FETCH p.usuarioId WHERE p.idUsuarioLabSedes = (:id)")
    UsuariosLabSedes findTest(@Param("id") Long id);

}
