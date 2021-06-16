package com.threedev.machinetest.login.model;

import com.google.gson.annotations.SerializedName;

public class LoginModel {


    private String token;


    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getToken ()
    {
        return token;
    }

    public void setToken (String token)
    {
        this.token = token;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [token = "+token+"]";
    }
}
