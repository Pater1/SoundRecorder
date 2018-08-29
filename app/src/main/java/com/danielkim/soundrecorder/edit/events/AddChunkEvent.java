package com.danielkim.soundrecorder.edit.events;

import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.Channel;
import com.danielkim.soundrecorder.edit.Deck;
import com.danielkim.soundrecorder.edit.SubChunk;

public class AddChunkEvent extends Event {
    public AddChunkEvent(AudioChunk toAdd, int effectChannel) {
        super(0,0, effectChannel, EffectTarget.DECK);
        this.toAdd = toAdd;
    }

    private AudioChunk toAdd;

    @Override
    public boolean applyEvent(EventHandler handler) {
        if(EffectTarget.isTargeted(this.getTargetFlags(), handler.getTargetedFlag())){
            if(handler instanceof Deck){
                Deck deck = (Deck)handler;

                int ch = getEffectChannel();
                try {
                    deck.getChannel(ch).add(toAdd);
                }catch (Exception e){
                    int i = ch;
                }

                return true;
            }
        }
        return false;
    }
}
