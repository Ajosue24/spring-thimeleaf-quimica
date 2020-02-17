package com.proasecal.software.web.entity.seguridad;

public enum EnumAccionAuditoria {CREAR("CREAR"),ELIMINAR("ELIMINAR"),ACTUALIZAR("ACTUALIZAR"),CONSULTA("CONSULTA");

    private String nombreAccion;

    EnumAccionAuditoria(String nombreAccion){
        this.nombreAccion = nombreAccion;
    }

public String getNombreAccion(){
    return  nombreAccion;
}

}
