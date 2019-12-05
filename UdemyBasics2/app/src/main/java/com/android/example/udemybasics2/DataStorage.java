package com.android.example.udemybasics2;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class DataStorage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_storage);

        // Shared Preferences Data Base

//
//        SharedPreferences sharedPreferences = this.getSharedPreferences("com.android.example.udemybasics2", Context.MODE_PRIVATE);
//
//        sharedPreferences.edit().putString("UserName", "Karthiga").apply();
//
//        String output = sharedPreferences.getString("UserName","");
//
//        Log.i("Result", output);
//
//        sharedPreferences.edit().clear();
//
//
//
//        ArrayList<String> friends = new ArrayList<String>();
//
//        friends.add("Monica");
//        friends.add("Chandler");
//        friends.add("Joey");
//        friends.add("Rachel");
//        friends.add("Ross");
//        friends.add("Pheobe");
//        friends.add("Bharath");
//
//
//        try {
//            sharedPreferences.edit().putString("Friends", ObjectSerializer.serialize(friends)).apply();
//
//            ArrayList<String> newList = new ArrayList<String>();
//
//            newList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("Friends",ObjectSerializer.serialize(new ArrayList<String>())));
//
//            Log.i("Output", String.valueOf(newList));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



        // SQLLite Data Base

        SQLiteDatabase eventsDB = this.openOrCreateDatabase("EVENTS",Context.MODE_PRIVATE,null);

        eventsDB.execSQL("CREATE TABLE IF NOT EXISTS xyz (event VARCHAR, year INT(4))");

        eventsDB.execSQL("INSERT INTO xyz VALUES ('India Independence', 1947)");

        eventsDB.execSQL("INSERT INTO xyz VALUES ('I was Born :P', 1993)");

        Cursor cursor = eventsDB.rawQuery("SELECT * FROM xyz",null);

        if (cursor.getCount() >= 1) {

            int eventInd = cursor.getColumnIndex("event");
            int yearInd = cursor.getColumnIndex("year");


            cursor.moveToFirst();

            while (cursor != null) {

                Log.i("event", cursor.getString(eventInd));
                Log.i("year", String.valueOf(cursor.getInt(yearInd)));

                cursor.moveToNext();
            }
        } else {

            Log.i("NoData", "N Data");
        }



    }
}
