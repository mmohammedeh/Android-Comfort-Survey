package com.gol.exoskeletonsurvey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class SurveyVAS_1 extends BaseActivity {

    SeekBar seekBar;
    Button submitButton;
    String schoolID;
    String subjectID_string;
    HashMap<String, Object> dictionary;
    HashMap<Integer, Boolean> question_used;
    Boolean other_question;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_vas_1);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new seekListener());
        submitButton = findViewById(R.id.submitButton);
        submitButton.setEnabled(false);
        Intent previous_screen = getIntent();
        dictionary = (HashMap<String, Object>) previous_screen.getSerializableExtra("dictionary");
        schoolID = previous_screen.getStringExtra("schoolID");
        subjectID_string = previous_screen.getStringExtra("subjectID");
        question_used = (HashMap<Integer, Boolean>) previous_screen.getSerializableExtra("questionUsed");
        question_used.put(0, true);
        other_question = question_used.get(1);
        if (other_question){
            submitButton.setText(R.string.submit_button);
        }
    }

    public void nextQuestion(View view) {
        submitButton.setEnabled(false);
        // still need to add code to do stuff with the results.....
        dictionary.put("VAS_1", seekBar.getProgress());

        if (!other_question) {
            Intent q12q2 = new Intent(SurveyVAS_1.this, SurveyVAS_2.class);
            q12q2.putExtra("schoolID", schoolID);
            q12q2.putExtra("subjectID", subjectID_string);
            q12q2.putExtra("questionUsed", question_used);
            q12q2.putExtra("dictionary", dictionary);
            startActivity(q12q2);
        } else if (other_question) {
            send_button();

        }
    }

    public void send_button() {

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
        Intent Survey2End = new Intent(SurveyVAS_1.this, End.class);
        Survey2End.putExtra("schoolID", schoolID);
        Survey2End.putExtra("subjectID", subjectID_string);
        Survey2End.putExtra("dictionary", dictionary);
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

    private class seekListener implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {}

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {
            submitButton.setEnabled(true);
        }
    }
}


