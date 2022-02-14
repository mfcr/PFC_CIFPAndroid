package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

public class RubricasConGrupos {

    @SerializedName("id")               private Integer id;
    @SerializedName("curso")            private Integer curso;
    @SerializedName("rubrica")          private String rubrica;
    @SerializedName("descExcelente")    private String descExcelente;
    @SerializedName("descBien")         private String descBien;
    @SerializedName("descRegular")      private String descRegular;
    @SerializedName("descInsuficiente") private String descInsuficiente;
    @SerializedName("porcentaje")       private Double porcentaje;
    @SerializedName("ciclo_id")         private Integer cicloId;
    @SerializedName("grupo_rubrica_id") private Integer grupoRubricaId;
    @SerializedName("grupo_rubricas")   private GrupoRubricas grupoRubricas;

    public RubricasConGrupos() {    }

    /**
     * @param cicloId
     * @param rubrica
     * @param grupoRubricaId
     * @param curso
     * @param descRegular
     * @param descExcelente
     * @param grupoRubricas
     * @param id
     * @param descBien
     * @param porcentaje
     * @param descInsuficiente
     */
    public RubricasConGrupos(Integer id, Integer curso, String rubrica, String descExcelente, String descBien, String descRegular, String descInsuficiente, Double porcentaje, Integer cicloId, Integer grupoRubricaId, GrupoRubricas grupoRubricas) {
        super();        this.id = id;        this.curso = curso;        this.rubrica = rubrica;        this.descExcelente = descExcelente;
        this.descBien = descBien;        this.descRegular = descRegular;        this.descInsuficiente = descInsuficiente;
        this.porcentaje = porcentaje;        this.cicloId = cicloId;        this.grupoRubricaId = grupoRubricaId;
        this.grupoRubricas = grupoRubricas;
    }

    public Integer getId() {        return id;    }
    public void setId(Integer id) {        this.id = id;    }
    public Integer getCurso() {        return curso;    }
    public void setCurso(Integer curso) {        this.curso = curso;    }
    public String getRubrica() {        return rubrica;    }
    public void setRubrica(String rubrica) {        this.rubrica = rubrica;    }
    public String getDescExcelente() {        return descExcelente;    }
    public void setDescExcelente(String descExcelente) {        this.descExcelente = descExcelente;    }
    public String getDescBien() {        return descBien;    }
    public void setDescBien(String descBien) {        this.descBien = descBien;    }
    public String getDescRegular() {        return descRegular;    }
    public void setDescRegular(String descRegular) {        this.descRegular = descRegular;    }
    public String getDescInsuficiente() {        return descInsuficiente;    }
    public void setDescInsuficiente(String descInsuficiente) {        this.descInsuficiente = descInsuficiente;    }
    public Double getPorcentaje() {        return porcentaje;   }
    public void setPorcentaje(Double porcentaje) {        this.porcentaje = porcentaje;    }
    public Integer getCicloId() {        return cicloId;    }
    public void setCicloId(Integer cicloId) {        this.cicloId = cicloId;    }
    public Integer getGrupoRubricaId() {        return grupoRubricaId;    }
    public void setGrupoRubricaId(Integer grupoRubricaId) {        this.grupoRubricaId = grupoRubricaId;    }
    public GrupoRubricas getGrupoRubricas() {        return grupoRubricas;    }
    public void setGrupoRubricas(GrupoRubricas grupoRubricas) {        this.grupoRubricas = grupoRubricas;    }
}
