package com.danielkim.soundrecorder.edit;

import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;

import java.nio.ByteBuffer;

public class BufferedAudioProvider implements AudioProvider {
    short[] buffer;
    public BufferedAudioProvider(short[] buffer, int sampleRate, int length){
        this.buffer = buffer;
        this.sampleRate = sampleRate;
        this.length = length;
    }

    @Override
    public float getSample(long sampleIndex) {
        throw new NotImplementedException();
    }

    @Override
    public long getSamples(long startSampleIndex, float[] returnedSamples) {
        int start = (int)startSampleIndex;
        int length = buffer.length - start;
        if(length > returnedSamples.length){
            length = returnedSamples.length;
        }

        if(length == 0 && returnedSamples.length > 0){
            return -1;
        }

        for(int i = 0; i < length; i++){
            float s2 = (float)buffer[i+start] / Short.MAX_VALUE;
            if(s2 > 1){
                s2 -= 2;
            }
            returnedSamples[i] = s2;
        }
        return length;
    }

    private long length;
    @Override
    public long getLength() {
        return length;
    }

    int sampleRate;
    @Override
    public long getSampleRate() {
        return sampleRate;
    }

    @Override
    public void setSampleRate(long setTo) {
        sampleRate = (int)setTo;
    }
}
