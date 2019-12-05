package com.android.example.udemybasics2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AllInOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_in_one);

        ListView listView = findViewById(R.id.list);

        ArrayList<String> activityName = new ArrayList<String>();

        activityName.add("Tables");
        activityName.add("Egg Timer");
        activityName.add("Brain Trainer");
        activityName.add("Guess the Celebrity");
        activityName.add("Weather Info");
        activityName.add("Calculator");
        activityName.add("Tic Tac Toe");
        activityName.add("Square Triangular");
        activityName.add("Random Number");
        activityName.add("Music Player");
        activityName.add("Video Player");
        activityName.add("Hikers Watch");
        activityName.add("Memorable Places");

        activityName.add("Practice - JSON Data Processing");
        activityName.add("Practice - Download Image");
        activityName.add("Practice - Show Hide");
        activityName.add("Practice - Timer");
        activityName.add("Practice - List View");
        activityName.add("Practice - Grids");
        activityName.add("Practice - Image Animation");
        activityName.add("Practice - User Id Password");
        activityName.add("Practice - Maps");
        activityName.add("Practice - Data Storage");
        activityName.add("Practice - Menu Alert");

        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.listviewstyle,activityName);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                goToActivity(pos);

            }

        });


    }

    public void goToActivity(int pos) {

        switch (pos){

            case 0:
                Intent appTables = new Intent(this,Tables.class);
                startActivity(appTables);
                break;

            case 1:
                Intent appEggTimer = new Intent(this,EggTimer.class);
                startActivity(appEggTimer);
                break;

            case 2:
                Intent appBrainTrainer = new Intent(this,BrainTrainer.class);
                startActivity(appBrainTrainer);
                break;

            case 3:
                Intent appGuessCelebrity = new Intent(this,GuessTheCelebrity.class);
                startActivity(appGuessCelebrity);
                break;

            case 4:
                Intent appWeatherInfo = new Intent(this,WeatherInfo.class);
                startActivity(appWeatherInfo);
                break;

            case 5:
                Intent appCalculator = new Intent(this,Calculator.class);
                startActivity(appCalculator);
                break;

            case 6:
                Intent appticTacToe = new Intent(this,TicTacToePlyr.class);
                startActivity(appticTacToe);
                break;

            case 7:
                Intent appSquareTriangular = new Intent(this,SquareTriangular.class);
                startActivity(appSquareTriangular);
                break;

            case 8:
                Intent appRandomnumber = new Intent(this,RandomNum.class);
                startActivity(appRandomnumber);
                break;

            case 9:
                Intent appMusicPlayer = new Intent(this,MusicPlayer.class);
                startActivity(appMusicPlayer);
                break;

            case 10:
                Intent appVideoPlayer = new Intent(this,VideoPlayer.class);
                startActivity(appVideoPlayer);
                break;

            case 11:
                Intent appHikersWatch = new Intent(this,HikersWatch.class);
                startActivity(appHikersWatch);
                break;

            case 12:
                Intent appMemorablePlaces = new Intent(this,HikersWatch.class);
                startActivity(appMemorablePlaces);
                break;

            case 13:
                Intent pracJsonData = new Intent(this,JsonData.class);
                startActivity(pracJsonData);
                break;

            case 14:
                Intent pracDownloadImage = new Intent(this,DownloadImage.class);
                startActivity(pracDownloadImage);
                break;

            case 15:
                Intent pracShowhide = new Intent(this,ShowHide.class);
                startActivity(pracShowhide);
                break;

            case 16:
                Intent pracTimer = new Intent(this,Timer.class);
                startActivity(pracTimer);
                break;

            case 17:
                Intent pracListView = new Intent(this,MainActivity.class);
                startActivity(pracListView);
                break;

            case 18:
                Intent pracGrids = new Intent(this,Grids.class);
                startActivity(pracGrids);
                break;

            case 19:
                Intent pracImageAnimate = new Intent(this,ImageAnimate.class);
                startActivity(pracImageAnimate);
                break;

            case 20:
                Intent pracUsrIDPwd = new Intent(this,UsridPwdImg.class);
                startActivity(pracUsrIDPwd);
                break;

            case 21:
                Intent pracMaps = new Intent(this,MapsActivity.class);
                startActivity(pracMaps);
                break;

            case 22:
                Intent pracData = new Intent(this,DataStorage.class);
                startActivity(pracData);
                break;

            case 23:
                Intent pracMenuAlert = new Intent(this,MenuAlert.class);
                startActivity(pracMenuAlert);
                break;

            default:
                Toast.makeText(this,"Couldnt Load the App", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
