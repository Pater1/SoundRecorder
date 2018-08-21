package com.danielkim.soundrecorder.edit;

import junit.framework.TestCase;

import java.util.Random;

public class AudioChunkInMemoryTest extends TestCase {
	private static final Random rand = new Random();
	private AudioChunk buildChunkInMemory(float[] memory) {
		float[] rawSamples = new float[rand.nextInt()];
		for (int i = 0; i < rawSamples.length; i++) {
			rawSamples[i] = rand.nextFloat();
		}

		return new AudioChunkInMemory(rawSamples);
	}
	
	public void testGetSample() {

	}
	
	public void testGetSamples() {


	}
	
	public void testPassdownHandleEvent() {
	}
}