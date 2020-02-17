package com.proasecal.software.controlexterno.service;

import com.proasecal.software.controlexterno.entity.Videos;
import com.proasecal.software.controlexterno.repository.VideosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class VideosService {

    @Autowired
    private VideosRepository videosRepository;

    public Page<Videos> ListPaginated(String nombre, Pageable pageable) {
        return videosRepository.filtro(nombre, pageable);
    }

    public Optional<Videos> findByid(Long id ){
        return videosRepository.findById(id);
    }
}