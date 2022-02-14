package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")                       private Integer id;
    @SerializedName("email")                    private String email;
    @SerializedName("category")                 private Integer category;
    @SerializedName("password_modificada")      private Integer passwordModificada;
    @SerializedName("created_at")               private String createdAt;
    @SerializedName("updated_at")               private String updatedAt;

    public User() {    }

    /**
     * @param createdAt
     * @param id
     * @param category
     * @param passwordModificada
     * @param email
     * @param updatedAt
     */
    public User(Integer id, String email, Integer category, Integer passwordModificada, String createdAt, String updatedAt) {
        super();
        this.id = id;                       this.email = email;        this.category = category;        this.passwordModificada = passwordModificada;
        this.createdAt = createdAt;         this.updatedAt = updatedAt;
    }

    public Integer getId() {        return id;    }
    public void setId(Integer id) {        this.id = id;    }
    public String getEmail() {        return email;    }
    public void setEmail(String email) {        this.email = email;    }
    public Integer getCategory() {        return category;    }
    public void setCategory(Integer category) {        this.category = category;    }
    public Integer getPasswordModificada() {        return passwordModificada;    }
    public void setPasswordModificada(Integer passwordModificada) {        this.passwordModificada = passwordModificada;    }
    public String getCreatedAt() {        return createdAt;    }
    public void setCreatedAt(String createdAt) {        this.createdAt = createdAt;    }
    public String getUpdatedAt() {        return updatedAt;    }
    public void setUpdatedAt(String updatedAt) {        this.updatedAt = updatedAt;    }
}
