package com.danielkim.soundrecorder.edit.effects;

import com.danielkim.soundrecorder.edit.AudioProvider;
import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;

public class Resample extends Effect {
    public Resample(Rack effectIn, long fromSampleRate, long toSampleRate) {
        super(effectIn);
    }

    private long fromSampleRate, toSampleRate;

    public long getFromSampleRate() {
        return fromSampleRate;
    }
    public void setFromSampleRate(long fromSampleRate) {
        this.fromSampleRate = fromSampleRate;
    }

    public long getToSampleRate() {
        return toSampleRate;
    }
    public void setToSampleRate(long toSampleRate) {
        this.toSampleRate = toSampleRate;
    }

    @Override
    public float[] apply(float[] samples) {
        throw new NotImplementedException();
    }
}
