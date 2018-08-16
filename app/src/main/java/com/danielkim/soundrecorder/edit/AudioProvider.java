package com.danielkim.soundrecorder.edit;

public interface AudioProvider {
    public float getSample(long sampleIndex);
    public long getSamples(long startSampleIndex, float[] returnedSamples);

    public long getSampleRate();
    public void setSampleRate(long setTo);
}
