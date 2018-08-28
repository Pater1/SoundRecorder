package com.danielkim.soundrecorder.edit.editingoptions;

import android.app.Activity;
import android.graphics.Color;
import android.widget.Toast;

import com.danielkim.soundrecorder.edit.AudioPlayer;
import com.danielkim.soundrecorder.edit.AudioProvider;

public class PlayOption extends Option {
    AudioProvider ap;
    AudioPlayer play;
    Activity toaster;
    public PlayOption(AudioProvider audioProvider, Activity toaster){
        ap = audioProvider;
        this.toaster = toaster;
    }

    @Override
    protected boolean passedDownOnTouchUp(long[] cursorArray, int channelIndex) {
        if(play != null){
        }
        play = new AudioPlayer(ap);
        play.play();
        Toast.makeText(toaster, "Playing...", Toast.LENGTH_SHORT).show();
        return false;
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
        return Color.YELLOW;
    }
}
