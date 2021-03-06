package com.danielkim.soundrecorder.edit.editingoptions;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.danielkim.soundrecorder.edit.canvases.OptionsJoystickCanvas;
import com.danielkim.soundrecorder.edit.canvases.Point;
import com.danielkim.soundrecorder.edit.fragments.DeckFragment;

public abstract class Option {
	protected static DeckFragment UPDATE_FRAGMENT = null;
	public static void setUpdateFragment(DeckFragment df){
		UPDATE_FRAGMENT = df;
	}
	public static DeckFragment getUpdateFragment(){
		return UPDATE_FRAGMENT;
	}

	protected abstract boolean passedDownOnTouchUp(long[] cursorArray, int channelIndex);
	
	protected abstract boolean passedDownOnTouchMove(long[] cursorArray, int channelIndex);
	
	protected abstract boolean passedDownOnTouchDown(long[] cursorArray, int channelIndex);
	
	public abstract int getColor();

	public Bitmap getIcon(Resources res) {return null;}
//	public abstract Bitmap getIcon(Resources res);
	
	private Thread onTouchMove;
	private boolean isUpdating;
	
	public Option() {}
	
	public final boolean onTouchUp(long[] cursorArray, int channelIndex) {
		cancelTouch();
		return passedDownOnTouchUp(cursorArray, channelIndex);
	}

	public final void cancelTouch(){
		this.isUpdating = false;
		this.onTouchMove = null;
	}
	
	public final boolean onTouchDown(final long[] cursorArray, final int channelIndex) {
		this.onTouchMove = new Thread(new Runnable() {
			@Override
			public void run() {
				Thread thisThread;
				do {
					thisThread = Thread.currentThread();
					passedDownOnTouchMove(cursorArray, channelIndex);
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}while (Option.this.isUpdating && thisThread == Option.this.onTouchMove);
			}
		});
		this.isUpdating = true;
		this.onTouchMove.start();
		return passedDownOnTouchDown(cursorArray, channelIndex);
	}

	public Point getCenterOffset() {
		return new Point(0,0);
	}
}
