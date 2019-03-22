package com.example.infomatrix.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Food implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("date")
    @Expose
    private Date date;

    @SerializedName("created")
    @Expose
    private Date created;

    @SerializedName("updated")
    @Expose
    private Date updated;

    @SerializedName("food_background_image")
    @Expose
    FoodBackgroundImage foodBackgroundImage;

    public Food() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public FoodBackgroundImage getFoodBackgroundImage() {
        return foodBackgroundImage;
    }

    public void setFoodBackgroundImage(FoodBackgroundImage foodBackgroundImage) {
        this.foodBackgroundImage = foodBackgroundImage;
    }

    protected Food(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        date = (Date) in.readSerializable();
        created = (Date) in.readSerializable();
        updated = (Date) in.readSerializable();
        foodBackgroundImage = in.readParcelable(FoodBackgroundImage.class.getClassLoader());

    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeSerializable(date);
        dest.writeSerializable(created);
        dest.writeSerializable(updated);
        dest.writeParcelable(foodBackgroundImage, flags);
    }
}
