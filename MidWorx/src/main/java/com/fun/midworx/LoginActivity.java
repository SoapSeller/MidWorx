package com.fun.midworx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        updateTextTotalScore();
        findViewById(R.id.start_game_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, GameActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        try {
            Words w = new Words(getApplicationContext());

            long start  = System.currentTimeMillis();
            List<String> words = w.getWords();
            for (String s: words) {
                Log.i("WORDS", s);
            }
            Log.i("WORDS", "Time: " + (System.currentTimeMillis()-start));
        } catch (Exception e){
            Log.i("WORDS", e.toString());
        }
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
