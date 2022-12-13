package com.gol.exoskeletonsurvey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SplashPop extends BaseActivity {

    TextView popupText;
    String schoolID;
    String subjectID_string;
    HashMap<String, Object> dictionary;
    HashMap<Integer, Boolean> question_used1;
    HashMap<Integer, Boolean> question_used2;
    Integer previousVAS_1;
    Integer previousVAS_2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splashpop);
        // Here I want the questions to appear randomly, so I made a dictionary with the status of
        // of the question, false means it hasn't been shown yet, and true means it has been shown.
        question_used1 = new HashMap<>();
        question_used2 = new HashMap<>();
        dictionary = new HashMap<>();
        question_used1.put(0, false); // SurveyVAS_1
        question_used1.put(1, false); // SurveyVAS_2
        question_used2.put(2, false); // SurveyLikertVAS_1
        question_used2.put(3, false); // SurveyLikertVAS_2
        previousVAS_1 = 0;
        previousVAS_2 = 0;

        // In order to move on to the next activity, I must pass an intent. This is the next
        // activity so I retrieve the intent form the previous activity which will have variables
        // I need.
        Intent splash2popup = getIntent();
        schoolID = splash2popup.getStringExtra("schoolID");
        subjectID_string = splash2popup.getStringExtra("subjectID");
        popupText = findViewById(R.id.popupTextView);
        String popupString = "You selected " + schoolID + " and " + subjectID_string + " is this correct?";
        popupText.setText(popupString);

        Button btYes = findViewById(R.id.yesButton);
        Button btNo = findViewById(R.id.noButton);
        setYes(btYes);
        setNo(btNo);
    }

    private void setYes(Button button1) {
        button1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // diceRoll() is the function used to determine what the next question shown will be
                // it will take into account previous questions shown before "rolling the dice"


                // add code to skip to likert if survey started during experiment
                // Get reference
                FirebaseDatabase database = FirebaseDatabase.getInstance();


                // Get info submitted from MATLAB
                DatabaseReference currentTrial = database.getReference("/" + schoolID + "/currentTrial/");

                currentTrial.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Integer trial_num = dataSnapshot.child("Subject" + subjectID_string).getValue(Integer.class);
                        retrieved_data(trial_num);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                }
        });
    }

    public void retrieved_data(Integer trial_num) {

        if (trial_num > 1) {
            Intent splash2q2 = new Intent(SplashPop.this, SurveyLikertVAS_1.class);
            splash2q2.putExtra("schoolID", schoolID);
            splash2q2.putExtra("subjectID", subjectID_string);
            splash2q2.putExtra("questionUsed", question_used2);
            splash2q2.putExtra("dictionary", dictionary);
            splash2q2.putExtra("previousVAS_1", previousVAS_1);
            splash2q2.putExtra("previousVAS_2", previousVAS_2);
            startActivity(splash2q2);
        }
        else {
            Intent splash2q1 = new Intent(SplashPop.this, SurveyVAS_1.class);
            splash2q1.putExtra("schoolID", schoolID);
            splash2q1.putExtra("subjectID", subjectID_string);
            splash2q1.putExtra("questionUsed", question_used1);
            splash2q1.putExtra("dictionary", dictionary);
            startActivity(splash2q1);
        }
    }

    private void setNo(Button button1) {
        button1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                finish();
            }
        });
    }
}
