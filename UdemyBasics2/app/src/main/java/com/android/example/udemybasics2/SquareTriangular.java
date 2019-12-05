package com.android.example.udemybasics2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.lang.Math;

public class SquareTriangular extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.square_triangular);
    }

    public void Check(View view) {

        EditText num = (EditText)findViewById(R.id.num);
        try
        {
            int number = Integer.parseInt(num.getText().toString());

            boolean isSquare = CheckSquare(number);
            boolean isTriangular = CheckTriangular(number);
            Result(isSquare,isTriangular);

        }
        catch(Exception e)
        {
            makeToast("That's not a number!");
        }
    }

    private boolean CheckTriangular(int number) {

        int i=0;
        int sum = 0;

        while (i < number)
        {
            sum = sum + i;
            if (sum == number){
                return true;
            }
            i = i+1;
        };

        return false;
    }


    private boolean CheckSquare(int number) {

        long y = (long) Math.sqrt(number);

        if (y*y == number){

            return true;

        } else {

            return false;

        }
    }


    private void Result(boolean isSquare, boolean isTriangular) {

        if (isSquare && isTriangular){

            makeToast(getResources().getString(R.string.square_triangular_toast));

        } else if(isSquare && !isTriangular) {

            makeToast(getResources().getString(R.string.square_not_triangular_toast));

        } else if (!isSquare && isTriangular) {

            makeToast(getResources().getString(R.string.triangular_not_square_toast));

        } else {

            makeToast(getResources().getString(R.string.neither_square_triangular_toast));

        }
    }

    private void makeToast(String msg) {

        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();

    }
}
