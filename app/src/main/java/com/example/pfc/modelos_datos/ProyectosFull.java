package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProyectosFull {

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
    @SerializedName("alumnos")                      private Alumnos alumnos;
    @SerializedName("ciclos")                       private Ciclos ciclos;
    @SerializedName("estados")                      private Estados estados;
    @SerializedName("modulos_matriculados")         private List<ModulosMatriculado> modulosMatriculados = null;
    @SerializedName("tutor_evalua_proyectos")       private List<TutorEvaluaProyectos> tutorEvaluaProyectos = null;
    @SerializedName("documentos_proyectos")         private List<DocumentosProyectos> documentosProyectos = null;


    public ProyectosFull() {    }

    /**
     * @param cicloId
     * @param notaPrevia
     * @param comentarioPrevio
     * @param textoRequisitosFuncionales
     * @param estado
     * @param alumnos
     * @param notaFinal
     * @param descTipo
     * @param nombreProyecto
     * @param docenteId
     * @param modulosMatriculados
     * @param textoModulosRelacionados
     * @param estados
     * @param documentosProyectos
     * @param alumnoId
     * @param ciclos
     * @param curso
     * @param tipoProyectoId
     * @param textoPropuestaProyecto
     * @param id
     * @param tutorEvaluaProyectos
     */
    public ProyectosFull(Integer id, Integer curso, Integer cicloId, Integer alumnoId, Integer notaPrevia, String comentarioPrevio, Integer notaFinal,
                         String nombreProyecto, String descTipo, String textoPropuestaProyecto, String textoRequisitosFuncionales, String textoModulosRelacionados,
                         Integer docenteId, Integer tipoProyectoId, Integer estado, Alumnos alumnos, Ciclos ciclos, Estados estados, List<ModulosMatriculado> modulosMatriculados,
                         List<DocumentosProyectos> documentosProyectos, List<TutorEvaluaProyectos> tutorEvaluaProyectos) {
        super();        this.id = id;        this.curso = curso;        this.cicloId = cicloId;        this.alumnoId = alumnoId;
        this.notaPrevia = notaPrevia;        this.comentarioPrevio = comentarioPrevio;        this.notaFinal = notaFinal;
        this.nombreProyecto = nombreProyecto;        this.descTipo = descTipo;        this.textoPropuestaProyecto = textoPropuestaProyecto;
        this.textoRequisitosFuncionales = textoRequisitosFuncionales;        this.textoModulosRelacionados = textoModulosRelacionados;
        this.docenteId = docenteId;        this.tipoProyectoId = tipoProyectoId;        this.estado = estado;        this.alumnos = alumnos;
        this.ciclos = ciclos;        this.estados = estados;        this.modulosMatriculados = modulosMatriculados;        this.documentosProyectos = documentosProyectos;
        this.tutorEvaluaProyectos = tutorEvaluaProyectos;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {        this.id = id;    }
    public Integer getCurso() {
        return curso;
    }
    public void setCurso(Integer curso) {
        this.curso = curso;
    }
    public Integer getCicloId() {
        return cicloId;
    }
    public void setCicloId(Integer cicloId) {
        this.cicloId = cicloId;
    }
    public Integer getAlumnoId() {
        return alumnoId;
    }
    public void setAlumnoId(Integer alumnoId) {
        this.alumnoId = alumnoId;
    }
    public Integer getNotaPrevia() {
        return notaPrevia;
    }
    public void setNotaPrevia(Integer notaPrevia) {
        this.notaPrevia = notaPrevia;
    }
    public String getComentarioPrevio() {
        return comentarioPrevio;
    }
    public void setComentarioPrevio(String comentarioPrevio) {       this.comentarioPrevio = comentarioPrevio;   }
    public Integer getNotaFinal() {
        return notaFinal;
    }
    public void setNotaFinal(Integer notaFinal) {
        this.notaFinal = notaFinal;
    }
    public String getNombreProyecto() {
        return nombreProyecto;
    }
    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }
    public String getDescTipo() {
        return descTipo;
    }
    public void setDescTipo(String descTipo) {
        this.descTipo = descTipo;
    }
    public String getTextoPropuestaProyecto() {
        return textoPropuestaProyecto;
    }
    public void setTextoPropuestaProyecto(String textoPropuestaProyecto) {        this.textoPropuestaProyecto = textoPropuestaProyecto;    }
    public String getTextoRequisitosFuncionales() {
        return textoRequisitosFuncionales;
    }
    public void setTextoRequisitosFuncionales(String textoRequisitosFuncionales) {        this.textoRequisitosFuncionales = textoRequisitosFuncionales;    }
    public String getTextoModulosRelacionados() {
        return textoModulosRelacionados;
    }
    public void setTextoModulosRelacionados(String textoModulosRelacionados) {        this.textoModulosRelacionados = textoModulosRelacionados;    }
    public Integer getDocenteId() {
        return docenteId;
    }
    public void setDocenteId(Integer docenteId) {
        this.docenteId = docenteId;
    }
    public Integer getTipoProyectoId() {
        return tipoProyectoId;
    }
    public void setTipoProyectoId(Integer tipoProyectoId) {
        this.tipoProyectoId = tipoProyectoId;
    }
    public Integer getEstado() {
        return estado;
    }
    public void setEstado(Integer estado) {
        this.estado = estado;
    }
    public Alumnos getAlumnos() {
        return alumnos;
    }
    public void setAlumnos(Alumnos alumnos) {
        this.alumnos = alumnos;
    }
    public Ciclos getCiclos() {
        return ciclos;
    }
    public void setCiclos(Ciclos ciclos) {
        this.ciclos = ciclos;
    }
    public Estados getEstados() {
        return estados;
    }
    public void setEstados(Estados estados) {
        this.estados = estados;
    }
    public List<ModulosMatriculado> getModulosMatriculados() {
        return modulosMatriculados;
    }
    public void setModulosMatriculados(List<ModulosMatriculado> modulosMatriculados) {        this.modulosMatriculados = modulosMatriculados;    }
    public List<DocumentosProyectos> getDocumentosProyectos() {
        return documentosProyectos;
    }
    public void setDocumentosProyectos(List<DocumentosProyectos> documentosProyectos) {        this.documentosProyectos = documentosProyectos;    }
    public List<TutorEvaluaProyectos> getTutorEvaluaProyectos() {
        return tutorEvaluaProyectos;
    }
    public void setTutorEvaluaProyectos(List<TutorEvaluaProyectos> tutorEvaluaProyectos) {        this.tutorEvaluaProyectos = tutorEvaluaProyectos;    }
}
