package com.example.infomatrix.models;

import android.graphics.Bitmap;

import java.util.Date;

public class Food {

    private String title;
    private String description;
    private Date date;
    private String imageURL;

    public Food() {
        title = "Title";
        description = "Description";
        date = new Date();
        imageURL = "https://bit.ly/2Eu8evY";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

}
