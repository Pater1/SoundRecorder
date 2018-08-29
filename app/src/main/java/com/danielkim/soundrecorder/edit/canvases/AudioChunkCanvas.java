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
	
	public static int GAP = 4;
	public static final int SCALE = 100;
	
	private float[] BUFFER = new float[512];
	
	private AudioChunk chunk;
	private Path mPath;
	private Paint chunkPaint, borderPaint;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	
	private Context context;
	private long sampleBegin;
	private long sampleEnd;
	
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
		if (w > 0 && h > 0) {
			mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);
			
			genChunkPath();
			invalidate();
		}
	}
	
	public void resize(int height) {
		onSizeChanged((int) ((sampleEnd - sampleBegin) * GAP), height, getWidth(), getHeight());
	}
	
	public void resize(int width, int height) {
		onSizeChanged(width, height, getWidth(), getHeight());
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
	
	public void genChunkPath() {
		// TODO make logic run on background thread
		final float MIDDLE = mCanvas.getHeight() / 2;
		
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
				mPath.reset();
				Log.d("audio_chunk_render", "Start gen chunk path");
				
				long ptr = sampleBegin;
				float curX = 0;
				long length = 0, samplesRead;
				long windowLength = (sampleEnd - sampleBegin);
				do {
					samplesRead = chunk.getSamples(ptr, BUFFER);
					if (ptr + samplesRead > windowLength) {
						samplesRead = windowLength - length;
						if(samplesRead == 0){
							samplesRead = -1;
						}
					}
					
					for (int i = 0; i < samplesRead; i++) {
						float curY = (BUFFER[i] * SCALE) + MIDDLE;
						if (mPath.isEmpty()) {
							mPath.moveTo(curX, curY);
						}
						
						mPath.lineTo(curX, curY);
						curX += GAP;
					}
					ptr += samplesRead;
					length += samplesRead;
				} while (length < windowLength && samplesRead >= 0);
				
//				invalidate();
//			}
//		});
		
//		t.run();
//		while (t.isAlive()) {}
		Log.d("audio_chunk_render", "Done rendering audiochunk");
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
	
	public void setSampleBegin(long sampleBegin) {
		this.sampleBegin = sampleBegin;
	}
	
	public void setSampleEnd(long sampleEnd) {
		this.sampleEnd = sampleEnd;
		if (sampleEnd == 0) {
			Log.d("begin_audiochunkcanvas", sampleBegin + "");
			Log.d("end_audiochunkcanvas", sampleEnd + "");
		}
	}
	
	public Canvas getmCanvas() {
		return mCanvas;
	}
}
