package com.danielkim.soundrecorder.edit.canvases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.OperationCanceledException;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.AudioChunk;

import java.util.Random;

/* In order for this view to work, it needs an AudioChunk to render, thus we have to create this
programmatically.
 */
@SuppressLint("ViewConstructor")
public class AudioChunkCanvas extends View {
	
	public static final int GAP = 4;
	public static final int SCALE = 100;
	public static final int TOLERANCE = 20;
	
	private static final float[] BUFFER = new float[1000];
	
	private AudioChunk chunk;
	private Path mPath;
	private Paint chunkPaint, cursorPaint, borderPaint;
	private Float startCursor, endCursor;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	
	private Context context;
	
	public AudioChunkCanvas(Context context, AudioChunk chunk) {
		super(context);
		this.context = context;
		
		mPath = new Path();
		
		chunkPaint = new Paint();
		chunkPaint.setAntiAlias(true);
		chunkPaint.setColor(getResources().getColor(R.color.primary));
		chunkPaint.setStyle(Paint.Style.STROKE);
		chunkPaint.setStrokeJoin(Paint.Join.ROUND);
		chunkPaint.setStrokeWidth(4f);
		
		cursorPaint = new Paint();
		cursorPaint.setAntiAlias(true);
		cursorPaint.setColor(getResources().getColor(R.color.cursor));
		cursorPaint.setStyle(Paint.Style.FILL);
		cursorPaint.setAlpha(150);
		cursorPaint.setStrokeWidth(4f);
		
		borderPaint = new Paint();
		borderPaint.setAntiAlias(true);
		borderPaint.setColor(Color.BLACK);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setStrokeWidth(4f);
		
		this.chunk = chunk;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w != 0 && h != 0) {
			mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);
			
			genChunkPath(0, (int) chunk.getLength());
			setSingleCursor(w / (GAP * 2));
		}
	}
	
	public void resize(int height) {
		onSizeChanged((int) (chunk.getLength() * GAP), height, getWidth(), getHeight());
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		mCanvas.drawRect(0, 0, getCanvasWidth(), getCanvasHeight(), borderPaint);
		mCanvas.drawPath(mPath, chunkPaint);
		
		if (isCursorPresent()) {
			float end = Math.max(endCursor, startCursor + GAP);
			end = Math.max(end, startCursor);
			
			float start = Math.min(startCursor, endCursor);
			mCanvas.drawRect(start, 0, end, getHeight(), cursorPaint);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				setSingleCursor(event.getX());
				moveSingleCursor(event.getX());
				break;
			
			case MotionEvent.ACTION_MOVE:
				endCursor = event.getX();
				break;
		}
		invalidate();
		
		return true;
	}
	
	public void moveSingleCursor(float newX) {
		if (Math.abs(newX - getCursor()[0]) <= TOLERANCE) {
			startCursor = endCursor = newX;
		}
	}
	
	public boolean isSingleCursor() {
		return startCursor != null && endCursor != null && startCursor == endCursor;
	}
	
	public void genChunkPath(int startIndex, int endIndex) {
		final float MIDDLE = mCanvas.getHeight() / 2;
		mPath.reset();
		
		long length, start = startIndex;
		float curX = 0;
		do {
			length = chunk.getSamples(startIndex, BUFFER);
			for (int i = 0; i < length; i++) {
				float curY = (BUFFER[i] * SCALE) + MIDDLE;
				if (mPath.isEmpty()) {
					mPath.moveTo(curX, curY);
				}
				
				mPath.lineTo(curX, curY);
				curX += GAP;
			}
			start += length;
		} while (length == BUFFER.length);
		
//		float prevY = (chunk.getSample(startIndex) * SCALE) + MIDDLE;
//		mPath.moveTo(0, prevY);
//
//		for (int i = startIndex; i <= endIndex; i++) {
//			float curX = GAP * i;
//			float curY = (chunk.getSample(i) * SCALE) + MIDDLE;
//
//			mPath.lineTo(curX, curY);
//		}
//
//		invalidate();
	}
	
	public boolean isCursorPresent() {
		return startCursor != null && endCursor != null;
	}
	
	public void setSingleCursor(float cursor) {
		startCursor = endCursor = cursor;
	}
	
	public float[] getCursor() {
		if (isCursorPresent()) {
			if (!isSingleCursor()) {
				float start = Math.min(startCursor, endCursor);
				float end = Math.max(startCursor, endCursor);
				return new float[]{start, end};
			}
			
			return new float[]{startCursor};
		}
		
		return null;
	}
	
	public Bitmap getmBitmap() {
		return mBitmap;
	}
	
	public AudioChunk getChunk() {
		return chunk;
	}
	
	public int getCanvasWidth() {
		return mCanvas.getWidth();
	}
	
	public int getCanvasHeight() {
		return mCanvas.getHeight();
	}
}
