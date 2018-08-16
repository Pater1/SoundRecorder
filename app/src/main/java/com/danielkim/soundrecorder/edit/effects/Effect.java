package com.danielkim.soundrecorder.edit.effects;

import com.danielkim.soundrecorder.edit.AudioProvider;

import java.util.Stack;

public abstract class Effect {
    private static Stack<Effect> undo, redo;
    public static boolean Undo(){
        if(!undo.empty()){
            Effect popped = undo.pop();
            redo.push(popped);
            popped.effectIn.remove(popped);
            return true;
        }else{
            return false;
        }
    }
    public static boolean Redo(){
        if(!redo.empty()){
            Effect popped = redo.pop();
            undo.push(popped);
            popped.effectIn.add(popped);
            return true;
        }else{
            return false;
        }
    }
    private static void Push(Effect toPush){
        undo.push(toPush);
        redo.clear();
    }

    public Effect(Rack effectIn) {
        this.effectIn = effectIn;
        Push(this);
    }

    private final Rack effectIn;

    public abstract float[] apply(float[] samples);
}
