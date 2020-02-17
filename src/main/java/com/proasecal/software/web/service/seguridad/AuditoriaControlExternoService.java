package com.proasecal.software.web.service.seguridad;

import com.proasecal.software.web.entity.seguridad.AuditoriaControlExterno;
import com.proasecal.software.web.repository.seguridad.AuditoriaControlExternoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuditoriaControlExternoService {
    @Autowired
    AuditoriaControlExternoRepository auditoriaControlExternoRepository;

    public void save(AuditoriaControlExterno auditoriaControlExterno){
        auditoriaControlExternoRepository.save(auditoriaControlExterno);
    }

    public List<AuditoriaControlExterno> informeAuditoria(Date desde, Date hasta){
        return auditoriaControlExternoRepository.informeAuditoria(desde,hasta);
    }

}
