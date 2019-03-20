package com.example.infomatrix.test;

        import com.example.infomatrix.models.Food;

        import java.util.ArrayList;

public class FoodsBuilder {

    public static ArrayList<Food> build() {
        ArrayList<Food> foodArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            foodArrayList.add(new Food());
        }
        return foodArrayList;
    }

}
