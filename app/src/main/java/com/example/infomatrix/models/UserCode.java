package com.example.infomatrix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class UserCode {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("created")
    @Expose
    private Date created;

    @SerializedName("updated")
    @Expose
    private Date updated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

}
