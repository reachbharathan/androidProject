package com.android.example.udemybasics2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuessTheCelebrity extends AppCompatActivity {


    ArrayList<String> celebURLs = new ArrayList<String>();
    ArrayList<String> celebNames = new ArrayList<String>();
    int celebChosen;
    int answerOption;

    Random random = new Random();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guess_the_celebrity);

        try {
            getCelebDetails();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setQuestions();

    }

    private void getCelebDetails() throws ExecutionException, InterruptedException {

        DownloadTask downloadTask = new DownloadTask();

        String result="";

        result = (downloadTask.execute("http://www.posh24.se/kandisar")).get();

        String[] splitresult = result.split("<div class=\"sidebarContainer\">");

        Pattern p  = Pattern.compile("<img src=\"(.*?)\"");
        Matcher m = p.matcher(splitresult[0]);

        while (m.find()){

            celebURLs.add(m.group(1));

        }

        p = Pattern.compile("alt=\"(.*?)\"");
        m = p.matcher(splitresult[0]);

        while (m.find()){

            celebNames.add(m.group(1));

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setQuestions() {

        ImageDownloader imageDownloader = new ImageDownloader();
        Bitmap imageBitmap;

        try {

            ImageView image = findViewById(R.id.image);
            Button opt1 = findViewById(R.id.opt1);
            Button opt2 = findViewById(R.id.opt2);
            Button opt3 = findViewById(R.id.opt3);
            Button opt4 = findViewById(R.id.opt4);

            celebChosen = random.nextInt(celebNames.size());

            imageBitmap = imageDownloader.execute(celebURLs.get(celebChosen)).get();
            image.setImageBitmap(imageBitmap);

            answerOption = random.nextInt(3);
            int ans[] = { };
            ans = new Random().ints(0,celebNames.size()).distinct().limit(3).toArray();

            switch (answerOption){
                case 0:

                    opt1.setText(celebNames.get(celebChosen));
                    opt2.setText(celebNames.get(ans[0]));
                    opt3.setText(celebNames.get(ans[1]));
                    opt4.setText(celebNames.get(ans[2]));

                    break;

                case 1:

                    opt2.setText(celebNames.get(celebChosen));
                    opt1.setText(celebNames.get(ans[0]));
                    opt3.setText(celebNames.get(ans[1]));
                    opt4.setText(celebNames.get(ans[2]));
                    break;

                case 2:

                    opt3.setText(celebNames.get(celebChosen));
                    opt1.setText(celebNames.get(ans[0]));
                    opt2.setText(celebNames.get(ans[1]));
                    opt4.setText(celebNames.get(ans[2]));

                    break;

                case 3:

                    opt4.setText(celebNames.get(celebChosen));
                    opt1.setText(celebNames.get(ans[0]));
                    opt3.setText(celebNames.get(ans[1]));
                    opt2.setText(celebNames.get(ans[2]));
                    break;

                default:
                    break;

            }



        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void checkAnswer(View view) {

        if (view.getTag().toString().equals(Integer.toString(answerOption))){

            Toast.makeText(getApplicationContext(),"Correct!", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(getApplicationContext(), "Wrong! It was " + celebNames.get(celebChosen) + "..", Toast.LENGTH_SHORT).show();

        }

        setQuestions();

    }

    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... url) {

            URL urlContent;
            String result = "";

            HttpURLConnection urlConnection;

            try {
                urlContent = new URL(url[0]);
                urlConnection = (HttpURLConnection) urlContent.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader =   new InputStreamReader(in);

                int data = reader.read();

                while (data != -1){

                    char current = (char) data;
                    result = result + current;

                    data = reader.read();
                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }


    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... images) {

            Bitmap imageBitmap = null;

            try {

                URL imageURL = new URL(images[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) imageURL.openConnection();

                urlConnection.connect();

                InputStream in = urlConnection.getInputStream();

                imageBitmap = (Bitmap) BitmapFactory.decodeStream(in);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return imageBitmap;


        }

    }

}
