package com.example.demoapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.demoapp.Data.Food;
import com.example.demoapp.Data.Restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DisplayHome extends AppCompatActivity {
    private String selectedRestaurant = null;
    private Scene homeScene;
    private Scene foodScene;
    public static ArrayList<Restaurant> resList = new ArrayList<>();
    public static ArrayList<Food> foodList = new ArrayList<>();

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
    public void displayHome() {
        Transition slide = new Slide(Gravity.LEFT);
        TransitionManager.go(homeScene, slide);

        ImageButton infoBtn = (ImageButton) findViewById(R.id.infobtn);
        infoBtn.setBackgroundColor(android.R.color.white);

        ListView listView = (ListView) findViewById(R.id.listRestaurant);
        if(DisplayLogin.resList.size() != 0) {
            resList = DisplayLogin.resList;
            foodList = DisplayLogin.foodList;
        }
        else {
            resList = DisplaySignUp.resList;
            foodList = DisplaySignUp.foodList;
        }
        listView.setAdapter(new CustomListRestaurantAdapter(this, resList));
        // When the user clicks on the ListItem
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                displayFood(v);
            }
        });
    }

    public void searchRes(View view) {
        String key = ((EditText) findViewById(R.id.resKey)).getText().toString();
        List<Restaurant> temp = new ArrayList<Restaurant>();

        for (int i = 0; i < resList.size(); i++) {
            Restaurant restaurant = resList.get(i);
            String name = restaurant.getName().toLowerCase();
            if (key != null) {
                if (name.contains(key.toLowerCase())) temp.add(restaurant);
                final ListView listView = (ListView) findViewById(R.id.listRestaurant);
                listView.setAdapter(new CustomListRestaurantAdapter(this, temp));
            } else {
                final ListView listView = (ListView) findViewById(R.id.listRestaurant);
                listView.setAdapter(new CustomListRestaurantAdapter(this, resList));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void displayFood(View view) {
        Transition slide = new Slide(Gravity.RIGHT);
        TransitionManager.go(foodScene, slide);
        final ListView listView = (ListView) findViewById(R.id.listFood);

        TextView resName = (TextView) view.findViewById(R.id.restaurantName);
        ArrayList<Food> foodResList = new ArrayList<>(); // Menu of chosen restaurant

        String name = resName.getText().toString();
        selectedRestaurant = name;
        int resID = 0;
        Log.d("restaurant chosen: ", selectedRestaurant);
        //Set title name (Restaurant name in orderfood scene)
        TextView title = (TextView) findViewById(R.id.resTitle);
        title.setText(name);

        for (int i = 0; i < resList.size(); i++) {
            Restaurant restaurant = resList.get(i);
            Log.d("Restaurant name ", restaurant.getName());
            Log.d("Restaurant ID ", restaurant.getID()+"");
            if(restaurant.getName().equals(selectedRestaurant)) {
                resID = resList.get(i).getID();
                break;
            }
        }
        Log.d("ID res", resID+"");
        for (int i = 0; i < foodList.size(); i++) {
            if(foodList.get(i).getRes_ID() == resID) {
                foodResList.add(foodList.get(i));
            }
        }
        Log.d("msg: ", foodResList.size()+"");
        listView.setAdapter(new CustomListFoodAdapter(this, foodResList));
        // When the user clicks on the ListItem
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                displayFoodDetail(v);
            }
        });
    }

    public void searchFood(View view) {
        String key = ((EditText) findViewById(R.id.foodKey)).getText().toString();
        List<Food> temp = new ArrayList<>();

        //Find restaurant ID
        int Res_ID = 0;
        for (int i = 0; i < resList.size(); i++) {
            if (resList.get(i).getName().equals(selectedRestaurant)) {
                Res_ID = i;
                break;
            }
        }

        for (int i = 0; i < foodList.size(); i++) {
            Food food = foodList.get(i);

            String name = food.getName().toLowerCase();
            if (key != null) {
                if (name.contains(key.toLowerCase())) temp.add(food);
                final ListView listView = (ListView) findViewById(R.id.listFood);
                listView.setAdapter(new CustomListFoodAdapter(this, temp));
            } else {
                final ListView listView = (ListView) findViewById(R.id.listFood);
                List<Food> listFood = foodList;
                listView.setAdapter(new CustomListFoodAdapter(this, listFood));
            }
        }
    }


    public void logoutActivity(View view) {
        Intent intent = new Intent(this, PopLogout.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void displayHome(View view) {
        displayHome();
    }

    public void displayInfo(View view) {
        Intent intent = new Intent(this, DisplayInfomation.class);
        startActivity(intent);
    }

    public void displayCart(View view) {
        Intent intent = new Intent(this, DisplayCart.class);
        startActivity(intent);
    }

    public void displayFoodDetail(View view) {
        String foodName = ((TextView) view.findViewById(R.id.foodName)).getText().toString();
        Intent intent = new Intent(this, DisplayFoodDetail.class);
        intent.putExtra("selectedRestaurant", selectedRestaurant);
        intent.putExtra("selectedFood", foodName);
        startActivity(intent);
    }


}
