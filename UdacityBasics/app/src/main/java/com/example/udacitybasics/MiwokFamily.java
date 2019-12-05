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


public class MiwokFamily extends Fragment {

    public MiwokFamily() {
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

            final ArrayList<MWordsArrLst> words  = new ArrayList<MWordsArrLst>();

            words.add(new MWordsArrLst(getString(R.string.father) , "әpә", R.drawable.family_father, R.raw.family_father));
            words.add(new MWordsArrLst(getString(R.string.mother) , "әṭa", R.drawable.family_mother, R.raw.family_mother));
            words.add(new MWordsArrLst(getString(R.string.son),"angsi", R.drawable.family_son, R.raw.family_son));
            words.add(new MWordsArrLst(getString(R.string.daughter), "tune", R.drawable.family_daughter, R.raw.family_daughter));
            words.add(new MWordsArrLst(getString(R.string.oldBro) , "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
            words.add(new MWordsArrLst(getString(R.string.youngBro) , "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
            words.add(new MWordsArrLst(getString(R.string.oldSis), "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
            words.add(new MWordsArrLst(getString(R.string.youngSis), "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
            words.add(new MWordsArrLst(getString(R.string.grandMother) , "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
            words.add(new MWordsArrLst(getString(R.string.grandFather), "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));

            MWordArrAdptr<MWordsArrLst> adapter = new MWordArrAdptr<MWordsArrLst>(getActivity(),words, getResources().getColor(R.color.lavender));

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
        super.onStop();
        releaseMedia();
    }
}
