package com.fun.midworx;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fun.midworx.com.fun.midworx.views.LetterOrganizer;
import com.fun.midworx.com.fun.midworx.views.WordsBox;
import com.fun.midworx.com.fun.midworx.views.BoxesContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    private BoxesContainer mBoxesContainer;
    private TextView mScoreText;
    private int mSessionScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setGuessButton();

        //dummy data
        ArrayList<String> words3 = new ArrayList<String>(Arrays.asList("rtg","nsr","uda","sdf","yui"));
        ArrayList<String> words4 = new ArrayList<String>(Arrays.asList("rtdg","nsar","yisr"));
        ArrayList<String> words5 = new ArrayList<String>(Arrays.asList("rtgsr","udsra"));
        ArrayList<String> words6 = new ArrayList<String>(Arrays.asList("rtguda"));


        mBoxesContainer = (BoxesContainer) findViewById(R.id.words_boxes_layout);
        mScoreText = (TextView) findViewById(R.id.score_txt);

        mBoxesContainer.addBox(words3);
        mBoxesContainer.addBox(words4);
        mBoxesContainer.addBox(words5);
        mBoxesContainer.addBox(words6);

		FrameLayout letterOrganizerContainer = (FrameLayout) findViewById(R.id.letters_organizer);
		LinearLayout letterOrganizer = new LetterOrganizer(this);
		letterOrganizerContainer.addView(letterOrganizer);


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
        return ((EditText)findViewById(R.id.letters_organizer)).getText().toString();
    }

}
