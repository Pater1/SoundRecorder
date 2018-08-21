package com.danielkim.soundrecorder.edit;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.Random;

public class AudioChunkInMemoryTest extends TestCase {
	private static final Random rand = new Random();
	private AudioChunk buildChunkInMemory(float[] memory) {
		for (int i = 0; i < memory.length; i++) {
			memory[i] = (rand.nextFloat() * 2) -1; // -1.0 to 1.0 inclusive
		}
		return new AudioChunkInMemory(memory);
	}
	
	public void testGetSample() {

	}
	
	public void testGetSamples() {
		float[] rawSamples = new float[Math.abs(rand.nextInt()) % 4096]; //mod just to ensure test doesn't take to long to run
		AudioChunk test = buildChunkInMemory(rawSamples);

		int index = 0;
		long length;
		float[] buffer = new float[rawSamples.length % 256]; //mod to increase likelyhood that getSamples will called multiple times and has a partial fill return
		do{
			length = test.getSamples(index, buffer);

			for(int i = 0; i < length; i++){
				Assert.assertEquals(rawSamples[i + index], buffer[i]);
			}

			index += length;
		}while(length > 0);

		Assert.assertEquals(index, rawSamples.length);
	}
	
	public void testPassdownHandleEvent() {
	}
}