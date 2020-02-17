package com.proasecal.software.web.entity.DAO;

import com.proasecal.software.web.entity.administrar.Clientes;
import com.proasecal.software.web.entity.parametricas.Ciudad;
import com.proasecal.software.web.entity.parametricas.Departamentos;
import com.proasecal.software.web.entity.parametricas.Pais;
import com.proasecal.software.web.entity.parametricas.TipoDocumentoPais;

public class LaboratoriosCliente {
    private long idLaboratoriosSedes;
    private Pais idPais;
    private Departamentos idDepartamentos;
    private Ciudad idCiudad;
    private Clientes clienteId;
    private TipoDocumentoPais idTipoPais;
    private String direccion;
    private String correo;
    private String correoAlternativo;
    private String telefono;
    private String telefonoAlternativo;
    private Boolean imprimirResultados;
    private String usuarioDirector;
    private String usuarioCalidad;
    private String numeroIdentificacion;
    private String razonSocial;
    private String nombreComercial;

    private long idUsuario;
    private String usuario;
    private String password;


    public LaboratoriosCliente() {
    }

    public LaboratoriosCliente(long idLaboratoriosSedes, Pais idPais, Departamentos idDepartamentos, Ciudad idCiudad,
                               Clientes clienteId, TipoDocumentoPais idTipoPais, String direccion, String correo,
                               String correoAlternativo, String telefono, String telefonoAlternativo,
                               Boolean imprimirResultados, String usuarioDirector, String usuarioCalidad,
                               String numeroIdentificacion, String razonSocial, String nombreComercial,
                               Long idUsuario, String usuario, String password) {
        this.idLaboratoriosSedes = idLaboratoriosSedes;
        this.idPais = idPais;
        this.idDepartamentos = idDepartamentos;
        this.idCiudad = idCiudad;
        this.clienteId = clienteId;
        this.idTipoPais = idTipoPais;
        this.direccion = direccion;
        this.correo = correo;
        this.correoAlternativo = correoAlternativo;
        this.telefono = telefono;
        this.telefonoAlternativo = telefonoAlternativo;
        this.imprimirResultados = imprimirResultados;
        this.usuarioDirector = usuarioDirector;
        this.usuarioCalidad = usuarioCalidad;
        this.numeroIdentificacion = numeroIdentificacion;
        this.razonSocial = razonSocial;
        this.nombreComercial = nombreComercial;
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.password = password;
    }


    public long getIdLaboratoriosSedes() {
        return idLaboratoriosSedes;
    }

    public void setIdLaboratoriosSedes(long idLaboratoriosSedes) {
        this.idLaboratoriosSedes = idLaboratoriosSedes;
    }

    public Pais getIdPais() {
        return idPais;
    }

    public void setIdPais(Pais idPais) {
        this.idPais = idPais;
    }

    public Departamentos getIdDepartamentos() {
        return idDepartamentos;
    }

    public void setIdDepartamentos(Departamentos idDepartamentos) {
        this.idDepartamentos = idDepartamentos;
    }

    public Ciudad getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Ciudad idCiudad) {
        this.idCiudad = idCiudad;
    }

    public Clientes getClienteId() {
        return clienteId;
    }

    public void setClienteId(Clientes clienteId) {
        this.clienteId = clienteId;
    }

    public TipoDocumentoPais getIdTipoPais() {
        return idTipoPais;
    }

    public void setIdTipoPais(TipoDocumentoPais idTipoPais) {
        this.idTipoPais = idTipoPais;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCorreoAlternativo() {
        return correoAlternativo;
    }

    public void setCorreoAlternativo(String correoAlternativo) {
        this.correoAlternativo = correoAlternativo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefonoAlternativo() {
        return telefonoAlternativo;
    }

    public void setTelefonoAlternativo(String telefonoAlternativo) {
        this.telefonoAlternativo = telefonoAlternativo;
    }

    public Boolean getImprimirResultados() {
        return imprimirResultados;
    }

    public void setImprimirResultados(Boolean imprimirResultados) {
        this.imprimirResultados = imprimirResultados;
    }

    public String getUsuarioDirector() {
        return usuarioDirector;
    }

    public void setUsuarioDirector(String usuarioDirector) {
        this.usuarioDirector = usuarioDirector;
    }

    public String getUsuarioCalidad() {
        return usuarioCalidad;
    }

    public void setUsuarioCalidad(String usuarioCalidad) {
        this.usuarioCalidad = usuarioCalidad;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public long getIdUsuario() {return idUsuario;}

    public void setIdUsuario(long idUsuario) {this.idUsuario = idUsuario;}

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}