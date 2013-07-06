package com.fun.midworx;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;

public class MidWorxActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= 11) {
            dimSystemUi();
        }
    }

    @TargetApi(11)
    private void dimSystemUi() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }
}
