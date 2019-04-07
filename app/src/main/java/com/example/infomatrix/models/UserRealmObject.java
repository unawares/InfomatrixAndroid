package com.example.infomatrix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class UserRealmObject extends RealmObject {

    private String fullName;
    private String code;
    private int role;
    private boolean isFood;
    private boolean isTransport;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean isFood() {
        return isFood;
    }

    public void setFood(boolean food) {
        isFood = food;
    }

    public boolean isTransport() {
        return isTransport;
    }

    public void setTransport(boolean transport) {
        isTransport = transport;
    }
}
