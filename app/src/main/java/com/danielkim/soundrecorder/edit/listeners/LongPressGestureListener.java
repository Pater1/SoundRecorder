package com.danielkim.soundrecorder.edit.listeners;

import android.content.Context;
import android.os.Build;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.danielkim.soundrecorder.edit.canvases.ChannelCanvas;
import com.danielkim.soundrecorder.edit.fragments.DeckFragment;

public class LongPressGestureListener extends GestureDetector.SimpleOnGestureListener {
	
	private DeckFragment deckFragment;
	private ChannelCanvas channelCanvas;
	
	public LongPressGestureListener(DeckFragment deckFragment, ChannelCanvas channelCanvas) {
		super();
		
		this.deckFragment = deckFragment;
		this.channelCanvas = channelCanvas;
	}
	
	@Override
	public void onLongPress(MotionEvent e) {
		super.onLongPress(e);

		Vibrator v = (Vibrator) deckFragment.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(100);
		
		deckFragment.startDragging(channelCanvas.getNearestAudioChunk());
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		return super.onDown(e);
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return true;
	}
}
