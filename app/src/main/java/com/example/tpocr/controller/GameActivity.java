package com.example.tpocr.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
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

    private TextToSpeech mTextToSpeech;

    private TextView mQuestionTextView;
    private TextView mTimerTextView;
    private Button mGameButton1;
    private Button mGameButton2;
    private Button mGameButton3;
    private Button mGameButton4;
    private Button mFakeSkipButton;
    private String correctornot;
    private Boolean TTSOn;
    QuestionBank mQuestionBank ;
    Question mCurrentQuestion;
    private int questionNumber=9;
    private int mRemainingQuestionCount=questionNumber;
    private int mScore;
    private String namePlayer;
    private String gameMode;
    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    private boolean mEnableTouchEvents;
    public static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    public static final String BUNDLE_STATE_QUESTION = "BUNDLE_STATE_QUESTION";
    public static final String BUNDLE_QUESTION_BANK = "BUNDLE_QUESTION_BANK";
    //public static final String BUNDLE_STATE_QUESTION_CURRENT = "BUNDLE_STATE_QUESTION_CURRENT";
    private DatabaseManager databaseManager;

    public static final long COUNTDOWN_IN_MILLIS = 10000;
    //private ColorStateList textColorDefaultCd; //changement couleur countdown

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private MediaPlayer mediaPlayer;
    private Button mSoundPlayButton;


    int mGamemode;

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
        SharedPreferences userDetails = getSharedPreferences("userdetails", MODE_PRIVATE);
        mGamemode = userDetails.getInt("gamemode",0);
        TTSOn = userDetails.getBoolean("TTS",false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        databaseManager = new DatabaseManager(this);

        // On récupère le nom du joueur et le mode de jeu
        Intent intent = getIntent();
        namePlayer = intent.getStringExtra("UserName");
        gameMode = intent.getStringExtra("GameMode");


        this.mediaPlayer= MediaPlayer.create(getApplicationContext(),R.raw.sound_android);
        mQuestionTextView = findViewById(R.id.game_activity_textview_question);
        mTimerTextView = findViewById(R.id.game_activity_textview_cd);
        if(mGamemode==1) mTimerTextView.setVisibility(View.VISIBLE);
        else mTimerTextView.setVisibility(View.INVISIBLE);
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
            mRemainingQuestionCount = questionNumber;
            mQuestionBank = generateQuestion();

        }

        mQuestionBank.setCurrentIndex(questionNumber - mRemainingQuestionCount);
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
                "https://en.wikipedia.org/wiki/Andy_Rubin");

        Question question2 = new Question(
                "When did the first man land on the moon?",
                Arrays.asList(
                        "1958",
                        "1962",
                        "1967",
                        "1969"
                ),
                3,
                "https://en.wikipedia.org/wiki/Apollo_11");

        Question question3 = new Question(
                "What is the house number of The Simpsons?",
                Arrays.asList(
                        "42",
                        "101",
                        "666",
                        "742"
                ),
                3,
                "https://en.wikipedia.org/wiki/The_Simpsons_house");

        Question question4 = new Question(
                "Which country won the 2014 football world cup in 2014?",
                Arrays.asList(
                        "Brazil",
                        "Argentina",
                        "Germany",
                        "Italy"
                ),
                2,
                "https://en.wikipedia.org/wiki/2014_FIFA_World_Cup");

        Question question5 = new Question(
                "Who was the king of gods in Greek mythology?",
                Arrays.asList(
                        "Apollon",
                        "Zeus",
                        "Athena",
                        "Hermès"
                ),
                1,
                "https://en.wikipedia.org/wiki/Zeus");

        Question question6 = new Question(
                "What is the capital of Australia?",
                Arrays.asList(
                        "Sydney",
                        "New York",
                        "Canberra",
                        "Melbourne"
                ),
                3,
                "https://en.wikipedia.org/wiki/Australia");

        Question question7 = new Question(
                "What is the longest river in the world?",
                Arrays.asList(
                        "the Amazon",
                        "the Nil",
                        "the Mississippi",
                        "the Congo"
                ),
                0,
                "https://en.wikipedia.org/wiki/List_of_rivers_by_length");

        Question question8 = new Question(
                "Who wrote Les Miserables ?",
                Arrays.asList(
                        "William Shakespeare",
                        "Victor Hugo",
                        "Albert Camus",
                        "Charles Dickens"
                ),
                1,
                "https://en.wikipedia.org/wiki/Les_Mis%C3%A9rables");

        Question question9 = new Question(
                "What are the three primary colors ?",
                Arrays.asList(
                        "Blue Red White",
                        "Red Yellow Purple",
                        "Blue Red Yellow",
                        "Yellow Pink Green"
                ),
                3,
                "https://en.wikipedia.org/wiki/Primary_color");

        Question question10 = new Question(
                "In computing what is RAM short for ?",
                Arrays.asList(
                        "Random Access Memory",
                        "Real Audio Movie",
                        "Royal Air Maroc",
                        "Removing A Mistake"
                ),
                0,
                "https://en.wikipedia.org/wiki/Random-access_memory");


        return new QuestionBank(Arrays.asList(question1, question2, question3, question5,question6, question7, question8, question9, question10));

    }

    private void displayQuestion(final Question question) {

// Set the text for the question text view and the four
        String q=question.getQuestion();
        mQuestionTextView.setText(q);
        String a1=question.getChoiceList().get(0);
        mGameButton1.setText(a1);
        String a2=question.getChoiceList().get(1);
        mGameButton2.setText(a2);
        String a3=question.getChoiceList().get(2);
        mGameButton3.setText(a3);
        String a4=question.getChoiceList().get(3);
        mGameButton4.setText(a4);
        if(!TTSOn) return;
        String toSpeak = q + " "+ a1 + " "+ a2 + " "+ a3 + " "+ a4;
        mTextToSpeech=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    Log.e("TTS","TTS");
                    mTextToSpeech.setLanguage(Locale.US);
                    mTextToSpeech.stop();
                    mTextToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });


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
            correctornot="Correct !";
            mScore++;
        } else {
            correctornot="Incorrect !";
        }

        //désactive les boutons
        mEnableTouchEvents = false;
        countDownTimer.cancel();

        if(mGamemode==1) {
            Toast.makeText(this, correctornot, Toast.LENGTH_SHORT).show();
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
                        databaseManager.insertGame(namePlayer, gameMode, mScore);
                        databaseManager.close();
                        endGame();
                    }
                    // on réactive les boutons après le timer
                    mEnableTouchEvents = true;

                }
                }, 1_000); // LENGTH_SHORT is usually 2 second long*/
        }
        else createAlertDialog(mQuestionBank.getCurrentQuestion());

    }

    private void createAlertDialog(Question q){
        AlertDialog.Builder alertDialog1  = new AlertDialog.Builder(this);
        String str = "You are " + correctornot + " ! \n"  ;
        alertDialog1.setTitle(str)
                .setMessage("Do you want to know more about this question ? ")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String l = q.getWikiLink();
                        Uri uri= Uri.parse(l);
                        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                        showNextQuestion();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showNextQuestion();
                    }
                })
                .create()
                .show();
    }

    public void showNextQuestion(){
        mRemainingQuestionCount--;

        if (mRemainingQuestionCount > 0) {
            mCurrentQuestion = mQuestionBank.getNextQuestion();
            displayQuestion(mCurrentQuestion);

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();

        } else {
            databaseManager.insertGame(namePlayer, gameMode, mScore);
            databaseManager.close();
            endGame();
        }
        // on réactive les boutons après le timer
        mEnableTouchEvents = true;
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
                if(mGamemode==1) onClick(mFakeSkipButton);
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
        if(mTextToSpeech!=null){
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }
    }

    public void playSound() {

        mediaPlayer.start();

    }



    public void pauseSound() {

        mediaPlayer.stop();

    }
}

