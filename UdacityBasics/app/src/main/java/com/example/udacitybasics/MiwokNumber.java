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


public class MiwokNumber extends Fragment {

    public MiwokNumber() {
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

        View rootView = inflater.inflate(R.layout.fragment_miwok_number, container, false);

        final ArrayList<MWordsArrLst> words  = new ArrayList<MWordsArrLst>();

        words.add(new MWordsArrLst(getString(R.string.one) , "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new MWordsArrLst(getString(R.string.two) , "otiiko", R.drawable.number_two, R.raw.number_two));
        words.add(new MWordsArrLst(getString(R.string.three),"tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new MWordsArrLst(getString(R.string.four), "oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new MWordsArrLst(getString(R.string.five) , "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new MWordsArrLst(getString(R.string.six) , "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new MWordsArrLst(getString(R.string.seven), "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new MWordsArrLst(getString(R.string.eight), "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new MWordsArrLst(getString(R.string.nine) , "wo’e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new MWordsArrLst(getString(R.string.ten), "na’aacha", R.drawable.number_ten, R.raw.number_ten));

        MWordArrAdptr<ArrayList> adapter = new MWordArrAdptr<>(getActivity(),words,getResources().getColor(R.color.lightRed));

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

    @Override
    public void onStop() {
        releaseMedia();
        super.onStop();
    }
}
