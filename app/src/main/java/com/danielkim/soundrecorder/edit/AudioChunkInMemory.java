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
    public float getSample(long sampleIndex) {
        if(sampleIndex < 0 || sampleIndex >= pcm.length) {
            return 0;
        }else{
            return pcm[(int)sampleIndex];
        }
    }

    @Override
    public long getSamples(long startSampleIndex, float[] returnedSamples) {
        int length = returnedSamples.length;
        if(startSampleIndex < 0) {
            if(startSampleIndex + returnedSamples.length < 0){
                return 0;
            }else{
                length += startSampleIndex;
                startSampleIndex = 0;
            }
        }else if(startSampleIndex >= pcm.length){
            return 0;
        }

        int start = (int)startSampleIndex;
        length =(int)(length > (pcm.length - startSampleIndex)? pcm.length - startSampleIndex: length);
        for(int i = 0; i < length; i++){
            returnedSamples[i] = pcm[i + start];
        }
        return length;
    }

    @Override
    public boolean passdownHandleEvent(Event toHandle) {
        throw new NotImplementedException();
    }
}
