package com.danielkim.soundrecorder.edit.canvases;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.danielkim.soundrecorder.edit.editingoptions.Option;
import com.danielkim.soundrecorder.edit.fragments.DeckFragment;

import java.util.ArrayList;
import java.util.List;

public class OptionsJoystickCanvas extends View {
	
	private Float initialX;
	private Float initialY;
	private float currentX;
	private float currentY;
	private static final float MAIN_RADIUS = 100;
	private static final float OPTION_RADIUS = 50;
	private static final float OPTION_DISTANCE = MAIN_RADIUS + OPTION_RADIUS + 50;
	private Paint circlePaint, borderPaint, optionPaint;
	private List<Option> optionsList;
	private DeckFragment deckFragment;
	
	public OptionsJoystickCanvas(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		
		optionsList = new ArrayList<>();
		
		circlePaint = new Paint();
		circlePaint.setAntiAlias(true);
		circlePaint.setColor(Color.GRAY);
		circlePaint.setStyle(Paint.Style.FILL);
		
		borderPaint = new Paint(circlePaint);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setColor(Color.BLACK);
		borderPaint.setStrokeWidth(10f);
		
		optionPaint = new Paint(circlePaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Option optionSelected;
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				initialX = event.getX();
				initialY = event.getY();
				optionSelected = getOptionSelected();
				if (optionSelected != null) {
					optionSelected.onTouchDown(deckFragment.getCursorPoints(), deckFragment.getCursorChannelIndex());
				}
				break;
			case MotionEvent.ACTION_MOVE:
				currentX = event.getX();
				currentY = event.getY();
				break;
			case MotionEvent.ACTION_UP:
				//if move == up, down, left or right => do respective option
				optionSelected = getOptionSelected();
				if (optionSelected != null) {
					optionSelected.onTouchUp(deckFragment.getCursorPoints(), deckFragment.getCursorChannelIndex());
					deckFragment.refreshChannel(deckFragment.getCursorChannelIndex());
				}
				initialX = null;
				initialY = null;
				break;
		}
		
		invalidate();
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (initialX != null && initialY != null) {
			Integer closestOptionIndex = getClosestOptionIndex();
			
			for (int i = 0; i < optionsList.size(); i++) {
				Point point = getOptionPosition(i);
				optionPaint.setColor(optionsList.get(i).getColor());
				canvas.drawCircle((float) (initialX + point.x), (float) (initialY + point.y), OPTION_RADIUS, optionPaint);
				if (closestOptionIndex != null && closestOptionIndex == i) {
					canvas.drawCircle(initialX, initialY, MAIN_RADIUS, optionPaint);
				}
			}
			
			if (closestOptionIndex == null) {
				canvas.drawCircle(initialX, initialY, MAIN_RADIUS, circlePaint);
			}
			
		} else {
			canvas.drawColor(Color.WHITE);
		}
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight() - 10, borderPaint);
	}
	
	public void setOptionsList(List<Option> options) {
		this.optionsList = options;
	}
	
	public void addOption(Option op) {
		optionsList.add(op);
	}
	
	public void setDeckFragment(DeckFragment deckFragment) {
		this.deckFragment = deckFragment;
	}
	
	private Point getOptionPosition(int index) {
		double angle = Math.toRadians(360.0 / optionsList.size() * index);
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		double x = cos * OPTION_DISTANCE;
		double y = sin * OPTION_DISTANCE;
		return new Point(x, -y);
	}
	
	private Option getOptionSelected() {
		Integer index = getClosestOptionIndex();
		if (index != null) {
			return optionsList.get(index);
		}
		
		return null;
	}
	
	private Integer getClosestOptionIndex() {
		Point initPos = new Point(initialX, initialY);
		Point curTouch = new Point(currentX, currentY);
		Integer closestIndex = null;
		double closestDistance = curTouch.distanceSquaredTo(initPos);
		
		for (int i = 0; i < optionsList.size(); i++) {
			Point optionPos = getOptionPosition(i).add(initPos);
			double distance = optionPos.distanceSquaredTo(curTouch);
			if (distance <= closestDistance) {
				closestIndex = i;
				closestDistance = distance;
			}
		}
		
		return closestIndex;
	}
	
	class Point {
		
		double x;
		double y;
		
		Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		double distanceSquaredTo(Point p2) {
			return Math.pow(this.x - p2.x, 2) + Math.pow(this.y - p2.y, 2);
		}
		
		double distanceTo(Point p2) {
			return Math.sqrt(distanceSquaredTo(p2));
		}
		
		Point add(Point p2) {
			return new Point(this.x + p2.x, this.y + p2.y);
		}
	}
}
