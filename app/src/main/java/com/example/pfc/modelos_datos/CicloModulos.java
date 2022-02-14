package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CicloModulos {

    @SerializedName("id")                   private Integer id;
    @SerializedName("modulo_id")            private Integer moduloId;
    @SerializedName("ciclo_id")             private Integer cicloId;
    @SerializedName("docente_imparte_modulos") private List<DocenteImparteModulo> docenteImparteModulos = null;

    public CicloModulos() {    }

    /**
     * @param cicloId
     * @param moduloId
     * @param id
     * @param docenteImparteModulos
     */
    public CicloModulos(Integer id, Integer moduloId, Integer cicloId, List<DocenteImparteModulo> docenteImparteModulos) {
        super();        this.id = id;        this.moduloId = moduloId;        this.cicloId = cicloId;
        this.docenteImparteModulos = docenteImparteModulos;
    }

    public Integer getId() {        return id;    }
    public void setId(Integer id) {        this.id = id;    }
    public Integer getModuloId() {        return moduloId;    }
    public void setModuloId(Integer moduloId) {        this.moduloId = moduloId;    }
    public Integer getCicloId() {        return cicloId;    }
    public void setCicloId(Integer cicloId) {        this.cicloId = cicloId;    }
    public List<DocenteImparteModulo> getDocenteImparteModulos() {        return docenteImparteModulos;    }
    public void setDocenteImparteModulos(List<DocenteImparteModulo> docenteImparteModulos) {        this.docenteImparteModulos = docenteImparteModulos;    }
}
