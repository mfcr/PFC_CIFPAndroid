package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

public class TutorEvaluaProyectos {

    @SerializedName("id")               private Integer id;
    @SerializedName("nota")             private Integer nota;
    @SerializedName("comentario")       private String comentario;
    @SerializedName("docente_id")       private Integer docenteId;
    @SerializedName("proyecto_id")      private Integer proyectoId;
    @SerializedName("rubrica_id")       private Integer rubricaId;
    @SerializedName("esColectivo")      private Boolean esColectivo;

    public TutorEvaluaProyectos() {    }

    /**
     * @param proyectoId
     * @param docenteId
     * @param esColectivo
     * @param id
     * @param rubricaId
     * @param comentario
     * @param nota
     */
    public TutorEvaluaProyectos(Integer id, Integer nota, String comentario, Integer docenteId, Integer proyectoId, Integer rubricaId, Boolean esColectivo) {
        super();        this.id = id;        this.nota = nota;        this.comentario = comentario;        this.docenteId = docenteId;
        this.proyectoId = proyectoId;        this.rubricaId = rubricaId;        this.esColectivo = esColectivo;
    }

    public Integer getId() {        return id;    }
    public void setId(Integer id) {        this.id = id;    }
    public Integer getNota() {        return nota;    }
    public void setNota(Integer nota) {        this.nota = nota;    }
    public String getComentario() {        return comentario;    }
    public void setComentario(String comentario) {        this.comentario = comentario;    }
    public Integer getDocenteId() {        return docenteId;    }
    public void setDocenteId(Integer docenteId) {        this.docenteId = docenteId;    }
    public Integer getProyectoId() {        return proyectoId;    }
    public void setProyectoId(Integer proyectoId) {        this.proyectoId = proyectoId;    }
    public Integer getRubricaId() {        return rubricaId;    }
    public void setRubricaId(Integer rubricaId) {        this.rubricaId = rubricaId;    }
    public Boolean getEsColectivo() {        return esColectivo;    }
    public void setEsColectivo(Boolean esColectivo) {        this.esColectivo = esColectivo;    }
}
