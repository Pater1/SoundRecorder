package com.danielkim.soundrecorder.edit.canvases;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;

public class AudioChunkCanvas extends View {
	
	private static final int GAP = 4;
	private static final int SCALE = 1;
	
	private AudioChunk chunk;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mPaint;
	private int startHighlight, endHighlight;
	
	Context context;
	
	public AudioChunkCanvas(Context context, AudioChunk chunk) {
		super(context);
		
		mPath = new Path();
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(getResources().getColor(R.color.primary));
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeWidth(4f);
		
		this.chunk = chunk;
		drawChunk();
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
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		throw new NotImplementedException();
	}
	
	public void drawChunk() {
		mPath.reset();
		float prevX = 0, prevY = 0;
		
		for (int i = 0; i < chunk.getLength(); i++) {
			float curX = GAP * i;
			float curY = chunk.getSample(i) * SCALE;
			
			mPath.quadTo(prevX, prevY, curX, curY);
			
			prevX = curX;
			prevY = curY;
		}
	}
}
