package com.danielkim.soundrecorder.edit.events;

public abstract class Event<T extends EventHandler> {
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
}