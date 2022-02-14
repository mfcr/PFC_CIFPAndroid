package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

public class ModulosMatriculado {

    @SerializedName("id")               private Integer id;
    @SerializedName("ciclo_modulo_id")  private Integer cicloModuloId;
    @SerializedName("proyecto_id")      private Integer proyectoId;
    @SerializedName("estado")           private String estado;
    @SerializedName("preevaluado")      private Integer preevaluado;
    @SerializedName("comentario")       private String comentario;
    @SerializedName("modulos")          private Modulos modulos;
    @SerializedName("ciclo_modulos")    private CicloModulos cicloModulos;

    public ModulosMatriculado() {    }

    /**
     * @param preevaluado
     * @param proyectoId
     * @param estado
     * @param modulos
     * @param cicloModuloId
     * @param cicloModulos
     * @param id
     * @param comentario
     */
    public ModulosMatriculado(Integer id, Integer cicloModuloId, Integer proyectoId, String estado, Integer preevaluado, String comentario, Modulos modulos, CicloModulos cicloModulos) {
        super();        this.id = id;        this.cicloModuloId = cicloModuloId;        this.proyectoId = proyectoId;        this.estado = estado;
        this.preevaluado = preevaluado;        this.comentario = comentario;        this.modulos = modulos;        this.cicloModulos = cicloModulos;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getCicloModuloId() {
        return cicloModuloId;
    }
    public void setCicloModuloId(Integer cicloModuloId) {
        this.cicloModuloId = cicloModuloId;
    }
    public Integer getProyectoId() {
        return proyectoId;
    }
    public void setProyectoId(Integer proyectoId) {
        this.proyectoId = proyectoId;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public Integer getPreevaluado() {
        return preevaluado;
    }
    public void setPreevaluado(Integer preevaluado) {
        this.preevaluado = preevaluado;
    }
    public String getComentario() {
        return comentario;
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    public Modulos getModulos() {
        return modulos;
    }
    public void setModulos(Modulos modulos) {
        this.modulos = modulos;
    }
    public CicloModulos getCicloModulos() {
        return cicloModulos;
    }
    public void setCicloModulos(CicloModulos cicloModulos) {
        this.cicloModulos = cicloModulos;
    }
}
