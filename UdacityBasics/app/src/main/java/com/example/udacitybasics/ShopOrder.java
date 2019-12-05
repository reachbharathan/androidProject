package com.example.udacitybasics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class ShopOrder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_order);
    }

    int quantity = 0 ;
    int price = 0 ;

    public void Increament(View view) {

        quantity = quantity + 1;
        setQuantity(quantity);
        price = calculatePrice(false, false);
        setPrice(price);
    }

    private void setQuantity(int quantity) {

        TextView quantityText = findViewById(R.id.quantity);
        quantityText.setText(String.valueOf(quantity));

    }

    public void Decreament(View view) {

        quantity = quantity - 1;

        if (quantity < 1) {
            quantity = 1;
             sendMessage(getResources().getString(R.string.minimumOrder));
        }

        setQuantity(quantity);
        price = calculatePrice(false, false);
        setPrice(price);

    }

    private void sendMessage(String msg) {

        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();

    }

    public void Order(View view) {

        boolean addWhippedCream = checkWhippedCream();
        boolean addChoclate = checkChocolate();

        price = calculatePrice(addWhippedCream, addChoclate);
        setPrice(price);

        String orderSummary = createOrderSummary(addWhippedCream, addChoclate);
        setOrderSummary(orderSummary);
    }

    private boolean checkChocolate() {

        CheckBox checkBox1 = findViewById(R.id.addChocolate);
        boolean addChoclate = checkBox1.isChecked();

        return addChoclate;
    }

    private boolean checkWhippedCream() {
        CheckBox checkBox = findViewById(R.id.addWhippedCream);
        boolean addWhippedCream = checkBox.isChecked();

        return  addWhippedCream;

    }

    private void setOrderSummary(String orderSummary) {

        TextView orderSummaryText = findViewById(R.id.summary);
        orderSummaryText.setText(orderSummary);
    }

    private String createOrderSummary(boolean addWhippedCream, boolean addChoclate) {

        EditText userName = findViewById(R.id.name);

        String chocolateYesNo = addChoclate ? getResources().getString(R.string.yes):getResources().getString(R.string.no);
        String whippedCreamYesNo= addWhippedCream ? getResources().getString(R.string.yes):getResources().getString(R.string.no);

        String summary = getResources().getString(R.string.name, userName.getText().toString()) + "\n" + getResources().getString(R.string.addWhippedCream, whippedCreamYesNo) +
                "\n" + getResources().getString(R.string.addChocolate, chocolateYesNo) + "\n"  + getResources().getString(R.string.quantity1, String.valueOf(quantity)) + "\n" +
                getResources().getString(R.string.price1, (NumberFormat.getCurrencyInstance().format(price))) + "\n" + getResources().getString(R.string.thnkYou);
        return summary;

    }

    private void setPrice(int price) {

        TextView priceText = findViewById(R.id.price);
        priceText.setText((NumberFormat.getCurrencyInstance().format(price)));

    }

    private int calculatePrice(boolean addWhippedCream, boolean addChoclate) {

        int basePrice = 10;

        if (addChoclate) {
            basePrice = basePrice + 4;
        }

        if (addWhippedCream) {
            basePrice = basePrice + 8;
        }
        return (quantity*basePrice);
    }

    public void Mail(View view) {

        boolean addWhippedCream = checkWhippedCream();
        boolean addChoclate = checkChocolate();

        String orderSummary = createOrderSummary(addWhippedCream, addChoclate);

        Intent mailTo = new Intent(Intent.ACTION_SENDTO);
        mailTo.setData(Uri.parse("mailto:"));
        mailTo.putExtra(Intent.EXTRA_TEXT, orderSummary );
        mailTo.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.subject));
        mailTo.putExtra(Intent.EXTRA_EMAIL, new String[]{"someone@gmail.com"});
        if (mailTo.resolveActivity(getPackageManager()) != null) {
            startActivity(mailTo);
        }
    }
}
