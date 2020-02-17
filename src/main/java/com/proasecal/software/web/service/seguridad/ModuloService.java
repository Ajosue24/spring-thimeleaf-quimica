package com.proasecal.software.web.service.seguridad;

import com.proasecal.software.web.entity.seguridad.Modulos;
import com.proasecal.software.web.repository.seguridad.ModuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ModuloService {

    @Autowired
    ModuloRepository moduloRepository;

    public List<Modulos> obtenerModulos() {
        return (List<Modulos>) moduloRepository.findAll();
    }

    public Modulos buscarModuloPorId(Long id) {
        return moduloRepository.findById(id).get();
    }

    public void guardarModulo(Modulos modulos) {
        moduloRepository.save(modulos);
    }

    public Modulos obtenerModuloPorNombre(String nombreModulo) {
        return moduloRepository.findByNombreModulo(nombreModulo);
    }

    public List<Modulos> obtenerModulosXSeccion(Long idSeccion) {
        return (List<Modulos>) moduloRepository.findByIdSecciones_IdSecciones(idSeccion);
    }
}
