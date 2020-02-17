package com.proasecal.software.controlexterno.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proasecal.software.controlexterno.repository.ResultadosParticipanteRepository;


@Service
@Transactional(rollbackFor = Exception.class)
public class ResultadosParticipanteService {
	@Autowired
	private ResultadosParticipanteRepository resultadosParticipanteRepository;

}
