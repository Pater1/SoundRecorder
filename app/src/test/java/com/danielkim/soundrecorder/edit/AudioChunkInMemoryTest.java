package com.danielkim.soundrecorder.edit;

import junit.framework.TestCase;

import java.util.Random;

public class AudioChunkInMemoryTest extends TestCase {
	private static final Random rand = new Random();
	private AudioChunk buildChunkInMemory(float[] memory) {
		for (int i = 0; i < memory.length; i++) {
			memory[i] = (rand.nextFloat() * 2) -1;
		}
		return new AudioChunkInMemory(memory);
	}
	
	public void testGetSample() {

	}
	
	public void testGetSamples() {
		//float[] rawSamples = new float[rand.nextInt()];

	}
	
	public void testPassdownHandleEvent() {
	}
}