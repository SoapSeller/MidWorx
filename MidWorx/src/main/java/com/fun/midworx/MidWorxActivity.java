package com.fun.midworx;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MidWorxActivity extends Activity {
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 11) {
            dimSystemUi();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            if (Build.VERSION.SDK_INT >= 11) {
                dimSystemUi();
            }
        }
    }

    @TargetApi(11)
    private void dimSystemUi() {
        final View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }
}
