package com.example.infomatrix.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class FoodService extends Service implements Parcelable {

    @SerializedName("orders")
    @Expose
    private List<FoodServiceOrder> orders;

    @SerializedName("amount")
    @Expose
    private int amount;

    @SerializedName("food")
    @Expose
    private int food;

    public FoodService() {
        super();
    }

    protected FoodService(Parcel in) {
        super(in);
        in.readTypedList(orders, FoodServiceOrder.CREATOR);
        amount = in.readInt();
        food = in.readInt();
    }

    public static final Creator<FoodService> CREATOR = new Creator<FoodService>() {
        @Override
        public FoodService createFromParcel(Parcel in) {
            return new FoodService(in);
        }

        @Override
        public FoodService[] newArray(int size) {
            return new FoodService[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(orders);
        dest.writeInt(amount);
        dest.writeInt(food);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    public List<FoodServiceOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<FoodServiceOrder> orders) {
        this.orders = orders;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

}
