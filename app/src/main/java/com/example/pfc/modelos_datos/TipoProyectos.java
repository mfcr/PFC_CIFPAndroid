package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

public class TipoProyectos {

    @SerializedName("id")       private Integer id;
    @SerializedName("tipo")     private String tipo;

    public TipoProyectos() {    }

    /**
    * @param tipo
     * @param id
     */
    public TipoProyectos(Integer id, String tipo) {
        super();        this.id = id;        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
