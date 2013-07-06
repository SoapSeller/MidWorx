package com.fun.midworx.com.fun.midworx.views;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rotem on 29/06/13.
 */
public class WordsBox extends LinearLayout {

    private final int mLettersNum;
    private final HashMap<String,WordView> mWords = new HashMap<String, WordView>();
    private final Context c;
    private String mEmpty;
    private int mGuessed = 0;

    public WordsBox(Context context, ArrayList<String> words) {
        super(context);
        setOrientation(VERTICAL);
        setPadding(5, 5, 5, 5);

        mLettersNum = words.size() > 0 ? words.get(0).length() : 0;
        setBackgroundColor(0xffff0000 * mLettersNum / 6);

        c = context;

        //create empty text before word is guessed
        mEmpty = "";
        for (int i=0 ; i<mLettersNum ; i++)
            mEmpty += "_ ";

        //create a TextView for each word
        for (int i=0 ; i<words.size() ; i++)
            mWords.put(words.get(i), addWordView());
    }

    public boolean guessWord(String word) {
        if (mWords.containsKey(word)) {
            WordView wordView = mWords.get(word);
            //only handle the guess if this is the first time the user guesses this word
            if (!wordView.isGuessed()) {
                wordView.setGuessed(true);
                wordView.setText(word);
                mGuessed++;
                //check if we guessed all words
                if (mGuessed == mWords.size())
                    setBackgroundColor(0xff00ff00);
                return true;
            }
        }
        return false;
    }

    private WordView addWordView() {
        WordView wordView = new WordView(c);
        wordView.setText(mEmpty);
        addView(wordView);
        return wordView;
    }

    public Integer getLettersNum() {
        return mLettersNum;
    }

    private class WordView extends TextView {
        private boolean mGuessed = false;

        public WordView(Context context) {
            super(context);
            setGravity(Gravity.CENTER);
        }

        private boolean isGuessed() {
            return mGuessed;
        }

        private void setGuessed(boolean guessed) {
            this.mGuessed = guessed;
        }
    }
}
