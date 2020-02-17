package com.proasecal.software.controlexterno.entity.DAO;

import com.proasecal.software.web.entity.administrar.Mensurandos;

public class MensurandosXSimular {

    private boolean simulado;
    private Mensurandos mensurando;
    private int total, grupoBase, fueraFecha;

    public MensurandosXSimular(boolean simulado, Mensurandos mensurando, int total, int grupoBase, int fueraFecha) {
        this.simulado = simulado;
        this.mensurando = mensurando;
        this.total = total;
        this.grupoBase = grupoBase;
        this.fueraFecha = fueraFecha;
    }

    public boolean isSimulado() {
        return simulado;
    }

    public void setSimulado(boolean simulado) {
        this.simulado = simulado;
    }

    public Mensurandos getMensurando() {
        return mensurando;
    }

    public void setMensurando(Mensurandos mensurando) {
        this.mensurando = mensurando;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getFueraFecha() {
        return fueraFecha;
    }

    public void setFueraFecha(int fueraFecha) {
        this.fueraFecha = fueraFecha;
    }

    public int getGrupoBase() {
        return grupoBase;
    }

    public void setGrupoBase(int grupoBase) {
        this.grupoBase = grupoBase;
    }
}
