package com.danielkim.soundrecorder.edit.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.canvases.AudioChunkCanvas;
import com.danielkim.soundrecorder.edit.fragments.DeckFragment;
import com.danielkim.soundrecorder.fragments.EditFragment;

public class StationaryHorizontalScrollView extends HorizontalScrollView {
	
	private int scrolledDistance;
	private int scrolledLeft;
	
	public StationaryHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
//		return super.onTouchEvent(ev);
		return false;
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		
		int horizontalScrollDistance = l - oldl;
		
		if (horizontalScrollDistance > 0) {
			if (scrolledLeft <= 0) {
				scrolledDistance += horizontalScrollDistance;
				DeckFragment fragment = (DeckFragment) ((ActionBarActivity) getContext())
						.getSupportFragmentManager().findFragmentByTag(EditFragment.DECK_FRAGMENT_TAG);
				fragment.resize(fragment.getGreatestChannelLength() + scrolledDistance);
			} else {
				scrolledLeft -= horizontalScrollDistance;
			}
		} else {
			scrolledLeft += Math.abs(horizontalScrollDistance);
		}
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		return super.onInterceptTouchEvent(ev);
		return false;
	}
}
