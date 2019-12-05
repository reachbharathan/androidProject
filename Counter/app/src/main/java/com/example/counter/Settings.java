package com.example.counter;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.counter.MainActivity.counterDB;
import static com.example.counter.MainActivity.maximum;
import static com.example.counter.MainActivity.minimum;
import static com.example.counter.MainActivity.step;


public class Settings extends Fragment {

    int oldStep , oldMax , oldMin;
    ContentValues contentValues;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_settings, container, false);

        getPreviousValues();
        setSeekBars();

        Button saveButton = rootView.findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        Button clearDataButton = rootView.findViewById(R.id.clearData);
        clearDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmation();
            }
        });

        return rootView;
    }

    private void confirmation() {

        new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to Delete all the Counters? ")
                .setTitle("Confirmation")
                .setIcon(android.R.drawable.ic_notification_clear_all)
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearAllData();
                    }
                })
                .show();

    }

    private void clearAllData() {

        counterDB.execSQL("DELETE FROM tagTable");

        counterDB.execSQL("DELETE FROM countTable");

        sendMessage("All Counters Deleted!");

    }


    private void getPreviousValues() {

        counterDB = getActivity().openOrCreateDatabase("COUNTER", Context.MODE_PRIVATE, null);

        Cursor cursor = counterDB.rawQuery("SELECT * FROM settingsTable", null);

        cursor.moveToFirst();

        if (cursor.getCount() > 0 ) {

            oldStep = cursor.getInt(cursor.getColumnIndex("stepValue"));
            oldMax = cursor.getInt(cursor.getColumnIndex("maximum"));
            oldMin = cursor.getInt(cursor.getColumnIndex("minimum"));

        } else {

            oldStep = 1;
            oldMax = 9999;
            oldMin = 0;

            contentValues = new ContentValues();

            contentValues.put("stepValue", oldStep);
            contentValues.put("maximum", oldMax);
            contentValues.put("minimum", oldMin);

            counterDB.insert("settingsTable", null, contentValues);

        }

    }


    private void setSeekBars() {

        stepSeekBar();
        maxSeekBar();
        minSeekBar();

    }

    private void minSeekBar() {

        final SeekBar miniValue = rootView.findViewById(R.id.min);
        final TextView miniValueText = rootView.findViewById(R.id.minValue);

        miniValue.setMax(9998);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            miniValue.setMin(0);
        }

        miniValue.incrementProgressBy(100);
        miniValue.setProgress(oldMin);
        miniValueText.setText(String.valueOf(oldMin));

        miniValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                miniValueText.setText(String.valueOf(i));
                miniValue.setProgress(i);
                minimum = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void maxSeekBar() {

        final SeekBar maxiValue = rootView.findViewById(R.id.max);
        final TextView maxiValueText = rootView.findViewById(R.id.maxValue);

        maxiValue.setMax(9999+1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            maxiValue.setMin(1);
        }

        maxiValue.incrementProgressBy(100);
        maxiValue.setProgress(oldMax);
        maxiValueText.setText(String.valueOf(oldMax));

        maxiValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                maxiValueText.setText(String.valueOf(i));
                maxiValue.setProgress(i);
                maximum = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void stepSeekBar() {

        final SeekBar stepValue = rootView.findViewById(R.id.step);
        final TextView stepValueText = rootView.findViewById(R.id.stepValue);

        stepValue.setMax(9);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stepValue.setMin(1);
        }

        stepValue.setProgress(oldStep);
        stepValueText.setText(String.valueOf(oldStep));

        stepValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                stepValueText.setText(String.valueOf(i));
                stepValue.setProgress(i);
                step = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public void update() {

        contentValues = new ContentValues();

        contentValues.put("stepValue", step);
        contentValues.put("maximum", maximum);
        contentValues.put("minimum", minimum);

        counterDB.update("settingsTable", contentValues, "", null);

        sendMessage("Saved!");

    }

    private void sendMessage(String msg) {

        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

    }

}
