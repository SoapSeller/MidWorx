package com.fun.midworx.com.fun.midworx.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fun.midworx.R;

/**
 * Created by Rotem on 06/07/13.
 */
public class WordFlipper extends LinearLayout {
    private static final int PADDING = 3;
    private static final int ANIMATION_LETTERS_DELAY = 100;
    private String mWord;
    private Context mContext;
    private LayoutParams params;

    public WordFlipper(Context context) {
        super(context);
        init(context);
    }

    public WordFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.half_letter_height)*2, getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.half_letter_height)*2, getResources().getDisplayMetrics());
        params = new LayoutParams(width,height);
        params.setMargins(PADDING, PADDING, PADDING, PADDING);
        setOrientation(HORIZONTAL);
    }

    public void setWord(String word) {
        mWord = word;
        createViews();
    }

    public void showWord() {
        for (int i=0 ; i<getChildCount() ; i++) {
            ((CharView)getChildAt(i)).showLetter(i * ANIMATION_LETTERS_DELAY);
        }
    }

    public void hideWord() {
        for (int i=0 ; i<getChildCount() ; i++) {
            ((CharView)getChildAt(i)).hideLetter(i * ANIMATION_LETTERS_DELAY);
        }
    }

    private void createViews() {
        removeAllViews();
        for (int i=0 ; i<mWord.length() ; i++)
            addView(new CharView(mContext,mWord.substring(i,i+1)),params);
    }


    private class CharView extends TextView {
        private static final long ANIMATION_DURATION = 400;
        private String mLetter;

        public CharView(Context context,String letter) {
            super(context);
            mLetter = letter;
            setBackgroundResource(R.drawable.flipper_letter_bg);
            setGravity(Gravity.CENTER);
        }

        public void showLetter(int startDelay) {
            startFlipAnim(startDelay);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    setText(mLetter);
                }
            }, startDelay + ANIMATION_DURATION/2);
        }

        public void hideLetter(int startDelay) {
            startFlipAnim(startDelay);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    setText("");
                }
            },startDelay + ANIMATION_DURATION/2);
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        private void startFlipAnim(int startDelay) {
            animate().rotationX(360).setDuration(ANIMATION_DURATION).setStartDelay(startDelay).start();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    setRotationX(0);
                }
            },startDelay + ANIMATION_DURATION + 200);
        }
    }
}
