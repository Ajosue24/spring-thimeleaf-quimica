package com.proasecal.software.controlexterno.entity.documentacionjson;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ObjetoJson {

    @Getter
    @Setter
    Portada Portada;
    @Getter
    @Setter
    List<CuadroDesempeno> cuadro_desempeño;
    @Getter
    @Setter
    List<CuadroIncertidumbre> cuadro_incertidumbre;
    @Getter
    @Setter
    List<HistoricoIV> historico_iv;
    @Getter
    @Setter
    List<ConsolidadoIV> consolidado_iv;
    @Getter
    @Setter
    List<SdiMetodos> sdi_metodo;
    @Getter
    @Setter
    List<SdiMetodos> sdi_metodo_equipo;
    @Setter
    @Getter
    List<Resumen> resumen;
}
