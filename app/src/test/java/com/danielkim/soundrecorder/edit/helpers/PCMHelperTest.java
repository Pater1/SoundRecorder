package com.danielkim.soundrecorder.edit.helpers;

import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.AudioChunkInMemory;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class PCMHelperTest {
    private static final Random rand = new Random();
    private float[] fillRanomArray(float[] memory) {
        for (int i = 0; i < memory.length; i++) {
            memory[i] = (rand.nextFloat() * 2) -1; // -1.0 to 1.0 inclusive
        }
        return memory;
    }
    private short[] fillRanomArray(short[] memory) {
        for (int i = 0; i < memory.length; i++) {
            memory[i] = (short) (rand.nextInt() & 0xffff);
        }
        return memory;
    }
    private byte[] fillRanomArray(byte[] memory) {
        for (int i = 0; i < memory.length; i++) {
            memory[i] = (byte) (rand.nextInt() & 0xff);
        }
        return memory;
    }

    @Test
    public void convert16bitPCMTo8bitWAV() {
        int testLength = Math.abs(rand.nextInt()) % 4096;
        short[] source = fillRanomArray(new short[testLength]);
        byte[] converted = PCMHelper.convert16bitPCMTo8bitWAV(source);

        Assert.assertEquals(source.length *2, converted.length);

        short[] unconverted = PCMHelper.convert8bitWAVTo16bitPCM(converted);
        Assert.assertArrayEquals(source, unconverted);
    }

    @Test
    public void convert8bitWAVTo16bitPCM() {
        int testLength = (Math.abs(rand.nextInt()) % 4096)*2;
        byte[] source = fillRanomArray(new byte[testLength]);
        short[] converted = PCMHelper.convert8bitWAVTo16bitPCM(source);

        Assert.assertEquals(source.length /2, converted.length);

        byte[] unconverted = PCMHelper.convert16bitPCMTo8bitWAV(converted);
        Assert.assertArrayEquals(source, unconverted);
    }

    @Test
    public void convert16bitPCMto32bitSamples(){
        int testLength = Math.abs(rand.nextInt()) % 4096;
        short[] source = fillRanomArray(new short[testLength]);
        float[] converted = PCMHelper.convert16bitPCMto32bitSamples(source);

        Assert.assertEquals(source.length, converted.length);

        short[] unconverted = PCMHelper.convert32bitSamplesPCMto16bitPCM(converted);
        Assert.assertArrayEquals(source, unconverted);
    }
    @Test
    public void convert32bitSamplesPCMto16bitPCM(){
        int testLength = Math.abs(rand.nextInt()) % 4096;
        float[] source = fillRanomArray(new float[testLength]);
        short[] converted = PCMHelper.convert32bitSamplesPCMto16bitPCM(source);

        Assert.assertEquals(source.length, converted.length);

        float[] unconverted = PCMHelper.convert16bitPCMto32bitSamples(converted);
        for(int i = 0; i < source.length; i++){
            Assert.assertTrue("" + source[i] + " and " + unconverted[i] + " are not equal",Math.abs(source[i]-unconverted[i]) < 1E-4);
        }
    }
}