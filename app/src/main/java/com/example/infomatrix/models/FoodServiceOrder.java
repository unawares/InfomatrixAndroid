package com.example.infomatrix.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class FoodServiceOrder implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("created")
    @Expose
    private Date created;

    @SerializedName("food_service")
    @Expose
    private int foodService;

    public FoodServiceOrder() {

    }

    protected FoodServiceOrder(Parcel in) {
        id = in.readInt();
        created = (Date) in.readSerializable();
        foodService = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeSerializable(created);
        dest.writeInt(foodService);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FoodServiceOrder> CREATOR = new Creator<FoodServiceOrder>() {
        @Override
        public FoodServiceOrder createFromParcel(Parcel in) {
            return new FoodServiceOrder(in);
        }

        @Override
        public FoodServiceOrder[] newArray(int size) {
            return new FoodServiceOrder[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getFoodService() {
        return foodService;
    }

    public void setFoodService(int foodService) {
        this.foodService = foodService;
    }
}
