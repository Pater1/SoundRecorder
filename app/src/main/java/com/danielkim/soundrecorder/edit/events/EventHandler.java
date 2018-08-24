package com.danielkim.soundrecorder.edit.events;

public interface EventHandler {
    public int getTargetedFlag();
    public boolean handleEvent(Event toHandle);
}
