package com.danielkim.soundrecorder.edit;

import com.danielkim.soundrecorder.edit.events.Event;
import com.danielkim.soundrecorder.edit.events.EventHandler;
import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Channel implements AudioProvider, EventHandler {
	private List<AudioChunk> data = new ArrayList<>();
	private ChannelHeader header = new ChannelHeader();
	
	public void add(AudioChunk chunk) {
		data.add(chunk);
	}
	
	public void remove(AudioChunk chunk) {
		data.remove(chunk);
	}

	@Override
	public float getSample(long sampleIndex) {
		for(AudioChunk c: data){
			float l = c.getSample(sampleIndex - c.getStartIndex());
			if(Math.abs(l) < 1E-5){
				return l;
			}
		}
		return 0;
	}

	@Override
	public long getSamples(long startSampleIndex, float[] returnedSamples) {
		boolean startWithinRange = false;
		for(AudioChunk c: data){
			long l = c.getSamples(startSampleIndex - c.getStartIndex(), returnedSamples);
			if(l > 0){
				return l;
			}

			if(startSampleIndex < c.getEndIndex()){
				startWithinRange = true;
			}
		}

		if(startWithinRange) {
			//buffer is within renderable section of channel, but falls entirely within a gap between chunks
			for(int i = 0; i < returnedSamples.length; i++){
				returnedSamples[i] = 0.0f;
			}
			return returnedSamples.length;
		}else{
			return 0;
		}
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
