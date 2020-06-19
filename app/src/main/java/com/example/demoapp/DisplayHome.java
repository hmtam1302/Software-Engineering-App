package com.example.demoapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DisplayHome extends AppCompatActivity {

    private Scene homeScene;
    private Scene foodScene;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Hide title bar and enable full-screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide the title
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        //Set main content view
        setContentView(R.layout.activity_main);

        ViewGroup root = findViewById(R.id.mainContainer);
        homeScene = Scene.getSceneForLayout(root, R.layout.orderhome, this);
        foodScene = Scene.getSceneForLayout(root, R.layout.orderfood, this);

        displayHome();

    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void displayHome(){
        Transition explode = new Explode();
        TransitionManager.go(homeScene, explode);

        ImageButton infoBtn = (ImageButton)findViewById(R.id.infobtn);
        infoBtn.setBackgroundColor(android.R.color.white);

        List<Restaurant> image_details = getListRestaurantData();
        final ListView listView = (ListView) findViewById(R.id.listRestaurant);
        listView.setAdapter(new CustomListRestaurantAdapter(this, image_details));

        // When the user clicks on the ListItem
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                displayFood(v);
            }
        });
    }
    private  List<Restaurant> getListRestaurantData() {
        List<Restaurant> list = new ArrayList<Restaurant>();
        Restaurant kfc = new Restaurant("KFC", "kfc", "Discription: KFC Chicken","4.5/5.0");
        Restaurant kichikichi = new Restaurant("Kichi Kichi", "kichikichi", "Discription: Hotpot", "4.0/5.0");
        Restaurant loteria = new Restaurant("Lotteria", "lotteria", "Discription: Chiken, Cake, and Chips", "5.0/5.0");
        Restaurant phuclong = new Restaurant("Phúc Long", "phuclong", "Discription: MilkTea", "4.75/5.0");
        Restaurant thecoffeehouse = new Restaurant("THE COFFEE HOUSE", "thecoffeehouse", "Discription: Milktea, Cake", "4.5/5.0");
        Restaurant mcdonald = new Restaurant("McDonald's","mcdonald", "Discription: McDonald's Chicken", "4.5/5.0");


        list.add(kfc);
        list.add(kichikichi);
        list.add(loteria);
        list.add(phuclong);
        list.add(thecoffeehouse);
        list.add(mcdonald);

        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void displayFood(View view){
        Transition explode = new Explode();
        TransitionManager.go(foodScene, explode);

        List<Food> image_details = getListFoodData();
        final ListView listView = (ListView) findViewById(R.id.listFood);
        listView.setAdapter(new CustomListFoodAdapter(this, image_details));
    }
    private List<Food> getListFoodData() {
        List<Food> list = new ArrayList<Food>();
        Food duiga = new Food("Đùi Gà KFC", "duiga", 15,"Đùi gà chiên thơm giòn", "35000", "4.5/5.0");
        Food canhga = new Food("Cánh Gà KFC", "canhga", 20, "Cánh gà chiên nước mắm", "30000", "4.5/5.0");
        Food khoaitaychien = new Food("Khoai Tây Chiên KFC", "khoaitaychien", 25, "Khoai tây chiên giòn rụm", "20000", "4.5/5.0");
        Food tranhanlai = new Food("Trà Nhãn Lài", "tranhanlai", 40, "Trà nhãn lài ngọt ngào", "50000", "5.0/5.0");
        Food trahoahong = new Food("Trà Hoa Hồng", "trahoahong", 35, "Trà hoa hồng thơm dịu dàng", "45000", "5.0/5.0");


        list.add(duiga);
        list.add(canhga);
        list.add(khoaitaychien);
        list.add(tranhanlai);
        list.add(trahoahong);

        return list;
    }
}