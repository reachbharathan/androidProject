package com.android.example.udemybasics2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MemorablePlaces extends AppCompatActivity {

    static  ArrayList<String> latitude = new ArrayList<String>();
    static  ArrayList<String> longitude = new ArrayList<String>();
    static  ArrayList<String> locationAddress = new ArrayList<String>();
    static  ArrayAdapter arrayAdapter;
    static  ListView list;
    SharedPreferences memory;

    public class ImageDownloader extends AsyncTask<String,Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream in = urlConnection.getInputStream();

                Bitmap image = BitmapFactory.decodeStream(in);

                return image;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memorable_places);

        setBackground();

        memory = this.getSharedPreferences("com.android.example.udemybasics2", Context.MODE_PRIVATE);

        try {
            setPlaces();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setBackground() {

        try {

            ImageDownloader imageDownloader = new ImageDownloader();

            Bitmap image = imageDownloader.execute("https://images.unsplash.com/photo-1501702580495-ec0f3a4517b5?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=740&q=80").get();

            ImageView imageView = findViewById(R.id.image);

            imageView.setImageBitmap(image);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void setPlaces() throws IOException {

        locationAddress = (ArrayList<String>) ObjectSerializer.deserialize(memory.getString("locationAddress",ObjectSerializer.serialize(new ArrayList<String>())));

        latitude = (ArrayList<String>) ObjectSerializer.deserialize(memory.getString("latitude", ObjectSerializer.serialize(new ArrayList<String>())));

        longitude = (ArrayList<String>) ObjectSerializer.deserialize(memory.getString("longitude", ObjectSerializer.serialize(new ArrayList<String>())));

        if ( (locationAddress.size() <= 0 || latitude.size() <= 0 || longitude.size() <= 0) || (locationAddress.size() != latitude.size() || latitude.size() != longitude.size())) {

            memory.edit().clear().apply();
            locationAddress.clear();
            latitude.clear();
            longitude.clear();

            locationAddress.add("Add a new Place");
            latitude.add("0");
            longitude.add("0");

        }

        arrayAdapter = new ArrayAdapter(this, R.layout.places_list_view,locationAddress);

        list = findViewById(R.id.list);

        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent mapsActivity = new Intent(getApplicationContext(),MemorablePlacesMaps.class);

                    mapsActivity.putExtra("Location",String.valueOf(position));

                    startActivity(mapsActivity);

                }
        });


    }
}
