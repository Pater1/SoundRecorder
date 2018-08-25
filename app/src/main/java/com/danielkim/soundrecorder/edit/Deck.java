package com.danielkim.soundrecorder.edit;

import com.danielkim.soundrecorder.edit.events.EffectTarget;
import com.danielkim.soundrecorder.edit.events.Event;
import com.danielkim.soundrecorder.edit.events.EventHandler;
import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;
import com.danielkim.soundrecorder.edit.renderers.WAVRenderer;

import java.util.ArrayList;
import java.util.List;

public class Deck implements AudioProvider, EventHandler {
    private List<Channel> data = new ArrayList<>();
    public boolean add(Channel c){
        return data.add(c);
    }
    public boolean remove(Channel c){
        return data.remove(c);
    }

    public Channel getChannel(int index){
        if(index < 0 || index > data.size()){
            return null;
        }else{
            return data.get(index);
        }
    }


    @Override
    public long getLength() {
        long lastEnd = 0;
        for(Channel c: data){
            long l = c.getLength();
            if(l > lastEnd){
                lastEnd = l;
            }
        }
        return lastEnd;
    }

    public void render(String fileName) {
        new WAVRenderer().render(fileName, this);
    }

    @Override
    public int getTargetedFlag(){
        return EffectTarget.DECK;
    }
    @Override
    public boolean handleEvent(Event toHandle) {
        if(!toHandle.applyEvent(this)){
            return data.get(toHandle.getEffectChannel()).handleEvent(toHandle);
        } else {
            return true;
        }
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
