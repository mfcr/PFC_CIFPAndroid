package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

public class Alumnos {

    @SerializedName("id")           private Integer id;
    @SerializedName("email")        private String email;
    @SerializedName("dni")          private String dni;
    @SerializedName("nombre")       private String nombre;
    @SerializedName("apellido1")    private String apellido1;
    @SerializedName("apellido2")    private String apellido2;
    @SerializedName("telefono")     private String telefono;
    @SerializedName("activo")       private Boolean activo;

    public Alumnos() {    }

    /**
     * @param apellido2
     * @param apellido1
     * @param id
     * @param telefono
     * @param nombre
     * @param email
     * @param dni
     * @param activo
     */
    public Alumnos(Integer id, String email, String dni, String nombre, String apellido1, String apellido2, String telefono, Boolean activo) {
        super();
        this.id = id;        this.email = email;        this.dni = dni;        this.nombre = nombre;
        this.apellido1 = apellido1;        this.apellido2 = apellido2;        this.telefono = telefono;        this.activo = activo;
    }

    public Integer getId() {        return id;    }
    public void setId(Integer id) {        this.id = id;    }
    public String getEmail() {        return email;    }
    public void setEmail(String email) {        this.email = email;    }
    public String getDni() {        return dni;    }
    public void setDni(String dni) {        this.dni = dni;    }
    public String getNombre() {        return nombre;    }
    public void setNombre(String nombre) {        this.nombre = nombre;    }
    public String getApellido1() {        return apellido1;    }
    public void setApellido1(String apellido1) {        this.apellido1 = apellido1;    }
    public String getApellido2() {        return apellido2;    }
    public void setApellido2(String apellido2) {        this.apellido2 = apellido2;    }
    public String getTelefono() {        return telefono;    }
    public void setTelefono(String telefono) {        this.telefono = telefono;    }
    public Boolean getActivo() {        return activo;    }
    public void setActivo(Boolean activo) {        this.activo = activo;    }

}
