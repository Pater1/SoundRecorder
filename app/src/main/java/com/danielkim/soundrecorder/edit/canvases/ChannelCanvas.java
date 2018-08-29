package com.danielkim.soundrecorder.edit.canvases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.OperationCanceledException;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.Channel;
import com.danielkim.soundrecorder.edit.fragments.DeckFragment;
import com.danielkim.soundrecorder.edit.listeners.LongPressGestureListener;

import java.util.ArrayList;
import java.util.List;

import static com.danielkim.soundrecorder.edit.canvases.AudioChunkCanvas.GAP;

@SuppressLint("ViewConstructor")
public class ChannelCanvas extends View {
	
	public static final int TOLERANCE = 50;
	
	private Channel channel;
	private List<AudioChunkCanvas> chunkCanvasList;
	private Float startCursor, endCursor;
	private Paint cursorPaint, selectedPaint;
	private int channelIndex;
	private DeckFragment deckFragment;
	private boolean areCanvasesInit;
	private GestureDetector.SimpleOnGestureListener longPressListener;
	private GestureDetectorCompat gestureDetector;
	private long sampleBegin, sampleEnd;
	
	Context context;
	
	public ChannelCanvas(Context context, Channel channel, int channelIndex, DeckFragment deckFragment) {
		super(context);
		
		this.channel = channel;
		this.context = context;
		this.channelIndex = channelIndex;
		this.deckFragment = deckFragment;
		chunkCanvasList = new ArrayList<>();
		longPressListener = new LongPressGestureListener(deckFragment, this);
		gestureDetector = new GestureDetectorCompat(context, longPressListener);
		
		cursorPaint = new Paint();
		cursorPaint.setAntiAlias(true);
		cursorPaint.setColor(getResources().getColor(R.color.cursor));
		cursorPaint.setStyle(Paint.Style.FILL);
		cursorPaint.setAlpha(150);
		cursorPaint.setStrokeWidth(4f);
		
		selectedPaint = new Paint(cursorPaint);
		selectedPaint.setStyle(Paint.Style.STROKE);
		selectedPaint.setAlpha(255);
		selectedPaint.setStrokeWidth(20f);
		
		assembleAudioChunkCanvases();
	}
	
	private void assembleAudioChunkCanvases() {
		for (int i = 0; i < channel.getDataSize(); i++) {
			AudioChunkCanvas chunkCanvas = new AudioChunkCanvas(context, channel.get(i));
			//chunkCanvasList.add(chunkCanvas);
		}
	}
	
	public boolean addChunk(AudioChunk chunk) {
		if (channel.getChunksForIndexes(chunk.getStartIndex(), chunk.getEndIndex()).size() >= 1) {
			return false;
		}
		channel.add(chunk);
		AudioChunkCanvas chunkCanvas = new AudioChunkCanvas(context, chunk);
		//chunkCanvasList.add(chunkCanvas);
		return true;
	}
	
	public void removeChunk(AudioChunk chunk) {
		channel.remove(chunk);
		regenChunks();
	}
	
	public void regenChunks() {
		chunkCanvasList.clear();
		assembleAudioChunkCanvases();
		invalidate();
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		canvas.drawColor(Color.LTGRAY);
		chunkCanvasList.clear();
		
		long lastEnd = 0;
		for (int i = 0; i < channel.getDataSize(); i++) {
			if (channel.get(i).getEndIndex() > lastEnd) {
				lastEnd = channel.get(i).getEndIndex();
				AudioChunk audioChunk = channel.get(i);
				AudioChunkCanvas audioChunkCanvas = new AudioChunkCanvas(context, audioChunk);
				chunkCanvasList.add(audioChunkCanvas);
				
				long end = Math.min(sampleEnd, audioChunkCanvas.getChunk().getEndIndex());
				long begin = Math.max(sampleBegin, audioChunkCanvas.getChunk().getStartIndex());
				audioChunkCanvas.setSampleBegin(begin);
				audioChunkCanvas.setSampleEnd(end);
				audioChunkCanvas.resize((int) (AudioChunkCanvas.GAP * (end - begin)), DeckFragment.CHANNEL_HEIGHT);
			}
		}
		resize((int) (lastEnd * GAP), DeckFragment.CHANNEL_HEIGHT);
		for (AudioChunkCanvas audioChunkCanvas : chunkCanvasList) {
			if (audioChunkCanvas.getChunk().getEndIndex() < sampleBegin || audioChunkCanvas.getChunk().getStartIndex() > sampleEnd) {
				continue;
			}
			
			audioChunkCanvas.draw(canvas);
			Bitmap chunkBitmap = audioChunkCanvas.getmBitmap();
			canvas.drawBitmap(chunkBitmap, Math.max(audioChunkCanvas.getChunk().getStartIndex(), sampleBegin)* GAP, 0, null);
		}
		
		if (isCursorPresent()) {
			canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), selectedPaint);
			float end = Math.max(endCursor, startCursor + GAP);
			end = Math.max(end, startCursor);
			
