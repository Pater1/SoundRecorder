package com.danielkim.soundrecorder.edit;

import com.danielkim.soundrecorder.edit.renderers.WAVRenderer;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.*;

public class WAVAudioChunkTest {
    private static final int testSampleRate = 44100;
    private static final int testLength = testSampleRate*100;
    private static final Random rand = new Random();
    private AudioChunk buildChunkInMemory(float[] memory) {
        for (int i = 0; i < memory.length; i++) {
            memory[i] = (float)Math.sin((i/(double)testSampleRate) * 1440);
        }
        return new AudioChunkInMemory(memory);
    }
    private String render(AudioChunk audio) throws IOException {
        String path = "C:\\Users\\Patrick Conner\\Desktop";
        if(audio == null)
            audio = buildChunkInMemory(new float[testLength]);
        audio.setSampleRate(testSampleRate);

        return new WAVRenderer().render("testFile004", path, audio);
    }

    @Test
    public void testConstructor() throws IOException {
        File f = new File(render(null));
    }

    @Test
    public void getSamples() throws IOException {
        AudioChunk reffAudio = buildChunkInMemory(new float[testLength]);
        String path = render(reffAudio);
        File f = new File(path);
        WAVAudioChunk wav = new WAVAudioChunk(f);

        float[] testBuffer = new float[testLength], refferenceBuffer = new float[testLength];
        long refferenceLength = reffAudio.getSamples(0, refferenceBuffer);
        long testLength = wav.getSamples(0, testBuffer);

        junit.framework.Assert.assertEquals(testLength, refferenceLength);
        for(int i = 0; i < testBuffer.length; i++){
            junit.framework.Assert.assertTrue("" + testBuffer[i] + " does not equal " + refferenceBuffer[i] + " error: " + Math.abs(testBuffer[i] - refferenceBuffer[i]),
                    Math.abs(testBuffer[i] - refferenceBuffer[i]) < 1E-4);
        }
    }

    @Test
    public void getLength() throws IOException {
        File f = new File(render(null));
        WAVAudioChunk wav = new WAVAudioChunk(f);
        Assert.assertEquals(testSampleRate, wav.getSampleRate());
    }
}