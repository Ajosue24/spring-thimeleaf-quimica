package com.proasecal.software.controlexterno.entity.DAO;

public class ValoresEscenarioSimulacion {
    private String laboratorio, sede, usuario, aceptabilidad;
    private double valorReportado, limiteInferior, limiteSuperior,
            desviacionValorAsignado, varianza, desviacionEstandar;

    public ValoresEscenarioSimulacion() {
    }

    public ValoresEscenarioSimulacion(String laboratorio, String sede, String usuario, double valorReportado,
                                      String aceptabilidad, double limiteInferior, double limiteSuperior,
                                      double desviacionValorAsignado, double varianza, double desviacionEstandar) {
        this.laboratorio = laboratorio;
        this.sede = sede;
        this.usuario = usuario;
        this.valorReportado = valorReportado;
        this.aceptabilidad = aceptabilidad;
        this.limiteInferior = limiteInferior;
        this.limiteSuperior = limiteSuperior;
        this.desviacionValorAsignado = desviacionValorAsignado;
        this.varianza = varianza;
        this.desviacionEstandar = desviacionEstandar;
    }


    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getAceptabilidad() {
        return aceptabilidad;
    }

    public void setAceptabilidad(String aceptabilidad) {
        this.aceptabilidad = aceptabilidad;
    }

    public double getValorReportado() {
        return valorReportado;
    }

    public void setValorReportado(double valorReportado) {
        this.valorReportado = valorReportado;
    }

    public double getLimiteInferior() {
        return limiteInferior;
    }

    public void setLimiteInferior(double limiteInferior) {
        this.limiteInferior = limiteInferior;
    }

    public double getLimiteSuperior() {
        return limiteSuperior;
    }

    public void setLimiteSuperior(double limiteSuperior) {
        this.limiteSuperior = limiteSuperior;
    }

    public double getDesviacionValorAsignado() {
        return desviacionValorAsignado;
    }

    public void setDesviacionValorAsignado(double desviacionValorAsignado) {
        this.desviacionValorAsignado = desviacionValorAsignado;
    }

    public double getVarianza() {
        return varianza;
    }

    public void setVarianza(double varianza) {
        this.varianza = varianza;
    }

    public double getDesviacionEstandar() {
        return desviacionEstandar;
    }

    public void setDesviacionEstandar(double desviacionEstandar) {
        this.desviacionEstandar = desviacionEstandar;
    }
}