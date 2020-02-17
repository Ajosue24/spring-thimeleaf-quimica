package com.proasecal.software.web.repository.parametricas;

import com.proasecal.software.web.entity.parametricas.Variables;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VariablesRepository extends CrudRepository<Variables,Long> {

    Variables findByNombreVariableEqualsIgnoreCase(String string);

    List<Variables> findAllByIdProceso_IdProceso(Long procesos);

}
