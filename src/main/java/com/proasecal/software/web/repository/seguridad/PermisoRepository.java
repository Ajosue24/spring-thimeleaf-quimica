package com.proasecal.software.web.repository.seguridad;

import com.proasecal.software.web.entity.seguridad.Modulos;
import com.proasecal.software.web.entity.seguridad.Permisos;
import com.proasecal.software.web.entity.seguridad.Roles;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PermisoRepository extends CrudRepository<Permisos, Long> {

    List<Permisos> findAllByIdModulos(Modulos modulos);
    List<Permisos> findAllByListRolesAndIdModulos(Roles roles,Modulos modulos);
    List<Permisos> findAll();
}
