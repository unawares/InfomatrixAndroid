package com.example.infomatrix.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Services implements Parcelable {

    @SerializedName("foods")
    @Expose
    private List<FoodService> foods;

    public Services() {

    }

    protected Services(Parcel in) {
        foods = in.createTypedArrayList(FoodService.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(foods);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Services> CREATOR = new Creator<Services>() {
        @Override
        public Services createFromParcel(Parcel in) {
            return new Services(in);
        }

        @Override
        public Services[] newArray(int size) {
            return new Services[size];
        }
    };

    public List<FoodService> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodService> foods) {
        this.foods = foods;
    }



}
