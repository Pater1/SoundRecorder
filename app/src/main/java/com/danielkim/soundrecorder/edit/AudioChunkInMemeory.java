package com.danielkim.soundrecorder.edit;

import com.danielkim.soundrecorder.edit.events.Event;
import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;

public class AudioChunkInMemeory extends AudioChunk {
    public AudioChunkInMemeory(float[] pcm) {
        this.pcm = pcm;
    }

    public float[] getPcm() {
        return pcm;
    }

    public void setPcm(float[] pcm) {
        this.pcm = pcm;
    }

    private float[] pcm;

    @Override
    public float getSample(long sampleIndex) {
        throw new NotImplementedException();
    }

    @Override
    public long getSamples(long startSampleIndex, float[] returnedSamples) {
        throw new NotImplementedException();
    }

    @Override
    public boolean passdownHandleEvent(Event toHandle) {
        throw new NotImplementedException();
    }
}
