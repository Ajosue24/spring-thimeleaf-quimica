package com.proasecal.software.web.repository.administrar;

import com.proasecal.software.web.entity.administrar.Laboratorios;
import com.proasecal.software.web.entity.parametricas.TipoDocumentoPais;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LaboratorioRepository extends CrudRepository<Laboratorios, Long> {

    List<Laboratorios> findByNumeroIdentificacionAndIdTipoPais(String numero, TipoDocumentoPais tipoDocumentoPais);

    List<Laboratorios> findByNumeroIdentificacionContainingIgnoreCaseOrRazonSocialContainingIgnoreCase(String idLaboratorio, String razonSocial);

    List<Laboratorios> findByRazonSocialContainingIgnoreCase(String razonSocial);


    @Query("select l from Laboratorios  l "
            + "inner join Clientes c on l.clienteId= c.clienteId "
            + "inner join Pais p on l.idPais=p.idPais "
            + "inner join TipoDocumentoPais t on l.idTipoPais=t.idTipoPais "

            + "where CONCAT(c.numeroIdentificacionCliente,'') LIKE CONCAT('%',:cliente,'%') "
            + "and CONCAT(lower(unaccent(l.nombreComercial)), '') LIKE CONCAT('%',lower(unaccent(:nombreComercial)),'%') "
            + "and CONCAT(lower(unaccent(l.razonSocial)),'') LIKE CONCAT('%',lower(unaccent(:razonSocial)),'%') "
            + "and CONCAT(p.nombrePais, '') LIKE CONCAT('%',:pais,'%') "
            + "and CONCAT(t.nombreId,'') LIKE CONCAT('%',:tipoId,'%') "
            + "and CONCAT(l.numeroIdentificacion,'') LIKE CONCAT('%',:id,'%') "
            + "and l.estadoLabSedes = true ")
    Page<Laboratorios> filtro(@Param("cliente") String cliente,
                              @Param("nombreComercial") String nombreComercial,
                              @Param("razonSocial") String razonSocial,
                              @Param("pais") String pais,
                              @Param("tipoId") String tipoId,
                              @Param("id") String id,
                              Pageable pageable);


    @Query("select distinct l from Laboratorios l "
            + "inner join Sedes s on l.idLaboratoriosSedes=s.idLaboratoriosSedes "
            + "inner join UsuariosLabSedes uls on s.idSedes=uls.idSedes "
            + "inner join InscripcionMuestras im on uls.idUsuarioLabSedes=im.idUsuarios "
            + "inner join Muestras m on im.idMuestras=m.muestraId "
            + " where CONCAT(m.muestraId,'')  LIKE CONCAT('%',:muestra,'%') ")
    List<Laboratorios> obtMuestrasxAno(@Param("muestra") String muestra);

}
