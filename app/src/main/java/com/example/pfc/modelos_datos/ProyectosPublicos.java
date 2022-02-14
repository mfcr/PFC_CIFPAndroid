package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

public class ProyectosPublicos {

        @SerializedName("id")                           private Integer id;
        @SerializedName("curso")                        private Integer curso;
        @SerializedName("ciclo_id")                     private Integer cicloId;
        @SerializedName("alumno_id")                    private Integer alumnoId;
        @SerializedName("notaPrevia")                   private Integer notaPrevia;
        @SerializedName("comentarioPrevio")             private String comentarioPrevio;
        @SerializedName("NotaFinal")                    private Integer notaFinal;
        @SerializedName("nombreProyecto")               private String nombreProyecto;
        @SerializedName("descTipo")                     private String descTipo;
        @SerializedName("textoPropuestaProyecto")       private String textoPropuestaProyecto;
        @SerializedName("textoRequisitosFuncionales")   private String textoRequisitosFuncionales;
        @SerializedName("textoModulosRelacionados")     private String textoModulosRelacionados;
        @SerializedName("docente_id")                   private Integer docenteId;
        @SerializedName("tipo_proyecto_id")             private Integer tipoProyectoId;
        @SerializedName("estado")                       private Integer estado;

        public ProyectosPublicos() {        }

        /**
         * @param cicloId
         * @param notaPrevia
         * @param comentarioPrevio
         * @param textoRequisitosFuncionales
         * @param estado
         * @param notaFinal
         * @param descTipo
         * @param nombreProyecto
         * @param docenteId
         * @param textoModulosRelacionados
         * @param alumnoId
         * @param curso
         * @param tipoProyectoId
         * @param textoPropuestaProyecto
         * @param id
         */
        public ProyectosPublicos(Integer id, Integer curso, Integer cicloId, Integer alumnoId, Integer notaPrevia, String comentarioPrevio, Integer notaFinal, String nombreProyecto, String descTipo, String textoPropuestaProyecto, String textoRequisitosFuncionales, String textoModulosRelacionados, Integer docenteId, Integer tipoProyectoId, Integer estado) {
            super();            this.id = id;            this.curso = curso;            this.cicloId = cicloId;            this.alumnoId = alumnoId;
            this.notaPrevia = notaPrevia;            this.comentarioPrevio = comentarioPrevio;            this.notaFinal = notaFinal;
            this.nombreProyecto = nombreProyecto;            this.descTipo = descTipo;            this.textoPropuestaProyecto = textoPropuestaProyecto;
            this.textoRequisitosFuncionales = textoRequisitosFuncionales;            this.textoModulosRelacionados = textoModulosRelacionados;
            this.docenteId = docenteId;            this.tipoProyectoId = tipoProyectoId;            this.estado = estado;
        }

        public Integer getId() {            return id;        }
        public void setId(Integer id) {            this.id = id;        }
        public Integer getCurso() {            return curso;        }
        public void setCurso(Integer curso) {            this.curso = curso;        }
        public Integer getCicloId() {            return cicloId;        }
        public void setCicloId(Integer cicloId) {            this.cicloId = cicloId;        }
        public Integer getAlumnoId() {            return alumnoId;        }
        public void setAlumnoId(Integer alumnoId) {            this.alumnoId = alumnoId;        }
        public Integer getNotaPrevia() {            return notaPrevia;        }
        public void setNotaPrevia(Integer notaPrevia) {            this.notaPrevia = notaPrevia;        }
        public String getComentarioPrevio() {            return comentarioPrevio;        }
        public void setComentarioPrevio(String comentarioPrevio) {            this.comentarioPrevio = comentarioPrevio;        }
        public Integer getNotaFinal() {            return notaFinal;        }
        public void setNotaFinal(Integer notaFinal) {            this.notaFinal = notaFinal;        }
        public String getNombreProyecto() {            return nombreProyecto;        }
        public void setNombreProyecto(String nombreProyecto) {            this.nombreProyecto = nombreProyecto;        }
        public String getDescTipo() {            return descTipo;        }
        public void setDescTipo(String descTipo) {            this.descTipo = descTipo;        }
        public String getTextoPropuestaProyecto() {            return textoPropuestaProyecto;        }
        public void setTextoPropuestaProyecto(String textoPropuestaProyecto) {            this.textoPropuestaProyecto = textoPropuestaProyecto;        }
        public String getTextoRequisitosFuncionales() {            return textoRequisitosFuncionales;        }
        public void setTextoRequisitosFuncionales(String textoRequisitosFuncionales) {            this.textoRequisitosFuncionales = textoRequisitosFuncionales;        }
        public String getTextoModulosRelacionados() {            return textoModulosRelacionados;        }
        public void setTextoModulosRelacionados(String textoModulosRelacionados) {            this.textoModulosRelacionados = textoModulosRelacionados;        }
        public Integer getDocenteId() {            return docenteId;        }
        public void setDocenteId(Integer docenteId) {            this.docenteId = docenteId;        }
        public Integer getTipoProyectoId() {            return tipoProyectoId;        }
        public void setTipoProyectoId(Integer tipoProyectoId) {            this.tipoProyectoId = tipoProyectoId;        }
        public Integer getEstado() {            return estado;        }
        public void setEstado(Integer estado) {            this.estado = estado;        }
}
