package com.proasecal.software.controlexterno.repository;

import com.proasecal.software.controlexterno.entity.DocRelacionados;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DocRelacionadosRepository extends CrudRepository<DocRelacionados,Long> {

    @Query("select d from DocRelacionados  d  "
            + "inner join TipoDocumentos td on d.idTipoDocumento= td.tipoDocumentoId  "
            + " where CONCAT(lower(unaccent(d.nombre)), '') LIKE CONCAT('%',lower(unaccent(:nombre)),'%') "
            + "and  CONCAT(td.tipoDocumentoId,'') LIKE CONCAT('%',:tipoDocumentos,'%') ")
    Page<DocRelacionados> filtro(@Param("nombre") String nombre,
                              @Param("tipoDocumentos") String tipoDocumentos,
                              Pageable pageable);

}
