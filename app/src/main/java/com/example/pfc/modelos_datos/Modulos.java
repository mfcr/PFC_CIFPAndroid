package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

public class Modulos {

    @SerializedName("id")                   private Integer id;
    @SerializedName("codigoModulo")         private String codigoModulo;
    @SerializedName("nombreModulo")         private String nombreModulo;
    @SerializedName("tiene_UC")             private Boolean tieneUC;
    @SerializedName("curso")                private Integer curso;
    @SerializedName("laravel_through_key")  private Integer laravelThroughKey;

    public Modulos() {    }

    /**
     * @param codigoModulo
     * @param laravelThroughKey
     * @param curso
     * @param tieneUC
     * @param id
     * @param nombreModulo
     */
    public Modulos(Integer id, String codigoModulo, String nombreModulo, Boolean tieneUC, Integer curso, Integer laravelThroughKey) {
        super();        this.id = id;        this.codigoModulo = codigoModulo;        this.nombreModulo = nombreModulo;
        this.tieneUC = tieneUC;        this.curso = curso;        this.laravelThroughKey = laravelThroughKey;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getCodigoModulo() {
        return codigoModulo;
    }
    public void setCodigoModulo(String codigoModulo) {
        this.codigoModulo = codigoModulo;
    }
    public String getNombreModulo() {
        return nombreModulo;
    }
    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }
    public Boolean getTieneUC() {
        return tieneUC;
    }
    public void setTieneUC(Boolean tieneUC) {
        this.tieneUC = tieneUC;
    }
    public Integer getCurso() {
        return curso;
    }
    public void setCurso(Integer curso) {
        this.curso = curso;
    }
    public Integer getLaravelThroughKey() {
        return laravelThroughKey;
    }
    public void setLaravelThroughKey(Integer laravelThroughKey) {        this.laravelThroughKey = laravelThroughKey;    }
}
