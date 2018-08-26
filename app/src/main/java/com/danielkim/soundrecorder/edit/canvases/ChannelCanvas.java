package com.danielkim.soundrecorder.edit.canvases;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.Channel;

import java.util.ArrayList;
import java.util.List;

public class ChannelCanvas extends View {
	
	private Channel channel;
	private List<AudioChunkCanvas> chunkCanvasList;
	
	Context context;
	
	public ChannelCanvas(Context context, Channel channel) {
		super(context);
		
		this.channel = channel;
		this.context = context;
		chunkCanvasList = new ArrayList<>();
		
		assembleAudioChunkCanvases();
	}
	
	private void assembleAudioChunkCanvases() {
		for (int i = 0; i < channel.getDataSize(); i++) {
			AudioChunkCanvas chunkCanvas = new AudioChunkCanvas(context, channel.getChunk(i));
			chunkCanvasList.add(chunkCanvas);
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		int spaceTaken = 0;
		AudioChunk prevChunk = null;
		for (AudioChunkCanvas audioChunkCanvas : chunkCanvasList) {
			if (audioChunkCanvas.getCanvasWidth() <= 0) {
				resize(getWidth());
			}
			audioChunkCanvas.draw(null);
			Bitmap chunkBitmap = audioChunkCanvas.getmBitmap();
			int gap = 0;
			if (prevChunk != null) {
				gap = (int) ((audioChunkCanvas.getChunk().getStartIndex() - prevChunk.getEndIndex()) * AudioChunkCanvas.GAP);
			} else {
				gap = (int) ((audioChunkCanvas.getChunk().getStartIndex()) * AudioChunkCanvas.GAP);
			}
			canvas.drawBitmap(chunkBitmap, spaceTaken + gap, 0, null);
			spaceTaken += (audioChunkCanvas.getCanvasWidth() + gap);
			prevChunk = audioChunkCanvas.getChunk();
		}
	}
	
	public void resize(int width) {
		resize(width, getHeight());
	}
	
	public void resize(int width, int height) {
		int totalWidth = 0;
		for (AudioChunkCanvas canvas : chunkCanvasList) {
			canvas.resize(height);
			totalWidth += canvas.getCanvasWidth();
		}
		
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
		params.width = totalWidth;
		params.height = height;
		setLayoutParams(params);
		
	}
}
