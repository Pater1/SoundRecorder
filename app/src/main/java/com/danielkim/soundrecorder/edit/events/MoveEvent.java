package com.danielkim.soundrecorder.edit.events;

import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.Channel;
import com.danielkim.soundrecorder.edit.SubChunk;

public class MoveEvent extends Event{
    private int effectEndChannel;
    public MoveEvent(long effectToSampleIndex, int effectStartChannel, int effectEndChannel) {
        super(effectToSampleIndex, effectToSampleIndex, effectStartChannel, EffectTarget.CHUNK);
        this.effectEndChannel = effectEndChannel;
    }

    @Override
    public boolean applyEvent(EventHandler handler) {
        if(EffectTarget.isTargeted(this.getTargetFlags(), handler.getTargetedFlag())){
            if(handler instanceof AudioChunk){
                AudioChunk original = (AudioChunk)handler;
                original.setStartIndex(getEffectStopSampleIndex());

                if(effectEndChannel != getEffectChannel()){
                    new RemoveChunkEvent(original, getEffectChannel()).handleEvent();
                    new AddChunkEvent(original, effectEndChannel).handleEvent();
                }

                return true;
            }
        }
        return false;
    }
}
