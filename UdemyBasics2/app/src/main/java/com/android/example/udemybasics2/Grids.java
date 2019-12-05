package com.android.example.udemybasics2;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Grids extends AppCompatActivity {

    MediaPlayer mplayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grids);
    }

    public void ButtonTapped(View view) {

        int id = view.getId();

        if (mplayer != null){

            mplayer.stop();
            mplayer.release();

        }

        String entryname = (String) getResources().getResourceEntryName(id);

        int resid = getResources().getIdentifier(entryname,"raw","com.android.example.udemybasics");

        mplayer = MediaPlayer.create(this,resid);

        mplayer.start();


    }
}
