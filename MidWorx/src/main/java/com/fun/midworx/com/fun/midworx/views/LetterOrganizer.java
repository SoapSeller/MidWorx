package com.fun.midworx.com.fun.midworx.views;

import android.R;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created: 6/29/13 7:59 PM
 */
public class LetterOrganizer extends LinearLayout {

	ChooserSource chooserSource;

	public LetterOrganizer(Context context) {
		super(context);
		this.setOrientation(LinearLayout.HORIZONTAL);

		chooserSource = new ChooserSource(context);
		this.addView(chooserSource,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		chooserSource.setLetters(Arrays.asList("A","B","C","D","E","F"));
	}


	private class SelectableLetterView extends TextView {

		private String letter;

		public SelectableLetterView(Context context) {
			super(context);
		}

		public void setLetter(String letter){
			this.letter = letter;
			this.setText(letter);
		}
	}


	public class ChooserSource extends TableLayout {

		private TableRow lettersRow;

		private List<SelectableLetterView> selectableLetterViews;


		public ChooserSource(Context context) {
			super(context);
			setupRow();
		}

		private void setupRow() {
			lettersRow = new TableRow(getContext());
			lettersRow.setBackgroundColor(0xff00ff00);
			LayoutParams rowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			this.addView(lettersRow,rowParams);
		}

		public void setLetters(List<String> letters){

			for (String letter : letters) {
				SelectableLetterView selectableLetter = new SelectableLetterView(getContext());
				selectableLetter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1));
				selectableLetter.setGravity(TableLayout.TEXT_ALIGNMENT_CENTER);
				selectableLetter.setLetter(letter);
				lettersRow.addView(selectableLetter);
			}
		}
	}


}
