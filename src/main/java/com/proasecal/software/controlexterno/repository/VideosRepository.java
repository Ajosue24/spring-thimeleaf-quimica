package com.proasecal.software.controlexterno.repository;

import com.proasecal.software.controlexterno.entity.Videos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface VideosRepository extends CrudRepository<Videos, Long> {

    @Query("select d from Videos  d  where CONCAT(lower(unaccent(d.nombre)), '') LIKE CONCAT('%',lower(unaccent(:nombre)),'%') ")
    Page<Videos> filtro(@Param("nombre") String nombre,
                                 Pageable pageable);
}