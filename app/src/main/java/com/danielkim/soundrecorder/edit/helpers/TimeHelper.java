package com.danielkim.soundrecorder.edit.helpers;

public class TimeHelper {
    public static long sampleIndexToMicrosecond(long index, int sampleRate){
        double samplesPerUsec = Math.pow(sampleRate / 1e+6, -1);
        return (long)(samplesPerUsec * index);
    }
    public static long microsecondToSampleIndex(long micro, int sampleRate){
        double samplesPerUsec = sampleRate / 1e+6;
        return (long)(samplesPerUsec * micro);
    }

    public static long microsecondToMillisecond(long micro){
        return micro / (long)1e+6;
    }
    public static long millsecondToMicrosecond(long milli){
        return milli * (long)1e+6;
    }
}
