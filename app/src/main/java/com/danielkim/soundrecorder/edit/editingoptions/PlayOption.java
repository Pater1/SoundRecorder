package com.danielkim.soundrecorder.edit.editingoptions;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.AudioPlayer;
import com.danielkim.soundrecorder.edit.AudioProvider;
import com.danielkim.soundrecorder.edit.canvases.Point;
import com.danielkim.soundrecorder.edit.canvases.OptionsJoystickCanvas;

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
        return Color.GREEN;
    }

    @Override
    public Bitmap getIcon(Resources res){
        return BitmapFactory.decodeResource(res, R.drawable.play);
    }
    @Override
    public Point getCenterOffset() {
        return new Point(5,0);
    }
}
