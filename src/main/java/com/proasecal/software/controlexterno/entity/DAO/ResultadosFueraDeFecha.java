package com.proasecal.software.controlexterno.entity.DAO;

import java.util.Date;

public class ResultadosFueraDeFecha {
    private String laboratorio, sede, usuario;
    private Date fecha;

    public ResultadosFueraDeFecha(String laboratorio, String sede, String usuario, Date fecha) {
        this.laboratorio = laboratorio;
        this.sede = sede;
        this.usuario = usuario;
        this.fecha = fecha;
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

    public Date getFecha() {
        return fecha;
    }

    public void Date(Date fecha) {
        this.fecha = fecha;
    }
}
