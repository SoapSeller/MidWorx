package com.fun.midworx.com.fun.midworx.views;

import android.view.View;
import com.fun.midworx.IChooserStateChange;
import com.fun.midworx.LetterChooserState;
import android.widget.*;
import com.fun.midworx.R;

import java.util.List;

/**
 * Created: 6/29/13 7:59 PM
 */
public class LetterOrganizer implements IChooserStateChange {

	LetterChooserState letterChooserState = new LetterChooserState();

	private final FrameLayout letterOrganizerContainer;

	public LetterOrganizer(FrameLayout letterOrganizerContainer) {
		this.letterOrganizerContainer = letterOrganizerContainer;
		this.letterChooserState.onStateChange(this);
		this.setupButtonListeners();
	}

	private void setupButtonListeners() {

		TableRow selectable = ((TableRow)letterOrganizerContainer.findViewById(R.id.selectable_letters));
		for (int i=0; i< selectable.getChildCount(); i++){
			selectable.getChildAt(i).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					selectableClicked(((TableRow) v.getParent()).indexOfChild(v));
				}
			});
		}

		TableRow selected = ((TableRow)letterOrganizerContainer.findViewById(R.id.selected_letters));
		for (int i=0; i< selected.getChildCount(); i++){
			selected.getChildAt(i).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					selectedClicked(((TableRow) v.getParent()).indexOfChild(v));
				}
			});
		}
	}

	public void setLettersPool(List<String> letters) {
		letterChooserState.setLettersPool(letters);
	}

	private void selectableClicked(int index){
		this.letterChooserState.chooseLetter(index);
	}

	private void selectedClicked(int index){
		this.letterChooserState.unChooseLetter(index);
	}

	public String getCurrentGuessAndReset() {
		return this.letterChooserState.getChosenWordAndReset();
	}

	@Override
	public void chooserStateChane(List<String> letterPool, List<Integer> sortedSelected) {
		TableRow selectable = ((TableRow)letterOrganizerContainer.findViewById(R.id.selectable_letters));
		for (int i=0; i< selectable.getChildCount(); i++){
			if (sortedSelected.contains(i)){
				((Button)selectable.getChildAt(i)).setText("");
			} else{
				((Button)selectable.getChildAt(i)).setText(letterPool.get(i));
			}
		}

		TableRow selected = ((TableRow)letterOrganizerContainer.findViewById(R.id.selected_letters));
		for (int i=0; i< selected.getChildCount(); i++){
			if (sortedSelected.size() > i){
				((Button)selected.getChildAt(i)).setText(letterPool.get( sortedSelected.get(i) ) );
			} else{
				((Button)selected.getChildAt(i)).setText("");
			}
		}
	}


}
