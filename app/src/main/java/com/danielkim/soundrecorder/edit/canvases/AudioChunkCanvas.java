package com.danielkim.soundrecorder.edit.canvases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.AudioChunk;

/* In order for this view to work, it needs an AudioChunk to render, thus we have to create this
programmatically.
 */
@SuppressLint("ViewConstructor")
public class AudioChunkCanvas extends View {
	
	public static final int GAP = 4;
	public static final int SCALE = 100;
	
	private static final float[] BUFFER = new float[1000];
	
	private AudioChunk chunk;
	private Path mPath;
	private Paint chunkPaint, borderPaint;
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
		}
	}
	
	public void resize(int height) {
		onSizeChanged((int) (chunk.getLength() * GAP), height, getWidth(), getHeight());
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		mCanvas.drawColor(Color.WHITE);
		mCanvas.drawRect(0, 0, getCanvasWidth(), getCanvasHeight(), borderPaint);
		mCanvas.drawPath(mPath, chunkPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
	
	public void genChunkPath(int startIndex, int endIndex) {
		final float MIDDLE = mCanvas.getHeight() / 2;
		mPath.reset();
		
		long length;
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
		} while (length == BUFFER.length);
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
