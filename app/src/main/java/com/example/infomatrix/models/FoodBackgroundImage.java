package com.example.infomatrix.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class FoodBackgroundImage implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("created")
    @Expose
    private Date created;

    @SerializedName("food")
    @Expose
    private int foodId;

    public FoodBackgroundImage() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    protected FoodBackgroundImage(Parcel in) {
        id = in.readInt();
        image = in.readString();
        description = in.readString();
        created = (Date) in.readSerializable();
        foodId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(image);
        dest.writeString(description);
        dest.writeSerializable(created);
        dest.writeInt(foodId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FoodBackgroundImage> CREATOR = new Creator<FoodBackgroundImage>() {
        @Override
        public FoodBackgroundImage createFromParcel(Parcel in) {
            return new FoodBackgroundImage(in);
        }

        @Override
        public FoodBackgroundImage[] newArray(int size) {
            return new FoodBackgroundImage[size];
        }
    };

}
