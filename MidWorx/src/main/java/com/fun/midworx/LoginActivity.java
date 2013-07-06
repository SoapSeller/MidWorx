package com.fun.midworx;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
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
