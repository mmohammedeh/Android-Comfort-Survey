package com.gol.exoskeletonsurvey;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;


public class SplashScreen extends BaseActivity {

    String schoolID;
    Integer subjectID;
    EditText subjectIDbox;
    Button btPSU;
    Button btSMU;
    Button continueButton;
    String subjectID_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        subjectIDbox = findViewById(R.id.subjectIDeditText);
        btPSU = findViewById(R.id.PSU);
        btSMU = findViewById(R.id.SMU);
        continueButton = findViewById(R.id.continueButton);
        setPSU();
        setSMU();
        continueButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                if (subjectID < 10) {
                    subjectID_string = "0" + String.valueOf(subjectID);}
                else {
                    subjectID_string = String.valueOf(subjectID);
                }
                Intent splash2SplashPop = new Intent(SplashScreen.this, SplashPop.class);
                splash2SplashPop.putExtra("schoolID", schoolID);
                splash2SplashPop.putExtra("subjectID", subjectID_string);
                startActivity(splash2SplashPop);
            }
        });
        activateContinue(subjectIDbox);
    }

    private void activateContinue(EditText textbox) {

        textbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String value = subjectIDbox.getText().toString();
                subjectID = Integer.parseInt(value);
                if (subjectID == null){
                    continueButton.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                continueButton.setEnabled(true);

            }
        });
    }

    private void setPSU() {

        btPSU.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                btPSU.setBackgroundColor(ContextCompat.getColor(SplashScreen.this, android.R.color.holo_blue_dark));
                btSMU.setBackgroundColor(ContextCompat.getColor(SplashScreen.this, android.R.color.holo_blue_bright));
                schoolID = "PSU";
            }
        });
    }

    private void setSMU() {
        btSMU.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                btPSU.setBackgroundColor(ContextCompat.getColor(SplashScreen.this, android.R.color.holo_blue_bright));
                btSMU.setBackgroundColor(ContextCompat.getColor(SplashScreen.this, android.R.color.holo_blue_dark));
                schoolID = "SMU";
            }
        });
    }
}