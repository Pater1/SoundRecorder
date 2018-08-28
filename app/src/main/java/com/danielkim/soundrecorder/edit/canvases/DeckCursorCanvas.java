package com.danielkim.soundrecorder.edit.canvases;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.danielkim.soundrecorder.R;

public class DeckCursorCanvas extends View {
	
	private float cursor;
	private Paint cursorPaint;
	
	public DeckCursorCanvas(Context context) {
		super(context);
		
		initPaint();
	}
	
	public DeckCursorCanvas(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		
		initPaint();
	}
	
	private void initPaint() {
		cursorPaint = new Paint();
		cursorPaint.setAntiAlias(true);
		cursorPaint.setColor(getResources().getColor(R.color.deck_cursor));
		cursorPaint.setStyle(Paint.Style.FILL);
		cursorPaint.setAlpha(255);
		cursorPaint.setStrokeWidth(4f);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(cursor, 0, cursor + AudioChunkCanvas.GAP, canvas.getHeight(), cursorPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		return super.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			cursor = event.getX();
			invalidate();
		}
		
		return false;
	}
	
	public void resize(int width, int height) {
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
		params.width = width;
		params.height = height;
		setLayoutParams(params);
	}
	
	private long getCursor() {
		return (long) (cursor / AudioChunkCanvas.GAP);
	}
}
