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

public class SurveyLikertVAS_2 extends BaseActivity {
    SeekBar seekBarTop;
    SeekBar seekBarBottom;
    SeekBar seekBar;
    Button submitButton;
    String schoolID;
    String subjectID_string;
    HashMap<String, Object> dictionary;
    HashMap<Integer, Boolean> question_used;
    Boolean other_question;
    Integer previousVAS_1;
    Integer previousVAS_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_likertvas_2);
        seekBarTop = findViewById(R.id.seekBarTop);
        seekBarTop.setEnabled(false);
        seekBarBottom = findViewById(R.id.seekBarBottom);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setEnabled(false);
        seekBarBottom.setOnSeekBarChangeListener(new SurveyLikertVAS_2.seekListener());
        submitButton = findViewById(R.id.submitButton);
        submitButton.setEnabled(false);
        Intent previous_screen = getIntent();
        dictionary = (HashMap<String, Object>) previous_screen.getSerializableExtra("dictionary");
        schoolID = previous_screen.getStringExtra("schoolID");
        subjectID_string = previous_screen.getStringExtra("subjectID");
        question_used = (HashMap<Integer, Boolean>) previous_screen.getSerializableExtra("questionUsed");
        previousVAS_1 = previous_screen.getIntExtra("previousVAS_1", 50);
        previousVAS_2 = previous_screen.getIntExtra("previousVAS_2", 50);
        seekBarTop.setProgress(previousVAS_2);
        question_used.put(3, true);
        other_question = question_used.get(2);
        if (other_question){
            submitButton.setText(R.string.submit_button);
        }
    }

    public void nextQuestion(View view) {
        submitButton.setEnabled(false);
        // still need to add code to do stuff with the results.....
        dictionary.put("LikertVAS2_1", previousVAS_2);
        dictionary.put("LikertVAS2_2", seekBarBottom.getProgress());
        if (!other_question) {
            Intent q42q3 = new Intent(SurveyLikertVAS_2.this, SurveyLikertVAS_1.class);
            q42q3.putExtra("schoolID", schoolID);
            q42q3.putExtra("subjectID", subjectID_string);
            q42q3.putExtra("questionUsed", question_used);
            q42q3.putExtra("dictionary", dictionary);
            q42q3.putExtra("previousVAS_1", previousVAS_1);
            q42q3.putExtra("previousVAS_2", previousVAS_2);
            startActivity(q42q3);
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
        Intent Survey2End = new Intent(SurveyLikertVAS_2.this, End.class);
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
