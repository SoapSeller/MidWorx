package com.fun.midworx.com.fun.midworx.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rotem on 29/06/13.
 */
public class BoxesContainer extends LinearLayout {
    private LayoutParams params;
    private HashMap<Integer,WordsBox> mBoxes = new HashMap<Integer,WordsBox>();

    public BoxesContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
        params.setMargins(5, 5, 5, 5);
    }



    public boolean guessWord(String word) {
        WordsBox box = mBoxes.get(word.length());
        if (box != null)
            return box.guessWord(word);
        return false;
    }

    public void addBox(ArrayList<String> words) {
        WordsBox box = new WordsBox(getContext(),words);
        addView(box, params);
        mBoxes.put(box.getLettersNum(), box);
    }

}
