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


public class MiwokPhrase extends Fragment {

    public MiwokPhrase() {
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
        View rootView = inflater.inflate(R.layout.fragment_miwok_number, container, false);

        final ArrayList<MWordsArrLst> words  = new ArrayList<MWordsArrLst>();

        words.add(new MWordsArrLst(getString(R.string.phrase1) , "minto wuksus", R.raw.phrase_where_are_you_going));
        words.add(new MWordsArrLst(getString(R.string.phrase2) , "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        words.add(new MWordsArrLst(getString(R.string.phrase3),"oyaaset...", R.raw.phrase_my_name_is));
        words.add(new MWordsArrLst(getString(R.string.phrase4), "michәksәs?", R.raw.phrase_how_are_you_feeling));
        words.add(new MWordsArrLst(getString(R.string.phrase5) , "kuchi achit", R.raw.phrase_im_feeling_good));
        words.add(new MWordsArrLst(getString(R.string.phrase6) , "әәnәs'aa?", R.raw.phrase_are_you_coming));
        words.add(new MWordsArrLst(getString(R.string.phrase7), "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        words.add(new MWordsArrLst(getString(R.string.phrase8), "әәnәm", R.raw.phrase_im_coming));
        words.add(new MWordsArrLst(getString(R.string.phrase9) , "yoowutis", R.raw.phrase_lets_go));
        words.add(new MWordsArrLst(getString(R.string.phrase0), "әnni'nem", R.raw.phrase_come_here));

        MWordArrAdptr<MWordsArrLst> adapter = new MWordArrAdptr<MWordsArrLst>(getActivity(),words, getResources().getColor(R.color.peacock));

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
