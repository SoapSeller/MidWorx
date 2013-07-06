package com.fun.midworx;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;

import android.view.View;
import android.widget.TextView;

import java.util.List;
//import android.widget.TextView;
//import com.fun.midworx.R;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        updateTextTotalScore();
        findViewById(R.id.start_game_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });

//        LinearLayout layout = new LinearLayout(this);
//        layout.setOrientation(LinearLayout.VERTICAL);
//        final WordFlipper flipper = new WordFlipper(this);
//        setContentView(layout);
//        layout.addView(flipper);
//        flipper.setWord("Rotem");
//        Button show = new Button(this);
//        show.setText("SHOW!");
//        layout.addView(show);
//        show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                flipper.showWord();
//            }
//        });
//        Button hide = new Button(this);
//        hide.setText("HIDE!");
//        layout.addView(hide);
//        hide.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                flipper.hideWord();
//            }
//        });

//        try {
//            Words w = new Words(getApplicationContext());
//
//            long start  = System.currentTimeMillis();
//            List<String> words = w.getWord();
//            for (String s: words) {
//                Log.i("WORDS", s);
//            }
//            Log.i("WORDS", "Time: " + (System.currentTimeMillis()-start));
//        } catch (Exception e){
//            Log.i("WORDS", e.toString());
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {

            if(resultCode == RESULT_OK){
                int sessionScore = data.getIntExtra("score",0);
                int totalScore = getPreferences(MODE_PRIVATE).getInt("total scores", 0);
                getPreferences(MODE_PRIVATE).edit().putInt("total scores",totalScore + sessionScore).commit();
                updateTextTotalScore();
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void updateTextTotalScore() {
        ((TextView)findViewById(R.id.total_score)).setText(getString(R.string.total_score) + " " + getPreferences(MODE_PRIVATE).getInt("total scores", 0));
    }

}
