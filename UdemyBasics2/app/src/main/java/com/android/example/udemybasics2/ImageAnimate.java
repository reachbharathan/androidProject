package com.android.example.udemybasics2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ImageAnimate extends AppCompatActivity {

    Boolean image = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_animate);
    }

    public void fade(View view) {


        ImageView image1 =  (ImageView)findViewById(R.id.image2);
        image1.animate().scaleX(0.5f).scaleY(0.5f).setDuration(2000);
        image1.animate().rotation(180f).setDuration(2000);
        image1.animate().alpha(0f).setDuration(2000);

    }

}
