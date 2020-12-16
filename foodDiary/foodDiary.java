package com.simple.foodDiary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


//implements OnItemSelectedListener
public class foodDiary extends AppCompatActivity {

    int tdee;
    String fitnessGoal;

    int caloriesEaten;
    int proteinNumber;
    int carbNumber;
    int fatNumber;


    int mealsEaten;

    int proteinPercentage = 0;
    int carbPercentage = 0;
    int fatPercentage = 0;

    TextView calorieZoneDisplay;
    TextView proteinZoneDisplay;
    TextView carbZoneDisplay;
    TextView fatZoneDisplay;

    int[] totalcaloricZone = new int[2];
    int[] proteinZone = new int[2];
    int[] carbZone = new int[2];
    int[] fatZone = new int[2];

    Gson gson = new Gson();




    //final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);//getSharedPreferences("USER",MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_diary);
        getSupportActionBar().hide();

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

        mealsEaten=sharedPreferences.getInt("mealsEaten",0);
        mainStuff();
        if (mealsEaten!=0) uploadMeals();
    }


    protected void mainStuff() {
        fitnessGoal = loadFitnessGoal2("fitnessGoalString");
        tdee = loadTdee();


        getSavedMacStuff();
        if (caloriesEaten==0&&proteinNumber==0&&carbNumber==0&&fatNumber==0){
            //toast("saving a new start date");
            saveStartedDate();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            String todaysDate= DateFormat.getDateInstance().format(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            String startedDate=PreferenceManager.getDefaultSharedPreferences(this).getString("startedDate","null");
            if (!todaysDate.equals(startedDate)) {
                caloriesEaten=0;
                proteinNumber=0;
                carbNumber=0;
                fatNumber=0;

                mealsEaten=0;
                SharedPreferences.Editor shpf = PreferenceManager.getDefaultSharedPreferences(this).edit();
                shpf.putInt("mealsEaten",mealsEaten);
                shpf.apply();
            }
        }

        else Toast.makeText(this,"Build Version problems",Toast.LENGTH_LONG).show();


        caloriesEaten+=getNewMealNutriData[0];
        proteinNumber+=getNewMealNutriData[1];
        carbNumber+=getNewMealNutriData[2];
        fatNumber+=getNewMealNutriData[3];
        saveMacStuff();
        setGoalZones();

       Boolean isAssigned_fitnessGoal=false;
        for (String activityLvl: getResources().getStringArray(R.array.RadioGroup_activityLvls))if (fitnessGoal.equals(activityLvl))isAssigned_fitnessGoal=true;
        if (tdee<0||isAssigned_fitnessGoal) {startActivity(new Intent(this, infodata.class));}else {}


    }

    private void uploadMeals() {


        /*int buff=0;
        try {*/


            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            mealsEaten = sharedPreferences.getInt("mealsEaten", 0);

            for (int meal = 0; meal < mealsEaten; meal++) {
                String json = sharedPreferences.getString("parameter3_meal" + meal, "");
                //if (!json.isEmpty()) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<String[]>>() {}.getType();
                ArrayList<String[]> foods = gson.fromJson(json, type);
                createNewMeal(sharedPreferences.getString("parameter1_meal" + meal, ""), sharedPreferences.getInt("parameter2_meal" + meal, 0), foods);
            }

        //}catch (Exception e){toast("gay shit happened me= "+mealsEaten);}
    }

    private void saveStartedDate() {

        String strDate = "megaPOOP";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

            DateFormat dateFormat = DateFormat.getDateInstance();
            strDate = dateFormat.format(date);
        }


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("startedDate", strDate);
        editor.commit();
    }


    private void saveMacStuff() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("caloriesEaten", caloriesEaten);
        editor.putInt("proteinNumber", proteinNumber);
        editor.putInt("carbNumber", carbNumber);
        editor.putInt("fatNumber", fatNumber);

        editor.commit();

    }

    private void getSavedMacStuff() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        caloriesEaten = prefs.getInt("caloriesEaten", 0);
        proteinNumber = prefs.getInt("proteinNumber", 0);
        carbNumber = prefs.getInt("carbNumber", 0);
        fatNumber = prefs.getInt("fatNumber", 0);

    }



    /*private int[] getNewMealNutriData() {
        Intent intent=getIntent();
        int[] nutriData=new int[4];


        nutriData[0]=intent.getIntExtra("caloriesEaten",0);
        nutriData[1]=intent.getIntExtra("proteinNumber",0);
        nutriData[2]=intent.getIntExtra("carbNumber",0);
        nutriData[3]=intent.getIntExtra("fatNumber",0);

        return nutriData;
    }*/

    private int macroPercent(int macroNumber1, int macroNumber2, int macroNumber3) {

        int total = macroNumber1 + macroNumber2 + macroNumber3;
        if (total == 0) {
            return 0;
        } else {
            double div = macroNumber1 / total;
            int division = (int) (((double) macroNumber1) / total * 100);
            return division;
        }
    }


    //BIBLIOGRAPHY! macro diets for goals
    public void setGoalZones() {

        Intent intent = getIntent();
        intent.getExtras();

        calorieZoneDisplay = findViewById(R.id.RunningTotalCalories);
        proteinZoneDisplay = findViewById(R.id.RunningTotalProtein);
        carbZoneDisplay = findViewById(R.id.RunningTotalCarbs);
        fatZoneDisplay = findViewById(R.id.RunningTotalFat);


        /*int[]*/
        totalcaloricZone = new int[2];
        /*int[]*/
        proteinZone = new int[2];
        /*int[]*/
        carbZone = new int[2];
        /*int[]*/
        fatZone = new int[2];


        switch (fitnessGoal) {
            case "cut":
                totalcaloricZone[0] = tdee - 600;
                totalcaloricZone[1] = tdee - 300;
                proteinZone[0] = 40;
                proteinZone[1] = 50;
                carbZone[0] = 10;
                carbZone[1] = 30;
                fatZone[0] = 30;
                fatZone[1] = 40;
                break;
            case "maintain weight":
                totalcaloricZone[0] = tdee - 50;
                totalcaloricZone[1] = tdee + 50;
                proteinZone[0] = 25;
                proteinZone[1] = 35;
                carbZone[0] = 30;
                carbZone[1] = 50;
                fatZone[0] = 25;
                fatZone[1] = 35;
                break;
            case "bulk":
                totalcaloricZone[0] = tdee + 400;
                totalcaloricZone[1] = tdee + 600;
                proteinZone[0] = 25;
                proteinZone[1] = 35;
                carbZone[0] = 40;
                carbZone[1] = 60;
                fatZone[0] = 15;
                fatZone[1] = 25;

                break;
            default:
                //toast("FITGOAL SWITCH'S DEFAULT FOR SETZONES METHOD!");
        }


        String czd = caloriesEaten + "/(" + totalcaloricZone[0] + "-" + totalcaloricZone[1] + ")";
        calorieZoneDisplay.setText(czd);

        String pzd = macroPercent(proteinNumber, carbNumber, fatNumber) + "% /(" + proteinZone[0] + "-" + proteinZone[1] + ")";
        proteinZoneDisplay.setText(pzd);

        String c2zd = macroPercent(carbNumber, proteinNumber, fatNumber) + "% /(" + carbZone[0] + "-" + carbZone[1] + ")";
        carbZoneDisplay.setText(c2zd);

        String fzd = macroPercent(fatNumber, proteinNumber, carbNumber) + "% /(" + fatZone[0] + "-" + fatZone[1] + ")";
        fatZoneDisplay.setText(fzd);
    }

    public static final int REQUEST_CODE = 1;

    public void startNewMeal(View view) {
        //new Meal button function
        //Button b = findViewById(R.id.fd_createMealButton);
        //b.setOnClickListener(new View.OnClickListener() {

        Intent intent = new Intent(foodDiary.this, meal.class);
        intent.putExtra("meal_type", getMealType());
        intent.putExtra("calorie_zone", totalcaloricZone);
        intent.putExtra("protein_zone", proteinZone);
        intent.putExtra("carb_zone", carbZone);
        intent.putExtra("fat_zone", fatZone);
        startActivityForResult(intent, REQUEST_CODE);
        //}
        //});

    }


    int[] getNewMealNutriData = {0, 0, 0, 0};
    Bundle foodList;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Gson gson = new Gson();
        super.onActivityResult(requestCode, resultCode, data);

        Bundle intent = data.getExtras();
        foodList = intent.getBundle("foodList");
        ArrayList<String> foodIds = intent.getStringArrayList("foodIds");
        ArrayList<String[]> foods = new ArrayList<>();



        if (foodList != null && foodList.size() != 0) {

            for (int x = 0; x < foodList.size(); x++) {

                ArrayList<String> food = foodList.getStringArrayList(foodIds.get(x));

                String[] newFood = Arrays.copyOf(food.toArray(), food.toArray().length, String[].class);

                foods.add(newFood);

            }


            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {


                if (foodList.size() != 0) {
                    createNewMeal(intent.getString("meal_type"), intent.getInt("caloriesEaten"), foods);


                    SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this).edit();

                    sharedPreferences.putInt("mealsEaten", mealsEaten);
                    sharedPreferences.putString("parameter1_meal" + mealsEaten, intent.getString("meal_type"));
                    sharedPreferences.putInt("parameter2_meal" + mealsEaten, intent.getInt("caloriesEaten"));

            /*Set <String[]> strSet = new HashSet<>();
            for (int x=0;x<foods.size();x++)strSet.add(foods.get(x));
            //Collections.addAll(foods);*/

                    //JsonArray jsonArray = gson.toJsonTree(foods).getAsJsonArray();

                    String json = gson.toJson(foods);
                    sharedPreferences.putString("parameter3_meal"+mealsEaten,json);
                    mealsEaten+=1;
                    sharedPreferences.putInt("mealsEaten",mealsEaten);
                    sharedPreferences.apply();
                }
            }
        }

        getNewMealNutriData[0] = intent.getInt("caloriesEaten", 0);
        getNewMealNutriData[1] = intent.getInt("proteinNumber", 0);
        getNewMealNutriData[2] = intent.getInt("carbNumber", 0);
        getNewMealNutriData[3] = intent.getInt("fatNumber", 0);
        mainStuff();
    }


    public void createNewMeal(String mealName, int caloriesEaten, ArrayList<String[]> foods) {

        //HorizontalScrollView scrollView = findViewById(R.id.mealsScrollBar);
        LinearLayout chewy = findViewById(R.id.mealsScrollBar_linearLayout);//(LinearLayout)scrollView.getChildAt(0);
        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View mealView = inflator.inflate(R.layout.new_meal_template, chewy, false);


        //CREATE FOODS
        LinearLayout chewys_ScrollBar = mealView.findViewById(R.id.newMeal_scrollview_ll);
        LayoutInflater inflator2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        for (int x = 0; x < foods.size(); x++) {
            //go through the foods list (ArrayList String[])

            final View newFood = inflator2.inflate(R.layout.new_food_template, chewys_ScrollBar, false);
            TextView dan = newFood.findViewById(R.id.puT_foodName);
            dan.setText(foods.get(x)[0]);


            ConstraintLayout cl = newFood.findViewById(R.id.constraintLayout);


            //lazy coding
            dan = newFood.findViewById(R.id.puT_amnt);
            dan.setText(foods.get(x)[1]);

            dan = newFood.findViewById(R.id.puT_calories);
            dan.setText(foods.get(x)[2]);

            dan = newFood.findViewById(R.id.puT_protein);
            dan.setText(foods.get(x)[3]);

            dan = newFood.findViewById(R.id.puT_carbs);
            dan.setText(foods.get(x)[4]);


            dan = newFood.findViewById(R.id.puT_fat);
            dan.setText(foods.get(x)[5]);

            View deleteButton = newFood.findViewById(R.id.deleteButton);
            View favoriteButton = newFood.findViewById(R.id.favoriteButton);
            cl.removeView(deleteButton);
            cl.removeView(favoriteButton);

            chewys_ScrollBar.addView(newFood, chewys_ScrollBar.getChildCount() - 1);
        }


        TextView meal_textview = mealView.findViewById(R.id.MealName);
        meal_textview.setText(mealName);


        ConstraintLayout promptconstraints_meal = mealView.findViewById(R.id.promptconstraints_meal);
        CardView dynamicCardview=mealView.findViewById(R.id.dynamicCardview);
        dynamicCardview.setMinimumWidth(promptconstraints_meal.getWidth());


        TextView calorieSum_textview = mealView.findViewById(R.id.NewMealTotalCalories);
        calorieSum_textview.setText(Integer.toString(caloriesEaten));

        chewy.addView(mealView, chewy.getChildCount() - 1);

    }




    public String getMealType() {
        Spinner spinner = (Spinner) findViewById(R.id.mealTypeSpinner);
        String selectedMealType = spinner.getSelectedItem().toString();
        return selectedMealType;
    }

    public int loadnumMeals() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt("value2", -1000);
    }

    public int loadTdee() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt("tdee", -1000);
    }

    public String loadFitnessGoal2(String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(value, "fitnessGoalString");
    }

    public void toProfile(View view) {
        Intent FDTopfile_intent = new Intent(this, infodata.class);
        startActivity(FDTopfile_intent);
    }



}