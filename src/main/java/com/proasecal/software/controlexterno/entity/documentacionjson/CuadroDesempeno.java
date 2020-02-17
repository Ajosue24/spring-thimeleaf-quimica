package com.proasecal.software.controlexterno.entity.documentacionjson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@RequiredArgsConstructor
@AllArgsConstructor
public class CuadroDesempeno {

    @Getter
    @Setter
    String mensurando;
    @Getter
    @Setter
    Double iv;
    @Setter
    @Getter
    String desempeño;


}
