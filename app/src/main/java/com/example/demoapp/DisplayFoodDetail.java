package com.example.demoapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demoapp.Data.Food;

public class DisplayFoodDetail extends AppCompatActivity {

    private Scene foodDetailScene;

    private String resName;
    private String foodName;

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
        foodDetailScene = Scene.getSceneForLayout(root, R.layout.description_of_food, this);

        Intent intent = getIntent();
        resName = intent.getStringExtra("selectedRestaurant");
        foodName = intent.getStringExtra("selectedFood");

        displayFoodDetail();
    }

    Food selectedFood = null;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void displayFoodDetail(){
        Transition slide = new Slide(Gravity.RIGHT);
        TransitionManager.go(foodDetailScene, slide);

        TextView name_of_food = (TextView)findViewById(R.id.name_of_food);
        name_of_food.setText(foodName);

        for(int i = 0; i < DisplayHome.foodList.size(); i++) {
            if(DisplayHome.foodList.get(i).getName().equals(foodName)) {
                selectedFood = DisplayHome.foodList.get(i);
            }
        }

        TextView rating = (TextView)findViewById(R.id.rating_of_food); rating.setText(selectedFood.getRating());
        TextView price = (TextView)findViewById(R.id.price_of_food); price.setText(selectedFood.getPrice());
        TextView quantity = (TextView)findViewById(R.id.remain_of_food); quantity.setText(Integer.toString(selectedFood.getQuantity()));
        TextView description = (TextView)findViewById(R.id.des_of_food); description.setText(selectedFood.getDescription());
        ImageView logo = (ImageView)findViewById(R.id.logo_of_food);
        int id = getMipmapResIdByName(selectedFood.getLogo()); logo.setImageResource(id);
    }

    private Context context = this;
    // Find Image ID corresponding to the name of the image (in the directory mipmap).
    public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();
        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        return resID;
    }


    public void upNumber(View view){
        TextView numberTxt = (TextView)findViewById(R.id.quantity_of_food);
        Integer number = Integer.valueOf(numberTxt.getText().toString()) + 1;
        if (number > selectedFood.getQuantity()) number = selectedFood.getQuantity();
        if (number >= 10) numberTxt.setText(String.valueOf(number));
        else numberTxt.setText("0"+String.valueOf(number));
    }

    public void downNumber(View view){
        TextView numberTxt = (TextView)findViewById(R.id.quantity_of_food);
        Integer number = Integer.valueOf(numberTxt.getText().toString()) - 1;
        if (number < 1) number = 1;
        if (number >= 10) numberTxt.setText(String.valueOf(number));
        else numberTxt.setText("0"+String.valueOf(number));
    }

    public void list_food_interface(View view){
        finish();
    }

    public void add_to_cart(View view){
        Bill bill = DisplayCart.billManager.getBill();
        Log.d("OrderID", DisplayHome.num_of_order+"");
        int OrderID = DisplayHome.num_of_order + 1;
        int customerID = DisplayHome.customerID;
        int restaurantID = DisplayHome.restaurantID;
        String itemName = ((TextView)findViewById(R.id.name_of_food)).getText().toString();
        String itemPrice = ((TextView)findViewById(R.id.price_of_food)).getText().toString();
        String itemQuantity = ((TextView)findViewById(R.id.quantity_of_food)).getText().toString();
        String itemNote = ((TextView)findViewById(R.id.note_of_food)).getText().toString();
        String itemStatus = "unconfirmed";

        int index = getItemIndex(itemName);

        if(index == -1){
            bill.addNewItem(new BillItem(OrderID, customerID, restaurantID, selectedFood.getID(), itemName, itemQuantity, itemNote, itemPrice, itemStatus, ""));
        }
        else{
            BillItem item = bill.billItemList.get(index);
            int number = Integer.valueOf(item.getQuantity()) + Integer.valueOf(itemQuantity);
            if(number < 9) itemQuantity = "0" + String.valueOf(number);
            else itemQuantity = String.valueOf(number);

            item.setQuantity(itemQuantity); //Update item quantity
            if(itemNote != null) item.setDescription(itemNote); //Update note
        }
        //Update selected quantity after add to cart
        selectedFood.setQuantity(selectedFood.getQuantity() - Integer.valueOf(itemQuantity));

        //finish current activity
        finish();
    }

    private int getItemIndex(String name){
        for(int i = 0; i< DisplayCart.billManager.getBill().billItemList.size(); i++){
            if(DisplayCart.billManager.getBill().billItemList.get(i).getName().equals(name)){
                return i;
            }
        }
        return -1;
    }
}
