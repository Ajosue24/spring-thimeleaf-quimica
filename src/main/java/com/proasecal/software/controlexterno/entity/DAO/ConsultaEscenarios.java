package com.proasecal.software.controlexterno.entity.DAO;

import com.proasecal.software.controlexterno.entity.EscenariosFijos;
import com.proasecal.software.web.entity.administrar.Mensurandos;

import java.util.List;

public class ConsultaEscenarios {
    private Mensurandos mensurando;
    private List<EscenariosFijos> escenariosMetodo;

    public ConsultaEscenarios(Mensurandos mensurando, List<EscenariosFijos> escenariosMetodo) {
        this.mensurando = mensurando;
        this.escenariosMetodo = escenariosMetodo;
    }

    public Mensurandos getMensurando() {
        return mensurando;
    }

    public void setMensurando(Mensurandos mensurando) {
        this.mensurando = mensurando;
    }

    public List<EscenariosFijos> getEscenariosMetodo() {
        return escenariosMetodo;
    }

    public void setEscenariosMetodo(List<EscenariosFijos> escenariosMetodo) {
        this.escenariosMetodo = escenariosMetodo;
    }
}
