package com.android.example.udemybasics2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Calculator extends AppCompatActivity {

    String usrInput;
    String oprn;
    String num = "0";
    double num1;
    double num2;
    double result;
    boolean oprndne = false;
    boolean numdne = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void one(View view) {

        TextView input = (TextView) findViewById(R.id.inp);
        usrInput = input.getText().toString();
        input.setText(usrInput + "1");
        num = num + "1";
        if (numdne == false) {
            num1 = Double.parseDouble(num);
        } else {
            num2 = Double.parseDouble(num);
            calculate(num1, num2, oprn, result);
            oprndne = true;
        }

    }

    public void two(View view) {

        TextView input = (TextView) findViewById(R.id.inp);
        usrInput = input.getText().toString();
        input.setText(usrInput + "2");
        num = num + "2";
        if (numdne == false) {
            num1 = Double.parseDouble(num);
        } else {
            num2 = Double.parseDouble(num);
            calculate(num1, num2, oprn, result);
            oprndne = true;
        }
    }

    public void three(View view) {

        TextView input = (TextView) findViewById(R.id.inp);
        usrInput = input.getText().toString();
        input.setText(usrInput + "3");
        num = num + "3";
        if (numdne == false) {
            num1 = Double.parseDouble(num);
        } else {
            num2 = Double.parseDouble(num);
            calculate(num1, num2, oprn, result);
            oprndne = true;
        }

    }


    public void four(View view) {

        TextView input = (TextView) findViewById(R.id.inp);
        usrInput = input.getText().toString();
        input.setText(usrInput + "4");
        num = num + "4";
        if (numdne == false) {
            num1 = Double.parseDouble(num);
        } else {
            num2 = Double.parseDouble(num);
            calculate(num1, num2, oprn, result);
            oprndne = true;
        }

    }


    public void five(View view) {

        TextView input = (TextView) findViewById(R.id.inp);
        usrInput = input.getText().toString();
        input.setText(usrInput + "5");
        num = num + "5";
        if (numdne == false) {
            num1 = Double.parseDouble(num);
        } else {
            num2 = Double.parseDouble(num);
            calculate(num1, num2, oprn, result);
            oprndne = true;
        }

    }

    public void six(View view) {

        TextView input = (TextView) findViewById(R.id.inp);
        usrInput = input.getText().toString();
        input.setText(usrInput + "6");
        num = num + "6";
        if (numdne == false) {
            num1 = Double.parseDouble(num);
        } else {
            num2 = Double.parseDouble(num);
            calculate(num1, num2, oprn, result);
            oprndne = true;
        }

    }

    public void seven(View view) {

        TextView input = (TextView) findViewById(R.id.inp);
        usrInput = input.getText().toString();
        input.setText(usrInput + "7");
        num = num + "7";
        if (numdne == false) {
            num1 = Double.parseDouble(num);
        } else {
            num2 = Double.parseDouble(num);
            calculate(num1, num2, oprn, result);
            oprndne = true;
        }

    }

    public void eight(View view) {

        TextView input = (TextView) findViewById(R.id.inp);
        usrInput = input.getText().toString();
        input.setText(usrInput + "8");
        num = num + "8";
        if (numdne == false) {
            num1 = Double.parseDouble(num);
        } else {
            num2 = Double.parseDouble(num);
            calculate(num1, num2, oprn, result);
            oprndne = true;
        }

    }

    public void nine(View view) {

        TextView input = (TextView) findViewById(R.id.inp);
        usrInput = input.getText().toString();
        input.setText(usrInput + "9");
        num = num + "9";
        if (numdne == false) {
            num1 = Double.parseDouble(num);
        } else {
            num2 = Double.parseDouble(num);
            calculate(num1, num2, oprn, result);
            oprndne = true;
        }

    }

    public void zero(View view) {

        TextView input = (TextView) findViewById(R.id.inp);
        usrInput = input.getText().toString();
        input.setText(usrInput + "0");
        num = num + "0";
        if (numdne == false) {
            num1 = Double.parseDouble(num);
        } else {
            num2 = Double.parseDouble(num);
            calculate(num1, num2, oprn, result);
            oprndne = true;
        }
    }

    public void dec(View view) {

        TextView input = (TextView) findViewById(R.id.inp);
        usrInput = input.getText().toString();
        input.setText(usrInput + ".");
        num = num + ".";

    }

    public void div(View view) {

        TextView input = (TextView) findViewById(R.id.inp);
        usrInput = input.getText().toString();

        if (usrInput.length() == 0){
            return;
        }

        if (oprndne == false && numdne == true){
            usrInput = usrInput.substring(0,usrInput.length()-1);
        }

        input.setText(usrInput + "/");
        oprn = "/";
        numdne = true;
        num = "0";
        if (oprndne == true) {
            num1 = this.result;
            num2 = 0.0;
            oprndne = false;
        }

    }

    public void mult(View view) {

        TextView input = (TextView) findViewById(R.id.inp);
        usrInput = input.getText().toString();

        if (usrInput.length() == 0){
            return;
        }

        if (oprndne == false && numdne == true){
            usrInput = usrInput.substring(0,usrInput.length()-1);
        }

        input.setText(usrInput + "*");
        oprn = "*";
        numdne = true;
        num = "0";
        if (oprndne == true) {
            num1 = this.result;
            num2 = 0.0;
            oprndne = false;
        }
    }

    public void minus(View view) {

        TextView input = (TextView) findViewById(R.id.inp);
        usrInput = input.getText().toString();

        if (usrInput.length() == 0){
            return;
        }

        if (oprndne == false && numdne == true){
            usrInput = usrInput.substring(0,usrInput.length()-1);
        }

        input.setText(usrInput + "-");
        oprn = "-";
        numdne = true;
        num = "0";
        if (oprndne == true) {
            num1 = this.result;
            num2 = 0.0;
            oprndne = false;
        }

    }

    public void plus(View view) {

        TextView input = (TextView) findViewById(R.id.inp);
        usrInput = input.getText().toString();

        if (usrInput.length() == 0){
            return;
        }

        if (oprndne == false && numdne == true){
            usrInput = usrInput.substring(0,usrInput.length()-1);
        }

        input.setText(usrInput + "+");
        oprn = "+";
        numdne = true;
        num = "0";
        if (oprndne == true) {
            num1 = this.result;
            num2 = 0.0;
        }

    }

    public void calculate(double num1, double num2, String oprn, double result) {

        switch (oprn) {
            case "+":
                this.result = num1 + num2;
                break;
            case "-":
                this.result = num1 - num2;
                break;
            case "*":
                this.result = num1 * num2;
                break;
            case "/":
                this.result = num1 / num2;
                break;
            default:
                break;

        }

        TextView output = (TextView) findViewById(R.id.res);
        output.setText(String.valueOf(this.result));

    }

    public void clear(View view) {

        final TextView input = (TextView) findViewById(R.id.inp);
        final TextView output = findViewById(R.id.res);
        input.setText("");
        output.setText("");
        num = "";
        num1 = 0.0;
        num2 = 0.0;
        result = 0.0;
        this.result = 0.0;
        numdne = false;
        oprndne = false;

    }

}
