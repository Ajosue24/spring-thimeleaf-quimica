package com.proasecal.software.controlexterno.entity.DAO;

import com.proasecal.software.controlexterno.entity.EscenariosFijos;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class FormSimulacion {

    @Getter @Setter
    EscenariosFijos escenariosFijos;
    @Getter @Setter
    ConsensoInicial consensoInicialFiltro;
    @Getter @Setter
    List<ConsensoInicial> consensoInicialList;
    @Getter @Setter
    EscenariosFijos escenariosFijoAnterior = null;
    @Getter @Setter
    List<ValoresConsenso> valoresConsensosList;
    @Getter @Setter
    List<ValoresConsenso> valoresAtipicosList;

}
