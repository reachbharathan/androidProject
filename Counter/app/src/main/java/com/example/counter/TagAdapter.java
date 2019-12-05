package com.example.counter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter<M extends ArrayList> extends ArrayAdapter<TagItem> {


    Context thisContext;
    private SparseBooleanArray selectedTags;
    TagItem currentItem;

    public TagAdapter(Context context, ArrayList<TagItem> tags) {
        super(context, 0, tags);
        thisContext = context;
        selectedTags = new SparseBooleanArray();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(thisContext).inflate(R.layout.tag_list_view, parent, false);
        }

        currentItem = getItem(position);

        TextView tagNameText = listItemView.findViewById(R.id.tagName);
        TextView tagCountText = listItemView.findViewById(R.id.tagCount);

        tagNameText.setText(currentItem.getTagName());
        tagCountText.setText(String.valueOf(currentItem.getTagCount()));

        return listItemView;

    }

    public SparseBooleanArray getSelectedTags() {
        return selectedTags;
    }

    public void selectView(int position, boolean value) {

        if (value) {
            selectedTags.put(position, value);
        }  else {
            selectedTags.delete(position); }

        notifyDataSetChanged();

    }

    public void toggleSelection(int position) {
        selectView(position, !selectedTags.get(position));
    }

    public void removeSelection() {
        selectedTags = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void remove(int index) {
        TagListing.listTags.remove(index);
        notifyDataSetChanged();
    }


    public void setTagValue(int index, int tagValue) {
        currentItem = getItem(index);
        currentItem.setTagCount(tagValue);
        notifyDataSetChanged();
    }


    public void setTagItem(int index, String name, int tagValue) {
        currentItem = getItem(index);
        currentItem.setTagCount(tagValue);
        currentItem.setTagName(name);
    }

}
