package com.gol.exoskeletonsurvey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class End extends BaseActivity {
    Button exit;
    Button newSurvey;
    String schoolID;
    String subjectID_string;
    Boolean lastQuestion;
    Integer previousVAS_1;
    Integer previousVAS_2;
    HashMap<String, Object> dictionary;
    HashMap<Integer, Boolean> question_used;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        exit = findViewById(R.id.exit);
        newSurvey = findViewById(R.id.newSurvey);
        newSurvey.setEnabled(true);
        exit.setEnabled(true);
        Intent previous_screen = getIntent();
        schoolID = previous_screen.getStringExtra("schoolID");
        subjectID_string = previous_screen.getStringExtra("subjectID");
        dictionary = (HashMap<String, Object>) previous_screen.getSerializableExtra("dictionary");
        previousVAS_1 = (Integer) dictionary.get("LikertVAS1_2");
        previousVAS_2 = (Integer) dictionary.get("LikertVAS2_2");
        if (previousVAS_1 == null) {
            previousVAS_1 = (Integer) dictionary.get("VAS_1");
        }
        if (previousVAS_2 == null) {
            previousVAS_2 = (Integer) dictionary.get("VAS_2");
        }
    }

    public void newSurvey(View view) {
        newSurvey.setEnabled(false);
        question_used = new HashMap<>();
        dictionary = new HashMap<>();
        // Here I want the questions to appear randomly, so I made a dictionary with the status of
        // of the question, false means it hasn't been shown yet, and true means it has been shown.
        question_used.put(2, false); // SurveyLikertVAS_1
        question_used.put(3, false); // SurveyLikertVAS_2
        // Start activity SurveyFeel
        Intent End2Feel = new Intent(End.this, SurveyFeel.class);
        End2Feel.putExtra("schoolID", schoolID);
        End2Feel.putExtra("subjectID", subjectID_string);
        End2Feel.putExtra("questionUsed", question_used);
        End2Feel.putExtra("dictionary", dictionary);
        End2Feel.putExtra("previousVAS_1", previousVAS_1);
        End2Feel.putExtra("previousVAS_2", previousVAS_2);
        startActivity(End2Feel);
    }

    public void exit(View view) {
        exit.setEnabled(false);
        signOut();
        this.finishAffinity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void signOut() {
        // Firebase sign out
        FirebaseAuth.getInstance().signOut();
    }

}
