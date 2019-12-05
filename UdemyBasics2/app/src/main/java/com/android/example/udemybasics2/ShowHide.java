package com.android.example.udemybasics2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ShowHide extends AppCompatActivity {


    ImageView egg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_hide);

        egg = findViewById(R.id.egg);
    }

    public void Show(View view) {

        egg.setVisibility(View.VISIBLE);
    }

    public void Hide(View view) {

        egg.setVisibility(View.GONE);

    }
}
