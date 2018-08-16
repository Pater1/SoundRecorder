package com.danielkim.soundrecorder.edit;

import com.danielkim.soundrecorder.edit.events.Event;
import com.danielkim.soundrecorder.edit.events.EventHandler;
import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;

public class Channel implements AudioProvider, EventHandler {
	
	private AudioChunk[] data;
	private ChannelHeader header = new ChannelHeader();

	@Override
	public float getSample(long sampleIndex) {
		throw new NotImplementedException();
	}

	@Override
	public long getSamples(long startSampleIndex, float[] returnedSamples) {
		throw new NotImplementedException();
	}

	private long sampleRate;
	@Override
	public long getSampleRate() {
		return sampleRate;
	}
	@Override
	public void setSampleRate(long setTo) {
		sampleRate = setTo;
	}

	@Override
	public boolean handleEvent(Event toHandle) {
		throw new NotImplementedException();
	}
}
