package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

public class Documento {

    @SerializedName("id")           private Integer id;
    @SerializedName("nombre")       private String nombre;
    @SerializedName("descripcion")  private String descripcion;
    @SerializedName("tipo")         private String tipo;
    @SerializedName("uri")          private String uri;
    @SerializedName("isFile")       private Boolean isFile;
    @SerializedName("publico")      private Boolean publico;
    @SerializedName("ciclo_id")     private Integer cicloId;

    public Documento() {    }

    /**
     * @param descripcion
     * @param cicloId
     * @param tipo
     * @param isFile
     * @param id
     * @param nombre
     * @param uri
     * @param publico
     */
    public Documento(Integer id, String nombre, String descripcion, String tipo, String uri, Boolean isFile, Boolean publico,  Integer cicloId) {
        super();        this.id = id;        this.nombre = nombre;        this.descripcion = descripcion;
        this.tipo = tipo;        this.uri = uri;        this.isFile = isFile;        this.publico = publico;        this.cicloId = cicloId;
    }

    public Integer getId() {        return id;    }
    public void setId(Integer id) {        this.id = id;    }
    public String getNombre() {        return nombre;    }
    public void setNombre(String nombre) {        this.nombre = nombre;    }
    public String getDescripcion() {        return descripcion;    }
    public void setDescripcion(String descripcion) {        this.descripcion = descripcion;    }
    public String getTipo() {        return tipo;    }
    public void setTipo(String tipo) {        this.tipo = tipo;    }
    public String getUri() {        return uri;    }
    public void setUri(String uri) {        this.uri = uri;    }
    public Boolean getIsFile() {        return isFile;    }
    public void setIsFile(Boolean isFile) {        this.isFile = isFile;    }
    public Boolean getPublico() {        return publico;    }
    public void setPublico(Boolean publico) {        this.publico = publico;    }
    public Integer getCicloId() {        return cicloId;    }
    public void setCicloId(Integer cicloId) {        this.cicloId = cicloId;    }

}

