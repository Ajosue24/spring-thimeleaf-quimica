package com.proasecal.software.web.cache;


public class CacheAtrib {
    private static CacheAtrib ourInstance = new CacheAtrib();

    String nombreSede;


    public static CacheAtrib getInstance() {
        return ourInstance;
    }

    private CacheAtrib() {
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }
}
