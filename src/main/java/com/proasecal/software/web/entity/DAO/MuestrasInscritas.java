package com.proasecal.software.web.entity.DAO;

public class MuestrasInscritas {

    private long idMuestra;

    private Integer nombreMuestra;

    private boolean estado;

    public MuestrasInscritas(long idMuestra, Integer nombreMuestra, boolean estado) {
        this.idMuestra = idMuestra;
        this.nombreMuestra = nombreMuestra;
        this.estado = estado;
    }

    public MuestrasInscritas() {
    }

    public long getIdMuestra() {
        return idMuestra;
    }

    public void setIdMuestra(long idMuestra) {
        this.idMuestra = idMuestra;
    }

    public Integer getNombreMuestra() {
        return nombreMuestra;
    }

    public void setNombreMuestra(Integer nombreMuestra) {
        this.nombreMuestra = nombreMuestra;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
