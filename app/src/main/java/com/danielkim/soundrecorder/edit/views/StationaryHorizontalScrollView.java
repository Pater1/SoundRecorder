package com.danielkim.soundrecorder.edit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class StationaryHorizontalScrollView extends HorizontalScrollView {
	
	public StationaryHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
//		return super.onTouchEvent(ev);
		return false;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		return super.onInterceptTouchEvent(ev);
		return false;
	}
}
