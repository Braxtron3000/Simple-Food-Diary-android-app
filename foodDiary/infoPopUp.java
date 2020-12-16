package com.simple.foodDiary;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class infoPopUp extends AppCompatActivity {

    private Button stayButton=findViewById(R.id.stay_button);
    private Button stopButton=findViewById(R.id.stop_button);


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infopopup);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.5),(int)(height*.5));

    stopButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            startActivity(new Intent(infoPopUp.this,foodDiary.class));
        }
    });

    stayButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public  void onClick(View v){
        }
    });




    }



}
