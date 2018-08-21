package com.danielkim.soundrecorder.edit.canvases;

import android.content.Context;
import android.view.View;

import com.danielkim.soundrecorder.edit.AudioChunk;

public class AudioChunkCanvas extends View {
	
	private AudioChunk chunk;
	
	public AudioChunkCanvas(Context context, AudioChunk chunk) {
		super(context);
		
		this.chunk = chunk;
	}
}
