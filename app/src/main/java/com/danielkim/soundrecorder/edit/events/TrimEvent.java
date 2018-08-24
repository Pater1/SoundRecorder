package com.danielkim.soundrecorder.edit.events;

import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.Channel;
import com.danielkim.soundrecorder.edit.SubChunk;

public class TrimEvent extends Event {
    public TrimEvent(long effectStartSampleIndex, long effectStopSampleIndex, int effectChannel) {
        super(effectStartSampleIndex, effectStopSampleIndex, effectChannel, EffectTarget.CHANNEL | EffectTarget.CHUNK);
    }

    private Channel inChannel;

    @Override
    public boolean applyEvent(EventHandler handler) {
        if(EffectTarget.isTargeted(this.getTargetFlags(), handler.getTargetedFlag())){
            if(handler instanceof AudioChunk){
                new SplitEvent(getEffectStartSampleIndex(), getEffectChannel()).handleEvent();
                new SplitEvent(getEffectStopSampleIndex(), getEffectChannel()).handleEvent();

                new RemoveChunkEvent(getEffectStartSampleIndex()+1, getEffectChannel()).handleEvent();
                new RemoveChunkEvent(getEffectStopSampleIndex()-1, getEffectChannel()).handleEvent();

                return true;
            }else if(handler instanceof Channel){
                inChannel = (Channel)handler;
                return false;
            }
        }
        return false;
    }
}
