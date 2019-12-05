package com.android.example.udemybasics2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.net.URL;

public class DownloadImage extends AppCompatActivity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_image);


    }


    public class ImageDownloader extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                Bitmap imageBit = BitmapFactory.decodeStream(inputStream);

                return imageBit;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

    }

//    https://i1.trekearth.com/photos/52130/1temple_merge_web.jpg

    public void download(View view) {

        image = (ImageView)findViewById(R.id.image);

        ImageDownloader task = new ImageDownloader();

        Bitmap imageBitmap;

        try {
            imageBitmap = task.execute("https://scontent-yyz1-1.cdninstagram.com/vp/34dd74785be4335559ebb004cda2f210/5D3BEE86/t51.2885-15/e35/54731570_576472592861549_7469464410161574879_n.jpg?_nc_ht=scontent-yyz1-1.cdninstagram.com").get();

            image.setImageBitmap(imageBitmap);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
