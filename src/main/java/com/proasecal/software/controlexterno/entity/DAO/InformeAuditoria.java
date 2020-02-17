package com.proasecal.software.controlexterno.entity.DAO;

import java.util.Date;

public class InformeAuditoria {
	
    private String programa,accion, justificacion, responsable, idUsuario, nombreUsuario, usuarioResultado, mensurando, filtro, version; 
    private Double valorAsignado;
    private Date fechaHora;
    private Integer muestra;
    
    public InformeAuditoria(Date fechaHora, String programa, Integer muestra, String accion, String justificacion, String responsable, String idUsuario, String nombreUsuario, String usuarioResultado, Double valorAsignado,String mensurando, String filtro, String version) {
        this.fechaHora = fechaHora;
        this.programa = programa;
        this.muestra = muestra;
        this.accion = accion;
        this.justificacion = justificacion;
        this.responsable = responsable;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.usuarioResultado = usuarioResultado;
        this.valorAsignado = valorAsignado;
        this.mensurando = mensurando;
        this.filtro = filtro;
        this.version = version;      
    }

	public Date getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}
	public String getPrograma() {
		return programa;
	}
	public void setPrograma(String programa) {
		this.programa = programa;
	}
	public Integer getMuestra() {
		return muestra;
	}
	public void setMuestra(Integer muestra) {
		this.muestra = muestra;
	}
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public String getJustificacion() {
		return justificacion;
	}
	public void setJustificacion(String justificacion) {
		this.justificacion = justificacion;
	}
	public String getResponsable() {
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	public String getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public String getUsuarioResultado() {
		return usuarioResultado;
	}
	public void setUsuarioResultado(String usuarioResultado) {
		this.usuarioResultado = usuarioResultado;
	}
	public String getMensurando() {
		return mensurando;
	}
	public void setMensurando(String mensurando) {
		this.mensurando = mensurando;
	}
	public String getFiltro() {
		return filtro;
	}
	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Double getValorAsignado() {
		return valorAsignado;
	}
	public void setValorAsignado(Double valorAsignado) {
		this.valorAsignado = valorAsignado;
	}

}
