package com.android.example.udemybasics2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TicTacToePlyr extends AppCompatActivity {

    private Object TicTacToe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tic_tac_toe_plyr);
    }

    public void Game(View view) {

        EditText plyrx = (EditText)findViewById(R.id.xplyr);
        EditText plyro = (EditText)findViewById(R.id.oplyr);

        String plyr1 = plyrx.getText().toString();
        String plyr2 = plyro.getText().toString();

        if (plyr1.isEmpty() || plyr2.isEmpty())
        {
            Toast.makeText(this,"Enter Player Names!",Toast.LENGTH_SHORT).show();
        }

        else

        {
            Intent Gme = new Intent(this, TicTacToe.class);
            Gme.putExtra("plyr", new String[]{plyr1, plyr2});
            startActivity(Gme);

        }

    }

}
