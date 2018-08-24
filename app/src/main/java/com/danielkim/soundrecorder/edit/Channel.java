package com.danielkim.soundrecorder.edit;

import com.danielkim.soundrecorder.edit.events.EffectTarget;
import com.danielkim.soundrecorder.edit.events.Event;
import com.danielkim.soundrecorder.edit.events.EventHandler;
import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    public void remove(int index) {
        data.remove(index);
    }
    public void remove(long sample) {
        data.remove(getChunkForIndex(sample));
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
	
	public AudioChunk getChunk(int index) {
		if (index < 0 || index >= data.size()) {
			throw new IllegalArgumentException("index out of bounds: " + index);
		}
		
		return data.get(index);
	}
	
	public int getDataSize() {
		return data.size();
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

	public AudioChunk getChunkForIndex(long sampleIndex){
		for(AudioChunk c: data){
			if(sampleIndex >= c.getStartIndex() && sampleIndex < c.getEndIndex()){
				return c;
			}
		}
		return null;
	}

	public List<AudioChunk> getChunksForIndexes(long sampleIndexStart, long sampleIndexEnd){
		List<AudioChunk> ret = new ArrayList<>();
		for(AudioChunk c: data){
			if(c.getEndIndex() > sampleIndexStart || c.getStartIndex() < sampleIndexEnd){
				ret.add(c);
			}
		}
		Collections.sort(ret, new Comparator<AudioChunk>() {
			@Override
			public int compare(AudioChunk t0, AudioChunk t1) {
				return (int)(t0.getStartIndex() - t1.getStartIndex());
			}
		});
		return ret;
	}
	@Override
	public int getTargetedFlag(){
		return EffectTarget.CHANNEL;
	}
	@Override
	public boolean handleEvent(Event toHandle) {
		if(!toHandle.applyEvent(this)){
			AudioChunk eA = getChunkForIndex(toHandle.getEffectStartSampleIndex());
			AudioChunk eB = getChunkForIndex(toHandle.getEffectStartSampleIndex());
			boolean bA = false, bB = false;
			if(eA != null){
				bA = eA.handleEvent(toHandle);
			}
			if(eB != null && eB != eA){
				bB = eB.handleEvent(toHandle);
			}
			return bA || bB;
		}
		return true;
	}
}
