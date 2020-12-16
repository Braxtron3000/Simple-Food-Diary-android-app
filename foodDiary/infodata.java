package com.simple.foodDiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class infodata extends AppCompatActivity {


    public static final String FILE_NAME_fitnessGoal = "fitnessGoal.txt";

    public static final String FILE_NAME_numMeals = "numMeals";

    public static final String FILE_NAME_tdee = "tdee.txt";

    private int bmr = 0;
    private int tdee = 0;

    //public TextView showBmr;
    public TextView showTDEE;

    //physical
    RadioGroup sexChoices;
    RadioButton selectedSex;

    RadioGroup actLvlChoices;
    RadioButton selectedActivityLvl;

    RadioGroup fitnessGoalChoices;
    RadioButton selectedFitnessGoal;

    //syncing views to java from the xml file -- all edit text
    private EditText name; //= findViewById(R.id.editText_name);
    private EditText age; //= findViewById(R.id.editText_age);
    private EditText weight; //= findViewById(R.id.editText_weight);
    private EditText feet; //= findViewById(R.id.editText_feet);
    private EditText inches; //= findViewById(R.id.editText_inches);
    private EditText numMeals; //= findViewById(R.id.EditText_mealNumber);

    //create strings for selected radio buttons
    private String nameString; //= name.getText().toString();
    private String sexString; //= findViewById(sexChoices.getCheckedRadioButtonId()).toString();
    private String actLvlString; //= findViewById(actLvlChoices.getCheckedRadioButtonId()).toString();
    private String fitnessGoalString; //= findViewById(fitnessGoalChoices.getCheckedRadioButtonId()).toString();

    //create ints for number editText Views
    private int ageInt;//= Integer.parseInt(age.getText().toString());
    private int weightInt;//= Integer.parseInt(weight.getText().toString());
    private int feetInt;//= Integer.parseInt(feet.getText().toString());
    private int inchesInt;//= Integer.parseInt(inches.getText().toString());
    private int numMealsInt;//= Integer.parseInt(numMeals.getText().toString());

    private boolean isPushed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infodata);
        //gets rid of title screen
        getSupportActionBar().hide();

        showTDEE = (TextView) findViewById(R.id.TextView_TDEEnumber);



        showTDEE.setText(Integer.toString(getInt("tdee")));
        ((TextView) findViewById(R.id.TextView_calorieZoneNumber)).setText(getString("zonenumber"," -TBD- "));


        //name = findViewById(R.id.editText_name);
        age = findViewById(R.id.editText_age);
        weight = findViewById(R.id.editText_weight);
        feet = findViewById(R.id.editText_feet);
        inches = findViewById(R.id.editText_inches);
        numMeals = findViewById(R.id.EditText_mealNumber);


        //radio button syncage shit
        sexChoices = findViewById(R.id.radioGroup_sex);

        actLvlChoices = findViewById(R.id.radioGroup_activityLvls);

        fitnessGoalChoices = findViewById(R.id.RadioGroup_fitnessGoals);

        Button buttonApply = findViewById(R.id.Button_calculateBmr);



        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isFilledOut()) {
                    isPushed = true;


                    int sexRadioId = sexChoices.getCheckedRadioButtonId();
                    selectedSex = findViewById(sexRadioId);
                    int actlvlRadioId = actLvlChoices.getCheckedRadioButtonId();
                    selectedActivityLvl = findViewById(actlvlRadioId);
                    int fitnessGoalRadioId = fitnessGoalChoices.getCheckedRadioButtonId();
                    selectedFitnessGoal = findViewById(fitnessGoalRadioId);


                    //stays within Onclick so that it's asigned when it's clicked
                    sexString = selectedSex.getText().toString();
                    actLvlString = selectedActivityLvl.getText().toString();
                    fitnessGoalString = selectedFitnessGoal.getText().toString();
                    //nameString = name.getText().toString();


                    ageInt = Integer.parseInt(age.getText().toString());
                    weightInt = Integer.parseInt(weight.getText().toString());
                    feetInt = Integer.parseInt(feet.getText().toString());
                    inchesInt = Integer.parseInt(inches.getText().toString());
                    numMealsInt = Integer.parseInt(numMeals.getText().toString());


                    calculateBMR();
                }
            }

        });
    }

    public boolean isFilledOut() {
        if (
                sexChoices.getCheckedRadioButtonId() == -1 ||
                        actLvlChoices.getCheckedRadioButtonId() == -1 ||
                        fitnessGoalChoices.getCheckedRadioButtonId() == -1 ||
                        age.getText().toString().equals("")||
                        weight.getText().toString().equals("")||
                        feet.getText().toString().equals("")||
                        inches.getText().toString().equals("")|| numMeals.getText().toString().equals("")
        ) return false;
        else return true;


    }


    public void launchFoodDiary(View view) {

        final Intent pfileToFD_intent = new Intent(infodata.this, foodDiary.class);


        //if everything is empty do this shit if not use pop up that asks for verification.
        if (tdee==0&&getInt("tdee")==0)Toast.makeText(this,"please fill out all information",Toast.LENGTH_SHORT).show();

            /*new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("daily calorie expenditure not calculated")
                .setMessage("you must enter all information before continuing")
                .setPositiveButton("ok", null);*/




        else if(!isPushed){
            //startActivity(new Intent(this,infoPopUp.class));
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("stop editing")
                    .setMessage("Are you sure you want to stop editing?")
                    .setPositiveButton("Keep Editing", null)
                    .setNegativeButton("Stop editing", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //saveInt("value2", numMealsInt);

                            //saveString("fitnessGoalString", fitnessGoalString);

                            startActivity(pfileToFD_intent);
                        }
                    })
                    .show();


        }
        else if (isPushed){
            saveInt("value2", numMealsInt);
            saveString("fitnessGoalString", fitnessGoalString);
            saveInt("tdee",tdee);
            startActivity(pfileToFD_intent);
        }
    }


    public static int heightInches(int feet, int inches) {
        return (feet * 12 + inches);
    }

    //for BIBLIOGRAPHY -- Harris Benedict formula (bmr)
    public void calculateBMR() {

        int height = heightInches(feetInt, inchesInt);

        //this is only using the imperial measuring formula
        switch (sexString) {
            case "Male":
                bmr = (int) (66 + (6.2 * weightInt) + (12.7 * height) - (6.76 * ageInt));
                break;
            case "female":
                bmr = (int) (655.1 + (4.35 * weightInt) + (4.7 * height) - (4.7 * ageInt));
                break;
            default:
                Toast.makeText(this, "Please fill out either boy or girl", Toast.LENGTH_SHORT).show();
        }


        //now to calculate TDEE


        //BIBLIOGRAPHY: TDEE formula
        switch (actLvlString) {

            case "little to no exercise 0 days":
                tdee = (int) (bmr * 1.2);
                break;
            case "lightly active 1-3 days":
                tdee = (int) (bmr * 1.375);
                break;
            case "moderatly active 3-5 days":
                tdee = (int) (bmr * 1.55);
                break;
            case "very active 6-7 days":
                tdee = (int) (bmr * 1.725);
                break;
            case "extremely active (heavy exercise,6-7 days, 2x/day)":
                tdee = (int) (bmr * 1.9);
                break;
            default:
                Toast.makeText(this, "There was a problem calculating the BMR", Toast.LENGTH_SHORT).show();

                break;
        }

        String setTdeeTextBuffer =tdee + " calories";
        String setBMRTextBuffer =bmr + " calories";


        //if (showBmr != null) {
           // showBmr.setText(setBMRTextBuffer);
            saveInt("bmr", bmr);
        //}
        if (showTDEE != null) {
            showTDEE.setText(setTdeeTextBuffer);
            //saveInt("tdee", tdee);
        }

        setGoalZones();
    }




    public void saveInt(String valueKey, int newValue) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(valueKey, newValue);
        editor.apply();
    }

    public int getInt(String valueKey) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //toast("loadTdee="+prefs.getInt("value1", -1000));
        return prefs.getInt(valueKey, 0);
    }

    public void saveString(String valueKey, String newValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(valueKey, newValue);
        editor.commit();
    }

    public String getString(String valueKey,String defaultValue){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(valueKey,defaultValue);
    }

    public void setGoalZones() {

        Intent intent = getIntent();
        intent.getExtras();


        int[]totalcaloricZone = new int[2];
/*        int[] proteinZone = new int[2];
        int[] carbZone = new int[2];
        int[] fatZone = new int[2];
*/


        switch (selectedFitnessGoal.getText().toString()) {
            case "cut":
                totalcaloricZone[0] = tdee - 600;
                totalcaloricZone[1] = tdee - 300;
                /*proteinZone[0] = 40;
                proteinZone[1] = 50;
                carbZone[0] = 10;
                carbZone[1] = 30;
                fatZone[0] = 30;
                fatZone[1] = 40;*/
                break;
            case "maintain weight":
                totalcaloricZone[0] = tdee - 50;
                totalcaloricZone[1] = tdee + 50;
                /*proteinZone[0] = 25;
                proteinZone[1] = 35;
                carbZone[0] = 30;
                carbZone[1] = 50;
                fatZone[0] = 25;
                fatZone[1] = 35;*/
                break;
            case "bulk":
                totalcaloricZone[0] = tdee + 400;
                totalcaloricZone[1] = tdee + 600;
                /*proteinZone[0] = 25;
                proteinZone[1] = 35;
                carbZone[0] = 40;
                carbZone[1] = 60;
                fatZone[0] = 15;
                fatZone[1] = 25;*/
                break;
            default:
                Toast.makeText(this,"FITGOAL SWITCH'S DEFAULT FOR SETZONES METHOD!",Toast.LENGTH_LONG);
        }

        String czd = "\t"+totalcaloricZone[0] + "-" + totalcaloricZone[1];
        ((TextView) findViewById(R.id.TextView_calorieZoneNumber)).setText(czd);
        saveString("zonenumber",czd);

    }



}
