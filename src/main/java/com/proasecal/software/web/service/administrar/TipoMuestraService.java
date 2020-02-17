package com.proasecal.software.web.service.administrar;

import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.entity.administrar.TiposMuestras;
import com.proasecal.software.web.repository.administrar.TipoMuestraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class TipoMuestraService {

    @Autowired
    TipoMuestraRepository tipoMuestraRepository;

    public List<TiposMuestras> list(){
        return (List<TiposMuestras>)tipoMuestraRepository.findAll();
    }

    public List<TiposMuestras> obtenerTipoMuestraxPrograma(Programas Programa) {
        return tipoMuestraRepository.findTiposMuestrasByidPrograma(Programa);
    }   

    public List<TiposMuestras> searchName(String nombre) {
    	return this.tipoMuestraRepository.findByNombreContainingIgnoreCase(nombre);
    }

    private List<TiposMuestras> tiposMuestrasList;

    public Page<TiposMuestras> ListPaginated(String name, Pageable pageable){
    	 tiposMuestrasList= list();
        //INICIO
         int pageSize = pageable.getPageSize();
         int currentPage = pageable.getPageNumber();
         int startItem = currentPage * pageSize;
         List<TiposMuestras> list;
         if (tiposMuestrasList.size() < startItem) {
         list = Collections.emptyList();
         } else {
         int toIndex = Math.min(startItem + pageSize, tiposMuestrasList.size());
         list = tiposMuestrasList.subList(startItem, toIndex); }
         //PageRequest.of(currentPage, pageSize)
        Page<TiposMuestras> tiposMuestrasPage = new PageImpl<>(list, pageable, tiposMuestrasList.size());
        //FIN
        return tiposMuestrasPage;
    }
}
