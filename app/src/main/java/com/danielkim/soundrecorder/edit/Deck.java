package com.danielkim.soundrecorder.edit;

import com.danielkim.soundrecorder.edit.events.Event;
import com.danielkim.soundrecorder.edit.events.EventHandler;
import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;

public class Deck implements AudioProvider, EventHandler {
    private Channel[] data;

    public void render(String fileName) {
        throw new NotImplementedException();
    }

    @Override
    public boolean handleEvent(Event toHandle) {
        throw new NotImplementedException();
    }

    @Override
    public float getSample(long sampleIndex) {
        throw new NotImplementedException();
    }

    @Override
    public long getSamples(long startSampleIndex, float[] returnedSamples) {
        throw new NotImplementedException();
    }


    private long sampleRate;
    @Override
    public long getSampleRate() {
        return sampleRate;
    }
    @Override
    public void setSampleRate(long setTo) {
        sampleRate = setTo;
    }
}
