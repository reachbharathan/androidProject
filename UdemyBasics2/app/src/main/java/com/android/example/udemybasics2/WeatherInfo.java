package com.android.example.udemybasics2;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class WeatherInfo extends AppCompatActivity {


//    http://api.apixu.com/v1/current.json?key=90bc502cb4434caeae7100346190306&q=Chennai


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
    }

    public void loadWeather(View view) {

        DownLoadTask downLoadTask = new DownLoadTask();

        EditText editText = findViewById(R.id.city);

        InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(),0);

        String city = null;

        String result = "";

        try {

            city = URLEncoder.encode(String.valueOf(editText.getText()),"UTF-8");

            if (!city.isEmpty()){
                result = downLoadTask.execute("http://api.apixu.com/v1/current.json?key=90bc502cb4434caeae7100346190306&q=" + city).get();

                JSONObject jsonData = new JSONObject(result);

                JSONObject locationData = jsonData.getJSONObject("location");

                String currentTime = locationData.getString("localtime");

                JSONObject currentData = jsonData.getJSONObject("current");

                String temperature = currentData.getString("temp_c");
                String humidity = currentData.getString("humidity");

                JSONObject conditionData = currentData.getJSONObject("condition");

                String condition = conditionData.getString("text");

                TextView weather = findViewById(R.id.weather);

                weather.setText("Time : " + currentTime + "\n" + "Weather : " + condition + "\n" + "Temperature : " + temperature + " C\n" + "Humidity : " + humidity + "\n");

            } else {
                Toast.makeText(this,"Enter the City!",Toast.LENGTH_SHORT).show();
            }


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            Toast.makeText(this,"Couldn't Load Weather Information",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this,"City not Found!",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public class DownLoadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {

            String result = "";

            try {

                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data != -1){

                    char current = (char)data;

                    result = result + current;

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

            if (s.isEmpty()){

                Toast.makeText(getApplicationContext(), "City not found!", Toast.LENGTH_SHORT).show();
                TextView weather = findViewById(R.id.weather);
                weather.setText(" ");


            }
        }
    }


}
