package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

public class TiposProyectoCiclosConTipos {

    @SerializedName("id")               private Integer id;
    @SerializedName("ciclo_id")         private Integer cicloId;
    @SerializedName("tipo_proyecto_id") private Integer tipoProyectoId;
    @SerializedName("tipo_proyectos")   private TipoProyectos tipoProyectos;

    public TiposProyectoCiclosConTipos() {    }

    /**
     * @param cicloId
     * @param tipoProyectoId
     * @param id
     * @param tipoProyectos
     */
    public TiposProyectoCiclosConTipos(Integer id, Integer cicloId, Integer tipoProyectoId, TipoProyectos tipoProyectos) {
        super();        this.id = id;        this.cicloId = cicloId;        this.tipoProyectoId = tipoProyectoId;        this.tipoProyectos = tipoProyectos;
    }

    public Integer getId() {        return id;    }
    public void setId(Integer id) {        this.id = id;    }
    public Integer getCicloId() {        return cicloId;    }
    public void setCicloId(Integer cicloId) {        this.cicloId = cicloId;    }
    public Integer getTipoProyectoId() {        return tipoProyectoId;    }
    public void setTipoProyectoId(Integer tipoProyectoId) {        this.tipoProyectoId = tipoProyectoId;    }
    public TipoProyectos getTipoProyectos() {        return tipoProyectos;    }
    public void setTipoProyectos(TipoProyectos tipoProyectos) {       this.tipoProyectos = tipoProyectos;    }
}
