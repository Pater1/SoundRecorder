package com.danielkim.soundrecorder.edit.events;

import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.AudioChunkInMemory;
import com.danielkim.soundrecorder.edit.Channel;
import com.danielkim.soundrecorder.edit.Deck;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import java.util.Random;

public class SplitEventTest extends TestCase {
    private static final Random rand = new Random();
    private static AudioChunk buildChunkInMemory(float[] memory) {
        for (int i = 0; i < memory.length; i++) {
            memory[i] = (rand.nextFloat() * 2) -1; // -1.0 to 1.0 inclusive
        }
        return new AudioChunkInMemory(memory);
    }

    @Test
    public void testSendSplit(){
        Deck testDeck = new Deck(null);

        Channel testChannel = new Channel();
        AudioChunk testChunk = buildChunkInMemory(new float[512]);
        testChannel.add(testChunk);

        Channel refferenceChannel = new Channel();
        float[] testPCMCopy = new float[512];
        for(int i = 0; i < testPCMCopy.length; i++){
            testPCMCopy[i] = ((AudioChunkInMemory)testChunk).getPcm()[i];
        }
        AudioChunk refferenceChunk = new AudioChunkInMemory(testPCMCopy);
        refferenceChannel.add(refferenceChunk);

        testDeck.add(testChannel);
        testDeck.add(refferenceChannel);

        SplitEvent split = new SplitEvent(256, 0);
        testDeck.handleEvent(split);

        float[] testBuffer = new float[512], refferenceBuffer = new float[512], tmpBuffer = new float[30];
        long refferenceLength = refferenceChannel.getSamples(0, refferenceBuffer);
        long testLength = 0, tmp = 0;
        do {
            tmp = testChannel.getSamples(testLength, tmpBuffer);

            for (int i = 0; i < tmp; i++) {
                testBuffer[i + (int) testLength] = tmpBuffer[i];
            }

            testLength += tmp;
        }while (tmp > 0 && testLength < 512);

        Assert.assertEquals(testLength, refferenceLength);
        for(int i = 0; i < testBuffer.length; i++){
            Assert.assertEquals(testBuffer[i], refferenceBuffer[i]);
        }
        Assert.assertEquals(testChannel.getDataSize(), 2);
        Assert.assertEquals(refferenceChannel.getDataSize(), 1);
    }
}
