package com.proasecal.software.web.service.parametricas;

import com.proasecal.software.web.entity.parametricas.Pais;
import com.proasecal.software.web.entity.parametricas.TipoDocumentoPais;
import com.proasecal.software.web.repository.parametricas.IdTipoPaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class IdTipoPaisService {
    @Autowired
    IdTipoPaisRepository idTipoPaisRepository;

    public List<TipoDocumentoPais> obtenerIdxPais(){
        return (List<TipoDocumentoPais>) idTipoPaisRepository.findAll();
    }

    public List<TipoDocumentoPais> obtenerIdSegunPais(Pais idPais){
        return  idTipoPaisRepository.findByIdPais(idPais);
    }

    public List<TipoDocumentoPais> filtrarIdLike(String tipoId) {
        return  idTipoPaisRepository.findByNombreIdContainingIgnoreCase(tipoId);
    }
}

