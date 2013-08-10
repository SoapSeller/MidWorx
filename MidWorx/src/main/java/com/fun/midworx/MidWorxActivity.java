package com.fun.midworx;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.google.example.games.basegameutils.BaseGameActivity;

public class MidWorxActivity extends Activity {
    protected Tracker tracker;

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

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
        tracker = EasyTracker.getTracker();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
        tracker = null;
    }

    /**
     * Delegates call to GA tracker.
     */
    protected void sendEvent(String category, String action, String label) {
        // TODO(misha): Do not crash if fails in production.
        tracker.sendEvent(category, action, label, 1L);
    }
}
