package com.danielkim.soundrecorder.edit;

import android.test.AndroidTestCase;

import com.danielkim.soundrecorder.edit.renderers.MP3Renderer;
import com.danielkim.soundrecorder.edit.renderers.WAVRenderer;

import junit.framework.Assert;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import it.sauronsoftware.jave.EncoderException;

import static org.junit.Assert.*;

public class TestAudioChunkStreamedFromDisk extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public TestAudioChunkStreamedFromDisk(){
        super();
    }

    private static final Random rand = new Random();
    private AudioChunk buildChunkInMemory(float[] memory) {
        for (int i = 0; i < memory.length; i++) {
            memory[i] = (float)Math.sin((i/(double)44100) * 1440);
        }
        return new AudioChunkInMemory(memory);
    }

    @Test
    public void testGetLength() {
        Assert.assertTrue(true);
    }

    @Test
    public void testGetSamples() throws IOException, InterruptedException, EncoderException {
        String path = android.os.Environment.getExternalStorageDirectory().getPath() + "/audio";
        String file = "testFile001";

        int length = 44100 * 100;
        AudioChunk audio = buildChunkInMemory(new float[length]);
        audio.setSampleRate(44100);

        new MP3Renderer().render(file, path, audio);

        Thread.sleep(10000);

        AudioChunk pull = new AudioChunkStreamedFromDisk(new File(path), 44100);

        float[] testBuffer = new float[(int)audio.getLength()], refferenceBuffer = new float[(int)audio.getLength()], tmpBuffer = new float[30];
        long refferenceLength = audio.getSamples(0, refferenceBuffer);
        long testLength = 0, tmp = 0;
        do {
            tmp = pull.getSamples(testLength, tmpBuffer);

            for (int i = 0; i < tmp; i++) {
                testBuffer[i + (int) testLength] = tmpBuffer[i];
            }

            testLength += tmp;
        }while (tmp > 0 && testLength < length);

        Assert.assertEquals(testLength, refferenceLength);
        for(int i = 0; i < testBuffer.length; i++){
            Assert.assertEquals(testBuffer[i], refferenceBuffer[i]);
        }
    }
}