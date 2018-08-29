package com.danielkim.soundrecorder.edit;

import com.danielkim.soundrecorder.edit.events.Event;
import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;

public class AudioChunkInMemory extends AudioChunk {
    public AudioChunkInMemory(float[] pcm) {
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
    public long getLength() {
        return pcm.length;
    }

    @Override
    public float getSample(long sampleIndex) {
        if(sampleIndex < 0 || sampleIndex >= pcm.length) {
            return 0;
        }else{
            return pcm[(int)sampleIndex];
        }
    }

    @Override
    public long getSamples(long startSampleIndex, float[] returnedSamples) {
        int lengthLeft = pcm.length - (int)startSampleIndex;

        int length = (int)(returnedSamples.length > lengthLeft? lengthLeft: returnedSamples.length);

        if(startSampleIndex >= pcm.length || (startSampleIndex + returnedSamples.length) < 0){
            return -1;
        }

        int start = (int)startSampleIndex;
        for(int i = 0; i < length; i++){
            int index = i + start;
            if(index >= 0 && index < pcm.length) {
                returnedSamples[i] = pcm[index];
            }else {
                returnedSamples[i] = 0;
            }
        }
        return length;
    }
}
