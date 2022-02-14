
package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

public class Parametros {
    @SerializedName("cursoActual")    private Integer cursoActual;

    public Parametros(Integer cursoActual) {
        super();            this.cursoActual = cursoActual;
    }
    public Integer getCursoActual() {        return cursoActual;    }
    public void setCursoActual(Integer cursoActual) {        this.cursoActual = cursoActual;    }
}
