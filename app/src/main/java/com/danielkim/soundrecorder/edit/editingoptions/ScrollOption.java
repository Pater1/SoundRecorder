package com.danielkim.soundrecorder.edit.editingoptions;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.Toast;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.fragments.DeckFragment;

public class ScrollOption extends Option {
	
	public static final int MAGNITUDE = 55;

	private DeckFragment deckFragment;
	private Point direction;
	
	public ScrollOption(DeckFragment deckFragment, Point direction) {
		this.deckFragment = deckFragment;
		this.direction = direction;
	}
	
	@Override
	protected boolean passedDownOnTouchUp(long[] cursorArray, int channelIndex) {
		return false;
	}
	
	@Override
	protected boolean passedDownOnTouchMove(long[] cursorArray, int channelIndex) {
		if (direction.x != 0) {
			deckFragment.scrollHorizontally(direction.x * MAGNITUDE);
		} else if (direction.y != 0) {
			deckFragment.scrollVertically(direction.y * MAGNITUDE);
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

	@Override
	public Bitmap getIcon(Resources res){
		if (direction.y < 0) {
			return BitmapFactory.decodeResource(res, R.drawable.dir_up);
		} else if (direction.y > 0) {
			return BitmapFactory.decodeResource(res, R.drawable.dir_down);
		} else if (direction.x > 0) {
			return BitmapFactory.decodeResource(res, R.drawable.dir_right);
		} else {
			return BitmapFactory.decodeResource(res, R.drawable.dir_left);
		}
	}
}
