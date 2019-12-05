package com.example.counter;

import android.nfc.Tag;

import java.util.ArrayList;

public class TagItem extends ArrayList implements Comparable<TagItem> {

    private String tagName;
    private int tagCount;

    public TagItem(String cTagName, int cTagCount) {
        tagName = cTagName;
        tagCount = cTagCount;
    }


    public String getTagName(){
        return tagName;
    }

    public int getTagCount(){
        return tagCount;
    }

    public void setTagCount(int count) {
        tagCount = count;
    }

    public void setTagName(String name){
        tagName = name;
    }


    @Override
    public int compareTo(TagItem tagItem) {
        return this.getTagCount() - tagItem.getTagCount();
    }

}
