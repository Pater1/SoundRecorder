package com.danielkim.soundrecorder.edit;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.os.Environment;

import com.danielkim.soundrecorder.edit.events.Event;
import com.danielkim.soundrecorder.edit.helpers.PCMHelper;
import com.danielkim.soundrecorder.edit.helpers.TimeHelper;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import android.media.*;

public class AudioChunkStreamedFromDisk extends AudioChunk  {
    private MediaExtractor extractor = new MediaExtractor();
    public AudioChunkStreamedFromDisk(File file) throws IOException {
        extractor.setDataSource(file.getAbsolutePath());

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(file.getAbsolutePath());
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int millSecond = Integer.parseInt(durationStr);
        length = TimeHelper.microsecondToSampleIndex(TimeHelper.millsecondToMicrosecond(millSecond), (int)this.getSampleRate());
    }

    private int channel = 0;
    public void setChannel(int channel){
        this.channel = channel;
    }
    public int getChannel(){
        return channel;
    }

    private long length;
    @Override
    public long getLength() {
        return extractor.getSampleTime();
    }

    @Override
    protected boolean passdownHandleEvent(Event toHandle) {
        return false;
    }

    @Override
    public float getSample(long sampleIndex) {
        throw new RuntimeException("single sample access not supported!");
    }

    @Override
    public long getSamples(long startSampleIndex, float[] returnedSamples) {
        extractor.seekTo(TimeHelper.sampleIndexToMicrosecond(startSampleIndex, (int)this.getSampleRate()), MediaExtractor.SEEK_TO_CLOSEST_SYNC);
        ByteBuffer inputBuffer = ByteBuffer.allocate(returnedSamples.length * 2);
        long ret = extractor.readSampleData(inputBuffer, 0);

        inputBuffer.asFloatBuffer().get(returnedSamples);

        return ret >= 0? ret: 0; //readSampleData returns -1 on end, we return 0 on end
    }
}
