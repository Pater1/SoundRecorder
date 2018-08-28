package com.danielkim.soundrecorder.edit.events;

public class EffectTarget {
    public static final int DECK = 1 << 0, CHANNEL = 1 << 1, CHUNK = 1 << 2;
    public static final int ANY = DECK|CHANNEL|CHUNK;

    public static boolean isTargeted(int targeter, int targeted){
        return (targeted & targeter) != 0;
    }
}