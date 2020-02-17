package com.proasecal.software.web.repository.parametricas;


import com.proasecal.software.web.entity.parametricas.Departamentos;
import com.proasecal.software.web.entity.parametricas.Pais;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DepartamentoRepository extends CrudRepository<Departamentos,Long> {

    List<Departamentos> findDepartamentosByIdPais(Pais pais);
}

