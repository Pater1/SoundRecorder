package com.danielkim.soundrecorder.edit.editingoptions;

import android.graphics.Color;

public abstract class Option {
	protected abstract boolean passedDownOnTouchUp(long[] cursorArray, int channelIndex);
	
	protected abstract boolean passedDownOnTouchMove(long[] cursorArray, int channelIndex);
	
	protected abstract boolean passedDownOnTouchDown(long[] cursorArray, int channelIndex);
	
	public abstract int getColor();
	
	private Thread onTouchMove;
	private boolean isUpdating;
	private long[] cursorArray;
	private int channelIndex;
	
	public Option() {
		this.onTouchMove = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isUpdating) {
					passedDownOnTouchMove(cursorArray, channelIndex);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public final boolean onTouchUp(long[] cursorArray, int channelIndex) {
		this.isUpdating = false;
		onTouchMove.interrupt();
		return passedDownOnTouchUp(cursorArray, channelIndex);
	}
	
	public final boolean onTouchDown(long[] cursorArray, int channelIndex) {
		this.channelIndex = channelIndex;
		this.cursorArray = cursorArray;
		this.isUpdating = true;
		if (onTouchMove.isInterrupted()) {
			this.onTouchMove.start();
		}
		return passedDownOnTouchDown(cursorArray, channelIndex);
	}
}
