package com.gol.exoskeletonsurvey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

// This code is for the question about whether the subject felt an actual difference between trials.
// If they answer yes, the code proceeds to a randomly picked question. If they answer no, then
// the code will resubmit the last trial's answers to Firebase for the current trial.

public class SurveyFeel extends BaseActivity {

    Button yesBtn;
    Button noBtn;
    String schoolID;
    String subjectID_string;
    HashMap<String, Object> dictionary;
    HashMap<Integer, Boolean> question_used;
    Integer previousVAS_1;
    Integer previousVAS_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_feel);
        yesBtn = findViewById(R.id.yesbtn);
        noBtn = findViewById(R.id.nobtn);
        Intent previous_screen = getIntent();
        dictionary = (HashMap<String, Object>) previous_screen.getSerializableExtra("dictionary");
        question_used = (HashMap<Integer, Boolean>) previous_screen.getSerializableExtra("questionUsed");
        schoolID = previous_screen.getStringExtra("schoolID");
        subjectID_string = previous_screen.getStringExtra("subjectID");
        previousVAS_1 = previous_screen.getIntExtra("previousVAS_1", 50);
        previousVAS_2 = previous_screen.getIntExtra("previousVAS_2", 50);
    }

    public void yesButton(View view) {
        yesBtn.setEnabled(false);
        int_random = diceRoll(question_used);
        switch (int_random){
            case 2:
                Intent Feel2q3 = new Intent(SurveyFeel.this, SurveyLikertVAS_1.class);
                Feel2q3.putExtra("schoolID", schoolID);
                Feel2q3.putExtra("subjectID", subjectID_string);
                Feel2q3.putExtra("questionUsed", question_used);
                Feel2q3.putExtra("dictionary", dictionary);
                Feel2q3.putExtra("previousVAS_1", previousVAS_1);
                Feel2q3.putExtra("previousVAS_2", previousVAS_2);
                startActivity(Feel2q3);
                break;
            case 3:
                Intent Feel2q4 = new Intent(SurveyFeel.this, SurveyLikertVAS_2.class);
                Feel2q4.putExtra("schoolID", schoolID);
                Feel2q4.putExtra("subjectID", subjectID_string);
                Feel2q4.putExtra("questionUsed", question_used);
                Feel2q4.putExtra("dictionary", dictionary);
                Feel2q4.putExtra("previousVAS_1", previousVAS_1);
                Feel2q4.putExtra("previousVAS_2", previousVAS_2);
                startActivity(Feel2q4);
                break;
            default: break;}
    }

    public void noButton(View view) {
        noBtn.setEnabled(false);
        dictionary.put("LikertVAS1_1", previousVAS_1);
        dictionary.put("LikertVAS1_2", previousVAS_1);
        dictionary.put("LikertVAS2_1", previousVAS_2);
        dictionary.put("LikertVAS2_2", previousVAS_2);
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
        Intent Survey2End = new Intent(SurveyFeel.this, End.class);
        Survey2End.putExtra("schoolID", schoolID);
        Survey2End.putExtra("subjectID", subjectID_string);
        Survey2End.putExtra("dictionary", dictionary);
        Survey2End.putExtra("previousVAS_1", previousVAS_1);
        Survey2End.putExtra("previousVAS_2", previousVAS_2);
        startActivity(Survey2End);
    }

    public void retrieved_data(Integer trial_num) {

        // Get reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String trial_num_string;
        if (trial_num < 10) {
            trial_num_string = "0" + trial_num;}
        else {
            trial_num_string = String.valueOf(trial_num);
        }
        DatabaseReference history = database.getReference("/" + schoolID + "/Subject" + subjectID_string + "/Trial" + trial_num_string);
        DatabaseReference next_trial = database.getReference("/" + schoolID + "/currentTrial/");
        // Write a message to the database
        history.updateChildren(dictionary);
        HashMap<String, Object> currentTrial = new HashMap<String, Object>();;
        currentTrial.put("Subject" + subjectID_string, trial_num + 1);
        next_trial.updateChildren(currentTrial);
    }

}


