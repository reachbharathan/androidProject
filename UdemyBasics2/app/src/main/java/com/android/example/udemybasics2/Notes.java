package com.android.example.udemybasics2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class Notes extends AppCompatActivity {

    SharedPreferences notesSaved;
    static ArrayAdapter adapter;
    static ListView listView;

    private Object String;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.notes, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.newNote) {

            Intent newNote = new Intent(this, NewNote.class);
            startActivity(newNote);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_main);

        notesSaved = this.getSharedPreferences("com.android.example.udemybasics2", Context.MODE_PRIVATE);

        updateList();

    }

    public void updateList() {

        try {
            NewNote.notesList = new ArrayList<String>((Collection<? extends java.lang.String>) ObjectSerializer.deserialize(notesSaved.getString("notes", ObjectSerializer.serialize(new ArrayList<String>()))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, NewNote.notesList);

        listView = findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent newNote = new Intent(getApplicationContext(), NewNote.class);
                newNote.putExtra("noteNumber", i + 1);
                startActivity(newNote);


            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                delete(i);

                return true;
            }
        });

    }

    public void delete(final int deleteNumber) {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_delete)
                .setMessage("Do you want to delete this ?")
                .setTitle("Delete Confirmation")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NewNote.notesList.remove(deleteNumber);
                        try {
                            notesSaved.edit().putString("notes", ObjectSerializer.serialize(NewNote.notesList)).apply();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }
}


