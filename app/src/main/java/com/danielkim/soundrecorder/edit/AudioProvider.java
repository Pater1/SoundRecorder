package com.danielkim.soundrecorder.edit;

public interface AudioProvider {
    @Deprecated
    public float getSample(long sampleIndex);
    public long getSamples(long startSampleIndex, float[] returnedSamples);

    public long getLength();

    public long getSampleRate();
    public void setSampleRate(long setTo);
}
