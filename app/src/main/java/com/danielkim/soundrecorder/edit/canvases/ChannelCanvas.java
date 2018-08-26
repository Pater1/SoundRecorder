package com.danielkim.soundrecorder.edit.canvases;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.Channel;
import com.danielkim.soundrecorder.edit.fragments.DeckFragment;

import java.util.ArrayList;
import java.util.List;

import static com.danielkim.soundrecorder.edit.canvases.AudioChunkCanvas.GAP;

public class ChannelCanvas extends View {
	
	public static final int TOLERANCE = 20;
	
	private Channel channel;
	private List<AudioChunkCanvas> chunkCanvasList;
	private Float startCursor, endCursor;
	private Paint cursorPaint;
	private int channelIndex;
	private DeckFragment deckFragment;
	private boolean areCanvasesInit;
	
	Context context;
	
	public ChannelCanvas(Context context, Channel channel, int channelIndex, DeckFragment deckFragment) {
		super(context);
		
		this.channel = channel;
		this.context = context;
		this.channelIndex = channelIndex;
		this.deckFragment = deckFragment;
		chunkCanvasList = new ArrayList<>();
		
		cursorPaint = new Paint();
		cursorPaint.setAntiAlias(true);
		cursorPaint.setColor(getResources().getColor(R.color.cursor));
		cursorPaint.setStyle(Paint.Style.FILL);
		cursorPaint.setAlpha(150);
		cursorPaint.setStrokeWidth(4f);
		
		assembleAudioChunkCanvases();
	}
	
	private void assembleAudioChunkCanvases() {
		for (int i = 0; i < channel.getDataSize(); i++) {
			AudioChunkCanvas chunkCanvas = new AudioChunkCanvas(context, channel.getChunk(i));
			chunkCanvasList.add(chunkCanvas);
		}
	}
	
	public void addChunk(AudioChunk chunk) {
		channel.add(chunk);
		AudioChunkCanvas chunkCanvas = new AudioChunkCanvas(context, chunk);
		chunkCanvasList.add(chunkCanvas);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		canvas.drawColor(Color.GRAY);
		
		int spaceTaken = 0;
		AudioChunk prevChunk = null;
		for (AudioChunkCanvas audioChunkCanvas : chunkCanvasList) {
			if (audioChunkCanvas.getCanvasWidth() <= 0) {
				resizeHeight(getHeight());
			}
			audioChunkCanvas.draw(null);
			Bitmap chunkBitmap = audioChunkCanvas.getmBitmap();
			int gap;
			if (prevChunk != null) {
				gap = (int) ((audioChunkCanvas.getChunk().getStartIndex() - prevChunk.getEndIndex()) * GAP);
			} else {
				gap = (int) ((audioChunkCanvas.getChunk().getStartIndex()) * GAP);
			}
			canvas.drawBitmap(chunkBitmap, spaceTaken + gap, 0, null);
			spaceTaken += (audioChunkCanvas.getCanvasWidth() + gap);
			prevChunk = audioChunkCanvas.getChunk();
		}
		
		if (isCursorPresent()) {
			float end = Math.max(endCursor, startCursor + GAP);
			end = Math.max(end, startCursor);
			
			float start = Math.min(startCursor, endCursor);
			canvas.drawRect(start, 0, end, getHeight(), cursorPaint);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				setSingleCursor(event.getX());
				moveSingleCursor(event.getX());
				deckFragment.updateCursor(channelIndex);
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
			setSingleCursor(newX);
		}
	}
	
	public void resizeHeight(int height) {
		int totalWidth = 0;
		for (AudioChunkCanvas canvas : chunkCanvasList) {
			canvas.resize(height);
			totalWidth += canvas.getCanvasWidth();
		}
		
		areCanvasesInit = true;
		resize(totalWidth + 5, height);
	}
	
	public void resizeWidth(int width) {
		resize(width, getHeight());
	}
	
	public void resize(int width, int height) {
		if (!areCanvasesInit) {
			resizeHeight(height);
		}
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
		params.width = width;
		params.height = height;
		setLayoutParams(params);
	}
	
	public boolean isSingleCursor() {
		return startCursor != null && endCursor != null && startCursor == endCursor;
	}
	
	public boolean isCursorPresent() {
		return startCursor != null && endCursor != null;
	}
	
	public void setSingleCursor(Float cursor) {
		startCursor = endCursor = cursor;
	}
	
	public void disableCursor() {
		setSingleCursor(null);
	}
	
	public long[] getCursor() {
		if (isCursorPresent()) {
			if (!isSingleCursor()) {
				float start = Math.min(startCursor, endCursor);
				float end = Math.max(startCursor, endCursor);
				return new long[]{(long) (start / GAP), (long) (end / GAP)};
			}
			
			return new long[]{(long) (startCursor / GAP)};
		}
		
		return null;
	}
}
