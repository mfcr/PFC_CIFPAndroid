
package com.example.pfc.modelos_datos;

import com.google.gson.annotations.SerializedName;

public class Users {

    @SerializedName("user")    private User user;

    public Users() {    }

    public Users(User user) {
        super();        this.user = user;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}