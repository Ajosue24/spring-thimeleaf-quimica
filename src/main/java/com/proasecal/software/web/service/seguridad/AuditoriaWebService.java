package com.proasecal.software.web.service.seguridad;

import com.proasecal.software.web.entity.seguridad.AuditoriaWeb;
import com.proasecal.software.web.repository.seguridad.AuditoriaWebRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuditoriaWebService {

    @Autowired
    AuditoriaWebRepository auditoriaWebRepository;
    @Autowired
    UsuarioService usuarioService;

    public void save(AuditoriaWeb auditoriaWeb) {
        auditoriaWeb.setIdUsuario(usuarioService.findByUserName(auditoriaWeb.getUsuarioLogged().getNombreUsuario()));
        auditoriaWebRepository.save(auditoriaWeb);
    }

}
