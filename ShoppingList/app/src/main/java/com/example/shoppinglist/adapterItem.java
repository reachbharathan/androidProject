package com.example.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class adapterItem<I extends ArrayList> extends ArrayAdapter<item>{

    private SparseBooleanArray selectedListIds;
    List multipleSelectionList;
    Context context;

    public adapterItem (Activity context, ArrayList<item> items) {
        super(context,0,items);
        this.context  = context;
        selectedListIds = new SparseBooleanArray();
        multipleSelectionList = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        }

        item currentItem = getItem(position);

        TextView itemNameText = listItemView.findViewById(R.id.itemName);
        TextView quantityText = listItemView.findViewById(R.id.quantity);

        itemNameText.setText(currentItem.getcItemName());
        quantityText.setText(currentItem.getcItemQuantity());

        if (currentItem.getcStrike()) {
            itemNameText.setPaintFlags(itemNameText.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
            quantityText.setPaintFlags(quantityText.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return listItemView;
    }

    @Override
    public void remove(item object) {
        multipleSelectionList.remove(object);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedListIds() {
        return selectedListIds;
    }

    public int getSelectCount() {
        return selectedListIds.size();
    }

    public void toggleSelection(int position) {
        selectView(position, !selectedListIds.get(position));
    }

    public void removeSelection() {
        selectedListIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            selectedListIds.put(position, value);
        else
            selectedListIds.delete(position);
        notifyDataSetChanged();
    }

}


