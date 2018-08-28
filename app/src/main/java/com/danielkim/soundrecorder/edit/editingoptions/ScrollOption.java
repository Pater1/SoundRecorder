package com.danielkim.soundrecorder.edit.editingoptions;

import android.graphics.Color;
import android.graphics.Point;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

public class ScrollOption extends Option {
	
	public static final int MAGNITUDE = 20;
	
	private ScrollView verticalScrollView;
	private HorizontalScrollView horizontalScrollView;
	private Point direction;
	
	public ScrollOption(ScrollView verticalScrollView, Point direction) {
		this.verticalScrollView = verticalScrollView;
		this.direction = direction;
	}
	
	public ScrollOption(HorizontalScrollView horizontalScrollView, Point direction) {
		this.horizontalScrollView = horizontalScrollView;
		this.direction = direction;
	}
	
	@Override
	protected boolean passedDownOnTouchUp(long[] cursorArray, int channelIndex) {
		return false;
	}
	
	@Override
	protected boolean passedDownOnTouchMove(long[] cursorArray, int channelIndex) {
		if(horizontalScrollView != null) {
			if(direction.x == -1) {
				horizontalScrollView.scrollBy(-(MAGNITUDE), 0);
			} else if (direction.x == 1) {
				horizontalScrollView.scrollBy(MAGNITUDE, 0);
			}
		} else if (verticalScrollView != null) {
			if(direction.y == -1) {
				verticalScrollView.scrollBy(0, -(MAGNITUDE));
			} else if (direction.y == 1) {
				verticalScrollView.scrollBy(0, MAGNITUDE);
			}
		}
		return false;
	}
	
	@Override
	protected boolean passedDownOnTouchDown(long[] cursorArray, int channelIndex) {
		return false;
	}
	
	@Override
	public int getColor() {
		return Color.MAGENTA;
	}
}
