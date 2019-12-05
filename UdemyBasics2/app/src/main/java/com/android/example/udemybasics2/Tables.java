package com.android.example.udemybasics2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Tables extends AppCompatActivity {

    int numchsn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tables);

        SeekBar seekBar = (SeekBar)findViewById(R.id.seekbar);

        seekBar.setMax(40);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int chg, boolean b) {

                if (chg == 0){
                    seekBar.setProgress(1);
                    numchsn = 1;
                } else {
                    numchsn = chg;
                }

                TextView num = (TextView)findViewById(R.id.num);
                num.setText(String.valueOf(numchsn) + " Table");

                updateTable(numchsn);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public void updateTable(int numchsn) {

        ArrayList<String> table = new ArrayList<String>();

        for (int i=1; i<100; i++)
        {
            table.add(numchsn + " X " + i + " = "+ numchsn*i);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,table);

        ListView list = (ListView)findViewById(R.id.list);

        list.setAdapter(arrayAdapter);

    }
}
