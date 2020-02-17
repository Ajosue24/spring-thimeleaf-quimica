package com.proasecal.software.controlexterno.entity.DAO;

public class Observacion {
    private Long idTipo;
    private String nombre, observacion;

    public Observacion(Long idTipo, String nombre, String observacion) {
        this.idTipo = idTipo;
        this.nombre = nombre;
        this.observacion = observacion;
    }

    public Long getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Long idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
