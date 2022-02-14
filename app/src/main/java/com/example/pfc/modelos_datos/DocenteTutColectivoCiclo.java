package com.example.pfc.modelos_datos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocenteTutColectivoCiclo {

    @SerializedName("id")           private Integer id;
    @SerializedName("curso")        private Integer curso;
    @SerializedName("docente_id")   private Integer docenteId;
    @SerializedName("ciclo_id")     private Integer cicloId;

    public DocenteTutColectivoCiclo() {    }

    /**
     * @param cicloId
     * @param curso
     * @param docenteId
     * @param id
     */
    public DocenteTutColectivoCiclo(Integer id, Integer curso, Integer docenteId, Integer cicloId) {
        super();        this.id = id;        this.curso = curso;        this.docenteId = docenteId;        this.cicloId = cicloId;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getCurso() {
        return curso;
    }
    public void setCurso(Integer curso) {
        this.curso = curso;
    }
    public Integer getDocenteId() {
        return docenteId;
    }
    public void setDocenteId(Integer docenteId) {
        this.docenteId = docenteId;
    }
    public Integer getCicloId() {
        return cicloId;
    }
    public void setCicloId(Integer cicloId) {
        this.cicloId = cicloId;
    }
}
