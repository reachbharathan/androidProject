package com.android.example.udemybasics2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class JsonData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_data);

        parseJSONData();
    }

    public void parseJSONData() {

        DownloadTask downloadTask = new DownloadTask();
        try {

            String result = downloadTask.execute("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22").get();

            // Json element Straight Forward

            JSONObject jsonData = new JSONObject(result);

            Log.i("JSON Data Direct",jsonData.getString("name"));

            // Json Array element - in a Loop

            JSONArray weatherArray = new JSONArray(jsonData.getString("weather"));

            for (int i = 0; i < weatherArray.length(); i++){

                JSONObject weatherData = weatherArray.getJSONObject(i);

                Log.i("Json Array in a Loop", String.valueOf(weatherData));

            }

            // Json Array element - without a loop

            JSONObject weatherLearning = weatherArray.getJSONObject(0);

            Log.i("JSON Array - directly", weatherLearning.getString("main"));

            // Json element - with inner elements without an array

            JSONObject mainData = jsonData.getJSONObject("main");

            Log.i("JSON with Inner Element", mainData.getString("pressure"));


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public class DownloadTask extends AsyncTask<String, Void,String>{

        @Override
        protected String doInBackground(String... urls) {

            URL url;
            String result = "";

            try {
                url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data != -1){

                    char current = (char)data;

                    result = result+current;

                    data = inputStreamReader.read();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("Post Execute",s);

        }
    }
}

//
//{"coord":
//        {
//        "lon":-0.13,
//        "lat":51.51
//        },
//        "weather":
//        [
//        {
//        "id":300,
//        "main":"Drizzle",
//        "description":"light intensity drizzle",
//        "icon":"09d"
//        }
//        ],
//        "base":"stations",
//        "main":
//        {
//        "temp":280.32,
//        "pressure":1012,
//        "humidity":81,
//        "temp_min":279.15,
//        "temp_max":281.15
//        },
//        "visibility":10000,
//        "wind":
//        {
//        "speed":4.1,
//        "deg":80
//        },
//        "clouds":
//        {
//        "all":90
//        },
//        "dt":1485789600,
//        "sys":
//        {
//        "type":1,
//        "id":5091,
//        "message":0.0103,
//        "country":"GB",
//        "sunrise":1485762037,
//        "sunset":1485794875
//        },
//
//        "id":2643743,
//        "name":"London",
//        "cod":200
//        }
