package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

public class DocenteTutColectivoCiclos {

    @SerializedName("id")           private Integer id;
    @SerializedName("curso")        private Integer curso;
    @SerializedName("docente_id")   private Integer docenteId;
    @SerializedName("ciclo_id")     private Integer cicloId;
    @SerializedName("docentes")     private Docentes docentes;
    @SerializedName("ciclos")       private Ciclos ciclos;

    public DocenteTutColectivoCiclos() {    }

    /**
     * @param cicloId
     * @param ciclos
     * @param curso
     * @param docenteId
     * @param id
     * @param docentes
     */
    public DocenteTutColectivoCiclos(Integer id, Integer curso, Integer docenteId, Integer cicloId, Docentes docentes, Ciclos ciclos) {
        super();        this.id = id;        this.curso = curso;        this.docenteId = docenteId;        this.cicloId = cicloId;
        this.docentes = docentes;        this.ciclos = ciclos;
    }

    public Integer getId() {        return id;    }
    public void setId(Integer id) {        this.id = id;    }
    public Integer getCurso() {        return curso;    }
    public void setCurso(Integer curso) {        this.curso = curso;    }
    public Integer getDocenteId() {        return docenteId;    }
    public void setDocenteId(Integer docenteId) {        this.docenteId = docenteId;    }
    public Integer getCicloId() {        return cicloId;    }
    public void setCicloId(Integer cicloId) {        this.cicloId = cicloId;    }
    public Docentes getDocentes() {        return docentes;    }
    public void setDocentes(Docentes docentes) {        this.docentes = docentes;    }
    public Ciclos getCiclos() {        return ciclos;    }
    public void setCiclos(Ciclos ciclos) {        this.ciclos = ciclos;    }
}
