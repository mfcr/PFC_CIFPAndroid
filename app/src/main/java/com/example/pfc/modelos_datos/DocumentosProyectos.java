package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

public class DocumentosProyectos {

    @SerializedName("id")                   private Integer id;
    @SerializedName("tipoDocumento")        private String tipoDocumento;
    @SerializedName("descripcion")          private String descripcion;
    @SerializedName("UriDocumento")         private String uriDocumento;
    @SerializedName("isFile")               private Boolean isFile;
    @SerializedName("publico")              private Boolean publico;
    @SerializedName("proyecto_id")          private Integer proyectoId;
    @SerializedName("created_at")           private String createdAt;
    @SerializedName("updated_at")           private String updatedAt;

    public DocumentosProyectos() {    }

    /**
     * @param descripcion
     * @param tipoDocumento
     * @param createdAt
     * @param proyectoId
     * @param isFile
     * @param id
     * @param publico
     * @param uriDocumento
     * @param updatedAt
     */
    public DocumentosProyectos(Integer id, String tipoDocumento, String descripcion, String uriDocumento, Boolean isFile, Boolean publico, Integer proyectoId, String createdAt, String updatedAt) {
        super();            this.id = id;           this.tipoDocumento = tipoDocumento;         this.descripcion = descripcion;        this.uriDocumento = uriDocumento;
        this.isFile = isFile;           this.publico = publico;        this.proyectoId = proyectoId;        this.createdAt = createdAt;        this.updatedAt = updatedAt;
    }

    public Integer getId() {        return id;    }
    public void setId(Integer id) {        this.id = id;    }
    public String getTipoDocumento() {        return tipoDocumento;    }
    public void setTipoDocumento(String tipoDocumento) {        this.tipoDocumento = tipoDocumento;    }
    public String getDescripcion() {        return descripcion;    }
    public void setDescripcion(String descripcion) {        this.descripcion = descripcion;    }
    public String getUriDocumento() {        return uriDocumento;    }
    public void setUriDocumento(String uriDocumento) {        this.uriDocumento = uriDocumento;    }
    public Boolean getIsFile() {        return isFile;    }
    public void setIsFile(Boolean isFile) {        this.isFile = isFile;    }
    public Boolean getPublico() {        return publico;    }
    public void setPublico(Boolean publico) {        this.publico = publico;    }
    public Integer getProyectoId() {        return proyectoId;    }
    public void setProyectoId(Integer proyectoId) {        this.proyectoId = proyectoId;    }
    public String getCreatedAt() {        return createdAt;    }
    public void setCreatedAt(String createdAt) {        this.createdAt = createdAt;    }
    public String getUpdatedAt() {        return updatedAt;    }
    public void setUpdatedAt(String updatedAt) {        this.updatedAt = updatedAt;    }

}
