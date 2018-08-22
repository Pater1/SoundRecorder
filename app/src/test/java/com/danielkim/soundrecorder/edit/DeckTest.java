package com.danielkim.soundrecorder.edit;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.Random;

public class DeckTest extends TestCase {
	private static final Random rand = new Random();
	private AudioChunk buildChunkInMemory(float[] memory) {
		for (int i = 0; i < memory.length; i++) {
			memory[i] = (rand.nextFloat() * 2) -1; // -1.0 to 1.0 inclusive
		}
		return new AudioChunkInMemory(memory);
	}
	
	public void testRender() {
	}
	
	public void testHandleEvent() {
	}
	
	public void testGetSample() {
	}
	
	public void testGetSamples() {
		float[] rawSamples1 = new float[256];
		AudioChunk testChunk1 = buildChunkInMemory(rawSamples1);

		float[] rawSamples2 = new float[512];
		AudioChunk testChunk2 = buildChunkInMemory(rawSamples2);

		Channel test = new Channel();
		test.add(testChunk1);
		testChunk2.setStartIndex(256 + 64);
		test.add(testChunk2);

		int index = 0;
		long length;
		float[] buffer = new float[30]; //not power of two to force partial buffer fill
		do{
			length = test.getSamples(index, buffer);

			for(int i = 0; i < length; i++){
				float streamedOut = buffer[i];
				int testReffIndex = index + i;
				float testReff = 0;
				if(testReffIndex < 256){
					testReff = rawSamples1[testReffIndex];
				}else if(testReffIndex >= (256 + 64)){
					testReff = rawSamples2[testReffIndex - (256 + 64)];
				}

				Assert.assertEquals(streamedOut, testReff);
			}

			index += length;
		}while(length > 0);

		Assert.assertEquals(index, rawSamples1.length + rawSamples2.length + 64);
	}
	
	public void testGetSampleRate() {
	}
	
	public void testSetSampleRate() {
	}
}