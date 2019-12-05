package com.android.example.udemybasics2;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Random;

public class BrainTrainer extends AppCompatActivity {

    int answer = 0;
    int correctAnswerCount = 0;
    int totalQuestions = 0;
    boolean timeRunning = false;

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brain_trainer);

        startTimer();
        int answer = generateQuestion();
        setAnswerOptions(answer);

    }

    public void setAnswerOptions(int answer) {

        GridLayout gridLayout = (GridLayout)findViewById(R.id.grid);
        Random random = new Random();
        int answerPosition = random.nextInt(3);

        for (int i = 0; i < gridLayout.getChildCount(); i++ )

        {

            if (i == answerPosition)
            {
                ((TextView)gridLayout.getChildAt(i)).setText(String.valueOf(answer));  
            } else {

                int randomAnswer = random.nextInt(100)+1;

                while (randomAnswer == answer ){

                    randomAnswer = random.nextInt(100)+1;

                }

                ((TextView)gridLayout.getChildAt(i)).setText(String.valueOf(randomAnswer));

            }
        }
        
        totalQuestions = totalQuestions + 1;
        TextView answerCount = (TextView)findViewById(R.id.anscnt);
        
        answerCount.setText((correctAnswerCount) + "/" + (totalQuestions));

        if (totalQuestions == 1){

            TextView showanswer = (TextView)findViewById(R.id.answer);
            showanswer.setText("Game Started !!");

        }
    }

    public int generateQuestion() {

        Random random = new Random();
        TextView question = (TextView)findViewById(R.id.quest);

        int number1 = random.nextInt(100) + 1;

        int oprncode = random.nextInt(10);

        int number2;

        if ((oprncode % 2) ==  1) {

            number2 = random.nextInt(100 )  + 1;
            answer = number1 + number2 ;
            question.setText((number1) + " + " + (number2) );


        }

        if ((oprncode % 2) == 0 ) {

            number2 = random.nextInt( number1 )  + 1;
            answer = number1 - number2;
            question.setText((number1) + " - " + (number2) );

        }

        return answer;

    }

    public void startTimer() {
        
        final TextView timer = (TextView)findViewById(R.id.timer);

        if (countDownTimer != null){

            countDownTimer.cancel();

        }

         countDownTimer = new CountDownTimer(31000,1000) {
            @Override
            public void onTick(long l) {
                
               timer.setText((l/1000) + "s");
               timeRunning = true;

            }

            @Override
            public void onFinish() {

                UpdateResult();
                timeRunning = false;
                timer.setText("0s");

            }
        }.start();
    }

    public void UpdateResult() {

        TextView showanswer = (TextView)findViewById(R.id.answer);
        
        showanswer.setText("Your Score : " + (correctAnswerCount) + "/" + (totalQuestions ));
    }


    public void checkAnswer(View view) {

        if (timeRunning) {

            int id = view.getId();
        
             TextView answerView = (TextView)findViewById(id);
             TextView showanswer = (TextView)findViewById(R.id.answer);
        
             int answerValue = Integer.parseInt((String) answerView.getText());
        
             if (answerValue == answer){
            
            correctAnswerCount = correctAnswerCount + 1;
            showanswer.setText("Correct!");
            
            
        } else {
            
            showanswer.setText("Wrong!");
            
        }

            answer = generateQuestion();
            setAnswerOptions(answer);
            
        }
    }

    public void newGame(View view) {

        timeRunning = false;
        answer = 0;
        correctAnswerCount = 0;
        totalQuestions = 0;

        startTimer();
        answer = generateQuestion();
        setAnswerOptions(answer);


    }
}
