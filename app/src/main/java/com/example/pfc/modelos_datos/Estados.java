package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

public class Estados {

    @SerializedName("id")           private Integer id;
    @SerializedName("estado")       private String estado;
    @SerializedName("codigo")       private Integer codigo;

    public Estados() {    }

    /**
     * @param estado
     * @param codigo
     * @param id
     */
    public Estados(Integer id, String estado, Integer codigo) {
        super();        this.id = id;        this.estado = estado;        this.codigo = codigo;
    }

    public Integer getId() {        return id;    }
    public void setId(Integer id) {        this.id = id;    }
    public String getEstado() {        return estado;    }
    public void setEstado(String estado) {        this.estado = estado;    }
    public Integer getCodigo() {        return codigo;    }
    public void setCodigo(Integer codigo) {        this.codigo = codigo;    }
}
