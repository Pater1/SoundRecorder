package com.danielkim.soundrecorder.edit.canvases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.OperationCanceledException;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.AudioChunk;

/* In order for this view to work, it needs an AudioChunk to render, thus we have to create this
programmatically.
 */
@SuppressLint("ViewConstructor")
public class AudioChunkCanvas extends View {
	
	public static final int GAP = 4;
	public static final int SCALE = 1;
	public static final int TOLERANCE = 20;
	
	private AudioChunk chunk;
	private Path mPath;
	private Paint chunkPaint;
	private Paint cursorPaint;
	private Float startCursor, endCursor;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	

	
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
		
		this.chunk = chunk;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w != 0 && h != 0) {
			mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);
			
			genChunkPath((int) chunk.getStartIndex(), (int) chunk.getEndIndex());
			setSingleCursor(w / (GAP * 2));
		}
	}
	
	public void resize(int width, int height) {
		onSizeChanged((int) (chunk.getLength() * GAP), height, getWidth(), getHeight());
	}
	
	public void resize(int width) {
		resize(width, getHeight());
	}
 
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawPath(mPath, chunkPaint);
		mCanvas.drawPath(mPath, chunkPaint);
		
		if (isCursorPresent()) {
			float end = Math.max(endCursor, startCursor + GAP);
			end = Math.max(end, startCursor);
			
			float start = Math.min(startCursor, endCursor);
			canvas.drawRect(start, 0, end, getHeight(), cursorPaint);
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
		if (Math.abs(newX - getCursor()) <= TOLERANCE) {
			startCursor = endCursor = newX;
		}
	}
	
	public boolean isSingleCursor() {
		return startCursor != null && endCursor != null && startCursor == endCursor;
	}
	
	public void genChunkPath(int startIndex, int endIndex) {
		mPath.reset();
		float prevX = startIndex, prevY = chunk.getSample(endIndex);
		endIndex = Math.min(endIndex, getWidth() / GAP);
		
		for (int i = startIndex; i <= endIndex; i++) {
			float curX = GAP * i;
			float curY = chunk.getSample(i) * SCALE;
			
			mPath.quadTo(prevX, prevY, curX, curY);
			
			prevX = curX;
			prevY = curY;
		}
		
		invalidate();
	}
	
	public boolean isCursorPresent() {
		return startCursor != null && endCursor != null;
	}
	
	public void setSingleCursor(float cursor) {
		startCursor = endCursor = cursor;
	}
	
	public Float getCursor() {
		if (!isSingleCursor()) {
			throw new OperationCanceledException("there are 2 cursors present");
		}
		
		return startCursor;
	}
	
	public Bitmap getmBitmap() {
		return mBitmap;
	}
	
	public AudioChunk getChunk() {
		return chunk;
	}
}
