package com.danielkim.soundrecorder.edit.editingoptions;

import android.graphics.Color;

import com.danielkim.soundrecorder.activities.MainActivity;
import com.danielkim.soundrecorder.edit.events.Event;
import com.danielkim.soundrecorder.edit.events.MergeEvent;
import com.danielkim.soundrecorder.edit.fragments.DeckFragment;
import com.danielkim.soundrecorder.fragments.EditFragment;

public class MergeOption extends Option {

    @Override
    protected boolean passedDownOnTouchUp(long[] cursorArray, int channelIndex) {
        if (cursorArray == null) {
            return false;
        }
        
        Event event = null;
        if (cursorArray.length == 1) {
            event = new MergeEvent(cursorArray[0], cursorArray[0], channelIndex, true);
        } else if (cursorArray.length == 2) {
            event = new MergeEvent(cursorArray[0], cursorArray[1], channelIndex, false);
        }
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
        return Color.GREEN;
    }
}
