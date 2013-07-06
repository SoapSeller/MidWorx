package com.fun.midworx;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.*;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fun.midworx.com.fun.midworx.views.LetterOrganizer;
import com.fun.midworx.com.fun.midworx.views.WordsBox;
import com.fun.midworx.com.fun.midworx.views.BoxesContainer;
import com.fun.midworx.com.fun.midworx.views.LetterOrganizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    private static final int MAX_GAME_SECONDS = 30;
    private BoxesContainer mBoxesContainer;
    private TextView mScoreText;
    private int mSessionScore = 0;
    private int mLeftSecs;
    private TextView mTimeText;

	private LetterOrganizer letterOrganizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setGuessButton();

        mBoxesContainer = (BoxesContainer) findViewById(R.id.words_boxes_layout);
        mScoreText = (TextView) findViewById(R.id.score_txt);
        mTimeText = (TextView) findViewById(R.id.time_txt);


		FrameLayout letterOrganizerContainer = (FrameLayout) findViewById(R.id.letters_organizer);
		LinearLayout letterOrganizer = new LetterOrganizer(this);
		letterOrganizerContainer.addView(letterOrganizer);

        startNewGame();
    }

    private void startNewGame() {

        //dummy data
        ArrayList<String> words3 = new ArrayList<String>(Arrays.asList("rtg","nsr","uda","sdf","yui"));
        ArrayList<String> words4 = new ArrayList<String>(Arrays.asList("rtdg","nsar","yisr"));
        ArrayList<String> words5 = new ArrayList<String>(Arrays.asList("rtgsr","udsra"));
        ArrayList<String> words6 = new ArrayList<String>(Arrays.asList("rtguda"));

        mBoxesContainer.addBox(words3);
        mBoxesContainer.addBox(words4);
        mBoxesContainer.addBox(words5);
        mBoxesContainer.addBox(words6);

        startTimer();
		FrameLayout letterOrganizerContainer = (FrameLayout) findViewById(R.id.letters_organizer);
		letterOrganizer = new LetterOrganizer(this);
		letterOrganizerContainer.addView(letterOrganizer);

		List<String> letters = Arrays.asList("A","B","C","D","E","F");
		letterOrganizer.setLettersPool(letters);
    }

    private enum endGameReason {TIMEOUT,GUESS_ALL_WORDS};

    private void startTimer() {
        mLeftSecs = MAX_GAME_SECONDS;
        mTimeText.post(new Runnable() {
            @Override
            public void run() {
                mTimeText.setText("" + mLeftSecs);
                mLeftSecs--;
                if (mLeftSecs >= 0)
                    mTimeText.postDelayed(this,1000);
                else
                    endGame(endGameReason.TIMEOUT);
            }
        });
    }

    private void endGame(endGameReason reason) {
        if (reason == endGameReason.TIMEOUT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your score is " + mSessionScore).setTitle("Game Timeout!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("score",mSessionScore);
                    setResult(RESULT_OK,returnIntent);
                    MainActivity.this.finish();
                }
            });
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void setGuessButton() {
        findViewById(R.id.guess_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guessWord(getCurrentGuess());
            }
        });
    }

    private void guessWord(String word) {
        if (mBoxesContainer.guessWord(word));
            addToScore(word);
    }

    private void addToScore(String word) {
        mSessionScore += calculateWordScore(word);
        mScoreText.setText("" + mSessionScore);
    }

    private int calculateWordScore(String word) {
        return word.length();
    }

    private String getCurrentGuess() {
		return letterOrganizer.getCurrentGuess();
    }

}
