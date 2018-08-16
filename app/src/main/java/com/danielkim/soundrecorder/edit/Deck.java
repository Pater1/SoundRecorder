package com.danielkim.soundrecorder.edit;

import com.danielkim.soundrecorder.edit.events.Event;
import com.danielkim.soundrecorder.edit.events.EventHandler;
import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Deck implements AudioProvider, EventHandler {
    private List<Channel> data = new ArrayList<>();

    public void render(String fileName) {
        throw new NotImplementedException();
    }

    @Override
    public boolean handleEvent(Event toHandle) {
        throw new NotImplementedException();
    }

    @Override
    public float getSample(long sampleIndex) {
        float ret = 0;
        for(Channel c: data){
            ret += c.getSample(sampleIndex);
        }
        return ret;
    }

    @Override
    public long getSamples(long startSampleIndex, float[] returnedSamples) {
        for(int i = 0; i < returnedSamples.length; i++){
            returnedSamples[i] = 0;
        }
        long length = 0;
        float[] tmp = new float[returnedSamples.length];
        for(Channel c: data){
            long l = c.getSamples(startSampleIndex, tmp);
            for(int i = 0; i < l; i++){
                returnedSamples[i] += tmp[i];
            }
            if(l > length){
                length = l;
            }
        }
        return length;
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
