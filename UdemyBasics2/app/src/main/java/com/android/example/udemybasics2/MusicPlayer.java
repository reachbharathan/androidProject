package com.android.example.udemybasics2;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayer extends AppCompatActivity {

    MediaPlayer song;
    AudioManager volume;
    int sngprg = 0;
    boolean isSongRunning = false;
    boolean isActive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player);

        song = (MediaPlayer) MediaPlayer.create(this, R.raw.anbaeperanbe);
        volume = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxvol = volume.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curvol = volume.getStreamVolume(AudioManager.STREAM_MUSIC);


        final SeekBar volbar = (SeekBar) findViewById(R.id.volume);
        volbar.setMax(maxvol);
        volbar.setProgress(curvol);

        volbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int volchg, boolean b) {

                volume.setStreamVolume(AudioManager.STREAM_MUSIC, volchg, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        final SeekBar sngbar = (SeekBar) findViewById(R.id.player);
        sngbar.setMax(song.getDuration());

        final Handler snghdlr = new Handler();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isSongRunning = true;
                sngbar.setProgress(song.getCurrentPosition());
                snghdlr.postDelayed(this, 100);
                updateTime();
            }
        });


        sngbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int sngchg, boolean b) {
                if ((!isSongRunning) && (isActive)){
                    song.seekTo(sngchg);
                    updateTime();

                }else
                {
                    isSongRunning = !isSongRunning;
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

    private void updateTime() {

        TextView start = (TextView)findViewById(R.id.start);
        TextView stop = (TextView)findViewById(R.id.stop);

        int totaldur = song.getDuration()/1000;

        if (isActive){

            int played = song.getCurrentPosition()/1000;
            int yetTo = totaldur - played;

            if (song.getDuration() != 0){

                start.setText(String.format("%02d",played/60) + ":" + String.format("%02d",played%60));
                stop.setText(String.format("%02d",yetTo/60) + ":" + String.format("%02d",yetTo%60));

            }
        } else {

            stop.setText(String.format("%02d",totaldur/60) + ":" + String.format("%02d",totaldur%60));
        }

    }



    public void Pause(View view) {
        song.stop();
        isActive = false;
        sngprg = song.getCurrentPosition();

    }

    public void Play(View view) {

        if (song != null) {
            song.release();
            song = (MediaPlayer) MediaPlayer.create(this, R.raw.anbaeperanbe);
            if (sngprg != 0) {
                song.seekTo(sngprg);
            }
        }

        isActive = true;
        song.start();
        sngprg = 0;


    }


}
