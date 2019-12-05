package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class NewList extends AppCompatActivity {

    ArrayAdapter<CharSequence> categoryAdapter;
    ArrayAdapter<CharSequence> timeAdapter;
    ArrayAdapter<CharSequence> dayAdapter;
    ArrayAdapter<CharSequence> dateAdapter;
    ArrayAdapter<CharSequence> monthAdapter;

    String oldListName = "";
    Button saveListButton;
    String listName = "";
    String inputListName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

    }

    @Override
    protected void onStart() {
        super.onStart();

        inputListName = getListName();

        saveListButton = findViewById(R.id.button);

        MainActivity.shoppingList = this.openOrCreateDatabase("SHOPPINGLIST", Context.MODE_PRIVATE, null);

        updateSpinner();

        if (inputListName != "") {

            updateScreen(inputListName);
            saveListButton.setText(getString(R.string.update));

        } else {

            spinnerVisibility(0);

        }


        saveListButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EditText listNameText = findViewById(R.id.listName);
                CheckBox reminderBox = findViewById(R.id.reminder);
                Spinner categorySpinner = findViewById(R.id.category);
                Spinner monthSpinner = findViewById(R.id.month);
                Spinner dateSpinner = findViewById(R.id.date);
                Spinner daySpinner = findViewById(R.id.day);
                Spinner timeSpinner = findViewById(R.id.time);

                listName = String.valueOf(listNameText.getText());
                String reminder = String.valueOf(reminderBox.isChecked());
                String category = String.valueOf(categorySpinner.getSelectedItem());
                String month = String.valueOf(monthSpinner.getSelectedItem());
                int date = Integer.parseInt(String.valueOf(dateSpinner.getSelectedItem()));
                String day = String.valueOf(daySpinner.getSelectedItem());
                String time = String.valueOf(timeSpinner.getSelectedItem());


                Boolean availability = checkAvailability(listName);

                InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(listNameText.getWindowToken(),0);


                if (reminder == "true") {

                    switch (category) {

                        case "Monthly":
                            month = "";
                            day = "";
                            break;
                        case "Weekly":
                            month = "";
                            date = 0;
                            break;
                        case "Daily":
                            month = "";
                            day = "";
                            date = 0;
                            break;
                        case "One Time":
                            break;
                        case "None":
                            availability = false;
                            month = "";
                            day = "";
                            date = 0;
                            time = "";
                            Toast.makeText(getApplicationContext(), "Select Category", Toast.LENGTH_SHORT).show();
                            break;

                    }

                } else {

                    month = "";
                    date = 0;
                    day = "";
                    time = "";

                }

                if (inputListName != "") {

                    if ((!oldListName.equals(listName)) && (availability == false)) {
                        Toast.makeText(getApplicationContext(), "List is already exists!", Toast.LENGTH_SHORT).show();
                    } else {
                        updateDB(listName, reminder, category, month, date, day, time);
                    }
                }

                if (inputListName == "" ) {

                    if (availability == false) {
                        Toast.makeText(getApplicationContext(), "List Name already exists!", Toast.LENGTH_SHORT).show();
                    } else if (listName.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Enter a List Name", Toast.LENGTH_SHORT).show();
                    } else {
                        addDB(listName, reminder, category, month, date, day, time);
                    }
                }

            }
        });

    }

    private boolean checkAvailability(String listName) {

        Cursor cursor = MainActivity.shoppingList.rawQuery("SELECT * FROM mainList WHERE name = ?", new String[]{listName});

        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void updateSpinner() {

        Spinner categorySpinner = findViewById(R.id.category);
        categoryAdapter = ArrayAdapter.createFromResource(this, R.array.categorySpinner, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        Spinner timeSpinner = findViewById(R.id.time);
        timeAdapter = ArrayAdapter.createFromResource(this, R.array.timeSpinner, android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);


        Spinner daySpinner = findViewById(R.id.day);
        dayAdapter = ArrayAdapter.createFromResource(this, R.array.daySpinner, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);


        Spinner dateSpinner = findViewById(R.id.date);
        dateAdapter = ArrayAdapter.createFromResource(this, R.array.dateSpinner, android.R.layout.simple_spinner_item);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateAdapter);

        Spinner monthSpinner = findViewById(R.id.month);
        monthAdapter = ArrayAdapter.createFromResource(this, R.array.monthSpinner, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

    }

    private void updateScreen(String listName) {

        EditText listNameText = findViewById(R.id.listName);
        CheckBox reminderBox = findViewById(R.id.reminder);

        final Spinner categorySpinner = findViewById(R.id.category);
        final Spinner dateSpinner = findViewById(R.id.date);
        final Spinner daySpinner = findViewById(R.id.day);
        final Spinner timeSpinner = findViewById(R.id.time);
        final Spinner monthSpinner = findViewById(R.id.month);

        final TextView categoryText = findViewById(R.id.categoryText);
        final TextView dateText = findViewById(R.id.dateText);
        final TextView dayText = findViewById(R.id.dayText);
        final TextView monthText = findViewById(R.id.monthText);
        final TextView timeText = findViewById(R.id.timeText);

        Cursor cursor = MainActivity.shoppingList.rawQuery("SELECT * FROM mainList WHERE name = ? ", new String[]{listName});

        int reminderInd = cursor.getColumnIndex("reminder");
        int categoryInd = cursor.getColumnIndex("category");
        int monthInd = cursor.getColumnIndex("month");
        int dateInd = cursor.getColumnIndex("date");
        int dayInd = cursor.getColumnIndex("day");
        int timeInd = cursor.getColumnIndex("time");


        cursor.moveToFirst();

        listNameText.setText(listName);
        Boolean reminder = Boolean.parseBoolean(cursor.getString(reminderInd));
        reminderBox.setChecked(reminder);

        spinnerVisibility(0);

        if (reminder) {

            String category = cursor.getString(categoryInd);
            categorySpinner.setSelection(categoryAdapter.getPosition(category));
            categorySpinner.setVisibility(View.VISIBLE);
            categoryText.setVisibility(View.VISIBLE);

            timeSpinner.setSelection(timeAdapter.getPosition(cursor.getString(timeInd)));
            timeSpinner.setVisibility(View.VISIBLE);
            timeText.setVisibility(View.VISIBLE);

            switch (category) {
                case "Monthly":
                    dateSpinner.setVisibility(View.VISIBLE);
                    dateText.setVisibility(View.VISIBLE);
                    dateSpinner.setSelection(dateAdapter.getPosition(cursor.getString(dateInd)));
                    break;

                case "Weekly":
                    daySpinner.setVisibility(View.VISIBLE);
                    dayText.setVisibility(View.VISIBLE);
                    daySpinner.setSelection(dayAdapter.getPosition(cursor.getString(dayInd)));
                    break;

                case "Daily":
                    break;

                case "One Time":
                    monthSpinner.setVisibility(View.VISIBLE);
                    monthText.setVisibility(View.VISIBLE);
                    monthSpinner.setSelection(monthAdapter.getPosition(cursor.getString(monthInd)));

                    dateSpinner.setVisibility(View.VISIBLE);
                    dateText.setVisibility(View.VISIBLE);
                    dateSpinner.setSelection(dateAdapter.getPosition(cursor.getString(dateInd)));
                    break;

            }
        }

        oldListName = listName;

    }

    private String getListName() {

        String result = "";

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (getIntent().hasExtra("listName")) {

            result = extras.getString("listName", String.valueOf(0));

        }

        return result;
    }


    private void updateDB(String listName, String reminder, String category, String month, int date, String day, String time) {

        ContentValues cv = new ContentValues();
        cv.put("reminder", reminder);
        cv.put("category", category);
        cv.put("month", month);
        cv.put("date", date);
        cv.put("day", day);
        cv.put("time", time);

        if (listName != oldListName) {
            cv.put("name", listName);
        }

        MainActivity.shoppingList.update("mainList", cv, "name = ? ", new String[]{oldListName});

        Toast.makeText(this, " List Updated! ", Toast.LENGTH_SHORT).show();

    }

    private void addDB(String listName, String reminder, String category, String month, int date, String day, String time) {

        MainActivity.shoppingList.execSQL("CREATE TABLE IF NOT EXISTS mainList (name VARCHAR , reminder TEXT , category TEXT , month TEXT , date int , day TEXT, time TEXT)");

        ContentValues cv = new ContentValues();

        cv.put("name", listName);
        cv.put("reminder", reminder);
        cv.put("category", category);
        cv.put("month", month);
        cv.put("date", date);
        cv.put("day", day);
        cv.put("time", time);

        MainActivity.shoppingList.insert("mainList", null, cv);

        Toast.makeText(this, "List Created! Add shopping items to the list", Toast.LENGTH_SHORT).show();

//        int minute, hour, day1;
//        Calendar cal;
//
//        cal = new GregorianCalendar();
//        cal.setTimeInMillis(System.currentTimeMillis());
//        day1 = cal.get(Calendar.DAY_OF_WEEK);
//        hour = cal.get(Calendar.HOUR_OF_DAY);
//        minute = cal.get(Calendar.MINUTE);
//
//
//        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
//        i.putExtra(AlarmClock.EXTRA_MESSAGE, listName);
//        i.putExtra(AlarmClock.EXTRA_HOUR, hour);
//        i.putExtra(AlarmClock.EXTRA_MINUTES, 0 );
//        startActivity(i);

        callItemList();
    }


    public void callItemList() {

        Intent itemListIntent = new Intent(this, ItemList.class);
        itemListIntent.putExtra("listName", listName);
        startActivity(itemListIntent);
        finish();

    }

    public void spinnerVisibility(int i) {

        final CheckBox reminderBox = findViewById(R.id.reminder);
        final EditText listNameText = findViewById(R.id.listName);

        final Spinner categorySpinner = findViewById(R.id.category);
        final Spinner dateSpinner = findViewById(R.id.date);
        final Spinner daySpinner = findViewById(R.id.day);
        final Spinner timeSpinner = findViewById(R.id.time);
        final Spinner monthSpinner = findViewById(R.id.month);

        final TextView categoryText = findViewById(R.id.categoryText);
        final TextView dateText = findViewById(R.id.dateText);
        final TextView dayText = findViewById(R.id.dayText);
        final TextView monthText = findViewById(R.id.monthText);
        final TextView timeText = findViewById(R.id.timeText);

        final InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        reminderBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                manager.hideSoftInputFromWindow(listNameText.getWindowToken(),0);


                if (reminderBox.isChecked()) {

                    updateSpinner();

                    categorySpinner.setVisibility(View.VISIBLE);
                    categoryText.setVisibility(View.VISIBLE);


                } else {

                    categorySpinner.setVisibility(View.GONE);
                    categoryText.setVisibility(View.GONE);

                }
            }

        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                manager.hideSoftInputFromWindow(listNameText.getWindowToken(),0);

                if (reminderBox.isChecked()) {

                    String category = (String) categorySpinner.getSelectedItem();

                    timeSpinner.setVisibility(View.VISIBLE);
                    timeText.setVisibility(View.VISIBLE);

                    switch (category) {

                        case "Monthly":
                            dateSpinner.setVisibility(View.VISIBLE);
                            dateText.setVisibility(View.VISIBLE);

                            daySpinner.setVisibility(View.GONE);
                            monthSpinner.setVisibility(View.GONE);

                            dayText.setVisibility(View.GONE);
                            monthText.setVisibility(View.GONE);

                            break;

                        case "Weekly":

                            daySpinner.setVisibility(View.VISIBLE);
                            dayText.setVisibility(View.VISIBLE);

                            dateSpinner.setVisibility(View.GONE);
                            monthSpinner.setVisibility(View.GONE);

                            dateText.setVisibility(View.GONE);
                            monthText.setVisibility(View.GONE);

                            break;

                        case "Daily":

                            dateSpinner.setVisibility(View.GONE);
                            daySpinner.setVisibility(View.GONE);
                            monthSpinner.setVisibility(View.GONE);

                            dateText.setVisibility(View.GONE);
                            dayText.setVisibility(View.GONE);
                            monthText.setVisibility(View.GONE);

                            break;

                        case "One Time":
                            monthText.setVisibility(View.VISIBLE);
                            monthSpinner.setVisibility(View.VISIBLE);

                            dateSpinner.setVisibility(View.VISIBLE);
                            dateText.setVisibility(View.VISIBLE);

                            daySpinner.setVisibility(View.GONE);
                            dayText.setVisibility(View.GONE);

                            break;

                        case "None":
                            dateSpinner.setVisibility(View.GONE);
                            daySpinner.setVisibility(View.GONE);
                            timeSpinner.setVisibility(View.GONE);
                            monthSpinner.setVisibility(View.GONE);

                            dateText.setVisibility(View.GONE);
                            dayText.setVisibility(View.GONE);
                            timeText.setVisibility(View.GONE);
                            monthText.setVisibility(View.GONE);

                            Toast.makeText(getApplicationContext(), "Select an category", Toast.LENGTH_SHORT).show();

                            break;
                    }

                } else {

                    categorySpinner.setVisibility(View.GONE);
                    categorySpinner.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


        if (i == 0) {

            dateSpinner.setVisibility(View.GONE);
            daySpinner.setVisibility(View.GONE);
            timeSpinner.setVisibility(View.GONE);
            monthSpinner.setVisibility(View.GONE);
            categorySpinner.setVisibility(View.GONE);

            dateText.setVisibility(View.GONE);
            dayText.setVisibility(View.GONE);
            timeText.setVisibility(View.GONE);
            monthText.setVisibility(View.GONE);
            categoryText.setVisibility(View.GONE);

        }


        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                manager.hideSoftInputFromWindow(listNameText.getWindowToken(),0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                manager.hideSoftInputFromWindow(listNameText.getWindowToken(),0);
            }
        };

        dateSpinner.setOnItemSelectedListener(onItemSelectedListener);
        daySpinner.setOnItemSelectedListener(onItemSelectedListener);
        timeSpinner.setOnItemSelectedListener(onItemSelectedListener);
        monthSpinner.setOnItemSelectedListener(onItemSelectedListener);

    }

}

