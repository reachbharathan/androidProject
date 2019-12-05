package com.android.example.udemybasics2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MenuAlert extends AppCompatActivity {

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_list,menu);
//
//        return super.onCreateOptionsMenu(menu);
//
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//
//        switch (item.getItemId()) {
//
//            case R.id.settings:
//                Log.i("Menu","Setting Selected");
//                return true;
//
//            case R.id.help:
//                Log.i("Menu","Help Selected");
//                return true;
//
//            default:
//                Log.i("Menu","None Selected");
//                return false;
//
//        }
//    }


    SharedPreferences sharedPreferences;
    TextView textView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_list,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

         switch (item.getItemId()) {

             case R.id.Tamil:
                 update("Tamil");
                 textView.setText("Tamil");
                 return true;

             case R.id.English:
                 update("English");
                 textView.setText("English");
                 return true;

             default:
                 textView.setText(" ");
                 sharedPreferences.edit().clear();
                 return false;
         }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_alert);

        sharedPreferences = this.getSharedPreferences("com.android.example.udemybasics2", Context.MODE_PRIVATE);

        String languageChosen = sharedPreferences.getString("language",null);

        textView = findViewById(R.id.language);

        if (languageChosen == null ) {

            new AlertDialog.Builder(this)
                    .setMessage("Which language do you prefer ?")
                    .setTitle("Language Preference")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setNegativeButton("Tamil", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    update("Tamil");
                    textView.setText("Tamil");

                }
            })
                    .setPositiveButton("English", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    update("English");
                    textView.setText("English");

                }
            }).show();

        } else  {

            textView.setText(languageChosen);
        }



    }

    private void update(String tamil) {

        sharedPreferences.edit().putString("language", tamil).apply();


    }
}
