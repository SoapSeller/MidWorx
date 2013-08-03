package com.fun.midworx.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.fun.midworx.R;

/**
 * A view showing a scrolling background on the X-axis.
 * The background image is read from the application's resources.
 * Tiling is carried out on both axes.
 */
public class BackgroundFun extends View {

    private Bitmap cow;
    private float scrollX;
    private int tilesX;
    private int tilesY;

    public BackgroundFun(Context context) {
        super(context);
        loadImage();
    }

    public BackgroundFun(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadImage();
    }

    public BackgroundFun(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        loadImage();
    }

    private void loadImage() {
        cow = BitmapFactory.decodeResource(getResources(), R.drawable.peacock_blues_5);
        tilesX = tilesY = 1;
        scrollX = 0.0f;

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                // Ensure you call it only once :
                getViewTreeObserver().removeGlobalOnLayoutListener(this);

                // Here you can get the size :)
                tilesX = getWidth() / cow.getWidth() + 2;
                tilesY = getHeight() / cow.getHeight() + 1;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = cow.getWidth();
        int h = cow.getHeight();
        scrollX += 4.25;
        while (scrollX > cow.getWidth()) scrollX -= cow.getWidth();
        for (int x = 0; x < tilesX; x++)
            for (int y = 0; y < tilesY; y++)
                canvas.drawBitmap(cow, x*w-scrollX, y*h, new Paint());
        postInvalidateDelayed(20);
    }
}
