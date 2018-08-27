package com.danielkim.soundrecorder.renderers;

import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.AudioChunkInMemory;
import com.danielkim.soundrecorder.edit.renderers.MP3Renderer;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import it.sauronsoftware.jave.EncoderException;

public class MP3RendererTest {
    private static final Random rand = new Random();
    private AudioChunk buildChunkInMemory(float[] memory) {
        for (int i = 0; i < memory.length; i++) {
            memory[i] = (float)Math.sin((i/(double)44100) * 1440);
        }
        return new AudioChunkInMemory(memory);
    }

    @Test
    public void render() throws IOException, EncoderException {
        String path = android.os.Environment.getExternalStorageDirectory().getPath() + File.separator + "audio";
        AudioChunk audio = buildChunkInMemory(new float[44100 * 100]);
        audio.setSampleRate(44100);

        new MP3Renderer().render("testFile000", path, audio);
    }
}