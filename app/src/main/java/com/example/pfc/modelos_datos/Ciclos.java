
package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;


public class Ciclos {

    @SerializedName("id")               private Integer id;
    @SerializedName("codigoCiclo")      private String codigoCiclo;
    @SerializedName("nombreCiclo")      private String nombreCiclo;

    public Ciclos() {    }

    public Ciclos(Integer id,String codigoCiclo,  String nombreCiclo) {
        super();
        this.codigoCiclo = codigoCiclo;           this.id = id;                       this.nombreCiclo = nombreCiclo;
    }
    public Integer getId() {        return id;    }
    public void setId(Integer id) {        this.id = id;    }
    public String getCodigoCiclo() {        return codigoCiclo;    }
    public void setCodigoCiclo(String codigoCiclo) {        this.codigoCiclo = codigoCiclo;    }
    public String getNombreCiclo() {        return nombreCiclo;    }
    public void setNombreCiclo(String nombreCiclo) {        this.nombreCiclo = nombreCiclo;    }
}
