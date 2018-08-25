package com.danielkim.soundrecorder.edit.renderers;

import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.AudioChunkInMemory;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class WAVRendererTest {
    private static final Random rand = new Random();
    private AudioChunk buildChunkInMemory(float[] memory) {
        for (int i = 0; i < memory.length; i++) {
            memory[i] = (float)Math.sin((i/(double)44100) * 1440);
        }
        return new AudioChunkInMemory(memory);
    }

    @Test
    public void render() {
        String path = "C:\\Users\\Patrick Conner\\Desktop\\testFile.wav";
        AudioChunk audio = buildChunkInMemory(new float[44100 * 100]);
        audio.setSampleRate(44100);

        new WAVRenderer().render(path, audio);
    }
}