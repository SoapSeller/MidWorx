package com.fun.midworx.views;

import android.content.Context;
import android.content.res.TypedArray;
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

    private Bitmap tile;
    private float scrollX;
    private int tilesX;
    private int tilesY;

    private static final int DEFAULT_IMG = R.drawable.peacock_blues_5;

    public BackgroundFun(Context context) {
        super(context);
        loadImage(DEFAULT_IMG);
    }

    public BackgroundFun(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context, attrs);
    }

    public BackgroundFun(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttributes(context, attrs);
    }

    /**
     * Reads the attribute tile_image and prepares the resources.
     *
     * @param context from ctor
     * @param attrs from ctor
     */
    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.BackgroundFun);
        loadImage(values.getResourceId(R.styleable.BackgroundFun_tile_image, DEFAULT_IMG));
    }

    /**
     * Loads an image resource and stores the bitmap in 'tile'.
     * @param resource drawable resource id
     */
    private void loadImage(int resource) {
        tile = BitmapFactory.decodeResource(getResources(), resource);
        tilesX = tilesY = 1;
        scrollX = 0.0f;

        // Make sure we get a callback when
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // Ensure it gets called only once :
                getViewTreeObserver().removeGlobalOnLayoutListener(this);

                onResize();
            }
        });
    }

    /**
     * Called when the view is resized (see loadImage).
     */
    private void onResize() {
        // Compute number of tiles horizontally/vertically
        if (tile != null) {
            tilesX = getWidth() / tile.getWidth() + 2;
            tilesY = getHeight() / tile.getHeight() + 1;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = tile.getWidth();
        int h = tile.getHeight();
        scrollX += 4.25;
        while (scrollX > tile.getWidth()) scrollX -= tile.getWidth();
        for (int x = 0; x < tilesX; x++)
            for (int y = 0; y < tilesY; y++)
                canvas.drawBitmap(tile, x*w-scrollX, y*h, new Paint());
        postInvalidateDelayed(20);
    }
}
