package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    static SQLiteDatabase shoppingList;
    static int notificationId = 1;

    ArrayList<String> listName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }


    private void loadData() {


        listName = new ArrayList<String>();

        shoppingList = this.openOrCreateDatabase("SHOPPINGLIST", Context.MODE_PRIVATE, null);

        shoppingList .execSQL("CREATE TABLE IF NOT EXISTS mainList (name VARCHAR , reminder TEXT , category TEXT , month TEXT , date int , day TEXT, time TEXT)");

//        shoppingList.execSQL("DROP TABLE mainList");

        Cursor cursor = shoppingList.rawQuery("SELECT * FROM mainList ", null);

        int nameIndex = cursor.getColumnIndex("name");

        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {

            String names = cursor.getString(nameIndex);
            listName.add(names);

            cursor.moveToNext();

        }

        if (listName.size() >= 1) {

            TextView newTextView = findViewById(R.id.newText);
            newTextView.setVisibility(View.GONE);

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listName);

        ListView listView = findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent itemList = new Intent(getApplicationContext(), ItemList.class);
                itemList.putExtra("listName", listName.get(i));
                startActivity(itemList);

            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                choseOption(i);
                return true;

            }
        });

    }

    private void choseOption(final int listNumber) {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(getResources().getString(R.string.actionChoose))
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteDB(listNumber);
                    }
                })
                .setNegativeButton(R.string.edit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateList(listNumber);
                    }
                }).create().show();
    }

    private void updateList(int i) {

        Intent updateList = new Intent(this, NewList.class);
        updateList.putExtra("listName", listName.get(i));
        startActivity(updateList);

        listName.clear();

        loadData();

    }


    private void deleteDB(int i) {

        shoppingList.delete("mainList" , " name = ? " , new String[]{listName.get(i)});

        shoppingList.delete("itemList" , " listName = ? " , new String[]{listName.get(i)});

        Toast.makeText(this, " Deleted! ", Toast.LENGTH_SHORT).show();

        listName.clear();

        loadData();

    }

    public void addNewList(View view) {

        Intent newList = new Intent(this, NewList.class);
        startActivity(newList);

        listName.clear();

        loadData();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.deleteAll:
                deleteAll();
                return true;

            case R.id.userNotes:
                Toast.makeText(this, "will be updated later", Toast.LENGTH_SHORT).show();
                return true;

            default:
                Log.i("Menu","None Selected");
                return false;

        }
    }

    private void deleteAll() {

        shoppingList.execSQL("DELETE FROM mainList");

        shoppingList.execSQL("DELETE FROM itemList");

        Toast.makeText(this, "All data Deleted!", Toast.LENGTH_SHORT).show();

        listName.clear();

        loadData();

    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MainActivity.shoppingList = this.openOrCreateDatabase("SHOPPINGLIST", Context.MODE_PRIVATE, null);

//        ScheduledExecutorService scheduler =
//                Executors.newSingleThreadScheduledExecutor();
//
//        scheduler.scheduleAtFixedRate
//                (new Runnable() {
//                    public void run() {
//                        Intent service = new Intent(getApplicationContext(), NotficationClass.class);
//                        startService(service);}
//                }, 0, 10, TimeUnit.SECONDS);


//        ComponentName receiver = new ComponentName(getApplicationContext(), NotficationClass.class);
//        PackageManager pm = getApplicationContext().getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                PackageManager.DONT_KILL_APP);


        Calendar calendar = Calendar.getInstance();
        Intent intent1 = new Intent(MainActivity.this, NotficationClass.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000, pendingIntent);

    }
}
