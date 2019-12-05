package com.example.udacitybasics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int scoreTeamA = 0;
    int scoreTeamB = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void add3PointA(View view) {

        scoreTeamA = scoreTeamA + 3;
        displayScoreA(scoreTeamA);

    }

    public void displayScoreA(int scoreTeamA) {

        TextView teamAScore = findViewById(R.id.score_team_a);
        teamAScore.setText(String.valueOf(scoreTeamA));

    }

    public void add2PointA(View view) {

        scoreTeamA = scoreTeamA + 2;
        displayScoreA(scoreTeamA);

    }

    public void add1PointA(View view) {

        scoreTeamA = scoreTeamA + 1;
        displayScoreA(scoreTeamA);

    }


    public void add3PointB(View view) {

        scoreTeamB = scoreTeamB + 3;
        displayScoreB(scoreTeamB);

    }

    public void displayScoreB(int scoreTeamB) {

        TextView teamBScore = findViewById(R.id.score_team_b);
        teamBScore.setText(String.valueOf(scoreTeamB));

    }


    public void add2Point(View view) {

        scoreTeamB = scoreTeamB + 2;
        displayScoreB(scoreTeamB);

    }

    public void add1Point(View view) {

        scoreTeamB = scoreTeamB + 1;
        displayScoreB(scoreTeamB);

    }

    public void reset(View view) {

        scoreTeamA = 0;
        scoreTeamB = 0;
        displayScoreA(scoreTeamA);
        displayScoreB(scoreTeamB);

    }
}
