package com.danielkim.soundrecorder.edit.canvases;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

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
		for (AudioChunkCanvas audioChunkCanvas : chunkCanvasList) {
			if (audioChunkCanvas.getCanvasWidth() <= 0) {
				resize(getWidth());
			}
//			audioChunkCanvas.draw(canvas);
			audioChunkCanvas.draw(null);
			Bitmap chunkBitmap = audioChunkCanvas.getmBitmap();
			canvas.drawBitmap(chunkBitmap, spaceTaken, 0, null);
			spaceTaken += (audioChunkCanvas.getCanvasWidth());
		}
	}
	
	public void resize(int width) {
		resize(width, getHeight());
	}
	
	public void resize(int width, int height) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
		params.width = width;
		params.height = height;
		setLayoutParams(params);
		
		for (AudioChunkCanvas canvas : chunkCanvasList) {
			canvas.resize(width, height);
		}
	}
}
