package com.danielkim.soundrecorder.edit.canvases;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.danielkim.soundrecorder.edit.editingoptions.Option;

import java.util.ArrayList;
import java.util.List;

public class OptionsJoystickCanvas extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Float initialX;
    private Float initialY;
    private float currentX;
    private float currentY;
    private static final float TOLERANCE = 100;
    private static final float MAIN_RADIUS = 150;
    private static final float OPTION_RADIUS = 50;
    private static final float OPTION_DISTANCE = MAIN_RADIUS + OPTION_RADIUS + 50;
    private Paint circlePaint, borderPaint;
    private List<Option> optionsList;

    public OptionsJoystickCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        optionsList = new ArrayList<>();

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStyle(Paint.Style.FILL);

        borderPaint = new Paint(circlePaint);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStrokeWidth(10f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                initialY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                currentY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                //if move == up, down, left or right => do respective option
                initialX = null;
                initialY = null;
                break;
        }

        invalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w > 0 && h > 0) {
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // debug for area
//        canvas.drawARGB(255, 0, 0, 0);

        //draw joystick and the different options
        if (initialX != null && initialY != null) {

            for (int i = 0; i < optionsList.size(); i++) {
                Point point = getOptionPosition(i);
                canvas.drawCircle((float) (initialX + point.x), (float) (initialY + point.y), OPTION_RADIUS, circlePaint);
            }

            canvas.drawCircle(initialX, initialY, MAIN_RADIUS, circlePaint);

        } else {
            canvas.drawColor(Color.WHITE);
        }
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight() - 10, borderPaint);
    }

    public void setOptionsList(List<Option> options) {
        this.optionsList = options;
    }

    public void addOption(Option op) {
        optionsList.add(op);
    }

    private Point getOptionPosition(int index) {
        double angle = (360.0 / optionsList.size()) * index;
        double x = Math.cos(angle) * OPTION_DISTANCE;
        double y = Math.sin(angle) * OPTION_DISTANCE;
        return new Point(x, y);
    }

    class Point {

        double x;
        double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
