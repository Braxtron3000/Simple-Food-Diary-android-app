package com.simple.foodDiary;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

//import android.widget.Spinner;

public class meal extends Activity implements java.io.Serializable {


    String mealType;

    int caloriesEaten = 0;
    int proteinPercentage = 0;
    int proteinNumber = 0;

    int carbPercentage = 0;
    int carbNumber = 0;

    int fatPercentage = 0;
    int fatNumber = 0;

    int[] caloricZone;
    int[] proteinZone;
    int[] carbZone;
    int[] fatZone;


    TextView calorieZoneDisplay;
    TextView proteinZoneDisplay;
    TextView carbZoneDisplay;
    TextView fatZoneDisplay;

    TextView puTFood;
    TextView puTamnt;
    TextView puTcalories;
    TextView puTprotein;
    TextView puTcarbs;
    TextView puTfat;
    ImageSwitcher iSwitcher_calories;
    Bundle foodList = new Bundle();
    ArrayList foodIds = new ArrayList();
    int foodCount = 0;
    private LinearLayout parentLinearLayout;
    //private ImageSwitcher iSwitcher_Fats;
    private ImageSwitcher iSwitcher_Protein;
    private ImageSwitcher iSwitcher_Carbs;
    private ImageSwitcher iSwitcher_Fats;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal);
        //createGrid();
        //Toast.makeText(this,getIntent().getStringExtra("meal_type"),Toast.LENGTH_LONG).show();

        setGoalZones();

        TextView b=findViewById(R.id.mealName);
        b.setText(getIntent().getStringExtra("meal_type"));


        parentLinearLayout = (LinearLayout) findViewById(R.id.newFoodLinearLayout);


        setZoneBalanceNotifiers();
        zoneImbalanceNotify();


        EditText yourEditText = (EditText) findViewById(R.id.EditText_food);
        yourEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    ((ListView)findViewById(R.id.suggestionBox)).setVisibility(View.INVISIBLE);
                }
                else if (hasFocus){
                    suggestFavorites(v);
                }
            }
        });
        yourEditText.addTextChangedListener(new TextWatcher() {



            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                suggestFavorites(findViewById(R.id.suggestionBox));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }


    private void createGrid(){
        TableLayout tableLayout = new TableLayout(meal.this);

        //tableLayout.addView((TextView)findViewById(R.id.amntPrompt));
        //View f=(TextView)findViewById(R.id.amntPrompt).getParent();


    }

    public void EndMeal(View view) {
        Intent intent = new Intent(meal.this, foodDiary.class);
        intent.putExtra("meal_type", mealType);
        intent.putExtra("proteinNumber", proteinNumber);
        intent.putExtra("carbNumber", carbNumber);
        intent.putExtra("fatNumber", fatNumber);
        intent.putExtra("caloriesEaten", caloriesEaten);
        intent.putExtra("foodList", foodList);
        intent.putExtra("foodIds", foodIds);

        setResult(RESULT_OK, intent);
        finish();
        //startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(meal.this, foodDiary.class);
        intent.putExtra("meal_type", mealType);
        intent.putExtra("proteinNumber", proteinNumber);
        intent.putExtra("carbNumber", carbNumber);
        intent.putExtra("fatNumber", fatNumber);
        intent.putExtra("caloriesEaten", caloriesEaten);
        intent.putExtra("foodList", foodList);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void createNewFood(View view) {

        EditText etFood = findViewById(R.id.EditText_food);
        EditText etAmnt = findViewById(R.id.EditText_Amnt);
        EditText etCalories = findViewById(R.id.EditText_calories);
        EditText etFat = findViewById(R.id.EditText_createFat);
        EditText etCarbs = findViewById(R.id.EditText_createCarbs);
        EditText etProtein = findViewById(R.id.EditText_createProtein);


        // if (!LeditText_var1.getText().toString().equals("null")&&!LeditText_var2.getText().toString().equals("null")&&!LeditText_var3.getText().toString().equals("null")&&!LeditText_var4.getText().toString().equals("null")&&!LeditText_var5.getText().toString().equals("null")&&!LeditText_var6.getText().toString().equals("null")) {
        if ((!TextUtils.isEmpty(etFood.getText().toString())) && (!TextUtils.isEmpty(etAmnt.getText().toString())) && (!TextUtils.isEmpty(etCalories.getText().toString())) && (!TextUtils.isEmpty(etFat.getText().toString())) && (!TextUtils.isEmpty(etCarbs.getText().toString())) && (!TextUtils.isEmpty(etProtein.getText().toString()))) {
            LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View rowView = inflator.inflate(R.layout.new_food_template, parentLinearLayout, false);
            rowView.setId(foodCount);

            int[] idArray= {R.id.puT_foodName,R.id.puT_calories,R.id.puT_amnt,R.id.puT_fat,R.id.puT_carbs,R.id.puT_protein};
            String[] etStuff= {etFood.getText().toString(),etCalories.getText().toString(),etAmnt.getText().toString(),etFat.getText().toString(),etCarbs.getText().toString(),etProtein.getText().toString()};
            int[] promptArray= {R.id.foodNamePrompt,R.id.caloriesPrompt,R.id.amntPrompt,R.id.fatPrompt,R.id.carbsPrompt,R.id.proteinPrompt};

            TextView Vtextview;
            for (int x=0; x<idArray.length;x++){

                        Vtextview = rowView.findViewById(idArray[x]);
                        Vtextview.setText(etStuff[x]);
                        Vtextview.setVisibility(View.VISIBLE);

            }


            int amount = Integer.parseInt(etAmnt.getText().toString());

            caloriesEaten += Integer.parseInt(etCalories.getText().toString()) * amount;
            fatNumber += Integer.parseInt(etFat.getText().toString()) * amount;
            carbNumber += Integer.parseInt(etCarbs.getText().toString()) * amount;
            proteinNumber += Integer.parseInt(etProtein.getText().toString()) * amount;

            updateTally();
            parentLinearLayout.addView(rowView, 0);//parentLinearLayout.getChildCount() - 1);
            zoneImbalanceNotify();

            //constraintSet.applyTo(bigConstraint);

            ArrayList<String> foodListLocal = new ArrayList<>();

            foodListLocal.add(etFood.getText().toString());
            foodListLocal.add(etCalories.getText().toString());
            foodListLocal.add(etAmnt.getText().toString());
            foodListLocal.add(etFat.getText().toString());
            foodListLocal.add(etCarbs.getText().toString());
            foodListLocal.add(etProtein.getText().toString());


            foodList.putStringArrayList("el" + foodCount, foodListLocal);
            foodIds.add("el" + foodCount);

            foodCount += 1;

            etFood.getText().clear();
            etAmnt.getText().clear();
            etCalories.getText().clear();
            etCarbs.getText().clear();
            etFat.getText().clear();
            etProtein.getText().clear();

            etFood.requestFocus();

        } else Toast.makeText(this, "you forgot something", Toast.LENGTH_SHORT).show();


    }


    public void deleteFood(View view) {



        ConstraintLayout templateConstraintLayout = findViewById(R.id.constraintLayout);//(ConstraintLayout) view.getParent();
        //LinearLayout templatelinearLayout1 = findViewById(R.id.constraintColumn1);//(LinearLayout) templateConstraintLayout.getChildAt(0);
        //LinearLayout templatelinearLayout2 = findViewById(R.id.column3);//(LinearLayout) templateConstraintLayout.getChildAt(2);
        TextView templateCalories = findViewById(R.id.puT_calories);//(TextView) templateConstraintLayout.getChildAt(1);


        puTFood = templateConstraintLayout.findViewById(R.id.puT_foodName);//(TextView)templateConstraintLayout.getChildAt(1); //(TextView) templatelinearLayout1.getChildAt(0);
        puTamnt = templateConstraintLayout.findViewById(R.id.puT_amnt);//(TextView)templateConstraintLayout.getChildAt(2); //(TextView) templatelinearLayout1.getChildAt(1);
        puTcalories = templateConstraintLayout.findViewById(R.id.puT_calories);//(TextView)templateConstraintLayout.getChildAt(3); //(TextView) templateCalories;
        puTprotein = templateConstraintLayout.findViewById(R.id.puT_protein);//(TextView)templateConstraintLayout.getChildAt(4); //(TextView) templatelinearLayout2.getChildAt(0);
        puTcarbs = templateConstraintLayout.findViewById(R.id.puT_carbs);//(TextView)templateConstraintLayout.getChildAt(5); //(TextView) templatelinearLayout2.getChildAt(1);
        puTfat = templateConstraintLayout.findViewById(R.id.puT_fat);//(TextView)templateConstraintLayout.getChildAt(6);//(TextView) templatelinearLayout2.getChildAt(2);

        int amount = Integer.parseInt(puTamnt.getText().toString());

        caloriesEaten -= Integer.parseInt(puTcalories.getText().toString()) * amount;
        proteinNumber -= Integer.parseInt(puTprotein.getText().toString()) * amount;
        carbNumber -= Integer.parseInt(puTcarbs.getText().toString()) * amount;
        fatNumber -= Integer.parseInt(puTfat.getText().toString()) * amount;

        View theFoodView = (View) view.getParent().getParent();
        /*int indexOfChild=((LinearLayout)findViewById(R.id.newFoodLinearLayout)).indexOfChild((View)view.getParent().getParent());
        toast("size "+foodList.size()+" child: "+indexOfChild);
        del_foodList.add("el"+indexOfChild);*/
        //del_foodList.putString("el"+indexOfChild,"el"+indexOfChild);


        foodList.remove("el" + theFoodView.getId());
        foodIds.remove("el" + theFoodView.getId());

        updateTally();
        parentLinearLayout.removeView((View) view.getParent().getParent());
        zoneImbalanceNotify();


    }


    public void addToFavorites(View view) {
        view.setBackgroundResource(R.drawable.ic_star_black_24dp);

        ConstraintLayout templateConstraintLayout = (ConstraintLayout)view.getParent();//findViewById(R.id.constraintLayout);//(ConstraintLayout) view.getParent();
        //LinearLayout templatelinearLayout1 = findViewById(R.id.constraintColumn1);//(LinearLayout) templateConstraintLayout.getChildAt(0);
        //LinearLayout templatelinearLayout2 = findViewById(R.id.column3);//(LinearLayout) templateConstraintLayout.getChildAt(2);
        TextView templateCalories = findViewById(R.id.puT_calories);//(TextView) templateConstraintLayout.getChildAt(1);


        String food = ((TextView)templateConstraintLayout.getChildAt(1)).getText().toString();//((TextView) templatelinearLayout1.getChildAt(0)).getText().toString();
        //String amnt = ((TextView) templatelinearLayout1.getChildAt(1)).getText().toString();
        String calories = ((TextView) templateCalories).getText().toString();
        String protein = ((TextView) templateConstraintLayout.getChildAt(4)).getText().toString();
        String carbs = ((TextView) templateConstraintLayout.getChildAt(5)).getText().toString();
        String fat = ((TextView) templateConstraintLayout.getChildAt(6)).getText().toString();

        FoodModel foodModel = new FoodModel(-1, food, Integer.parseInt(calories), Integer.parseInt(fat), Integer.parseInt(carbs), Integer.parseInt(protein));

        databaseHelper dataBaseHelper = new databaseHelper(meal.this);

        String result = dataBaseHelper.addOne(foodModel);

        toast(result);
    }


    public void updateTally() {
        proteinPercentage = calculate_Running_MacroPercentage(proteinNumber);
        carbPercentage = calculate_Running_MacroPercentage(carbNumber);
        fatPercentage = calculate_Running_MacroPercentage(fatNumber);

        calorieZoneDisplay = findViewById(R.id.PuFd_RunningTotalCalories);
        proteinZoneDisplay = findViewById(R.id.PuFd_RunningTotalProtein);
        carbZoneDisplay = findViewById(R.id.PuFd_RunningTotalCarbs);
        fatZoneDisplay = findViewById(R.id.PuFd_RunningTotalFat);

        String czd;
        int[] newcaloricZone = new int[2];
        if (!(mealType.equals("snack"))) {
            newcaloricZone[0] = (int) (caloricZone[0] / loadnumMeals());
            newcaloricZone[1] = (int) (caloricZone[1] / loadnumMeals());
            //toast("loadnumMeals = "+loadnumMeals());
            czd = caloriesEaten + "/(" + newcaloricZone[0] + "-" + newcaloricZone[1] + ")";
        }
        else czd = caloriesEaten + "/(" + caloricZone[0] + "-" + caloricZone[1] + ")";

        String pzd = proteinPercentage + "% /(" + proteinZone[0] + "-" + proteinZone[1] + ")";
        String c2zd = carbPercentage + "% /(" + carbZone[0] + "-" + carbZone[1] + ")";
        String fzd = fatPercentage + "% /(" + fatZone[0] + "-" + fatZone[1] + ")";


        calorieZoneDisplay.setText(czd);
        proteinZoneDisplay.setText(pzd);
        carbZoneDisplay.setText(c2zd);
        fatZoneDisplay.setText(fzd);

    }

    int calculate_Running_MacroPercentage(int number) {
        int totalMacros = proteinNumber + carbNumber + fatNumber;
        int percent = (int) (((double) number / (double) totalMacros) * 100);
        return percent;
    }


    public void setGoalZones() {

        Intent intent = getIntent();
        calorieZoneDisplay = findViewById(R.id.PuFd_RunningTotalCalories);
        proteinZoneDisplay = findViewById(R.id.PuFd_RunningTotalProtein);
        carbZoneDisplay = findViewById(R.id.PuFd_RunningTotalCarbs);
        fatZoneDisplay = findViewById(R.id.PuFd_RunningTotalFat);
        Bundle bundle = getIntent().getExtras();

        caloricZone = bundle.getIntArray("calorie_zone");
        proteinZone = bundle.getIntArray("protein_zone");
        carbZone = bundle.getIntArray("carb_zone");
        fatZone = bundle.getIntArray("fat_zone");
        mealType = bundle.getString("meal_type");


        String czd = "";
        String pzd = "";
        String c2zd = "";
        String fzd = "";
        int[] newcaloricZone = new int[2];

        if (!(mealType.equals("snack"))) {
            newcaloricZone[0] = (int) (caloricZone[0] / loadnumMeals());
            newcaloricZone[1] = (int) (caloricZone[1] / loadnumMeals());
            //toast("loadnumMeals = "+loadnumMeals());
            czd = caloriesEaten + "/(" + newcaloricZone[0] + "-" + newcaloricZone[1] + ")";
        } else {
            czd = caloriesEaten + "/(" + caloricZone[0] + "-" + caloricZone[1] + ")";
        }

        pzd = proteinPercentage + "% /(" + proteinZone[0] + "-" + proteinZone[1] + ")";
        c2zd = carbPercentage + "% /(" + carbZone[0] + "-" + carbZone[1] + ")";
        fzd = fatPercentage + "% /(" + fatZone[0] + "-" + fatZone[1] + ")";


        calorieZoneDisplay.setText(czd);
        proteinZoneDisplay.setText(pzd);
        carbZoneDisplay.setText(c2zd);
        fatZoneDisplay.setText(fzd);


    }

    public int loadnumMeals() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt("value2", -1000);
    }

    public void setZoneBalanceNotifiers() {
        iSwitcher_calories = (ImageSwitcher) findViewById(R.id.iSwitcher_calories);
        iSwitcher_Protein = (ImageSwitcher) findViewById(R.id.iSwitcher_Protein);
        iSwitcher_Carbs = (ImageSwitcher) findViewById(R.id.iSwitcher_Carbs);
        iSwitcher_Fats = (ImageSwitcher) findViewById(R.id.iSwitcher_Fat);

        iSwitcher_Protein.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView((getApplicationContext()));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return imageView;

            }
        });
        iSwitcher_Carbs.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView((getApplicationContext()));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return imageView;

            }
        });
        iSwitcher_Fats.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView((getApplicationContext()));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return imageView;

            }
        });

    }

    public void zoneImbalanceNotify() {

        int proteinDifMinor = proteinPercentage - proteinZone[0];


        if (proteinPercentage != 0) {
            //too few calories
            if (proteinPercentage < proteinZone[0]) {

                if (proteinDifMinor <= -10) {
                    Toast.makeText(this, "protein is way too few!", Toast.LENGTH_SHORT).show();
                    iSwitcher_Protein.setImageResource(R.drawable.ic_arrow_upward_red_24dp);
                } else if (proteinDifMinor > -10) {
                    Toast.makeText(this, "protein is a little few!", Toast.LENGTH_SHORT).show();
                    iSwitcher_Protein.setImageResource(R.drawable.ic_arrow_upward_orange_24dp);
                }
            }

            //just right, in zone
            if ((proteinPercentage >= proteinZone[0]) && (proteinPercentage <= proteinZone[1])) {

                iSwitcher_Protein.setImageResource(R.drawable.ic_arrow_inzone_green_24dp);
            }


            int proteinDifMajor = proteinPercentage - proteinZone[1];

            //too many calories
            if (proteinPercentage > proteinZone[1]) {

                if (proteinDifMajor <= 10) {
                    //Toast.makeText(this, "a little too much protein", Toast.LENGTH_SHORT).show();
                    iSwitcher_Protein.setImageResource(R.drawable.ic_arrow_downward_orange_24dp);
                } else if (proteinDifMajor > 10) {
                    //Toast.makeText(this, "way too much protein!", Toast.LENGTH_SHORT).show();
                    iSwitcher_Protein.setImageResource(R.drawable.ic_arrow_downward_red_24dp);

                }

            }
        }


        //CARBS!!
        if (carbPercentage != 0) {
            int carbDifMinor = carbPercentage - carbZone[0];


            //too few calories
            if (carbPercentage < carbZone[0]) {

                if (carbDifMinor <= -10) {
                    //Toast.makeText(this, "protein is way too few!", Toast.LENGTH_SHORT).show();
                    iSwitcher_Carbs.setImageResource(R.drawable.ic_arrow_upward_red_24dp);
                } else if (carbDifMinor > -10) {
                    //Toast.makeText(this, "protein is a little few!", Toast.LENGTH_SHORT).show();
                    iSwitcher_Carbs.setImageResource(R.drawable.ic_arrow_upward_orange_24dp);
                }
            }

            //just right, in zone
            if ((carbPercentage >= carbZone[0]) && (carbPercentage <= carbZone[1])) {
                iSwitcher_Carbs.setImageResource(R.drawable.ic_arrow_inzone_green_24dp);
            }


            int carbDifMajor = carbPercentage - carbZone[1];

            //too many calories
            if (carbPercentage > carbZone[1]) {

                if (carbDifMajor <= 10) {
                    //Toast.makeText(this, "a little too much protein", Toast.LENGTH_SHORT).show();
                    iSwitcher_Carbs.setImageResource(R.drawable.ic_arrow_downward_orange_24dp);
                } else if (carbDifMajor > 10) {
                    //Toast.makeText(this, "way too much protein!", Toast.LENGTH_SHORT).show();
                    iSwitcher_Carbs.setImageResource(R.drawable.ic_arrow_downward_red_24dp);

                }

            }
        }

        //FATS!!
        if (fatPercentage != 0) {

            int fatDifMinor = fatPercentage - fatZone[0];


            //too few calories
            if (fatPercentage < fatZone[0]) {

                if (fatDifMinor <= -10) {
                    //Toast.makeText(this, "protein is way too few!", Toast.LENGTH_SHORT).show();
                    iSwitcher_Fats.setImageResource(R.drawable.ic_arrow_upward_red_24dp);
                } else if (fatDifMinor > -10) {
                    //Toast.makeText(this, "protein is a little few!", Toast.LENGTH_SHORT).show();
                    iSwitcher_Fats.setImageResource(R.drawable.ic_arrow_upward_orange_24dp);
                }
            }

            //just right, in zone
            if ((fatPercentage >= fatZone[0]) && (fatPercentage <= fatZone[1])) {

                iSwitcher_Fats.setImageResource(R.drawable.ic_arrow_inzone_green_24dp);
            }


            int fatDifMajor = fatPercentage - fatZone[1];

            //too many calories
            if (fatPercentage > fatZone[1]) {

                if (fatDifMajor <= 10) {
                    //Toast.makeText(this, "a little too much protein", Toast.LENGTH_SHORT).show();
                    iSwitcher_Fats.setImageResource(R.drawable.ic_arrow_downward_orange_24dp);
                } else if (fatDifMajor > 10) {
                    //Toast.makeText(this, "way too much protein!", Toast.LENGTH_SHORT).show();
                    iSwitcher_Fats.setImageResource(R.drawable.ic_arrow_downward_red_24dp);

                }

            }
        }


    }


    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }




    public void suggestFavorites(View view) {


        final databaseHelper dataBaseHelper = new databaseHelper(meal.this);

        List<String> favfoodsList = dataBaseHelper.getAllFood();
        if (((EditText)findViewById(R.id.EditText_food)).getText().toString().equals(""))favfoodsList = dataBaseHelper.getAllFood();
        else {favfoodsList=dataBaseHelper.getFoodBySearch(((EditText)findViewById(R.id.EditText_food)).getText().toString());} //Toast.makeText(this,"foodBySearch",Toast.LENGTH_SHORT).show();}

        final myAdapter favfoodsArrayAdapter = new myAdapter(meal.this, favfoodsList);

        final ListView suggestionBox = findViewById(R.id.suggestionBox);



        if (favfoodsList.size()!=0) {

            suggestionBox.setVisibility(View.VISIBLE);
            suggestionBox.bringToFront();
            suggestionBox.setAdapter(favfoodsArrayAdapter);
            /*suggestionBox.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    //TextView itemFoodName=((View)parent.getItemAtPosition(position)).findViewById(R.id.textview_adapterItem);
                    //favfoodsArrayAdapter.getView(position,parent.getSelectedView(),parent);
                    String foodName=((TextView)view.findViewById(R.id.textview_adapterItem)).getText().toString();
                    //String itemText = itemFoodName.getText().toString();
                    dataBaseHelper.deleteOne(foodName);
                    suggestFavorites(findViewById(R.id.EditText_food));
                    //showFoodOnListView(dataBaseHelper);
                    toast("id = "+foodName);
                }
            });*/
        }else suggestionBox.setVisibility(View.INVISIBLE);
    }

    private void showFoodOnListView(databaseHelper databaseHelper){
        ArrayAdapter foodModelArrayAdapter  = new ArrayAdapter<String>(meal.this,R.layout.adapter_item,databaseHelper.getAllFood());
        ((ListView)findViewById(R.id.suggestionBox)).setAdapter(foodModelArrayAdapter);
    }

    public void getSuggestion (View view){

        EditText etFood = findViewById(R.id.EditText_food);
        EditText etAmnt = findViewById(R.id.EditText_Amnt);
        EditText etCalories = findViewById(R.id.EditText_calories);
        EditText etFat = findViewById(R.id.EditText_createFat);
        EditText etCarbs = findViewById(R.id.EditText_createCarbs);
        EditText etProtein = findViewById(R.id.EditText_createProtein);


        final databaseHelper dataBaseHelper = new databaseHelper(meal.this);
        LinearLayout linearLayout= (LinearLayout) view.getParent();


        String foodName=((TextView) linearLayout.getChildAt(0)).getText().toString();
        FoodModel theFood =dataBaseHelper.getFoodModel(foodName).get(0);

        etFood.setText(theFood.getName(),TextView.BufferType.EDITABLE);
        etCalories.setText(Integer.toString(theFood.getCalories()),TextView.BufferType.EDITABLE);
        etFat.setText(Integer.toString(theFood.getFat()),TextView.BufferType.EDITABLE);
        etCarbs.setText(Integer.toString(theFood.getCarbs()),TextView.BufferType.EDITABLE);
        etProtein.setText(Integer.toString(theFood.getProtein()),TextView.BufferType.EDITABLE);
        etAmnt.requestFocus();


        suggestFavorites(findViewById(R.id.EditText_food));
    }

    public void deleteFavorite(View view){
        final databaseHelper dataBaseHelper = new databaseHelper(meal.this);
        LinearLayout linearLayout= (LinearLayout) view.getParent();
        String foodName=((TextView) linearLayout.getChildAt(0)).getText().toString();
        dataBaseHelper.deleteOne(foodName);
        suggestFavorites(findViewById(R.id.EditText_food));
        toast(foodName+" deleted from favorites");
    }

}

