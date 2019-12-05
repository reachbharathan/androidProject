package com.example.udacitybasics;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MWordArrAdptr<M extends ArrayList> extends ArrayAdapter<MWordsArrLst> {

    private  int mcolorId;

    public MWordArrAdptr(Activity context, ArrayList<MWordsArrLst> words, int colorId ) {

        super(context, 0, words);
        mcolorId = colorId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {

            listItemView = (View) LayoutInflater.from(getContext()).inflate(R.layout.miwok_list_view,parent,false);
        }

        MWordsArrLst currentWord = getItem(position);

        TextView  miwok = listItemView.findViewById(R.id.miwok);
        miwok.setText(currentWord.getMiwokWord());
        miwok.setBackgroundColor(mcolorId);

        TextView english = listItemView.findViewById(R.id.english);
        english.setText(currentWord.getEnglishWord());
        english.setBackgroundColor(mcolorId);

        ImageView image = listItemView.findViewById(R.id.image);

        if (currentWord.checkImage() ) {
            image.setImageResource(currentWord.getImageId());
        } else {
            image.setVisibility(View.GONE);
        }

        return listItemView;
    }

}
