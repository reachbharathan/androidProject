package com.example.reportcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> mainPageList = new ArrayList<String>();

        mainPageList.add("Student's Names List");
        mainPageList.add("Subject List");
        mainPageList.add("Mark Sheet");
        mainPageList.add("Calculate  Metrics");

        ArrayAdapter<String> mainPageAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , mainPageList);

        ListView listView = findViewById(R.id.list);

        listView.setAdapter(mainPageAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {

                    case 0:
                        Intent namesList = new Intent(getApplicationContext(), NamesList.class);
                        startActivity(namesList);
                        finish();

                }


            }
        });

      }
}
