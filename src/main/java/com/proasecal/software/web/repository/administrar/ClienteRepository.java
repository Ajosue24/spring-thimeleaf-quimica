package com.proasecal.software.web.repository.administrar;


import com.proasecal.software.web.entity.administrar.Clientes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends CrudRepository<Clientes,Long> {

    Clientes findClienteByNumeroIdentificacionClienteEquals(String idCliente);

    List<Clientes> findByNumeroIdentificacionClienteContainingOrRazonSocialContainingIgnoreCase(String nombre, String id);

    @Query("select u from Clientes u where lower(u.idPais.nombrePais) like lower(concat('%', :paisFront,'%'))"
            +"AND CONCAT(lower(u.idTipoPais.nombreId),'') LIKE CONCAT('%',lower(:idTipoPais),'%') "
            +"AND CONCAT(lower(u.numeroIdentificacionCliente),'') LIKE CONCAT('%',lower(:numeroid),'%')"
            +"AND CONCAT(lower(unaccent(u.razonSocial)),'') LIKE CONCAT('%',lower(unaccent(:razonSocial)),'%')"
            +"AND CONCAT(lower(unaccent(u.nombreComercial)),'') LIKE CONCAT('%',lower(unaccent(:nombreComercial)),'%')"
            +"AND CONCAT(u.estado,'') LIKE CONCAT('%',:estado,'%')")
    Page<Clientes> filtro(@Param("paisFront") String paisFront, @Param("idTipoPais") String idTipoPais, @Param("numeroid") String numeroid, @Param("razonSocial") String razonSocial, @Param("nombreComercial") String nombreComercial, @Param("estado") String estado, Pageable pageable);

}

