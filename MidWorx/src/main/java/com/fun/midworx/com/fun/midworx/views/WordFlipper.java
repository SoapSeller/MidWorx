package com.fun.midworx.com.fun.midworx.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fun.midworx.R;

public class WordFlipper extends LinearLayout {

	private String mWord;
	private Context mContext;
	private static LayoutParams mParams;

	// loaded from resources on runtime
	private static int ANIMATION_LETTERS_DELAY;
	private static int ANIMATION_DURATION;

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
		ANIMATION_LETTERS_DELAY = getResources().getInteger(R.integer.animation_letters_delay);
		ANIMATION_DURATION		= getResources().getInteger(R.integer.animation_duration);
		int height = getResources().getDimensionPixelSize(
				R.dimen.half_letter_height) * 2;
		int width = getResources().getDimensionPixelSize(
				R.dimen.half_letter_width) * 2;
		mParams = new LayoutParams(width, height);
		int margin = getResources().getDimensionPixelSize(R.dimen.margin);
		mParams.setMargins(margin, margin, margin, margin);
		setOrientation(HORIZONTAL);
	}

	public void setWord(String word) {
		mWord = word;
		createViews();
	}

	public void showWord() {
		for (int i = 0; i < getChildCount(); i++) {
			((CharView) getChildAt(i)).showLetter(i * ANIMATION_LETTERS_DELAY);
		}
	}

	public void hideWord() {
		for (int i = 0; i < getChildCount(); i++) {
			((CharView) getChildAt(i)).hideLetter(i * ANIMATION_LETTERS_DELAY);
		}
	}

	private void createViews() {
		removeAllViews();
		for (int i = 0; i < mWord.length(); i++)
			addView(new CharView(mContext, mWord.substring(i, i + 1)), mParams);
	}

	private class CharView extends TextView {
		private String mLetter;

		public CharView(Context context, String letter) {
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
			}, startDelay + ANIMATION_DURATION / 2);
		}

		public void hideLetter(int startDelay) {
			startFlipAnim(startDelay);
			postDelayed(new Runnable() {
				@Override
				public void run() {
					setText("");
				}
			}, startDelay + ANIMATION_DURATION / 2);
		}

		@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		private void startFlipAnim(int startDelay) {
			animate().rotationX(360).setDuration(ANIMATION_DURATION)
					.setStartDelay(startDelay).start();
			postDelayed(new Runnable() {
				@Override
				public void run() {
					setRotationX(0);
				}
			}, startDelay + ANIMATION_DURATION + 200);
		}
	}
}
