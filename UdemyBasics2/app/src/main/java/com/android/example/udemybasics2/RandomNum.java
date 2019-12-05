package com.android.example.udemybasics2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;


public class RandomNum extends AppCompatActivity {

    int randomNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_num);
        Random rand = new Random();
        randomNum = rand.nextInt(10) + 1;
    }


    public void checkNum(View view) {

        EditText Num = (EditText)findViewById(R.id.num);
        try {
            int givenNum = Integer.parseInt(Num.getText().toString());

            if (givenNum > randomNum) {

                makeToast(" You have guessed High!");
            } else if (givenNum < randomNum) {

                makeToast("You have guessed Less!");
            } else {
                makeToast("Correct! Try again! ");
                Random rand = new Random();
                randomNum = rand.nextInt(10) + 1;
            }

        }
        catch (Exception e ) {
            makeToast("That's not a Number!");
        }

    }

    public void makeToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
