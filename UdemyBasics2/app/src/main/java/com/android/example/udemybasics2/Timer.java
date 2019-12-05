package com.android.example.udemybasics2;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Timer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                Log.i("Tag","Runnable has run! A  milli Second has passed...");
                handler.postDelayed(this,1000);

            }
        };

        handler.post(runnable);


        new CountDownTimer(90000,1000){

            @Override
            public void onTick(long l) {

                Log.i("InProgress", "More " + l/1000 + " Seconds");
            }

            @Override
            public void onFinish() {

                Log.i("Finish","Count Down Done!");

            }

        }.start();


    }
}