			float start = Math.min(startCursor, endCursor);
			canvas.drawRect(start, 0, end, getHeight(), cursorPaint);
		}
		
		invalidate();
	}
	
	private float prevX;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		
		float touchX = event.getX();
		float touchY = event.getY();
		float distanceDragged = touchX - prevX;
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				setSingleCursor(touchX);
				moveSingleCursor(touchX);
				deckFragment.updateCursor(channelIndex);
				prevX = touchX;
				if (getNearestAudioChunk() != null) {
					gestureDetector.onTouchEvent(event);
				}
				break;
			
			case MotionEvent.ACTION_MOVE:
				gestureDetector.onTouchEvent(event);
				if (!deckFragment.isDragging()) {
					if (Math.abs(prevX - touchX) >= TOLERANCE) {
						endCursor = touchX;
					}
				} else {
					AudioChunk chunkDragged = deckFragment.getChunkDragged();
					if (touchY < getLayoutParams().height && touchY >= 0) {
						if (chunkDragged.getStartIndex() > 0 || distanceDragged > 0) {
							long indexSkipped = (long) (distanceDragged / GAP);
							chunkDragged.setStartIndex(chunkDragged.getStartIndex() + indexSkipped);
							List<AudioChunk> chunksFound = channel.getChunksForIndexes(
									chunkDragged.getStartIndex(), chunkDragged.getEndIndex());
							if (chunksFound.size() > 1) {
								chunkDragged.setStartIndex(chunkDragged.getStartIndex() - indexSkipped);
							}
							prevX = touchX;
							
							for (AudioChunkCanvas audioChunkCanvas : chunkCanvasList) {
								if (audioChunkCanvas.getChunk() == chunkDragged) {
									long end = Math.min(sampleEnd, audioChunkCanvas.getChunk().getEndIndex());
									long begin = Math.max(sampleBegin, audioChunkCanvas.getChunk().getStartIndex());
									audioChunkCanvas.setSampleBegin(begin);
									audioChunkCanvas.setSampleEnd(end);
									audioChunkCanvas.resize((int) (AudioChunkCanvas.GAP * (end - begin)), DeckFragment.CHANNEL_HEIGHT);
									audioChunkCanvas.draw(audioChunkCanvas.getmCanvas());
									break;
								}
							}
						}
					} else {
						deckFragment.trySwitchChannel(event, chunkDragged, this);
					}
				}
				break;
			
			case MotionEvent.ACTION_UP:
				gestureDetector.onTouchEvent(event);
				deckFragment.stopDragging();
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
	
	public void resize(int width, int height) {
		if (!areCanvasesInit) {
			resizeHeight(height);
		}
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
		params.width = Math.max(width, params.width);
		params.height = height;
		setLayoutParams(params);
	}
	
	public int getChannelIndex() {
		return channelIndex;
	}
	
	public boolean isSingleCursor() {
		return startCursor != null && endCursor != null && startCursor == endCursor;
	}
	
	public boolean contains(AudioChunk chunk) {
		return channel.contains(chunk);
	}
	
	public boolean isCursorPresent() {
		return startCursor != null && endCursor != null;
	}
	
	public void setSingleCursor(long index) {
		startCursor = endCursor = (float) (index * GAP);
	}
	
	private void setSingleCursor(Float cursor) {
		startCursor = endCursor = cursor;
	}
	
	public void disableCursor() {
		setSingleCursor(null);
	}
	
	public AudioChunk getNearestAudioChunk() {
		if (!isCursorPresent()) {
			throw new OperationCanceledException();
		}
		
		long[] cursors = getCursor();
		if (cursors.length != 1) {
			setSingleCursor(cursors[0]);
		}
		return channel.getChunkForIndex(cursors[0]);
	}
	
	public List<AudioChunk> getSelectedAudioChunks() {
		long[] cursors = getCursor();
		if (cursors == null) {
			throw new OperationCanceledException("the cursor was not present in this channel");
		}
		if (cursors.length != 2) {
			throw new OperationCanceledException("there was only 1 cursor found");
		}
		
		return channel.getChunksForIndexes(cursors[0], cursors[1]);
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
	
	public void setSampleBegin(long sampleBegin) {
		this.sampleBegin = sampleBegin;
		for (int i = 0; i < chunkCanvasList.size(); i++) {
			AudioChunkCanvas audioChunkCanvas = chunkCanvasList.get(i);
			long end = Math.min(sampleEnd, audioChunkCanvas.getChunk().getEndIndex());
			long begin = Math.max(sampleBegin, audioChunkCanvas.getChunk().getStartIndex());
			audioChunkCanvas.setSampleBegin(begin);
			audioChunkCanvas.setSampleEnd(end);
			audioChunkCanvas.resize((int) (AudioChunkCanvas.GAP * (end - begin)), DeckFragment.CHANNEL_HEIGHT);
		}
	}
	
	public void setSampleEnd(long sampleEnd) {
		this.sampleEnd = sampleEnd;
		for (int i = 0; i < chunkCanvasList.size(); i++) {
			AudioChunkCanvas audioChunkCanvas = chunkCanvasList.get(i);
			long end = Math.min(sampleEnd, audioChunkCanvas.getChunk().getEndIndex());
			long begin = Math.max(sampleBegin, audioChunkCanvas.getChunk().getStartIndex());
			audioChunkCanvas.setSampleBegin(begin);
			audioChunkCanvas.setSampleEnd(end);
			audioChunkCanvas.resize((int) (AudioChunkCanvas.GAP * (end - begin)), DeckFragment.CHANNEL_HEIGHT);
		}
	}
}
