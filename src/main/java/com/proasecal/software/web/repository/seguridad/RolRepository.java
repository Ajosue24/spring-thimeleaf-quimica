package com.proasecal.software.web.repository.seguridad;

import com.proasecal.software.web.entity.seguridad.Roles;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RolRepository extends CrudRepository<Roles,Long> {

     boolean existsByNombreRol(String nombre);

     Roles findByNombreRolIgnoreCase(String nombre);

     boolean existsByCodigoProasecalIsTrue();


     Page<Roles> findByNombreRolContainingIgnoreCaseAndDescripcionContainingIgnoreCaseAndEstado(String nombreRol, String descripcion, Boolean estado, Pageable pageable);

     @Query("select distinct r from Roles r where lower(nombreRol) LIKE lower('%Asesor de programas%')")
     List<Roles> listUsuariosRolAsesor();
}
