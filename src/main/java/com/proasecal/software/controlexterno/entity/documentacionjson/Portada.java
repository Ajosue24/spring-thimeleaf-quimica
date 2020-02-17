package com.proasecal.software.controlexterno.entity.documentacionjson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@RequiredArgsConstructor
@AllArgsConstructor
public class Portada {

    @Getter @Setter
    private String programa;
    @Getter @Setter
    private String codigo_proasecal;
    @Getter @Setter
    private String numero_muestra;
    @Getter @Setter
    private String tipo_muestra;
    @Getter @Setter
    private String numero_informe;
    @Getter @Setter
    private String fecha_emision;
    @Getter @Setter
    private String fecha_corte;
    @Getter @Setter
    private String fecha_ingreso;
    @Getter @Setter
    private String version;
    @Getter @Setter
    private String observaciones_generales;
    @Getter @Setter
    private String comentarios_generales;
    @Getter @Setter
    private String observaciones_participante;
    @Getter @Setter
    private String observaciones_para_participante;

    public Portada(String programa, String codigo_proasecal, String numero_muestra, String tipo_muestra, String numero_informe, String fecha_emision, String fecha_corte, String fecha_ingreso, String version) {
        this.programa = programa;
        this.codigo_proasecal = codigo_proasecal;
        this.numero_muestra = numero_muestra;
        this.tipo_muestra = tipo_muestra;
        this.numero_informe = numero_informe;
        this.fecha_emision = fecha_emision;
        this.fecha_corte = fecha_corte;
        this.fecha_ingreso = fecha_ingreso;
        this.version = version;
    }
}
