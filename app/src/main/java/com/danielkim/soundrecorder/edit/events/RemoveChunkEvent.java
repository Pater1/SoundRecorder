package com.danielkim.soundrecorder.edit.events;

import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.Channel;
import com.danielkim.soundrecorder.edit.Deck;
import com.danielkim.soundrecorder.edit.SubChunk;

public class RemoveChunkEvent extends Event {
    public RemoveChunkEvent(AudioChunk toRemove, int effectChannel) {
        super(0,0,effectChannel, EffectTarget.CHANNEL | EffectTarget.DECK);
        this.toRemove = toRemove;
    }
    public RemoveChunkEvent(int toRemoveIndex, int effectChannel) {
        super(0,0,effectChannel, EffectTarget.CHANNEL | EffectTarget.DECK);
        this.toRemove = null;
        removeAtIndex = true;
        this.toRemoveIndex = toRemoveIndex;
    }
    public RemoveChunkEvent(long removeAtSample, int effectChannel) {
        super(0,0,effectChannel, EffectTarget.CHANNEL | EffectTarget.DECK);
        this.toRemove = null;
        removeAtIndex = false;
        this.removeAtSample = removeAtSample;
    }

    private AudioChunk toRemove = null;
    private Deck parent = null;
    private boolean removeAtIndex = false;
    private int toRemoveIndex = 0;
    private long removeAtSample = 0;

    @Override
    public boolean applyEvent(EventHandler handler) {
        if(EffectTarget.isTargeted(this.getTargetFlags(), handler.getTargetedFlag())){
            if(handler instanceof Channel){
                Channel channel = (Channel)handler;

                if(toRemove == null){
                    if(removeAtIndex) {
                        channel.remove(toRemoveIndex);
                    } else {
                        channel.remove(removeAtSample);
                    }
                } else {
                    channel.remove(toRemove);
                }

                if(channel.chunkCount() == 0 && parent != null){
                    parent.remove(channel);
                }

                return true;
            }else if(handler instanceof Deck){
                parent = (Deck)handler;
            }
        }
        return false;
    }
}
