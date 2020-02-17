package com.proasecal.software.web.bootstrap;

import com.proasecal.software.web.cache.CacheAtrib;
import com.proasecal.software.web.service.seguridad.ModuloService;
import com.proasecal.software.web.service.seguridad.PermisoService;
import com.proasecal.software.web.service.seguridad.RolService;
import com.proasecal.software.web.service.seguridad.UsuarioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Bootstrap implements InitializingBean {
    private final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);
    private CacheAtrib cacheAtrib = CacheAtrib.getInstance();
    
    
    @Autowired
    private UsuarioService usuarioService;
    

    @Autowired
    private RolService rolService;
    @Autowired
    PermisoService permisoService;
    @Autowired
    ModuloService moduloService;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOG.info("obtener roles y accesos a url");
        cacheAtrib.setNombreSede("texto");
        
        LOG.info("Creando usuario admin");
    }
}
