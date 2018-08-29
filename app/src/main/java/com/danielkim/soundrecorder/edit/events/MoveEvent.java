package com.danielkim.soundrecorder.edit.events;

import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.Channel;
import com.danielkim.soundrecorder.edit.SubChunk;
import com.danielkim.soundrecorder.edit.fragments.DeckFragment;

public class MoveEvent extends Event{
    private int effectEndChannel;
    private long moveToSample;
    public MoveEvent(long effectFromSampleIndex, long effectToSampleIndex, int effectStartChannel, int effectEndChannel) {
        super(effectFromSampleIndex, effectFromSampleIndex, effectStartChannel, EffectTarget.CHANNEL);
        this.effectEndChannel = effectEndChannel;
        this.moveToSample = effectToSampleIndex;
    }

    @Override
    public boolean applyEvent(EventHandler handler) {
        if(EffectTarget.isTargeted(this.getTargetFlags(), handler.getTargetedFlag())){
            if(handler instanceof Channel){
                AudioChunk original = ((Channel)handler).get(getEffectStartSampleIndex());
                original.setStartIndex(effectEndChannel);

                if(effectEndChannel != getEffectChannel()){
                    new AddChunkEvent(original, effectEndChannel).handleEvent();
                    new RemoveChunkEvent(original, getEffectChannel()).handleEvent();
    
                    DeckFragment.instance.refresh();
                }

                return true;
            }
        }
        return false;
    }
}
