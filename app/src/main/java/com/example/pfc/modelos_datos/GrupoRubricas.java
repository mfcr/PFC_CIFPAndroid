package com.example.pfc.modelos_datos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GrupoRubricas {

    @SerializedName("id")       private Integer id;
    @SerializedName("grupo")    private String grupo;

    public GrupoRubricas() {    }

    /**
     * @param grupo
     * @param id
     */
    public GrupoRubricas(Integer id, String grupo) {
        super();        this.id = id;        this.grupo = grupo;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getGrupo() {
        return grupo;
    }
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
}
