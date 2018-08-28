package com.danielkim.soundrecorder.edit.canvases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
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
	public static final int SCALE = 100;
	
	private static final float[] BUFFER = new float[8192 / GAP];
	
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
			
			genChunkPath(0, BUFFER.length);
			invalidate();
		}
	}
	
	public void resize(int height) {
		onSizeChanged(BUFFER.length * GAP, height, getWidth(), getHeight());
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(mCanvas);
		
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
		
		long length, start = startIndex;
		float curX = 0;
		long totalLength = 0;
//		do {
			length = chunk.getSamples(start, BUFFER);
			for (int i = 0; i < length; i++) {
				float curY = (BUFFER[i] * SCALE) + MIDDLE;
//				Toast.makeText(context, BUFFER[i] + "", Toast.LENGTH_SHORT).show();
//				Toast.makeText(context, curY + " y", Toast.LENGTH_SHORT).show();
				Log.d("buffer", BUFFER[i] + "");
				Log.d("y", curY + "");
				if (mPath.isEmpty()) {
					mPath.moveTo(curX, curY);
				}
				
				mPath.lineTo(curX, curY);
				curX += GAP;
			}
			start += length;
//		} while (length >= 0);
	}
	
	public Bitmap getmBitmap() {
		return mBitmap;
	}
	
	public AudioChunk getChunk() {
		return chunk;
	}
	
	public int getCanvasWidth() {
		if (mCanvas == null) {
			return 0;
		}
		return mCanvas.getWidth();
	}
	
	public int getCanvasHeight() {
		return mCanvas.getHeight();
	}
}
