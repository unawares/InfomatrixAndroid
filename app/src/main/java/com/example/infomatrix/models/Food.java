package com.example.infomatrix.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {

    public enum FoodType {

        BREAKFAST,
        LUNCH,
        DINNER;

    }

    private String description;

    private String title;

    private FoodType foodType;

    private int drawableId;

    public Food() {

    }

    protected Food(Parcel in) {
        description = in.readString();
        foodType = FoodType.valueOf(in.readString());
        title = in.readString();
        drawableId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(foodType.name());
        dest.writeString(title);
        dest.writeInt(drawableId);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }
}
