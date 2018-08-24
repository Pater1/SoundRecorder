package com.danielkim.soundrecorder.edit.events;

import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.Channel;
import com.danielkim.soundrecorder.edit.MergedChunk;

import java.util.List;

public class MergeEvent extends Event {
    public MergeEvent(long effectStartSampleIndex, long effectStopSampleIndex, int effectChannel, boolean removeSilence) {
        super(effectStartSampleIndex, effectStopSampleIndex, effectChannel, EffectTarget.CHANNEL);
        this.removeSilence = removeSilence;
    }
    private boolean removeSilence;

    @Override
    public boolean applyEvent(EventHandler handler) {
        if(EffectTarget.isTargeted(this.getTargetFlags(), handler.getTargetedFlag())){
            if(handler instanceof Channel){
                Channel channel = (Channel)handler;

                List<AudioChunk> toMerge = channel.getChunksForIndexes(getEffectStartSampleIndex(), getEffectStopSampleIndex());

                if(toMerge.size() > 1){
                    AudioChunk rightMost = toMerge.get(0);
                    channel.remove(rightMost);
                    for(int i = 1; i < toMerge.size(); i++){
                        channel.remove(toMerge.get(i));
                        rightMost = new MergedChunk(rightMost, toMerge.get(i), removeSilence);
                    }
                    channel.add(rightMost);
                }

                return true;
            }
        }
        return false;
    }
}
