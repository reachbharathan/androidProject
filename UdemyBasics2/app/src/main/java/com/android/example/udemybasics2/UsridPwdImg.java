package com.android.example.udemybasics2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UsridPwdImg extends AppCompatActivity {

    boolean defaultVal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usrid_pwd_img);
    }

    public void Button(View view) {
        final EditText name = (EditText) findViewById(R.id.Name);
        if (name.getText() == null || name.getText().length() == 0 ){
            Toast.makeText(UsridPwdImg.this,  "Enter Your Name", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(UsridPwdImg.this, "Hi " + name.getText() + " !!", Toast.LENGTH_SHORT).show();
        }}

    public void Image(View view) {
        ImageView peacock = (ImageView)findViewById(R.id.image);
        defaultVal = !defaultVal;
        if (defaultVal) {
            peacock.setImageResource(R.drawable.peocock1);
        } else
        {
            peacock.setImageResource(R.drawable.peacock2);
        }

    }
}
