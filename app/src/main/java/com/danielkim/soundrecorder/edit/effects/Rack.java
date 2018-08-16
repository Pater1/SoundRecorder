package com.danielkim.soundrecorder.edit.effects;

import com.danielkim.soundrecorder.edit.AudioProvider;

import java.util.ArrayList;
import java.util.List;

public class Rack {
    private List<Effect> effects = new ArrayList<>();
    public void add(Effect toAdd){
        effects.add(toAdd);
    }
    public void remove(Effect toRemove){
        effects.remove(toRemove);
    }

    //may or may not edit in place
    public float[] applyEffects(float[] samples){
        for(Effect e: effects){
            samples = e.apply(samples);
        }
        return samples;
    }
}
