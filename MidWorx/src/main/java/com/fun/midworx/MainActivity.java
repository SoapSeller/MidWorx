package com.fun.midworx;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fun.midworx.com.fun.midworx.views.WordsBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    ArrayList<WordsBox> mBoxes = new ArrayList<WordsBox>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //dummy data
        ArrayList<String> words3 = new ArrayList<String>(Arrays.asList("rtg","nsr","uda","sdf","yui"));
        ArrayList<String> words4 = new ArrayList<String>(Arrays.asList("rtdg","nsar","yisr"));
        ArrayList<String> words5 = new ArrayList<String>(Arrays.asList("rtgsr","udsra"));
        ArrayList<String> words6 = new ArrayList<String>(Arrays.asList("rtguda"));


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5,5,5,5);
        LinearLayout boxesLayout= (LinearLayout) findViewById(R.id.words_boxes_layout);

        addBox(boxesLayout,params,words3);
        addBox(boxesLayout,params,words4);
        addBox(boxesLayout,params,words5);
        addBox(boxesLayout,params,words6);
    }

    private void addBox(LinearLayout boxesLayout,LinearLayout.LayoutParams params, ArrayList<String> words) {
        WordsBox box = new WordsBox(this,words);
        boxesLayout.addView(box,params);
        mBoxes.add(box);
    }

    private void guessWord(String word) {


    }

}
