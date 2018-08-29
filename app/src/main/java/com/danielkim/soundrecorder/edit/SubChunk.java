package com.danielkim.soundrecorder.edit;

import android.util.Log;

import com.danielkim.soundrecorder.edit.events.Event;

public class SubChunk extends AudioChunk {
    private AudioChunk superChunk;
    private long startSample, endSample;
    public SubChunk(AudioChunk superChunk, long startSample, long endSample) {
        this.superChunk = superChunk;
        if(startSample < 0 || endSample > superChunk.getLength()){
            throw new IllegalArgumentException("start and end samples must define a range within the source chunk provided!");
        }
        this.startSample = startSample;
        this.endSample = endSample;
    }

    @Override
    public long getLength() {
        return endSample - startSample;
    }

    @Override
    public float getSample(long sampleIndex) {
        return superChunk.getSample(sampleIndex - startSample);
    }

    @Override
    public long getSamples(long startSampleIndex, float[] returnedSamples) {
        if(startSampleIndex < 0){
            return Math.min(returnedSamples.length, -startSampleIndex);
        }
        if(startSampleIndex >= endSample){
            return -1;
        }
        long localIndex = startSampleIndex + startSample;
        long ret = superChunk.getSamples(localIndex, returnedSamples);
        if((ret+startSampleIndex)>endSample){
            ret = endSample - startSampleIndex;
        }
        if(ret > returnedSamples.length){
            ret = returnedSamples.length;
        }
        return ret;
    }
}
