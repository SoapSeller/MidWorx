package com.fun.midworx.com.fun.midworx.views;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Rotem on 29/06/13.
 */
public class WordsBox extends LinearLayout {

    private final int mLettersNum;
    private final HashMap<String,WordView> mWords = new HashMap<String, WordView>();
    private final Context c;
    private int mGuessed = 0;

    public WordsBox(Context context, ArrayList<String> words) {
        super(context);
        setOrientation(VERTICAL);
//        setPadding(5, 5, 5, 5);

        mLettersNum = words.size() > 0 ? words.get(0).length() : 0;
//        setBackgroundColor(0xff9cbd86);// * mLettersNum / 6);

        c = context;


        //create a TextView for each word
        for (int i=0 ; i<words.size() ; i++)
            mWords.put(words.get(i), addWordView(words.get(i)));
    }

    public boolean guessWord(String word) {
        if (mWords.containsKey(word)) {
            WordView wordView = mWords.get(word);
            //only handle the guess if this is the first time the user guesses this word
            if (!wordView.isGuessed()) {
                wordView.setGuessed(true);
                wordView.showWord(true, 0);
                mGuessed++;
                //check if we guessed all words
//                if (mGuessed == mWords.size())
//                    setBackgroundColor(0xff00ff00);
                return true;
            }
        }
        return false;
    }

    private WordView addWordView(String word) {
        WordView wordView = new WordView(c);
        wordView.setWord(word);
        addView(wordView);
        return wordView;
    }

    public Integer getLettersNum() {
        return mLettersNum;
    }

    public void showUnguessed() {
        Random rnd = new Random(System.nanoTime());
        for(WordView wordView: mWords.values()) {
            if(!wordView.isGuessed()){
                wordView.showWord(false, rnd.nextInt(700));
            }
        }
    }

    private class WordView extends WordFlipper {
        private boolean mGuessed = false;

        public WordView(Context context) {
            super(context);
        }

        private boolean isGuessed() {
            return mGuessed;
        }

        private void setGuessed(boolean guessed) {
            this.mGuessed = guessed;
        }
    }
}
