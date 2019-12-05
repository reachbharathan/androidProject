package com.example.counter;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    static SQLiteDatabase counterDB;
    static int step , maximum , minimum;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDB();

    }


    @Override
    protected void onStart() {
        super.onStart();

        setNavigationView();

    }


    private void createDB() {

        counterDB = this.openOrCreateDatabase("COUNTER", Context.MODE_PRIVATE, null);

        counterDB.execSQL("CREATE TABLE IF NOT EXISTS countTable (counterName VARCHAR, counterValue int)");

        counterDB.execSQL("CREATE TABLE IF NOT EXISTS tagTable (counterName VARCHAR, tagName VARCHAR , tagValue int)");

        counterDB.execSQL("CREATE TABLE IF NOT EXISTS settingsTable (stepValue int, maximum int , minimum int)");

        Cursor cursor = counterDB.rawQuery("SELECT * FROM settingsTable", null);

        cursor.moveToFirst();

        ContentValues contentValues = new ContentValues();

        if (cursor.getCount() == 0) {

            if (contentValues != null) {
                contentValues.clear();
            }


            step = 1;
            maximum = 9999;
            minimum = 0;

            contentValues.put("stepValue", step);
            contentValues.put("maximum", maximum);
            contentValues.put("minimum", minimum);

            counterDB.insert("settingsTable", null, contentValues);

        } else {

            step = cursor.getInt(cursor.getColumnIndex("stepValue"));
            maximum = cursor.getInt(cursor.getColumnIndex("maximum"));
            minimum = cursor.getInt(cursor.getColumnIndex("minimum"));

        }

    }

    private void setNavigationView() {

        drawerLayout = findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close);
        NavigationView navigationView = findViewById(R.id.navView);
        final FragmentManager manager = getSupportFragmentManager();

        navigationView.getMenu().getItem(0).setChecked(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        manager.beginTransaction().replace(R.id.container, new ViewPager()).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()){
                    case R.id.home:
                        manager.beginTransaction().replace(R.id.container, new ViewPager()).commit();
                        break;

                    case R.id.settings:
                        manager.beginTransaction().replace(R.id.container, new Settings()).commit();
                        break;

                    case R.id.savedCounters:
                        manager.beginTransaction().replace(R.id.container, new CountersListing()).commit();
                        break;

                    default:
                        manager.beginTransaction().replace(R.id.container, new ViewPager()).commit();
                        break;

                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        counterDB.close();
    }



}
