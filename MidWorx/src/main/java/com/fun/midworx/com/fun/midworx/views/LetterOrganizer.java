package com.fun.midworx.com.fun.midworx.views;

import android.view.View;
import com.fun.midworx.IPoolChangeCallback;
import com.fun.midworx.LetterChooserState;
import com.fun.midworx.R;
import android.content.Context;
import android.widget.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created: 6/29/13 7:59 PM
 */
public class LetterOrganizer extends LinearLayout {

	ChooserSource chooserSource;

	LetterChooserState letterChooserState;

	public LetterOrganizer(Context context) {
		super(context);
		this.setOrientation(LinearLayout.HORIZONTAL);

		letterChooserState = new LetterChooserState();
		chooserSource = new ChooserSource(context, letterChooserState);
		this.addView(chooserSource,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

	}

	public String getCurrentGuess() {
		return this.letterChooserState.getChosenWord();
	}

	public void setLettersPool(List<String> letters){
		this.letterChooserState.setLettersPool(letters);
	}


	private class SelectableLetterView extends Button implements IPoolChangeCallback {

		private LetterChooserState state;
		private int index;

		public SelectableLetterView(Context context, int index, LetterChooserState state) {
			super(context);
			this.index = index;
			this.state = state;
			setTextAppearance(getContext(), R.style.PrettyButton);
			setBackground(getResources().getDrawable(R.drawable.green_button));
			this.state.onPoolChange(this);
			this.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SelectableLetterView.this.clicked();
				}
			});
		}

		private void clicked(){
			if (!this.getText().equals("")){
				this.state.chooseLetter(this.index);
			}
		}

		@Override
		public void poolChanged(List<Integer> chosenLetterIndexes) {
			if (!chosenLetterIndexes.contains(this.index)){
				this.setText(this.state.getLetterForIndex(this.index));
			} else {
				this.setText("");
			}
		}
	}


	private class ChooserSource extends TableLayout {

		private TableRow lettersRow;

		private List<SelectableLetterView> selectableLetterViews;
		private LetterChooserState state;


		public ChooserSource(Context context, LetterChooserState state) {
			super(context);
			this.state = state;
			setupRow();
		}

		private void setupRow() {
			lettersRow = new TableRow(getContext());
			lettersRow.setBackgroundColor(0xff00ff00);
			LayoutParams rowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			this.addView(lettersRow,rowParams);
			this.setLettersButtons();
		}

		private void setLettersButtons() {
			for(int i=0; i<6;i++){
				SelectableLetterView selectableLetter = new SelectableLetterView(getContext(),i,state);
				selectableLetter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1));
				selectableLetter.setGravity(TableLayout.TEXT_ALIGNMENT_CENTER);
				lettersRow.addView(selectableLetter);
			}
		}
	}


}
