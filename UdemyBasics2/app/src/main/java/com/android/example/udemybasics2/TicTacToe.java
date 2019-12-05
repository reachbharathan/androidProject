package com.android.example.udemybasics2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TicTacToe extends AppCompatActivity {

    boolean tictactoe = true;
    int gridpos;
    int[] gamestate = {2,2,2,2,2,2,2,2,2};
    int[] curchk ;
    int[][] win = { {0,1,2} , {3,4,5} , {6,7,8} , {0,3,6} , {1,4,7} , {2,5,8} , {0,4,8} , {2,4,6} };
    String[] plyr;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tic_tac_toe);

        Bundle extras = getIntent().getExtras();
        plyr = (String[]) extras.get("plyr");

        TextView plyr1 = (TextView)findViewById(R.id.plyr1);
        TextView plyr2 = (TextView)findViewById(R.id.plyr2);
        TextView curplyr = (TextView)findViewById(R.id.curplyr);

        plyr1.setText(plyr[0]);
        plyr2.setText(plyr[1]);
        curplyr.setText( "New Game! " + plyr[0] + "'s turn!");

    }


    public void Clear(View view) {

        GridLayout grid = (GridLayout)findViewById(R.id.grid);

        for (int i = 0; i < grid.getChildCount(); i++)
        {
            ((ImageView) grid.getChildAt(i)).setImageResource(0);
        }

        GameStatus(2);
        tictactoe = true;
        count = 0;

        TextView curplyr = (TextView)findViewById(R.id.curplyr);
        curplyr.setText("New Game! " + plyr[0] + "'s turn!");

    }


    public void dropin(View view) {

        ImageView img = (ImageView) view;
        TextView curplyr = (TextView)findViewById(R.id.curplyr);
        gridpos = Integer.parseInt(img.getTag().toString());

        if (gamestate[gridpos] == 2) {

            count = count + 1;

            if (tictactoe) {

                img.setImageResource(R.drawable.tictacx);
                curplyr.setText(plyr[1] + "'s turn!");
                tictactoe = false;
                gamestate[gridpos] = 1;
                boolean won = checkWin();
                if (won)
                {
                    curplyr.setText("Game Over! Try again! ");
                    Toast.makeText(this,"You Won " + plyr[0] + "!",Toast.LENGTH_SHORT).show();
                }

                else if (count >= 9)

                {
                    curplyr.setText("Game Over! ");
                    Toast.makeText(this,"No one Won! Try Again!",Toast.LENGTH_SHORT).show();
                }

            } else {

                img.setImageResource(R.drawable.tictaco);
                curplyr.setText(plyr[0] + "'s turn!");
                tictactoe = true;
                gamestate[gridpos] = 0;
                boolean won = checkWin();
                if (won)
                {
                    curplyr.setText("Game Over! Try again! ");
                    Toast.makeText(this,"You Won " + plyr[1] + "!",Toast.LENGTH_SHORT).show();
                }

                else if (count >= 9)

                {
                    curplyr.setText("Game Over! Try again! ");
                    Toast.makeText(this,"No one Won! Try Again!",Toast.LENGTH_SHORT).show();
                }

            }

            img.animate().rotation(360f).setDuration(40);

        }

    }

    private boolean checkWin() {

        int i = 0;

        for (i = 0; i <= 7; i++)

        {
            curchk = new int[] {win[i][0], win[i][1], win[i][2]};

            if (gamestate[curchk[0]] == gamestate[curchk[1]] &&
                    gamestate[curchk[1]] == gamestate[curchk[2]] &&
                    gamestate[curchk[0]] == gamestate[curchk[2]] &&
                    gamestate[curchk[0]] != 2 )

            {
                GameStatus(0);
                return true;
            }

        }

        return false;
    }

    public void GameStatus(int chg) {

        for ( int i = 0; i <= 8; i++)
        {
            gamestate[i] = chg;
        }
    }

}
