package com.proasecal.software.web.repository.seguridad;

import com.proasecal.software.web.entity.seguridad.Usuarios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends CrudRepository<Usuarios, Long> {
    Optional<Usuarios> findByNombreUsuario(String nombreUsuario);

    Boolean existsByNombreUsuario(String nombreUsuario);

    Boolean existsByCodProasecal(Integer CodigoProasecal);

    Optional<Usuarios> findByCodProasecal(Integer CodigoProasecal);

    @Query("select u from Usuarios u where lower(u.nombreUsuario) like lower(concat('%', :nombreUsuario,'%'))"
            + "AND (CONCAT(lower(u.nombres),'') LIKE CONCAT('%',lower(:nombres),'%')"
            + "OR CONCAT(lower(u.apellidos),'') LIKE CONCAT('%',lower(:apellidos),'%') ) "
            + "AND CONCAT(u.codProasecal,'') LIKE CONCAT('%',:codProasecal,'%')"
            + "AND CONCAT(u.estado,'') LIKE CONCAT('%',:estado,'%')"
    )
    Page<Usuarios> filtro(@Param("nombreUsuario") String nombreUsuario,
                          @Param("nombres") String nombres,
                          @Param("apellidos") String apellidos,
                          @Param("codProasecal") String codProasecal,
                          @Param("estado") String estado, Pageable pageable);

    @Query("select u from Usuarios u where lower(u.nombreUsuario) like lower(concat('%', :nombreUsuario,'%'))"
            + "AND (CONCAT(lower(u.nombres),'') LIKE CONCAT('%',lower(:nombres),'%')"
            + "OR CONCAT(lower(u.apellidos),'') LIKE CONCAT('%',lower(:apellidos),'%') ) "
            + "AND CONCAT(u.estado,'') LIKE CONCAT('%',:estado,'%')"
    )
    Page<Usuarios> filtroSinCodProasecal(@Param("nombreUsuario") String nombreUsuario,
                                         @Param("nombres") String nombres,
                                         @Param("apellidos") String apellidos,
                                         @Param("estado") String estado, Pageable pageable);


    @Query("select distinct u from Usuarios u "
            + "inner join UsuariosLabSedes uls on u.idUsuario=uls.usuarioId "
            + "inner join InscripcionMuestras im on uls.idUsuarioLabSedes=im.idUsuarios "
            + "inner join Muestras m on im.idMuestras=m.muestraId "
            + " where CONCAT(m.muestraId,'')  LIKE CONCAT('%',:muestra,'%') ")
    List<Usuarios> obtUsuxMuestra(@Param("muestra") String muestra);

}
