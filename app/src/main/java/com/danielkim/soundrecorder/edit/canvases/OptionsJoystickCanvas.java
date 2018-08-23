package com.danielkim.soundrecorder.edit.canvases;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;

public class OptionsJoystickCanvas extends View {
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private float initialX;
    private float initialY;
    private float currentX;
    private float currentY;
    private static final float TOLERANCE = 100;

    Context context;

    public OptionsJoystickCanvas(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                initialY = event.getY();
                //draw joystick and the different options
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                currentY = event.getY();
                
                //if move == up, down, left or right do respective option
                break;
            case MotionEvent.ACTION_UP:
                //erase joystick from the canvas
                break;
        }

        throw new NotImplementedException();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mPath, mPaint);
        canvas.dra
    }
}
