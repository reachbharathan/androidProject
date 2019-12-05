package com.android.example.udemybasics2;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class EggTimer extends AppCompatActivity {

    int countDown = 30;
    SeekBar seekBar;
    TextView timer;
    boolean start = true;
    CountDownTimer count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.egg_timer);

        seekBar = findViewById(R.id.seekbar);
        seekBar.setMax(240);
        seekBar.incrementProgressBy(1);
        seekBar.setProgress(countDown);
        timer = findViewById(R.id.timer);
        timer.setText("00:" + String.format("%02d",countDown));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int chg, boolean b) {

                countDown = chg;

                if (countDown < 60){
                    timer.setText("00:" + String.format("%02d",countDown));
                }
                else {
                    timer.setText(String.format("%02d",(countDown)/60) + ":" + String.format("%02d",(countDown)%60));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void Timer(View view) {

        final Button button = (Button)findViewById(R.id.start);

        if (start){
            seekBar.setEnabled(false);
            button.setText("Stop");

            count = new CountDownTimer((countDown)*1000 + 1000,1000) {
                @Override
                public void onTick(long l) {

                    timer.setText(String.format("%02d",(l/1000)/60) + ":" + String.format("%02d",(l/1000)%60));

                }

                @Override
                public void onFinish() {

                    Toast.makeText(getApplicationContext(),"Time Up!!",Toast.LENGTH_SHORT).show();
                    seekBar.setEnabled(true);
                    button.setText("Start");
                    timer.setText("00:00");
                    seekBar.setProgress(0);

                }
            };

            count.start();

        } else {
            seekBar.setEnabled(true);
            button.setText("Start");
            countDown = 30;
            seekBar.setProgress(countDown);
            timer.setText("00:" + String.format("%02d",countDown));
            count.cancel();

        }
        
        start = !start;

    }
}


