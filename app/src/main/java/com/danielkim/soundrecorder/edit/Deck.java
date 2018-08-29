package com.danielkim.soundrecorder.edit;

import android.content.Context;

import com.danielkim.soundrecorder.DBHelper;
import com.danielkim.soundrecorder.RecordingService;
import com.danielkim.soundrecorder.edit.events.EffectTarget;
import com.danielkim.soundrecorder.edit.events.Event;
import com.danielkim.soundrecorder.edit.events.EventHandler;
import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;
import com.danielkim.soundrecorder.edit.helpers.TimeHelper;
import com.danielkim.soundrecorder.edit.renderers.WAVRenderer;

import java.io.File;
import java.io.IOException;
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

    private Context context;
    public Deck(Context context){
        this.context = context;
    }

    public Channel getChannel(int index){
        return data.get(index);
    }
    public int getChannelCount(){
        return data.size();
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

    public String render(String filePath, String fileName) throws IOException {
        String ret = new WAVRenderer().render(fileName, filePath, this);

        DBHelper db = new DBHelper(context);
        db.addRecording(fileName+".wav", filePath+File.separator+fileName+".wav",
                TimeHelper.microsecondToMillisecond(
                        TimeHelper.sampleIndexToMicrosecond(this.getLength(), (int)this.getSampleRate())
                )
        );

        return ret;
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
        long length = -1;
        float[] tmp = new float[returnedSamples.length];
        for(Channel c: data){
            long l = c.getSamples(startSampleIndex, tmp);
            if(l >= 0) {
                for (int i = 0; i < l; i++) {
                    try {
                        returnedSamples[i] += tmp[i];
                        tmp[i] = 0;
                    }catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            }
            if(l > length){
                length = l;
            }
        }
        for(int i = 0; i < length; i++){
            returnedSamples[i] /= data.size();
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
