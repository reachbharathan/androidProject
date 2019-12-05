package com.example.udacitybasics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class MiwokColor extends Fragment {

    public MiwokColor() {
        // Required empty public constructor
    }

    MediaPlayer mediaPlayer;
    AudioManager audioManager ;

    final AudioManager.OnAudioFocusChangeListener changeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int i) {

            if (i == AudioManager.AUDIOFOCUS_GAIN) {
                mediaPlayer.start();
            } else if (i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK || i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                mediaPlayer.pause();
            } else if (i == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMedia();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_miwok_number, container, false);

            final ArrayList<MWordsArrLst> words = new ArrayList<MWordsArrLst>();

            words.add(new MWordsArrLst(getString(R.string.red), "weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
            words.add(new MWordsArrLst(getString(R.string.green), "chokokki", R.drawable.color_green, R.raw.color_green));
            words.add(new MWordsArrLst(getString(R.string.brown), "ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
            words.add(new MWordsArrLst(getString(R.string.gray), "ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
            words.add(new MWordsArrLst(getString(R.string.black), "kululli", R.drawable.color_black, R.raw.color_black));
            words.add(new MWordsArrLst(getString(R.string.white), "kelelli", R.drawable.color_white, R.raw.color_white));
            words.add(new MWordsArrLst(getString(R.string.dustyYellow), "ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
            words.add(new MWordsArrLst(getString(R.string.mustardYellow), "chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

            MWordArrAdptr<MWordsArrLst> adapter = new MWordArrAdptr<MWordsArrLst>(getActivity(), words, getResources().getColor(R.color.grey));

            ListView listView = rootView.findViewById(R.id.listView);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    releaseMedia();
                    int result = audioManager.requestAudioFocus(changeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        mediaPlayer = MediaPlayer.create(getActivity(), words.get(i).getMusicId());
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(mOnCompletionListener);
                    }
                }
            });

        return rootView;
    }

    private  MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMedia();
        }
    };

    private void releaseMedia() {

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            audioManager.abandonAudioFocus(changeListener);
        }
    }



}
