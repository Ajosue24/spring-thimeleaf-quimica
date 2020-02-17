package com.proasecal.software.controlexterno.service;

import com.proasecal.software.controlexterno.entity.DocRelacionados;
import com.proasecal.software.controlexterno.repository.DocRelacionadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DocRelacionadosService {

    @Autowired
    private DocRelacionadosRepository docRelacionadosRepository;

    public Page<DocRelacionados> ListPaginated(String nombre, long id, Pageable pageable) {
        return docRelacionadosRepository.filtro(nombre,String.valueOf( id), pageable);
    }




}
