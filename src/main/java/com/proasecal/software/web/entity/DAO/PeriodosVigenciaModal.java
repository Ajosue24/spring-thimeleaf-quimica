package com.proasecal.software.web.entity.DAO;

import com.proasecal.software.web.entity.inscripcion.InscripcionProgramas;

public class PeriodosVigenciaModal {

    private Long periodosvigenciaId;

    private InscripcionProgramas inscripProgramaId;

    private String perVigenciaRegistrados;

    private String modalidad;

    private String fechaInicio;

    private String fechaFin;

    public PeriodosVigenciaModal(Long periodosvigenciaId, InscripcionProgramas inscripProgramaId, String perVigenciaRegistrados, String modalidad, String fechaInicio, String fechaFin) {
        this.periodosvigenciaId = periodosvigenciaId;
        this.inscripProgramaId = inscripProgramaId;
        this.perVigenciaRegistrados = perVigenciaRegistrados;
        this.modalidad = modalidad;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public PeriodosVigenciaModal() {
    }

    public Long getPeriodosvigenciaId() {
        return periodosvigenciaId;
    }

    public void setPeriodosvigenciaId(Long periodosvigenciaId) {
        this.periodosvigenciaId = periodosvigenciaId;
    }

    public InscripcionProgramas getInscripProgramaId() {
        return inscripProgramaId;
    }

    public void setInscripProgramaId(InscripcionProgramas inscripProgramaId) {
        this.inscripProgramaId = inscripProgramaId;
    }

    public String getPerVigenciaRegistrados() {
        return perVigenciaRegistrados;
    }

    public void setPerVigenciaRegistrados(String perVigenciaRegistrados) {
        this.perVigenciaRegistrados = perVigenciaRegistrados;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
}
