package com.danielkim.soundrecorder.edit.events;

import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.Channel;
import com.danielkim.soundrecorder.edit.SubChunk;
import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;

public class SplitEvent extends Event {
    public SplitEvent(long effectStartSampleIndex, long effectStopSampleIndex, int effectChannel) {
        super(effectStartSampleIndex, effectStopSampleIndex, effectChannel, EffectTarget.CHANNEL | EffectTarget.CHUNK);

        if(effectStartSampleIndex != effectStopSampleIndex){
            throw new IllegalArgumentException("effect start and stop indexes must be the same");
        }
    }

    private Channel inChannel = null;

    @Override
    public boolean applyEvent(EventHandler handler) {
        if(EffectTarget.isTargeted(this.getTargetFlags(), handler.getTargetedFlag())){
            if(handler instanceof AudioChunk){
                AudioChunk original = (AudioChunk)handler;
                AudioChunk pre = new SubChunk(original, 0, getEffectStartSampleIndex());
                AudioChunk post = new SubChunk(original, getEffectStartSampleIndex(), original.getLength());

                inChannel.remove(original);
                inChannel.add(pre);
                inChannel.add(post);

                return true;
            }else if(handler instanceof Channel){
                inChannel = (Channel)handler;
                return false;
            }
        }
        return false;
    }
}