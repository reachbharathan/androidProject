package com.android.example.udemybasics2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class NewsReader extends AppCompatActivity {


    SQLiteDatabase newsDB;
    ArrayList<String> newsArray = new ArrayList<String>();
    ArrayList<String> links = new ArrayList<String>();
    ArrayAdapter<String> newsAdapter ;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_reader);

        listView = findViewById(R.id.news);

        newsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,newsArray);

        listView.setAdapter(newsAdapter);

        newsDB = this.openOrCreateDatabase("News", Context.MODE_PRIVATE,null);

        newsDB.execSQL("CREATE TABLE IF NOT EXISTS news (id VARCHAR , newstitle VARCHAR, link VARCHAR)");

        NewsDownloader task = new NewsDownloader();

        try {

            task.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        updateList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent article = new Intent(getApplicationContext(),NewsArticle.class);
                article.putExtra("link", links.get(i));
                startActivity(article);
                return;

            }
        });

    }

    public void updateList() {

        Cursor cursor = newsDB.rawQuery("SELECT * FROM news", null);

        int titleIndex = cursor.getColumnIndex("newstitle");
        int linkIndex = cursor.getColumnIndex("link");

        if (cursor.moveToFirst()) {

            newsArray.clear();
            links.clear();

            do {

                newsArray.add(cursor.getString(titleIndex));
                links.add(cursor.getString(linkIndex));
            } while (cursor.moveToNext());

            newsAdapter.notifyDataSetChanged();

        }
    }



    public class NewsDownloader extends AsyncTask<String, Void,String>{

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            String newsItem = "";

            URL url = null;

            try {
                url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;
                    result = result + current;
                    data = reader.read();

                }

                JSONArray topNews = new JSONArray(result);

                newsDB.execSQL("DELETE FROM news");

                for (int i=0; i < 40; i++) {

                    newsItem = "";

                    url = new URL("https://hacker-news.firebaseio.com/v0/item/"+ topNews.getString(i)+".json?print=pretty");

                    HttpURLConnection urlConnection1 = (HttpURLConnection) url.openConnection();

                    InputStream inputStream = urlConnection1.getInputStream();
                    InputStreamReader reader1 = new InputStreamReader(inputStream);

                    int data1 = reader1.read();

                    while (data1 != -1) {

                        char current1 = (char) data1;
                        newsItem = newsItem + current1;
                        data1 = reader1.read();

                    }

                    JSONObject jsonObject = new JSONObject(newsItem);

                    String sql = "INSERT INTO news (id , newstitle, link )VALUES ( ? , ? , ?) " ;

                    SQLiteStatement  statement = newsDB.compileStatement(sql);

                    statement.bindString(1,jsonObject.getString("id"));
                    statement.bindString(2,jsonObject.getString("title"));
                    statement.bindString(3,jsonObject.getString("url"));

                    statement.execute();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            updateList();

        }
    }



}
