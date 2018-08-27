package com.danielkim.soundrecorder.renderers;

import android.test.InstrumentationTestCase;

import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.AudioChunkInMemory;
import com.danielkim.soundrecorder.edit.renderers.WAVRenderer;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class WAVRendererTest {
    private static final Random rand = new Random();
    private AudioChunk buildChunkInMemory(float[] memory) {
        for (int i = 0; i < memory.length; i++) {
            memory[i] = (float)Math.sin((i/(double)44100) * 1440);
        }
        return new AudioChunkInMemory(memory);
    }

    @Test
    public void testRender() throws IOException {
        String path = android.os.Environment.getExternalStorageDirectory().getPath() + File.separator + "audio";
        AudioChunk audio = buildChunkInMemory(new float[44100 * 100]);
        audio.setSampleRate(44100);

        new WAVRenderer().render("testFile002", path, audio);
    }
}