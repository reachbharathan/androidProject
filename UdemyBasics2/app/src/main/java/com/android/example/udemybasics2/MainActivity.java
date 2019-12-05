package com.android.example.udemybasics2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView mylist = (ListView)findViewById(R.id.listview);

        final ArrayList<String> myFamily = new ArrayList<String>();

        myFamily.add("Bharath");
        myFamily.add("Lakshman");
        myFamily.add("Megala");
        myFamily.add("Chella");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,myFamily);

        mylist.setAdapter(arrayAdapter);

        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos , long l) {

                Toast.makeText(getApplicationContext(),"Hi " + myFamily.get(pos) + "!",Toast.LENGTH_SHORT).show();


            }
        });


    }

}
