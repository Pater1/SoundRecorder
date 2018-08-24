package com.danielkim.soundrecorder.edit.events;

import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.Channel;
import com.danielkim.soundrecorder.edit.SubChunk;
import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;

public class SplitEvent extends Event {
    public SplitEvent(long effectSampleIndex, int effectChannel) {
        super(effectSampleIndex, effectSampleIndex, effectChannel, EffectTarget.CHANNEL | EffectTarget.CHUNK);
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