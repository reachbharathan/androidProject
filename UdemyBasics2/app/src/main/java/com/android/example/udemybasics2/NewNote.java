package com.android.example.udemybasics2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;

public class NewNote extends AppCompatActivity {

    SharedPreferences notesSaved;
    EditText newNote;
    int position;
    static ArrayList<String> notesList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note);

        notesSaved = this.getSharedPreferences("com.android.example.udemybasics2", Context.MODE_PRIVATE);
        newNote = findViewById(R.id.note);

        Intent extras = getIntent();
        position = 0;
        Bundle value = extras.getExtras();

        if (getIntent().hasExtra("noteNumber")) {

            position = (int) Integer.parseInt(String.valueOf(value.getInt("noteNumber",0)));
            newNote.setText(notesList.get(position-1));

        }


        newNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                updatedataBase(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });



    }

    public void updatedataBase(CharSequence textChanged) {

        if ( position == 0) {
            notesList.add(String.valueOf(textChanged));
        } else {
            notesList.set(position-1, String.valueOf(textChanged));
        }


        Notes.adapter.notifyDataSetChanged();

        notesSaved = this.getSharedPreferences("com.android.example.udemybasics2", Context.MODE_PRIVATE);

        try {
            notesSaved.edit().putString("notes",ObjectSerializer.serialize(notesList)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();


    }
}
