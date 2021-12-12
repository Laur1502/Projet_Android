package com.example.tpocr.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpocr.R;
import com.example.tpocr.model.model.model.Question;
import com.example.tpocr.model.model.model.QuestionBank;

import java.util.Arrays;
import java.util.Locale;
import java.lang.Object;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mQuestionTextView;
    private TextView mTimerTextView;
    private Button mGameButton1;
    private Button mGameButton2;
    private Button mGameButton3;
    private Button mGameButton4;
    private Button mFakeSkipButton;
    QuestionBank mQuestionBank ;
    Question mCurrentQuestion;
    private int mRemainingQuestionCount=3;
    private int mScore;
    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    private boolean mEnableTouchEvents;
    public static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    public static final String BUNDLE_STATE_QUESTION = "BUNDLE_STATE_QUESTION";
    public static final String BUNDLE_QUESTION_BANK = "BUNDLE_QUESTION_BANK";
    //public static final String BUNDLE_STATE_QUESTION_CURRENT = "BUNDLE_STATE_QUESTION_CURRENT";

    public static final long COUNTDOWN_IN_MILLIS = 30000;
    //private ColorStateList textColorDefaultCd; //changement couleur countdown

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private MediaPlayer mediaPlayer;
    private Button mSoundPlayButton;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION, mRemainingQuestionCount);
        outState.putParcelable(BUNDLE_QUESTION_BANK, mQuestionBank);
        //outState.putSerializable(BUNDLE_STATE_QUESTION_CURRENT, (Serializable) mCurrentQuestion);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.mediaPlayer= MediaPlayer.create(getApplicationContext(),R.raw.sound_android);
        mQuestionTextView = findViewById(R.id.game_activity_textview_question);
        mTimerTextView = findViewById(R.id.game_activity_textview_cd);
        mGameButton1 = findViewById(R.id.game_activity_button_1);
        mGameButton2 = findViewById(R.id.game_activity_button_2);
        mGameButton3 = findViewById(R.id.game_activity_button_3);
        mGameButton4 = findViewById(R.id.game_activity_button_4);

        mGameButton1.setOnClickListener(this);
        mGameButton2.setOnClickListener(this);
        mGameButton3.setOnClickListener(this);
        mGameButton4.setOnClickListener(this);

        this.mediaPlayer= MediaPlayer.create(getApplicationContext(),R.raw.sound);
        mSoundPlayButton = findViewById(R.id.sound_button_play);

        mSoundPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying())
                {
                    pauseSound();
                    mSoundPlayButton.setBackgroundResource(android.R.drawable.ic_media_play);
                }

                else
                {
                    playSound();
                    mSoundPlayButton.setBackgroundResource(android.R.drawable.ic_media_pause);
                }
            }
        });

        //textColorDefaultCd = mTimerTextView.getTextColors();

        mEnableTouchEvents = true;

        mScore = 0;


        if (savedInstanceState != null) {
            //Log.d("STATE", savedInstanceState.toString());
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mRemainingQuestionCount = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
            mQuestionBank = savedInstanceState.getParcelable(BUNDLE_QUESTION_BANK);


        } else {
            //Log.d("FAIL", savedInstanceState.toString());
            mScore = 0;
            mRemainingQuestionCount = 3;
            mQuestionBank = generateQuestion();

        }

        mQuestionBank.setCurrentIndex(3 - mRemainingQuestionCount);
        mCurrentQuestion = mQuestionBank.getCurrentQuestion();
        Log.d("int"," " + mQuestionBank.getCurrentIndex());
        displayQuestion(mCurrentQuestion);
        timeLeftInMillis = COUNTDOWN_IN_MILLIS;
        startCountDown();

    }

    private QuestionBank generateQuestion(){
        Question question1 = new Question(
                "Who is the creator of Android?",
                Arrays.asList(
                        "Andy Rubin",
                        "Steve Wozniak",
                        "Jake Wharton",
                        "Paul Smith"
                ),
                0,
                "test");

        Question question2 = new Question(
                "When did the first man land on the moon?",
                Arrays.asList(
                        "1958",
                        "1962",
                        "1967",
                        "1969"
                ),
                3,
                "wikiLink");

        Question question3 = new Question(
                "What is the house number of The Simpsons?",
                Arrays.asList(
                        "42",
                        "101",
                        "666",
                        "742"
                ),
                3,
                "wikiLink");


        return new QuestionBank(Arrays.asList(question1, question2, question3));

    }

    private void displayQuestion(final Question question) {
// Set the text for the question text view and the four buttons
        mQuestionTextView.setText(question.getQuestion());
        mGameButton1.setText(question.getChoiceList().get(0));
        mGameButton2.setText(question.getChoiceList().get(1));
        mGameButton3.setText(question.getChoiceList().get(2));
        mGameButton4.setText(question.getChoiceList().get(3));


    }

    @Override
    public void onClick(View v) {
        int index;

        if (v == mGameButton1) {
            index = 0;
        } else if (v == mGameButton2) {
            index = 1;
        } else if (v == mGameButton3) {
            index = 2;
        } else if (v == mGameButton4) {
            index = 3;
        } else if (v == mFakeSkipButton) {
            index = -1;
        }
        else {
            throw new IllegalStateException("Unknown clicked view : " + v);
        }

        // Bonne réponse / mauvaise
        if (index == mQuestionBank.getCurrentQuestion().getAnswerIndex()){
            Toast.makeText(this, "Correct !", Toast.LENGTH_SHORT).show();
            mScore++;
        } else {
            Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
        }

        //désactive les boutons
        mEnableTouchEvents = false;
        countDownTimer.cancel();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // If this is the last question, ends the game.
                // Else, display the next question.

                // passer à la question suivante / fin de partie
                mRemainingQuestionCount--;

                if (mRemainingQuestionCount > 0) {
                    mCurrentQuestion = mQuestionBank.getNextQuestion();
                    displayQuestion(mCurrentQuestion);

                    timeLeftInMillis = COUNTDOWN_IN_MILLIS;
                    startCountDown();

                } else {
                    endGame();
                }
                // on réactive les boutons après le timer
                mEnableTouchEvents = true;
            }
        }, 2_000); // LENGTH_SHORT is usually 2 second long





    }

    public void updateCountDownText(){
        int minutes = (int) (timeLeftInMillis/1000)/60;
        int seconds = (int) (timeLeftInMillis/1000) %60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTimerTextView.setText(timeFormatted);


    }

    private void startCountDown(){
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis =0;
                onClick(mFakeSkipButton);
                updateCountDownText();

            }
        }.start();
    }

    private void endGame(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Well done!")
                .setMessage("Your score is " + mScore)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .create()
                .show();
    }

    protected void onDestroy(){
        super.onDestroy();
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    public void playSound() {

        mediaPlayer.start();

    }



    public void pauseSound() {

        mediaPlayer.stop();

    }
}

