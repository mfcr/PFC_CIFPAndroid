package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

public class DocenteImparteModulo {

    @SerializedName("id")               private Integer id;
    @SerializedName("curso")            private Integer curso;
    @SerializedName("docente_id")       private Integer docenteId;
    @SerializedName("ciclo_modulo_id")  private Integer cicloModuloId;

    public DocenteImparteModulo() {    }

    /**
     * @param curso
     * @param cicloModuloId
     * @param docenteId
     * @param id
     */
    public DocenteImparteModulo(Integer id, Integer curso, Integer docenteId, Integer cicloModuloId) {
        super();        this.id = id;        this.curso = curso;        this.docenteId = docenteId;        this.cicloModuloId = cicloModuloId;
    }

    public Integer getId() {        return id;    }
    public void setId(Integer id) {        this.id = id;    }
    public Integer getCurso() {        return curso;    }
    public void setCurso(Integer curso) {        this.curso = curso;    }
    public Integer getDocenteId() {        return docenteId;    }
    public void setDocenteId(Integer docenteId) {        this.docenteId = docenteId;    }
    public Integer getCicloModuloId() {        return cicloModuloId;    }
    public void setCicloModuloId(Integer cicloModuloId) {        this.cicloModuloId = cicloModuloId;    }
}
