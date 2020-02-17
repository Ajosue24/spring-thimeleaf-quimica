package com.proasecal.software.web.entity.parametricas;

public class RetornoSalida {
    private String salidaRespuesta;
    private String valError;

    public RetornoSalida() {
    }

    public String getSalidaRespuesta() {
        return salidaRespuesta;
    }

    public void setSalidaRespuesta(String salidaRespuesta) {
        this.salidaRespuesta = salidaRespuesta;
    }

    public String getValError() {
        return valError;
    }

    public void setValError(String valError) {
        this.valError = valError;
    }
}
