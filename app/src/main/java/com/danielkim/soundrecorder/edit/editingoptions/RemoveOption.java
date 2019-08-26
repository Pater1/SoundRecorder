package com.danielkim.soundrecorder.edit.editingoptions;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.events.Event;
import com.danielkim.soundrecorder.edit.events.MergeEvent;
import com.danielkim.soundrecorder.edit.events.RemoveChunkEvent;

public class RemoveOption extends Option {
    @Override
    protected boolean passedDownOnTouchUp(long[] cursorArray, int channelIndex) {
        if (cursorArray == null) {
            return false;
        }

        Event event = new RemoveChunkEvent(cursorArray[0], channelIndex);
        boolean b = event.handleEvent();
        UPDATE_FRAGMENT.refresh();
        return b;
    }

    @Override
    protected boolean passedDownOnTouchMove(long[] cursorArray, int channelIndex) {
        return false;
    }

    @Override
    protected boolean passedDownOnTouchDown(long[] cursorArray, int channelIndex) {
        return false;
    }

    @Override
    public int getColor() {
        return Color.RED;
    }

    @Override
    public Bitmap getIcon(Resources res){
        return BitmapFactory.decodeResource(res, R.drawable.delete);
    }
}
