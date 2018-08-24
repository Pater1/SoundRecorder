package com.danielkim.soundrecorder.edit.events;

public abstract class Event {
    public static boolean handleEvent(Event toHandle){
        return getPrimaryHandler().handleEvent(toHandle);
    }
    public static EventHandler getPrimaryHandler() {
        return primaryHandler;
    }
    public static void setPrimaryHandler(EventHandler primaryHandler) {
        Event.primaryHandler = primaryHandler;
    }
    private static EventHandler primaryHandler;

    public Event(long effectStartSampleIndex, long effectStopSampleIndex, int effectChannel) {
        this.effectStartSampleIndex = effectStartSampleIndex;
        this.effectStopSampleIndex = effectStopSampleIndex;
        this.effectChannel = effectChannel;
        this.targetFlags = getDefaultTargetFlags();
    }
    public Event(long effectStartSampleIndex, long effectStopSampleIndex, int effectChannel, int targetFlags) {
        this.effectStartSampleIndex = effectStartSampleIndex;
        this.effectStopSampleIndex = effectStopSampleIndex;
        this.effectChannel = effectChannel;
        this.targetFlags = targetFlags;
    }

    private long effectStartSampleIndex, effectStopSampleIndex;
    private int effectChannel;
    public long getEffectStartSampleIndex() {
        return effectStartSampleIndex;
    }
    public void setEffectStartSampleIndex(long effectStartSampleIndex) {
        this.effectStartSampleIndex = effectStartSampleIndex;
    }

    public long getEffectStopSampleIndex() {
        return effectStopSampleIndex;
    }
    public void setEffectStopSampleIndex(long effectStopSampleIndex) {
        this.effectStopSampleIndex = effectStopSampleIndex;
    }

    public int getEffectChannel() {
        return effectChannel;
    }
    public void setEffectChannel(int effectChannel) {
        this.effectChannel = effectChannel;
    }

    public boolean handleEvent(){
        return Event.handleEvent(this);
    }
    public abstract boolean applyEvent(EventHandler handler);

    //Override
    public int getDefaultTargetFlags(){
        return EffectTarget.ANY;
    }

    private int targetFlags;
    public int getTargetFlags() {
        return targetFlags;
    }
    public void setTargetFlags(int targetFlags) {
        this.targetFlags = targetFlags;
    }
}