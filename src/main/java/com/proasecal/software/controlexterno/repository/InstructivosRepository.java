package com.proasecal.software.controlexterno.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.proasecal.software.controlexterno.entity.DocRelacionados;

public interface InstructivosRepository extends CrudRepository<DocRelacionados,Long> {

	Page<DocRelacionados> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

}