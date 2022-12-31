package com.gol.exoskeletonsurvey;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

// This activity has functions that I did not want to put into the other files because they were
// already getting pretty long.

public abstract class BaseActivity extends  AppCompatActivity {

    private ProgressBar mProgressBar;
    int int_random;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_activity);
    }
    public void setProgressBar(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    public void showProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
    @Override
    public void onStop() {
        super.onStop();
        hideProgressBar();
    }
    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public Integer diceRoll(HashMap<Integer, Boolean> question_used) {
        // I think asking the questions randomly is better because there is evidence from other
        // research that suggests that order of questions matter. I want to remove this effect
        // completely by asking the questions randomly.
        Random r= new Random(); //instance of random class
        List keysAsArray = new ArrayList(question_used.keySet());
        int_random = (int) keysAsArray.get(r.nextInt(keysAsArray.size()));
        return int_random;
    }

//    protected void showBackArrow() {
//        ActionBar supportActionBar = getSupportActionBar();
//        if (supportActionBar != null) {
//            supportActionBar.setDisplayHomeAsUpEnabled(true);
//            supportActionBar.setDisplayShowHomeEnabled(true);
//        }
//    }


//    protected void showAlertDialog(String msg) {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        dialogBuilder.setTitle(null);
//        dialogBuilder.setIcon(R.mipmap.ic_launcher);
//        dialogBuilder.setMessage(msg);
//        dialogBuilder.setPositiveButton(getString(R.string.dialog_ok_btn), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        dialogBuilder.setCancelable(false);
//        dialogBuilder.show();
//    }

    protected void toast(String mToastMsg) {
        Toast.makeText(this, mToastMsg, Toast.LENGTH_LONG).show();
    }

}
