package com.example.udacitybasics;

import java.util.ArrayList;

public class MWordsArrLst extends ArrayList {

    private String mEnglishWord;
    private String mMiwokWord;
    private int mresourceId = NO_IMAGE;
    private static final int NO_IMAGE = -1;
    private int mMusicId;

    public MWordsArrLst (String englishWord, String miwokWord, int resourceId, int musicId) {

        mEnglishWord = englishWord;
        mMiwokWord = miwokWord;
        mresourceId = resourceId;
        mMusicId = musicId;

    }


    public MWordsArrLst (String englishWord, String miwokWord, int musicId) {

        mEnglishWord = englishWord;
        mMiwokWord = miwokWord;
        mMusicId = musicId;

    }

    public String getEnglishWord() {
        return mEnglishWord;
    }

    public String getMiwokWord() {
        return mMiwokWord;
    }

    public int getImageId(){
        return mresourceId;
    }

    public int getMusicId() {
        return mMusicId;
    }

    public boolean checkImage() {
        return mresourceId != NO_IMAGE;
    }

}
